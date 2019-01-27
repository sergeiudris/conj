(ns core.mbrainz
  (:require [clj-time.core :as t]
            [nrepl.server :refer [start-server stop-server]]
            [clj-time.format :as f]
            [clojure.repl :refer :all]
            [clojure.data :refer :all]
            [clojure.pprint :as pp]
            
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


(comment

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

(->> (d/q '[:find [?title ...]
            :in $ ?artist-name
            :where
            [?a :artist/name ?artist-name]
            [?t :track/artists ?a]
            [?t :track/name ?title]]
          (cdb) "John Lennon"
          )
     seq
     pp/pprint
     )

(pp/pprint *1)

(doc seq)

(doc reify)

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

)
;;;; https://docs.datomic.com/on-prem/query.html

(comment

  (def data-1 '[[sally :age 21]
                [fred :age 42]
                [bob :age 42]
                [bob :likes "veggies"]])

  ; all who is 42
  (d/q '[:find ?e
         :where [?e :age 42]]
       people)

  ; find all who is 42 and likes veggies
  (d/q '[:find ?e ?x
         :where [?e :age 42]
         [?e :likes ?x]]
       data-1)

  ; anything that is liked
  (d/q '[:find ?x
         :where [_ :likes ?x]]
       data-1)

  (doc take)


  ; find all release names
  (->> (d/q '[:find ?release-name
              :where [_ :release/name ?release-name]]
            (cdb))
       (take 10))

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
  
  (->> (d/q '[:find ?release ?release-name ?release-year ?release-month ?release-day ?release-country
              :in $ [?artist-name ?release-name]
              :where [?artist :artist/name ?artist-name]
              [?release :release/artists ?artist]
              [?release :release/name ?release-name]
              [?release :release/year ?release-year]
              [?release :release/month ?release-month]
              [?release :release/day ?release-day]
              [?release :release/country ?release-country]]
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
       ["release"])

  (def release-17592186079767 (d/touch (d/entity (d/db conn) 17592186079767)))
  (def release-17592186079770 (d/touch (d/entity (d/db conn) 17592186079770)))
  (type release-17592186079767)
  (doc diff)
  (doc map)

  (pp/pprint (diff release-17592186079767 release-17592186079770))
  ; :medium/format :medium.format/vinyl12, :medium/position 1, :medium/trackCount 12
  ;:medium/format :medium.format/vinyl, :medium/position 1, :medium/trackCount 2
  
  (diff {:a 3 :b {:c 2}} {:a 1 :b {:c 5}})

  ; what releases are associated w either Paul McCartney or George Harrison
  
  (d/q '[:find ?release-name
         :in $ [?artist-name ...]
         :where [?artist :artist/name ?artist-name]
         [?release :release/artists ?artist]
         [?release :release/name ?release-name]]
       (d/db conn) ["Paul McCartney" "George Harrison"])

  ; what releases are associated w/ either John Lennon's Mind Games or Paul McCartney's Ram ?
  
  (->> (d/q '[:find ?release ?release-month ?release-day
              :in $ [[?artist-name ?release-name]]
              :where
              [?artist :artist/name ?artist-name]
              [?release :release/name ?release-name]
              [?release :release/month ?release-month]
              [?release :release/day ?release-day]]
            (d/db conn) [["John Lennon" "Mind Games"] ["Paul McCartney" "Ram"]])
       ; (map first)
       ; (map #( d/touch (d/entity (d/db conn) %)  ))
       )

  ; collection find spec
  
  (d/q '[:find [?release-name ...]
         :in $ ?artist-name
         :where
         [?artist :artist/name ?artist-name]
         [?release :release/artists ?artist]
         [?release :release/name ?release-name]]
       (d/db conn) "John Lennon"
       ; (d/db conn) "Paul McCartney"
       )

  ; single tuple find spec
  
  ; wrong useless query - just picks the first of many
  (d/q '[:find [?release-name ?year ?month ?day]
         :in $ ?name
         :where
         [?artist :artist/name ?name]
         [?release :release/artists ?artist]
         [?release :release/name ?release-name]
         [?release :release/year ?year]
         [?release :release/month ?month]
         [?release :release/day ?day]]
       ; (d/db conn) "Ringo Starr"
       (d/db conn) "John Lennon")

  ;=>
