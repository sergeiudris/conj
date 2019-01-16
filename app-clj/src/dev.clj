; https://clojure.org/guides/repl/enhancing_your_repl_workflow


; Consider creating a 'dev' namespace in your project (e.g myproject.dev) 
; in which you define functions for automating common development tasks (for example: starting a local web server, 
; running a database query, turning on/off email sending, etc.)

(ns dev 
  (:require [clojure.pprint :as pp]
            [clojure.repl :refer :all]))

(defn myfn [] (pp/pprint "just saying.."))

