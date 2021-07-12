

(ns core.schema 
  (:require [datomic.api :as d]
            [clojure.repl :refer :all]))


(def db-uri "datomic:dev://datomicdb:4334/dayofdatomic")

(def conn (d/connect db-uri))

(defn cdb [] (d/db conn))

(cdb)

;   (def design-data-0 (read-string (slurp "core/day2014/design-data-0.edn")))

; (d/transact conn design-data-0)

(comment 

;; find the idents of all schema elements in the system
(sort (d/q '[:find [?ident ...]
             :where [_ :db/ident ?ident]]
           (cdb)))


;; find just the attributes
(sort (d/q '[:find [?ident ...]
             :where 
             [?e :db/ident ?ident]
             [_ :db.install/attribute ?e]]
           (cdb)))


;; find jsut the data functions 
(sort (d/q '[:find [?ident ...]
             :where 
             [?e :db/ident ?ident]
             [_ :db.install/function ?e]]
           (cdb)))


;; documentation of a schema element
(-> (cdb) (d/entity :db.unique/identity) :db/doc)


;; complete details of a schema element
(-> (cdb) (d/entity :uuid) d/touch)

;; find all attributes w/ AVET index
(sort (d/q '[:find [?ident ...]
             :where
             [?e :db/ident ?ident]
             [?e :db/index true]
             [_ :db.install/attribute ?e]]
           (cdb)))

;; find attributes in the user namespace 
(sort (d/q '[:find [?ident ...]
             :where
             [?e :db/ident ?ident]
             [_ :db.install/attribute ?e]
             [(namespace ?ident) ?ns]
             [(= ?ns "db")]]
           (cdb)))

;; find all reference attributes
(sort (d/q '[:find [?ident ...]
             :where
             [?e :db/ident ?ident]
             [_ :db.install/attribute ?e]
             [?e :db/valueType :db.type/ref]]
           (cdb)))

;; cardinality-many
(sort (d/q '[:find [?ident ...]
             :where
             [?e :db/ident ?ident]
             [_ :db.install/attribute ?e]
             [?e :db/cardinality :db.cardinality/many]]
           (cdb)))


)