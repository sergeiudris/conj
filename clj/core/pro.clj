(ns pro
  (:require [clj-time.core :as t]
            [clj-time.format :as f]
            [datomic_pro/datomic.api :as d]))

; (def cfg {:server-type :peer-server
;           :access-key "myaccesskey"
;           :secret "mysecret"
;           :endpoint "datomicdb:4334"})

; (def client (d/client cfg))
(def db-uri "datomic:dev://datomicdb:4334/hello")


(d/create-database db-uri)
(def conn (d/connect db-uri))
; (def conn (d/connect client {:db-name "hello"}))

(defn -main [] (println conn))