(ns learn.learn
  (:require [clojure.repl :refer :all]
            [clojure.pprint :as pp]
            [learn.functions :as functions]
            [nrepl.server :refer [start-server stop-server]]))

(println 0101)

(println (functions/greet 3))

(println 0101)

(defn -main []
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))