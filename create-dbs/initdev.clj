(ns initdev
  (:require [datomic.api :as d]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]))




(def db-uri-dod "datomic:dev://datomicdb:4334/dayofdatomic")
(def db-uri-tutorial "datomic:dev://datomicdb:4334/tutorial")
(def db-uri-movies "datomic:dev://datomicdb:4334/movies")



(d/create-database db-uri-dod)
(d/create-database db-uri-tutorial)
(d/create-database db-uri-movies)








(defn respond-hello [request]
  {:status 200 :body "Hello, world!"})

(def routes
  (route/expand-routes
   #{["/health" :get respond-hello :route-name :health]}))

(defn create-server []
  (http/create-server
   {::http/routes routes
    ::http/type   :jetty
    ::http/host "0.0.0.0"
    ::http/port   8890}))

(defn start []
  (http/start (create-server)))

(defn -main []
  (prn "successfully created db dayofdatomic")
  (start))