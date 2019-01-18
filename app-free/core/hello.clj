(ns hello 
  (:require [nrepl.server :refer [start-server stop-server]]
            [clojure.repl :refer :all]
            [datomic.api :as d]
            [clojure.pprint :as pp]))

(def db-uri "datomic:free://datomicdbfree:4334/hello")

(d/create-database db-uri)

(def conn (d/connect db-uri))
(def db (d/db conn))

(def movie-schema (read-string (slurp "core/movie-schema.edn")))
@(d/transact conn movie-schema)

(def first-movies (read-string (slurp "core/movie-data-0.edn")))

@(d/transact conn first-movies)



movie-schema


; (def seattle-schema (read-string (slurp "core/seattle-schema.edn")))
; (def schema-tx (read-string (slurp "core/seattle-schema.edn")))
; @(d/transact conn schema-tx)




(defn -main [] 
  (prn "starting hello")
  (prn conn)
  (prn db)
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))




(comment
  
  (+)
  
  conn
  
  db
  (doc slurp)
  
  (apropos "source")
  
  (apropos "source")
  
  (source doc)
  
  (doc deref)
  
  )