(ns reference.core
  (:require [nrepl-server]
            [clojure.repl :refer :all]
            []
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

;; https://clojuredocs.org/clojure.core/transduce
;; https://clojure.org/reference/transducers   
;; http://blog.cognitect.com/blog/2014/8/6/transducers-are-coming

(comment
  
  (def xform (comp (map inc) (filter even?) ) )
  
  (sequence xform [1 2 3] )
  
  (transduce xform + 0 [1 2 3])
  
  (source transduce)
  
  (into [] xform [1 2 3])
  
  (iteration xform [1 2 3])
  
  (chan 1 xform)
  
  (def xf (comp (filter odd?) (take 10)))
  (transduce xf conj (range))
  (transduce xf conj [1 2 3])
  
  (transduce xf + [1 2 3])
  
  (transduce xf + 17  [1 2 3])
  (transduce xf str  [1 2 3])
  
  (transduce xf str "..."  [1 2 3])
  
  
  
  
  
  
  
  ;; like ->>
  
  (def xfrom->>
    (fn [xs]
      (->>
       xs
       (map inc)
       (filter  even?)
       )
      )
    )
  
  (xfrom->> [1 2 3])
  
  
  )