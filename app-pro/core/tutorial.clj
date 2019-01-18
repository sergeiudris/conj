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

(comment
  
  (d/q '[:find ?e
         :where [?e :age 42]] db)
  
  (d/q '[:find ?e
         :where [?e :age 42]] '[[sally :age 21]
                                [fred :age 42]
                                [ethel :age 42]
                                [fred :likes pizza]
                                [sally :likes opera]
                                [ethel :likes sushi]])
  
  )