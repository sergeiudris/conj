; Copyright 2013 Relevance, Inc.
; Copyright 2014 Cognitect, Inc.

; The use and distribution terms for this software are covered by the
; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0)
; which can be found in the file epl-v10.html at the root of this distribution.
;
; By using this software in any fashion, you are agreeing to be bound by
; the terms of this license.
;
; You must not remove this notice, or any other, from this software.

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
