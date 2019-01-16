
(ns learn.repl-enhanced
  (:require [nrepl.server :refer [start-server stop-server]]
            [clojure.pprint :refer [pprint pp print-table]]
            [clojure.repl :refer :all]))




(defn -main []
  (println "running learn repl")
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))
