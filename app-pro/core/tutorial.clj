(ns tutorial 
  (:require [nrepl.server :refer [start-server stop-server]]
            [clojure.repl :refer :all]
            [clojure.pprint :as pp]
            [datomic.api :as d]))

(def db-uri "datomic:dev://datomicdb:4334/tutorial")

(d/create-database db-uri)

(def conn (d/connect db-uri))
(def db (d/db conn))

(defn -main [] 
  (prn conn)
  (prn db)
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))