(ns core.day2014
  (:require [nrepl.server :refer [start-server stop-server]]
            [clojure.repl :refer :all]
            [clojure.pprint :as pp]
            [datomic.client.api :as d]))

(def cfg {:server-type :peer-server
             :access-key "myaccesskey"
             :secret "mysecret"
             :endpoint "datomicdbpeer:8998"})

(def client (d/client cfg))

(def conn (d/connect client {:db-name "dayofdatomic"}))

(def db (d/db conn))





(defn -main []
  (prn conn)
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))


(comment 
  
  db
  
  ; entity/attribute/value/tx/op
  ;; op - operation
  
  
  
  )