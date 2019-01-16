
(ns learn.syntax
   (:require [nrepl.server :refer [start-server stop-server]]
             [clojure.pprint :refer [pprint]]))


42

(println 42)


(defn -main []
  (println "running learn.syntax")
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))