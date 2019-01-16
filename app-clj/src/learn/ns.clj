(ns learn.ns
  (:require [clojure.repl :refer :all]
            [clojure.pprint :as pp]
            [nrepl.server :refer [start-server stop-server]]))


(defn -main [] 
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))

(pp/pprint "hello")

; try in repl, not editor

; (require '[clojure.set :as cset :refer [union]])

; (type cset/union)

; cset/union

; union

; *ns*

; (ns myapp.foo.bar)
; 
; (in-ns 'user)

; (ns-aliases 'myapp.foo.bar)



; (def my-favorite-number 42)

; my-favorite-number

; (ns myapp.baz)

; myapp.foo.bar/my-favorite-number

; (require '[myapp.foo.bar :as foobar])

; foobar/my-favorite-number



; (in-ns 'myapp.never-created)

; (defn say-hello [x] (println "Hello, " x "!"))

; (clojure.core/refer-clojure)

; (defn say-hello [x] (println "Hello, " x "!"))

; (say-hello "Jane")


; example lib

; (ns com.my-company.clojure.examples.my-utils
;   (:import java.util.Date)
;   (:use [clojure.string :only (join)])
;   (:require [clojure.java.io :as jio]))


; prefix lists

; (require 'clojure.contrib.def 'clojure.contrib.except 'clojure.contrib.sql)
; (require '(clojure.contrib def except sql))


