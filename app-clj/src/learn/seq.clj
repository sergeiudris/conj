
(ns learn.seq
  (:require [nrepl.server :refer [start-server stop-server]]
            [clojure.pprint :refer [pprint]]
            [clojure.repl :refer :all]))


(def cards '(10 :ace :jack 9))



(defn -main []
  (println "running learn sequatial collections")
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))