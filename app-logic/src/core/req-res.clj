(ns core.req-res
  (:require [clojure.repl :refer :all]
            [clojure.core.logic :refer :all]
            [clojure.core.logic.fd :as fd]
            
            )
  )

(defn hi 
  "i am a non-intrusive function"
  []
  "hi"
  )

(comment
  
  (doc fd/==)
  
  (doc fd/in)
  
  (doc fd/interval)
  

  
  ; https://spin.atomicobject.com/2015/12/07/logic-programming-clojure-palindromes/
  ; https://spin.atomicobject.com/2015/12/14/logic-programming-clojure-finite-domain-constraints/
  
  (defn reverso [l r]
    (conde
     [(== l ()) (== r ())]
     [(fresh [la ld ldr]
             (conso la ld l)
             (appendo ldr (list la) r)
             (reverso ld ldr))]))
  
  (defn palindromo [v]
    (fresh [r]
           (== v r)
           (reverso v r)))
  
  (defn everyo [l f]
    (fresh [head tail]
           (conde
            [(== l ())]
            [(conso head tail l)
             (f head)
             (everyo tail f)])))
  
  
  (defn sumo [l sum]
    (fresh [a d sum-of-remaining]
           (conde
            [(== l ()) (== sum 0)]
            [(conso a d l)
             (fd/+ a sum-of-remaining sum)
             (sumo d sum-of-remaining)])))
  
  (defn find-palindromes-totalling [sum results]
    (let [domain (fd/interval 1 1000)]
      (run results [q]
           (palindromo q)
           (everyo q #(fd/in % domain))
           (sumo q sum))))
  
  
  (find-palindromes-totalling 20 10)
  
  (run 10 [q]
       (palindromo q)
       )
  
  (run 1 [q]
       (palindromo '(1 2 8 3 7 3 8 2 1))
       (== q "hooray!"))
  

  (defn find-palindromes-totalling-2 [interval sum results]
    (let [domain (fd/interval 1 interval)]
      (run results [q]
           (palindromo q)
           (everyo q #(fd/in % domain))
           (sumo q sum))))


  (find-palindromes-totalling-2 1000 20 10)
  
  )


