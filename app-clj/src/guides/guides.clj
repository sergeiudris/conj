(ns guides.guides
  (:require [clojure.repl :refer :all]
            [clojure.spec.alpha :as s]
            [clojure.pprint :as pp]
            )
)


(comment
  
  ;; https://clojure.org/guides/weird_characters
  
  
    ; (+ 1 ##Inf)
  
  (type #inst "2014-05-19T19:12:37.925-00:00")


  (read-string "#inst \"2014-05-19T19:12:37.925-00:00\"")


  (def x (atom 1))

  x

  @x

  (deref x)

  (def ^{:debug true} five 5)

  (meta #'five)

  (def ^:debug five 5)

  (meta #'five)

  (def ^Integer five 5)

  (meta #'five)

  (def ^Integer ^:debug ^:private five 5)

  (meta #'five)

;   (import (basex.core BaseXClient$EventNotifier)
  
; (defn- build-notifier [notifier-action]
;   (reify BaseXClient$EventNotifier
;     (notify [this value]
;       (notifier-action value))))
  
  (defmacro debug [body]
    #_=>   `(let [val# ~body]
              #_=>      (println "DEBUG: " val#)
              #_=>      val#))


  (debug (+ 2 2))



  (def five 5)

  `five

  `~five

  (def three-and-four (list 3 4))

  `(1 ~three-and-four)

  `(1 ~@three-and-four)

  (defmacro m [] `(let [x 1] x))

  ;  (m)
  
  (defmacro m [] `(let [x# 1] x#))

  (m)
  
  
  
  )


(comment
  
  ;; https://clojure.org/guides/destructuring
  
  
  
  )