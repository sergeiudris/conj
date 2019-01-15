(ns pro
  (:require [clj-time.core :as t]
            [nrepl.server :refer [start-server stop-server]]
            [clj-time.format :as f]
            [datomic.api :as d]))

; (def cfg {:server-type :peer-server
;           :access-key "myaccesskey"
;           :secret "mysecret"
;           :endpoint "datomicdb:4334"})

; (def client (d/client cfg))
(def db-uri "datomic:dev://datomicdb:4334/hello")


(d/create-database db-uri)
(def conn (d/connect db-uri))
; (def conn (d/connect client {:db-name "hello"}))




(defn -main [] 
  (println conn)
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))



@(d/transact conn [{:db/doc "Hello world"}])