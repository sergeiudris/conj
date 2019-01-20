(ns core.design
  (:require [nrepl.server :refer [start-server stop-server]]
            [clojure.repl :refer :all]
            [clojure.pprint :as pp]
            [core.dev ]
            [datomic.client.api :as d]
            [datomic.api :as dapi]))

(def cfg {:server-type :peer-server
          :access-key "myaccesskey"
          :secret "mysecret"
          :endpoint "datomicdbpeer:8998"})

(def client (d/client cfg))

(def conn (d/connect client {:db-name "dayofdatomic"}))

(def db (d/db conn))

(defn cdb [] (d/db conn))




(defn -main []
  (prn conn)
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))


(comment

  db
  

  (core.dev/echo)
  
  (dapi/squuid)
  

  (def design-schema-0 (read-string (slurp "core/day2014/design-schema-0.edn")))
  (def design-data-0 (read-string (slurp "core/day2014/design-data-0.edn")))

  (d/transact conn {:tx-data design-schema-0})
  (d/transact conn {:tx-data design-data-0})

;   (d/delete-database client {:db-name "dayofdatomic"})
  

  (d/q '[:find ?e
         :where [?e :record/text ?e]]
       (cdb))

  (d/q '[:find ?e ?text ?tags
         :where
         [?e :record/text ?text]
         [?e :record/tags ?tags]]
       (cdb))

  (d/q '[:find ?text
         :where [?e :idea/text ?text]]
       (cdb))


  (d/q '[:find ?text ?threads
         :where [?e :idea/text ?text]
         [?e :idea/threads ?threads]]
       (cdb))
  
  (d/q '[:find ?text
         :in $ ?uuid
         :where [?e :uuid ?uuid]
         [?e :record/text ?text]
         ]
       (cdb)  #uuid "5c444f10-c0f5-4eef-b004-ef9331b487a2")
  
  


; (keys (ns-publics 'd/db.type))
  
  ; entity/attribute/value/tx/op
  ;; op - operation
  )


(comment


  (core.dev/create-delete-dayofdatomic)
  
  
  )