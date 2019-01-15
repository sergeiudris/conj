(ns free
  (:require [clj-time.core :as t]
            [nrepl.server :refer [start-server stop-server]]
            [clj-time.format :as f]
            [datomic.api :as d]))

(def db-uri "datomic:free://datomicdbfree:4334/codeq")

(d/create-database db-uri)
(def conn (d/connect db-uri))
(def db (d/db conn))


(defn -main []
  (println conn)
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))

(map first
     (d/q '[:find ?repo
            :where [?e :repo/uri ?repo]]
          db))

(map first
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

(defn foo
  "I don't do a whole lot."
  []
  (println "Hello, World!"))

(foo )
