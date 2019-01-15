(ns free
  (:require [clj-time.core :as t]
            [clj-time.format :as f]
            [datomic.api :as d]))

(def db-uri "datomic:free://datomicdbfree:4334/hello")

(d/create-database db-uri)
(def conn (d/connect db-uri))

(defn -main [] (println conn))
