(ns dev.routes
  (:require [clojure.repl :refer :all]))




(defn gen-resp []
  {:status 200 :body (str {:data "ok!"})})

(defn health [request]
  (gen-resp))

