(ns nrepl-server
  (:require [nrepl.server :refer [start-server stop-server]]
            [clj-time.core :as t]
            [clj-time.format :as f]))


  (defn hi [] (println "hi"))

; (defn -main [] 
;   (hi)
;   (future
;     (run!
;      print-number-and-wait
;      (range))))

(defn -main []
  (hi)
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))



(comment

  (defn print-number-and-wait
    [i]
    (println i "green bottles, standing on the wall. ♫")
    (Thread/sleep 60000))


  (nrepl-server/hi)

  (user/hey)

  (+)

  (defn yay [] (println "yay")))