; ["Happy Xmas (War Is Over)" 1972 11 24]
  
  ; correct - get the only tuple
  (d/q '[:find [?year ?month ?day]
         :in $ ?name
         :where [?artist :artist/name ?name]
         [?artist :artist/startDay ?day]
         [?artist :artist/startMonth ?month]
         [?artist :artist/startYear ?year]]
       ; (d/db conn) "Ringo Starr"
       (d/db conn) "John Lennon")


   ; scalar find spec
  
  (d/q '[:find ?year .
         :in $ ?name
         :where
         [?artist :artist/name ?name]
         [?artist :artist/startYear ?year]]
       (d/db conn) "John Lennon")

  ; count all artists who are not Canadian
  
  (d/q '[:find (count ?eid) .
         :where
         [?eid :artist/name]
         (not [?eid :artist/country :country/CA])]
       (d/db conn))

  (doc not)

  ; not-join 
  
  ; number of artists who didn't release an album in 1970
  (d/q '[:find (count ?artist) .
         :where [?artist :artist/name]
         (not-join [?artist]
                   [?release :release/artists ?artist]
                   [?release :release/year 1970])]
       (d/db conn))

  ; multiple not-clauses are connect be 'and', just as they are in :where
  ; count the number of releases named 'Live at Carnegie Hall' that were not by Bill Withers
  
  (d/q '[:find (count ?r) .
         :where
         [?r :release/name "Live at Carnegie Hall"]
         (not-join [?r]
                   [?r :release/artists ?a]
                   [?a :artist/name "Bill Withers"])]
       (d/db conn))

   ; find conut of all vinyl media by listing the complete set of media that make up vinyl in the 'or' clause
  
  (d/q '[:find (count ?medium) .
         :where
         (or [?medium :medium/format :medium.format/vinyl7]
             [?medium :medium/format :medium.format/vinyl10]
             [?medium :medium/format :medium.format/vinyl12]
             [?medium :medium/format :medium.format/vinyl])]
       (d/db conn))



  ; use 'and' cluse inside 'or' to find the number of artists who are either groups or females
  
  (d/q '[:find (count ?artist) .
         :where
         (or
          [?artist :artist/type :artist.type/group]
          (and
           [?artist :artist/type :artist.type/person]
           [?artist :artist/gender :artist.gender/female]))]
       (d/db conn))

  ; the number of releases that are either by Canadian artists or released in 1970
  
  (d/q '[:find (count ?release) .
         :where
         [?release :release/name]
         (or-join [?release]
                  (and
                   [?release :release/artists ?artist]
                   [?artist :artist/country :country/CA])
                  [?release :release/year 1970])]
       (d/db conn))

  ; < linits the results to artists who started before 16000
  

  (d/q '[:find ?name ?year
         :where
         [?artist :artist/name ?name]
         [?artist :artist/startYear ?year]
         [(< ?year 1600)]]
       (d/db conn))

  (doc quot)
  ; quot converts track lengths from milliseconds to minutes
  
  (d/q '[:find ?track-name ?minutes
         :in $ ?artist-name
         :where
         [?artist :artist/name ?artist-name]
         [?track :track/artists ?artist]
         [?track :track/duration ?millis]
         [(quot ?millis 60000) ?minutes]
         [?track :track/name ?track-name]]
       (d/db conn) "John Lennon")

  ; no nesting, use multistep
  
  (d/q '[:find ?celcius .
         :in ?fahrenheit
         :where
         [(- ?fahrenheit 32) ?f-32]
         [(/ ?f-32 1.8) ?celcius]]
       212)

  ; get-else  : report N/A whenever an artist's startYear is not in th database
  
  (d/q '[:find ?artist-name ?year
         :in $ [?artist-name ...]
         :where
         [?artist :artist/name ?artist-name]
         [(get-else $ ?artist :artist/startYear "N/A") ?year]]
       (d/db conn) ["Crosby, Stills & Nash" "Crosby & Nash"])

  ; get-some : try finding :country/name for an entity and then fallback to :artist/name
  
  (d/q '[:find [?e ?attr ?name]
         :in $ ?e
         :where
         [(get-some $ ?e :country/name :artist/name) [?attr ?name]]]
       (d/db conn) :country/US)

  ; ground: takes a single arg (constant) and returns same arg
  
  ; fulltext: find all artists whose name includes "Jane"
  
  (d/q '[:find ?e ?name ?tx ?score
         :in $ ?search
         :where
         [(fulltext $ :artist/name ?search) [[?e ?name ?tx ?score]]]]
       (d/db conn) "Jane")


  ; missing? :  find all artsis whose startYear is not recorded
  
  (d/q '[:find (count ?name) .
         :where
         [?artist :artist/name ?name]
         [(missing? $ ?artist :artist/startYear)]]
       (d/db conn))

