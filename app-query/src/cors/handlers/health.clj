(ns cors.handlers.health
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
            [aq.query]))




(defn gen-resp []
  {:status 200 :body (str {:data "ok"})})

(defn handler [request]
  (gen-resp))

