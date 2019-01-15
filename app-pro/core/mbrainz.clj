(ns mbrainz
  (:require [clj-time.core :as t]
            [nrepl.server :refer [start-server stop-server]]
            [clj-time.format :as f]
            [datomic.api :as d]))

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


(defn -main [] 
  (println conn)
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))


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
          [?t :track/name ?title]
          ])

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
      [?r :release/year ?year]
      
      ]
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
