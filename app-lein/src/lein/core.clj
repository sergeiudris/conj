(ns lein.core
  (:require [datomic.api :as d]))

(def  uri "datomic:free://localhost:4334/git")

(d/create-database uri)

(def conn (d/connect uri))

conn

(def db (d/db conn))

(d/q '[:find ?e :in $] db)

; first query!
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
  [x]
  (println x "Hello, World!"))


(+ 3 4)

; (hello)

; (h1)