; tx-ids: find txs from t 1000 through 1050
  
  (d/q '[:find [?tx ...]
         :in ?log
         :where [(tx-ids ?log 1000 1050) [?tx ...]]]
       (d/log conn))


  ; tx-data : find the entities, referenced byt transaction id
  
  (d/q '[:find [?e ...]
         :in ?log ?tx
         :where [(tx-data ?log ?tx) [[?e]]]]
       (d/log conn) 13194139534312)


; java methods
  
  (d/q '[:find ?k ?v
         :where
         [(System/getProperties) [[?k ?v]]]])

  (d/q '[:find ?k ?v
         :where
         [(System/getProperties) [[?k ?v]]]
         [(.endsWith ?k "version")]])

  ; clojure funs
  ; use 'subs' to extract prefixes of words
  
  (d/q '[:find [?prefix ...]
         :in [?word ...]
         :where [(subs ?word 0 5) ?prefix]]
       ["hello" "antidisestablishmentarianism"])

  ; grouping via :with
  
  ;wrong query (set removes duplicates) - find hwo many heads in total
  (d/q '[:find (sum ?heads) .
         :in [[_ ?heads]]]
       [["Cerberus" 3]
        ["Medusa" 1]
        ["Cyclops" 1]
        ["Chimera" 1]])

  (d/q '[:find (sum ?heads) .
         :with ?monster
         :in [[?monster ?heads]]]
       [["Cerberus" 3]
        ["Medusa" 1]
        ["Cyclops" 1]
        ["Chimera" 1]])



  ; find smalles and largest track lengths
  
  (d/q '[:find [(min ?dur) (max ?dur)]
         :where [_ :track/duration ?dur]
         ]
       (d/db conn)
       )

  ; sum to find total nuber of tracks on all media on db
  
  (d/q '[:find (sum ?count) .
         :with ?medium
         :where [?medium :medium/trackCount ?count]
         ]
       (d/db conn))
  
  ; count total and distinct artist names
  
  (d/q '[:find (count ?name) (count-distinct ?name)
         :with ?artist
         :where [?artist :artist/name ?name]
         ]
       (d/db conn)
       )

  ; stats: report median, avg and standard deviation of song title lengths and includes year in the find set to break out the results by year
  
  (d/q '[:find ?year (median ?namelen) (avg ?namelen) (stddev ?namelen)
         :with ?track 
         :where [?track :track/name ?name]
         [(count ?name) ?namelen]
         [?medium :medium/tracks ?track]
         [?release :release/media ?medium]
         [?release :release/year ?year]
         
         ]
       (d/db conn)
       )

  (count "abc")
  
  ; aggregates returning collections
  ; distinct 
  
  
  (d/q '[:find (distinct ?v)
         :in [?v ...]
         ]
       [1 1 2 2 2 3]
       )

  ; min n, max n
  ; get five shortest and longest tracks in th database
  
  (d/q '[:find [(min 5 ?millis) (max 5 ?millis)]
         :where [?track :track/duration ?millis]
         ]
       (d/db conn)
       )
  
  ; rand - select n items w/ potential for duplicates
  ; sample - select n distinct
  
  (d/q '[:find [(rand 2 ?name) (sample 2 ?name)]
         :where [_ :artist/name ?name]]
       (d/db conn))

  ;custom aggregates
  
  (defn mode 
    [vals]
    (->> (frequencies vals)
         (sort-by (comp - second))
         ffirst))

  (doc frequencies)

  ; what is the most common release medium length, in tracks ?
  
  (d/q '[:find (core.mbrainz/mode ?track-count) .
         :with ?media 
         :where [?media :medium/trackCount ?track-count]]
       (d/db conn)
       )
  
