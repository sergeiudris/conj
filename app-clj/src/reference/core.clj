(ns reference.core
  (:require [nrepl-server]
            [clojure.repl :refer :all]
            ))


(comment
  (+)
  (nrepl-server/hi)
  
  )

(defn -main []
  (nrepl-server/-main))


;; https://clojure.org/reference

(comment
  
  (def m #:person{:first "Han"
                  :last "Solo"
                  :ship #:ship{:name "Millenium Falcon"
                               :model "YT-1300f ligth freighter"
                               }})

    \u03A9
  
  
  )