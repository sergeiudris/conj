

(ns dev.service
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
            [dev.routes]
            ))

(defroutes routes
  [[["/"   {:get  [::index dev.routes/health]}]
    ["/health" {:get [::health dev.routes/health]}]
    ]])



(def service {:env :prod
              ::http/routes routes
              ;; Allow services that are accessing this
              ;; service from a http-referer[sic] of http://localhost:8080.
              ;; All others are denied.
              ::http/allowed-origins ["*"]
              ::http/resource-path "/public"
              ::http/type :jetty
              ::http/host "0.0.0.0"
              ;; Run this service on port 8081 (not default).
              ::http/port 8080})
