(ns core.dev
  (:require [clj-time.core :as t]
            [nrepl.server :refer [start-server stop-server]]
            [clj-time.format :as f]
            [datomic.api :as d]
            [core.rules :refer (rules)]))

; (def cfg {:server-type :peer-server
;           :access-key "myaccesskey"
;           :secret "mysecret"
;           :endpoint "datomicdb:4334"})

; (def client (d/client cfg))

;   (def db-uri "datomic:dev://datomicdb:4334/dayofdatomic")



; (d/create-database db-uri)
; (def conn (d/connect db-uri))
; (def conn (d/connect client {:db-name "hello"}))

; (def db (d/db conn))


(defn -main [] 
;   (println conn)
  (println "started server on 7888")
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))

(comment
  
(d/create-database db-uri)
(def conn (d/connect db-uri))
; (def conn (d/connect client {:db-name "hello"}))

(def db (d/db conn))
  (def db-uri "datomic:dev://datomicdb:4334/dayofdatomic")
  
  (d/delete-database db-uri)
  
  (d/create-database db-uri)
  
  
  )