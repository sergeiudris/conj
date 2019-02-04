(ns aq.conn
  (:require [datomic.api :as d]
            [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.repl :refer :all]
            
            ))

; (def  db-uri "datomic:free://datomicdbfree:4334/hello")


(def db-uri "datomic:free://datomicdbfree:4334/mbrainz-1968-1973")


; (d/create-database db-uri)
; (d/delete-database db-uri)


(do
 (def conn (d/connect db-uri))
(def db (d/db conn))
(defn cdb [] (d/db conn))
 )






(comment


  (d/q '[:find ?e :in $] db)

(d/q '{:find [(count ?e) .]
       :where [
               [?e :artist/name]
               ]
       }
     (cdb)
     )

; first query!
  (map first
       (d/q '[:find ?repo
              :where [?e :repo/uri ?repo]]
            db))

  (map firstf
       (d/q '[:find ?ns
              :where
              [?e :clj/ns ?n]
              [?n :code/name ?ns]]
            db))

  (reduce (fn [agg [o d]]
            (update-in agg [o] (fnil conj []) d))
          {}
          (d/q '[:find ?op ?def
                 :where
                 [?e :clj/def ?d]
                 [?e :clj/defop ?op]
                 [?d :code/name ?def]]
               db))

  (+ 3 4)
  
  (find-ns 'aq.aq)

  (str {:a 3 :b {}})

  
  
  (pp/pprint (keys (ns-publics 'datomic.api)))
  
  (doc datomic.api/tempid)
  
  (d/tempid :db/user 12)
  
  )

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "I don't do a whole lot."))