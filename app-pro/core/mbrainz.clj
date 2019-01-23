(ns core.mbrainz
  (:require [clj-time.core :as t]
            [nrepl.server :refer [start-server stop-server]]
            [clj-time.format :as f]
            [clojure.repl :refer :all]
            [datomic.api :as d]
            [core.rules :refer (rules)]))

; (def cfg {:server-type :peer-server
;           :access-key "myaccesskey"
;           :secret "mysecret"
;           :endpoint "datomicdb:4334"})

; (def client (d/client cfg))
(def db-uri "datomic:dev://datomicdb:4334/mbrainz-1968-1973")


(d/create-database db-uri)
(def conn (d/connect db-uri))
; (def conn (d/connect client {:db-name "hello"}))

(def db (d/db conn))

(defn cdb [] (d/db conn))


(defn -main [] 
  (println conn)
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))


;; https://github.com/Datomic/mbrainz-sample/wiki/Queries
;; https://github.com/Datomic/mbrainz-sample/blob/master/examples/clj/datomic/samples/mbrainz.clj

(d/q '[:find ?id ?type ?gender
       :in $ ?name
       :where
       [?e :artist/name ?name]
       [?e :artist/gid ?id]
       [?e :artist/type ?teid]
       [?teid :db/ident ?type]
       [?e :artist/gender ?geid]
       [?geid :db/ident ?gender]]
     db
     "Janis Joplin")

;; What are titles of the tracks John Lennon palyed on?

(def qy1 '[:find ?title
           :in $ ?artist-name
           :where
           [?a :artist/name ?artist-name]
           [?t :track/artists ?a]
           [?t :track/name ?title]])
          

(d/q qy1 db "John Lennon")

(d/q '[:find ?title ?album ?year
       :in $ ?artist-name
       :where
       [?a :artist/name ?artist-name]
       [?t :track/artists ?a]
       [?t :track/name ?title]
       [?m :medium/tracks ?t]
       [?r :release/media ?m]
       [?r :release/name ?album]
       [?r :release/year ?year]]
      
      
     db "John Lennon")


(d/q '[:find ?title ?album ?year
       :in $ ?artist-name
       :where
       [?a :artist/name ?artist-name]
       [?t :track/artists ?a]
       [?t :track/name ?title]
       [?m :medium/tracks ?t]
       [?r :release/media ?m]
       [?r :release/name ?album]
       [?r :release/year ?year]
       [(< ?year 1970)]]
     
     db "John Lennon")

(def rules-custom '[;; Given ?t bound to track entity-ids, binds ?r to the corresponding
 ;; set of album release entity-ids
             [(track-release ?t ?r)
              [?m :medium/tracks ?t]
              [?r :release/media ?m]]])

; rules

(d/q '[:find ?title ?album ?year
       :in $ % ?artist-name
       :where
       [?a :artist/name ?artist-name]
       [?t :track/artists ?a]
       [?t :track/name ?title]
       (track-release ?t ?r)
       [?r :release/name ?album]
       [?r :release/year ?year]]
     db rules-custom "John Lennon")

(d/q '[:find ?title ?album ?year
       :in $ % ?artist-name
       :where
       [?a :artist/name   ?artist-name]
       [?t :track/artists ?a]
       [?t :track/name    ?title]
       (track-release ?t ?r)
       [?r :release/name  ?album]
       [?r :release/year  ?year]]
     db
     rules
     "John Lennon")


; find all the tracks w/ the word always in the title

(d/q '[:find ?title ?artist ?album ?year
       :in $ % ?search
       :where
       (track-search ?search ?track)
       (track-info ?track ?title ?artist ?album ?year)
       ] db rules "always")

; who collaborated w/ one of the Beatles ?

(d/q '[:find ?aname ?aname2
       :in $ % [?name ...]
       :where (collab ?aname ?aname2)
       ] db rules ["John Lennon" "Paul McCartney" "George Harrison" "Ringo Starr"])

; who collaborated directly w/ George Harrison , or collaborated w/ one of his collaborators  ?

(d/q '[:find  ?aname2
       :in $ % ?aname
       :where (collab-net-2 ?aname ?aname2)] db rules "George Harrison" )

; chain queries

(def result1 (d/q '[:find ?aname2
       :in $ % [[?aname]]
       :where (collab ?aname ?aname2)] 
     db rules [["Diana Ross"]]))

result1

(def query '[:find ?aname2
             :in $ % [[?aname]]
             :where (collab ?aname ?aname2)])

(d/q query
     db
     rules
     (d/q query
          db
          rules
          [["Diana Ross"]]))

; who covered Bill Withers

(def query '[:find ?aname ?tname
            :in $ ?artist-name
            :where
            [?a :artist/name ?artist-name]
            [?t :track/artists ?a]
            [?t :track/name ?tname]
            [(!= "Outro" ?tname)]
            [(!= "[outro]" ?tname)]
            [(!= "Intro" ?tname)]
            [(!= "[intro]" ?tname)]
            [?t2 :track/name ?tname]
            [?t2 :track/artists ?a2]
            [(!= ?a2 ?a)]
            [?a2 :artist/name ?aname]] )

(d/q query db "Bill Withers")


;;;; https://docs.datomic.com/on-prem/query.html

(comment

  (def data-1 '[[sally :age 21]
                [fred :age 42]
                [bob :age 42]
                [bob :likes "veggies"]
                
                ])

  ; all who is 42
  (d/q '[:find ?e
         :where [?e :age 42]]
       people)

  ; find all who is 42 and likes veggies
  (d/q '[:find ?e ?x 
         :where [?e :age 42]
         [?e :likes ?x]]
       data-1
       )
  
  ; anything that is liked
  (d/q '[:find ?x
         :where [_ :likes ?x]]
       data-1)
  
  (doc take)
  
  
  ; find all release names
  (->> (d/q '[:find ?release-name
              :where [_ :release/name ?release-name]]
            (cdb))
       (take 10)
       )
  
  ; find releases performed by John Lennon
  
  (->> (d/q '[:find ?release-name
              :in $ ?artist-name
              :where [?artist :artist/name ?artist-name]
              [?release :release/artists ?artist]
              [?release :release/name ?release-name]]
            (cdb) "John Lennon")
       ; count
       )
  
  
  ; what releases are associated with the artist named John Lennon and named Mind Games ?
  
  (->> (d/q '[:find ?release ?release-name ?release-year
              :in $ [?artist-name ?release-name]
              :where [?artist :artist/name ?artist-name]
              [?release :release/artists ?artist]
              [?release :release/name ?release-name]
              [?release :release/year ?release-year]]
            (cdb) ["John Lennon" "Mind Games"]))
  
  (d/attribute (cdb) :release/designs)
  
  ; https://gist.github.com/stuarthalloway/2321773
  (d/q '[:find ?attr
       :in $ [?include-ns ...]                ;; bind ?include-ns once for each item in collection
       :where
       [?e :db/valueType]                     ;; all schema types (must have a valueType)
       [?e :db/ident ?attr]                   ;; schema type name
       [(datomic.Util/namespace ?attr) ?ns]   ;; namespace of name
       [(= ?ns ?include-ns)]]                 ;; must match one of the ?include-ns 
     (d/db conn)
     ["release" ])
  
  )