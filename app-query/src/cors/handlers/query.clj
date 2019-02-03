(ns cors.handlers.query
  (:require [clojure.java.io :as io]
            [clojure.core.async :as async]
            [io.pedestal.log :as log]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.route.definition :refer [defroutes]]
            [io.pedestal.http.sse :as sse]
            [ring.util.response :as ring-resp]
            [ring.middleware.cors :as cors]
            [aq.conn]
            [aq.query]
            [datomic.api :as d]
            
            ))



(defn gen-resp [request]
  (let [{query-params :query-params} request
        {x :x} query-params
        ]
    {:status 200 
     :body (str {
                 :data (aq.query/get-paginted-entity :artist/name 10 0)
                 :query-params query-params
                 :random (Math/random)
                 :uuid (d/squuid)
                 :x x
                 })
     })
  )


(defn entity [request]
  (gen-resp request))

(prn 3)

