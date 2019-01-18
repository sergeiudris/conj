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


(def db (d/db conn))

(defn get-db [] (d/db conn))

(get-db)

(def all-movies-q '[:find ?e
                    :where [?e :movie/title]])



(d/q all-movies-q db)

(def all-titles-q '[:find ?movie-title
                    :where [_ :movie/title ?movie-title]])

(d/q all-titles-q db)

(def titles-from-1985 '[:find ?title
                        :where [?e :movie/title ?title]
                        [?e :movie/release-year 1985]])


(d/q titles-from-1985 db)

(def all-data-from-1985 '[:find ?title ?year ?genre
                          :where [?e :movie/title ?title]
                          [?e :movie/release-year ?year]
                          [?e :movie/genre ?genre]
                          [?e :movie/release-year 1985]])

(d/q all-data-from-1985 db)


(d/q '[:find ?e
       :where [?e :movie/title "Commando"]]
     db)

(defn get-commando-id [] 
  (ffirst (d/q '[:find ?e 
                 :where [?e :movie/title "Commando"]]
               (get-db))))

(def commando-id (get-commando-id))

; @(d/transact conn {:tx-data [{:db/id commando-id :movie/genre "future governor"}]})

(def extra-movies [ {:movie/title "The Matrix"
                     :movie/genre "science fiction"
                     :movie/release-year 1999}
                   ])

@(d/transact conn extra-movies)

(def db (d/db conn))



(comment
  (get-commando-id)

  (d/q all-titles-q db)

  (def commando-id (get-commando-id))

  commando-id

  ;;  Execution error (ClassNotFoundException) at java.net.URLClassLoader/findClass (URLClassLoader.java:466) . hello.d
  ;; solution - https://stackoverflow.com/questions/53139065/transacting-api-for-datomic
  (let [commando-id (get-commando-id)]
    (d/transact conn {:tx-data [{:db/id commando-id :movie/genre "future governor"}]}))



  (d/transact conn [{:db/id commando-id :movie/genre "future governor"}])

  (d/q all-data-from-1985 db)

  (def db (d/db conn))
  
  (dir d)
  
  (d/basis-t db)
  
  (d/next-t db)
  
  (def old-db (d/as-of db (d/basis-t db)))
  
  (d/q all-data-from-1985 old-db)
  
  (d/basis-t old-db)
  
  (def older-db (d/as-of db 1021))
  
  (d/q all-data-from-1985 older-db)
  
  (d/q all-data-from-1985 db)
  
  (def hdb (d/history db))
  
  (d/q '[:find ?genre
         :where [?e :movie/title "Commando"]
         [?e :movie/genre ?genre]]
       hdb)
  
  
  
  
  )




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

  (doc ffirst)
  
  (doc first)
  
  )