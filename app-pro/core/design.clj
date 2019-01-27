(ns core.design
  (:require [nrepl.server :refer [start-server stop-server]]
            [clojure.repl :refer :all]
            [clojure.pprint :as pp]
            [core.dev ]
            [core.schema]
            [core.seattle]
            [core.mbrainz]
            [core.tx]
            [datomic.client.api :as d]
            [datomic.api :as dapi]))

(def cfg {:server-type :peer-server
          :access-key "myaccesskey"
          :secret "mysecret"
          :endpoint "datomicdbpeer:8998"})

(def client (d/client cfg))

(def uri "datomic:dev://datomicdb:4334/dayofdatomic")

(def peerconn (dapi/connect uri))

(def peerdb (dapi/db peerconn))

peerdb

(def conn (d/connect client {:db-name "dayofdatomic"}))


(def db (d/db conn))

(defn cdb [] (d/db conn))

(defn cpeerdb [] (dapi/db peerconn))



(defn -main []
  (prn conn)
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))


(comment

  db
  (dapi/squuid)


  (core.dev/echo)

  (dapi/squuid)


  (def design-schema-0 (read-string (slurp "resources/design-schema-0.edn")))
  (def design-data-0 (read-string (slurp "resources/design-data-0.edn")))


  (def sample-data (read-string (slurp "resources/sample.edn")))

  (def seattle-schema (read-string (slurp "resources/seattle-schema.edn")))
  (def seattle-data-0 (read-string (slurp "resources/seattle-data0.edn")))

  seattle-schema
  design-data-0

  (d/transact conn {:tx-data design-schema-0})
  (d/transact conn {:tx-data design-data-0})

  (d/transact conn {:tx-data sample-data})
  (d/transact conn {:tx-data seattle-schema})
  (d/transact conn {:tx-data seattle-data-0})


  (dapi/transact peerconn seattle-schema)
  (dapi/transact peerconn seattle-data-0)

  (dapi/transact peerconn design-schema-0)
  (dapi/transact peerconn design-data-0)


  (def results (dapi/q '[:find ?c :where [?c :community/name]] peerdb))

  (def results (dapi/q '[:find ?c :where [?c :uuid]] peerdb))

  (dapi/q '[:find ?e :where [?e :uuid]] (dapi/db peerconn))

  (dapi/q '[:find ?e
            :in $ ?uuid
            :where [?e :uuid ?uuid]] (dapi/db peerconn) #uuid "5c444f10-c0f5-4eef-b004-ef9331b487a2")

  (dapi/touch (dapi/entity (cpeerdb) 17592186045428))

  ; caridanlity many is concatinated, not replaced
  (dapi/transact peerconn '[{:uuid #uuid "5c444f10-c0f5-4eef-b004-ef9331b487a2"
                             :record/tags ["hello"]}])



  (dapi/touch (dapi/entity (cpeerdb) 17592186045428))
  (dapi/touch (dapi/entity (cpeerdb) 17592186045429))
  (dapi/touch (dapi/entity (cpeerdb) 17592186045430))
  (dapi/touch (dapi/entity (cpeerdb) 17592186045431))
  (dapi/touch (dapi/entity (cpeerdb) 17592186045432))
  (dapi/touch (dapi/entity (cpeerdb) 17592186045433))
  (dapi/touch (dapi/entity (cpeerdb) 17592186045429))


  (defn touch [eid] (dapi/touch (dapi/entity (cpeerdb) eid)))
  
  (defn find-by-uuid [uuid]
    (->> (dapi/q '[:find ?e
                   :in $ ?uuid
                   :where [?e :uuid ?uuid]] (dapi/db peerconn) uuid)
         first first touch
         ))


  (find-by-uuid  #uuid "5c44516b-0ffb-463c-846c-cc6c71227ea0")

  (touch )

; bin/datomic -Xmx1g -Xms1g backup-db datomic:dev://0.0.0.0:4334/mbrainz-1968-1973 file:/opt/backups/mbrainz-1968-1973
  
  (count results)

;   (d/delete-database client {:db-name "dayofdatomic"})
  

  (Math/pow 2 24)
  
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
         [?e :record/text ?text]]
       (cdb)  #uuid "5c444f10-c0f5-4eef-b004-ef9331b487a2")

  (d/q '[:find ?items
         :in $ ?uuid
         :where [?e :idea/design-items ?items]]
       (cdb) #uuid "5c44516b-0ffb-463c-846c-cc6c71227ea0")

; https://github.com/Datomic/day-of-datomic/blob/master/tutorial/query.clj#L63
  
  (d/q '[:find  [?design-name ...]
         :in $ ?uuid
         :where [?idea :uuid ?uuid]
         [?item :idea/design-items ?idea]
         [?design :uuid ?item]
         [?design :design/name ?design-name]]
       (cdb) #uuid "5c44516b-0ffb-463c-846c-cc6c71227ea0")




  (d/q '[:find ?email ?record
         :where [?u :user/email ?email]
         [?u :record/user ?record]]
       (cdb))

  ; find idea text and record text
  (d/q '{:find [?idea-text ?record-text]
         :where [
                 [?idea :idea/text ?idea-text]
                 [?idea :idea/records ?record]
                 [?record :record/text ?record-text]
                 ]}
       (cdb)
       )

    ; find idea text and record 
  (d/q '{:find [?idea-text (pull ?record [*])]
         :where [[?idea :idea/text ?idea-text]
                 [?idea :idea/records ?record]
                ;  [?record :record/text ?record-text]
                 ]}
       (cdb))

  (d/q '{:find [(pull ?e [*]) ]
         :where [
                 [?e :uuid]
                 ]}
       (cdb)
       )

    (d/q '{:find [?e (pull ?tx [*])]
           :where [[?e :uuid ?val ?tx]]}
         (cdb))

  (pp/pprint *1)
  (count *1)

(d/q '[:find (count ?e ) 
         :where [?e :db/ident]
         [?e ?k ?v]]
       db)
  
  
; (keys (ns-publics 'd/db.type))
  
  ; entity/attribute/value/tx/op
  ;; op - operation
  

  
  )

(comment

  

  (dapi/attribute (cdb) :idea/designs)
  
  (keys (ns-publics 'datomic.client.api))
  (keys (ns-publics 'datomic.api))
  
  
  )


(comment


  (core.dev/create-delete-dayofdatomic)
  
  
  )
