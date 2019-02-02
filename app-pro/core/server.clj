(ns core.server
  (:require [datomic.api :as d]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            ))



(defn gen-resp []
  {:status 200 :body "ok"})

(defn respond-hello [request]
  (gen-resp))

(def routes
  (route/expand-routes
   #{["/health" :get respond-hello :route-name :health]}))

(defn create-server []
  (http/create-server
   {::http/routes routes
    ::http/type   :jetty
    ::http/host "0.0.0.0"
    ::http/port   8890
    ; io.pedestal.http/allowed-origins ["*"]
    ::http/allowed-origins ["*"]
    }))

(defn start []
  (http/start (create-server)))


(defn -main []
  (prn "starting server on 8890")
  (start)
  )

(comment


  (respond-hello 1)

  (defn gen-resp []
    {:status 200 :body "!Applause!"})
  
  )