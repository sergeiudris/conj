(ns reference.core
  (:require [nrepl-server]
            [clojure.repl :refer :all]
            [clojure.pprint :as pp]
            ; [reference.sample :as sample ]
            ; [reference.sample.b.c :as sample.b.c]
            ; [reference.sample-c.d :as sample-c.d]
            ; [reference.sample_d.e :as sample_d.e]
            ; [reference.sample-d.e :as sample-d.e]
            
            ))


(comment
  (+)
  (nrepl-server/hi)
  (sample/hi)
  (sample.b.c/hi)
  (sample-c.d/hi)
  (sample_d.e/hi)
  
  
  
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
  
  
  (defn step1
    [ins]
    (let [{xs :xs} ins]
      (merge ins {:xs (mapv inc xs)})
      )
    )
  
  (defn step2
    [ins]
    (let [{xs :xs} ins]
      (merge ins {
                  :xs (filterv even? xs) 
                  })
      
      )
    
    )

  (defn log
    [ins]
    (pp/pprint ins)
    ins
    )
  
  (defn xsteps
    [ins]
    (->>
     ins
     log
     step1
     log
     step2
     log
     )
    )

  (xsteps {
           :xs  [1 2 3 4]
           })
  
  (into [] (map inc [1 2 3 4]))
  (mapv inc [1 2 3 4])
  


  )


(comment
  
  ;; https://clojure.org/reference/special_forms
  
  (defn constrained-sqr [x]
    {
     :pre [(pos? x)]
     :post [(> % 16) (< % 225)]
     }
    (* x x)
    )
  
  (constrained-sqr 14)
  
  (def factorial
    (fn [n]
      (loop [cnt n acc 1]
        (if (zero? cnt)
          acc
          (recur (dec cnt) (* acc cnt) )
          )
        )
      )
    )
  
  
  )


(comment
  
  ;;   https://clojure.org/reference/macros
  
  
  (doc macroexpand)
  
  (macroexpand '(when (pos? a) (println "positive") (/ b a) ))
  
  (macroexpand '(-> {} (assoc :a 1) (assoc :b 2) ) )
  
  
  
  )

(comment 
  
  ;; https://clojure.org/reference/data_structures
  

      
  
  
  )