; pull expressions
  
  ; return the :release/name for all of the Led Zeppelin releases
  
  ; (d/q '[:find (pull ?e [:release/name])
  ;        :in $ ?artist
  ;        :where 
  ;        [?e :release/artists ?artist]
  ;        ]
  ;      (d/db conn)  led-zeppelin
  ;      )
  
  ; (d/q '[:find (pull ?e pattern)
  ;        :in $ ?artist pattern
  ;        :where
  ;        [?e :release/artists ?artist]]
  ;      (d/db conn)  led-zeppelin [:release/name])
  
  (d/q '[:find (pull ?e [:release/name])
         :in $ ?artist-name
         :where [?e :release/artists ?a]
         [?a :artist/name ?artist-name]]
       (d/db conn)   "Led Zeppelin")

  (d/q '[:find (pull ?e [:release/name]) (pull ?a [*])
         :in $ ?artist-name
         :where 
         [?e :release/artists ?a]
         [?a :artist/name ?artist-name]
         
         ]
       (d/db conn) "Led Zeppelin"
       )

  (d/q '[:find (pull ?e [:release/name :release/artists])
         :in $ ?artist-name
         :where [?e :release/artists ?a]
         [?a :artist/name ?artist-name]]
       (d/db conn)   "Led Zeppelin")
  

; query as map
  
  (d/q '{:find [(pull ?e [:release/name :release/artists]) (pull ?a [*])]
         :in [$ ?artist-name]
         :where [
                 [?e :release/artists ?a]
                 [?a :artist/name ?artist-name]
                 ]
         }
       (d/db conn)   "Led Zeppelin")
  

; performance - put restrictive, narrowing queries first
  
; slower
  (d/q '{:find [[?name ...]]
         :in [$ ?artist-name]
         :where [
                 [?release :release/name ?name]
                 [?release :release/artists ?artist]
                 [?artist :artist/name ?artist-name]
                 ]
         }
       (d/db conn) "Paul McCartney")
  
; faster
  (d/q '{:find [[?name ...]]
         :in [$ ?artist-name]
         :where [[?artist :artist/name ?artist-name] ; more seelctive clause
                 [?release :release/artists ?artist]
                 [?release :release/name ?name]]}
       (d/db conn) "Paul McCartney")


; The second query runs 50 times faster.


; entity identifiers

(def queryArtistByCountry '{:find [?artist-name]
                            :in [$ ?country ]
                            :where  [
                                     [?artist :artist/name ?artist-name]
                                     [?artist :artist/country ?country]
                                     ]
                            })

; lookup refs
(d/q queryArtistByCountry (d/db conn) [:country/name "Belgium"])

; ident
(d/q queryArtistByCountry (d/db conn) :country/BE)

; eid
(defn country-eid [name] (->> (d/q '{:find [(pull ?e [*])]
                              :in [$ ?name]
                              :where [[?e :country/name ?name]]}
                            (d/db conn) name)
                       ffirst first second
                       ))

(d/q queryArtistByCountry (d/db conn) (country-eid "Belgium"))


; dynamic query resolution

;fails
(d/q '{:find [[?artist-name ...]]
       :in [$ ?country [?reference ...]]
       :where [
               [?artist :artist/name ?artist-name]
               [?artist ?reference ?country]
               ]
       }
     (d/db conn) :country/BE [:artist/country]
     )

; solutions : A - don't do that (above) B - reolve eid yourself
(d/q '{:find [[?artist-name ...]]
       :in [$ ?country [?reference ...]]
       :where [
               [(datomic.api/entid $ ?country) ?country-id]
               [?artist :artist/name ?artist-name]
               [?artist ?reference ?country-id]
               
               ]
       }
     (d/db conn) :country/BE [:artist/country]
     )

  
  )


(comment 
  
  db
  
  (d/pull (cdb) '[*] 42)
  
  (pp/pprint *1)
  
  (d/pull (cdb) '[*] [:artist/name "Paul McCartney"])
  
  (d/pull (cdb) '[*] [:country/name "Bangladesh"])
  (d/pull (cdb) '[*] 17592186045729)
  (d/pull (cdb) '[*] :country/BD)
  
  (d/q '{:find [(pull ?a [:db/ident]) ?v]
         :in [$ ?name]
         :where [
                 [?e :artist/name ?name]
                 [?e ?a ?v]
                 ]}
       (cdb) "Paul McCartney"
       )
  
  (set! *print-length* 250)
  
  (d/q '[:find (count ?e)
         :where [?e :db/code]
         [?e ?k ?v]]
       db)
  
   (d/q '[:find (count ?e) .
          :where
          [?e]
          ]
        (d/db conn))
  )

