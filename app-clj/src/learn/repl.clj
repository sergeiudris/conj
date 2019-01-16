
(ns learn.repl
  (:require [nrepl.server :refer [start-server stop-server]]
            [clojure.pprint :refer [pprint]]
            [clojure.repl :refer :all]))


; Origin of apropos
; 1660–70; < French à propos literally, to purpose < Latin ad prōpositum. See ad-, proposition
(apropos "index")



(defn -main []
  (println "running learn repl")
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))