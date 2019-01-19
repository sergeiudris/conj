(ns core.movies
  (:require [nrepl.server :refer [start-server stop-server]]
            [clojure.repl :refer :all]
            [clojure.pprint :as pp]
            [datomic.client.api :as d]))

(def cfg {:server-type :peer-server
             :access-key "myaccesskey"
             :secret "mysecret"
             :endpoint "datomicdbprod:4334"})

(def client (d/client cfg))

(def conn (d/connect client {:db-name "dayofdatomic"}))

(def db (d/db conn))





(defn -main []
  (prn conn)
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))


(comment 
  
  db
  
  (def movie-schema [{:db/ident :movie/title
                      :db/valueType :db.type/string
                      :db/cardinality :db.cardinality/one
                      :db/doc "The title of the movie"}

                     {:db/ident :movie/genre
                      :db/valueType :db.type/string
                      :db/cardinality :db.cardinality/one
                      :db/doc "The genre of the movie"}

                     {:db/ident :movie/release-year
                      :db/valueType :db.type/long
                      :db/cardinality :db.cardinality/one
                      :db/doc "The year the movie was released in theaters"}])
  
  (d/transact conn {:tx-data movie-schema})
  
  (def first-movies [{:movie/title "The Goonies"
                      :movie/genre "action/adventure"
                      :movie/release-year 1985}
                     {:movie/title "Commando"
                      :movie/genre "action/adventure"
                      :movie/release-year 1985}
                     {:movie/title "Repo Man"
                      :movie/genre "punk dystopia"
                      :movie/release-year 1984}])
  
  (d/transact conn {:tx-data first-movies})
  
  (def db (d/db conn))
  
  db
  
  (def all-movies-q '[:find ?e
                      :where [?e :movie/title]])
  
  (d/q all-movies-q db)
  
  
  
  )