(ns core.reasoned-schemer
  (:require [ core.little-schemer :refer [car null? atom? cdr ]]
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


(run* [x]
      (conso x '(a b c) '(d a b c)))


(llist 'a 'b)

(doc ==)
(doc conso)
(doc lcons)

;; 2.14

;; does not work
(run* [r]
      (fresh [x y z]
             (== (list 'e 'a 'd x) r)
             (conso y (list 'a 'z 'c) r)))

;; works 
;; https://github.com/philoskim/reasoned-schemer-for-clojure/blob/master/src/rs/ch2.clj#L173
(run* [r]
      (fresh [x y z]
             (== (list 'e 'a 'd x) r)
             (conso y (list 'a z 'c) r)))



(run* [r]
      (fresh [x y z]
             (== (list 'e 'a 'd x) r)))

;; 2.25
(run* [x]
      (conso x (list 'a x 'c) (list 'd 'a x 'c) ))


;; 2.26
(run* [l]
      (fresh [x]
             (== (list 'd 'a x 'c) l)
             (conso x (list 'a x 'c) l)
             ))

;; 2.27
(run* [l]
      (fresh [x]
             (conso x (list 'a x 'c) l)
             (== (list 'd 'a x 'c) l)
             ))

;; 2.28
; (defn conso [a d l]
;   (== (lcons a d) l)
;   )
; (doc conso)
; (source conso)


;; 2.29

(run* [l]
      (fresh [d x y w s]
             (conso w (list 'a 'n 's) s)
             (cdro l s)
             (caro l x)
             (== 'b x)
             (cdro l d)
             (caro d y)
             (== 'e y)
             ))

(def nullo emptyo)
;; 2.32
(run* [q]
      (nullo (list 'grape))
      (== true q)
      )

;; 2.33
(run* [q]
      (nullo (list))
      (== true q))

;; 2.34
(run* [q]
      (nullo q))

;; 2.35

; (defn nullo [l]
;   (== l '())
;   )


(doc eqo)

;; 2.52

(run* [r]
      (fresh [x y]
             (== (lcons x (lcons y 'salad)) r )
             )
      )

;; 2.53
(doc pairo)
(defn pairo [p]
  (fresh [a d]
         (conso a d p)
         )
  )

;; 2.54
(run* [q]
      (pairo (lcons q q))
      (== true q)
      )


;; 2.55


(run* [q]
      (pairo '())
      (== true q)
      )

(run* [x]
      (pairo x)
      )

;; seeing old friends in new ways

;; 3.1

; (defn list? [l]
;   (cond
;     (lscm/null? l) true
;     (lscm/a-pair? l) (list? (cdr l))
;     :else false
;     ) 
;   )


(doc listo)

;; 3.5

(defn listo [l]
  (conde
   [(nullo l) succeed]
   [(pairo l) (fresh 
               [d]
               (cdro l d)
               (listo d)
               )]
   [succeed fail]
   
   )
  )

(run* [q]
      (listo (llist 'a 'b))
      (== q true)
      )


;; 3.10
(run 1 [x]
     (listo (llist 'a 'b 'c x)))

;; 3.13
;; infine loop , don't run
; (run* [x]
;      (listo (llist 'a 'b 'c x)))

;; 3.14
(run 5 [x]
     (listo (llist 'a 'b 'c x)))



;; 3.16

(defn lol? [l]
  (cond 
    (null? l) true
    (seq? (car l)) (lol? (cdr l))
    :else false
    )
  )

;; 3.17

(defn lolo [l]
  (conde
   [(nullo l)  succeed]
   [(fresh [a]
           (caro l a)
           (listo a))
    (fresh [d]
           (cdro l d)
           (lolo d)
           )
    
    ]
   [succeed fail]
   )
  )

;; 3.20
(run 1 [l]
     (lolo l)
     )



;; 3.21
(run* [q]
      (fresh [x y]
             (lolo (list (list 'a 'b) (list x 'c) (list 'd y) ))
             (== true q)
             )
      )



