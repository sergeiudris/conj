(ns core.reasoned-schemer
  (:require [ core.little-schemer :as lscm]
             [clojure.core.logic :refer :all]
             [clojure.repl :refer :all]
              )
  )

(defn hey [] "hey")

(hey)

(doc succeed)

(succeed 3)

(run* [q]
      succeed)

(run* [q]
      fail)

(source -run)


(run* [q]
      (== true q)
      )

(run* [q]
      succeed
      (== true q))


(run* [q]
      fail
      (== true q))

(run* [r]
      succeed
      (== :corn r)
      )

(run* [q]
       (let
        [x true]
         (== false x)))


(run* [q]
 (fresh [x]
  (== true x)
  (== true q)
  )
 )

(run* [x]
      (let 
       [x false]
        (fresh [x]
               (== true x)
               )
        ))

(run* [r]
      (fresh [x y]
             (== [x y] r )
             ))

(run* [r]
      (fresh [x]
             (let [y x]
               (fresh [x]
                      (== [y x y] r)
                      )
               )
             )
      )

(run* [q]
      (== false q)
      (== true q)
      )

(run* [q]
      (let [x q]
        (== true x)
        )
      )


(run* [r]
      (fresh [x]
             (== x r)))

(run* [q]
      (fresh [x]
             (== true x)
             (== x q)))



(run* [q]
      (fresh [x]
             (== x q)
             (== true x)
             ))

;; x and q are diff vars

(run* [q]
      (fresh [x]
             (== (= q x) q)))

(run* [q]
      (let [x q]
        (fresh [q]
               (== (= x q) x))))

(run* [q]
      (conde
       [succeed fail (== q true)]
       [succeed (== q true)]
       ))

(source run)
; (source bind-conde-clauses)

; condi looks and feels like conde . condi
; does not, however, wait until all the
; successful goals on a line are exhausted
; before it tries the next line.

(def anyo
  (fn [g]
    (conde
     [g succeed]
     [succeed g]
     )
    ))

(def alwayso (anyo succeed))
(def nevero (anyo fail))


(run 5 [q]
     (conde 
      [(== false q) alwayso]
      [(== true q) alwayso]
      [fail (== q fail)]
      ))


(run 2 [x]
     (conde
      [(== :extra x) succeed]
      [(== :virgin x) fail]
      [(== :olive x) succeed]
      [(== :oil x) succeed]
      [succeed nevero]))

(run* [r]
      (fresh [x y]
             (conde
              [(== :split x) (== :pea y)]
              [(== :navy x) (== :bean y)]
              [fail nevero]
              )
             (== [x y] r)
             )
      )

(run* [r]
      (fresh [x y]
             (conde
              [(== :split x) (== :pea y)]
              [(== :navy x) (== :bean y)]
              [fail nevero])
             (== [x y :soup] r)))

(def teacupo
  (fn [x]
    (conde 
     [(== :tea x) succeed]
     [(== :cup x) succeed]
     [succeed nevero]
     )
    ))

(run* [x]
      (teacupo x)
      )

(run* [r]
      (fresh [x y]
             (conde 
              [(teacupo x) (== true y) succeed]
              [(== false x) (== true y)]
              [succeed nevero]
              )
             (== [x y] r)
             )
      )

; conde will succeed when any cluase succeeds
; below, the first cluase succeeds, if y is associated with false and z is associated with fresh
; the second cluase succeeds, if y is associated with fresh and z is associated with false
; so r is a list of possible solutions to query: ( (false _.0) (0._ false) )


(run* [r]
      (fresh (x y z)
             (conde
              [(== y x) (fresh [x] (== z x))]
              [(fresh [x] (== z x)) (== z x)]
              [succeed nevero]
              )
             (== false x)
             (== [y z] r)
             )
      )

(run* [q]
      (let [a (== true q)
            b (== false q)
            ]
        b
        ))

(run* [q]
      (let [a (== true q)
            b (fresh [x]
                     (== x q)
                     (== false x)
                     )
            c (conde
               [(== true q) succeed]
               [succeed (== false q)]
               )
            ]
        b
        )
      )

(let [x (fn [a] a)
      y :c]
  (x y))


(run* [r]
      (fresh (y x)
             (== [x y] r)
             )
      )

(run* [r]
      (firsto '(a c o r n) r)
      )

(run* [q]
     (firsto '(a c o r n) 'a)
     (== true q)
     )

(run* [r]
      (fresh [x y]
             (firsto [r y] x)
             (== 'pear x)
             )
      )

(run* [r]
      (fresh [x y]
             (== (lcons x (lcons y 'salad)) r)))

(def caro
  (fn [p a]
    (fresh [d]
           (== (lcons a d) p)
           )
    ))


(run* [r]
      (caro '(a c o r n) r))


(run* [r]
      (fresh [x y]
             (caro '(grape raisin pear) x)
             (caro '((a) (b) (c)) y)
             (== (lcons x y) r)
             )
      )

(run* [r]
      (fresh (v)
             (resto '(a c o r n) v)
             (caro v r)
             )
      )

(def cdro
  (fn [p d]
    (fresh [a]
           (== (lcons a d ) p )
           )
    
    )
  )

(run* [r]
      (fresh (v)
             (cdro '(a c o r n) v)
             (caro v r)))

(run* [x]
      (cdro '(c o r n) [x 'r 'n])
      )

(run* [l]
      (fresh [x]
             (cdro l '(c o r n))
             (caro l x)
             (== 'a x)
             )
      )

(run* [l]
      (conso '(a b c ) '(d e) l )
      )

(run* [r]
      (fresh [x y z]
       ;       (== (list 'e 'a 'd x) r)
             (conso x '(e a d) r)
             
             (conso 3 '(a z c) r)
             )
      )

