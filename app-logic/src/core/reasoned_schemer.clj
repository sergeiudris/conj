(ns core.reasoned-schemer
  (:require [ core.little-schemer :refer [car null? atom? cdr equal? a-pair? ]]
            [clojure.core.logic :refer :all]
            [clojure.repl :refer :all]
            [clojure.pprint :as pp]
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
      (pairo (llist 1 2 3))
      (== true q)
      )

;; 2.56
(run* [q]
      (pairo 'pair)
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

;; 3.22
(run 1 [q]
     (fresh [x]
            (lolo (llist (list 'a 'b) x ))
            (== true q)
            )
     )

;; 3.23
(run 1 [x]
     (lolo (llist '(a b) '(c d) x)))


;; 3.24
(run 5 [x]
     (lolo (llist '(a b) '(c d) x))
     )


(run* [q]
     (conso 'x () q))

(run* [q]
      (conso 'x 'x q))


;; 3.31
(defn twinso
  [s]
  (fresh [x y]
         (conso x y s)
         (conso x '() y)
         )
  )

(run* [q]
      (twinso '(a a))
      (== q true)
      )

;; 3.32

(run* [q]
      (twinso '(tofu tofu))
      (== true q)
      )

;; 3.36

(defn twinso [s]
  (fresh [x]
         (== (list x x) s)
         )
  )

(run* [q]
      (twinso  (list 'a 'c) )
      (== q true)
      )


(run* [q]
      (twinso  (list 'a 'a))
      (== q true))

;; 3.37

(defn loto [l]
  (conde 
   [ (nullo l) succeed ]
   [ (fresh [a]
            (caro l a)
            (twinso a)) 
    (fresh [d]
           (cdro l d)
           (loto d)
           )]
   [succeed fail]
   )
  )

;; 3.38

(run 1 [z]
     (loto (llist '(g g) z) )
     )



;; 3.42

(run 5 [z]
     (loto (llist '(g g) z ))
     )

(run 5 [r]
     (fresh [w x y z]
            (loto (llist '(g g) (list 'e w ) (list x y) z ))
            (== (list w (list x y) z ) r)
            )
     )

;; 3.47

(run 3 [out]
     (fresh [w x y z]
            (== (llist '(g g) (list 'e w) (list x y) z ) out )
            (loto out)
            )
     )

;; 3.48

(defn listofo 
  [predo l]
  (conde
   [(nullo l) succeed]
   [(fresh [a]
           (caro l a)
           (predo a)
           )
    (fresh [d]
           (cdro l d)
           (listofo predo d)
           )
    ]
   [succeed fail]
   
   )
  )

;; 3.49

(run 3 [out]
     (fresh [w x y z]
            (== (llist '(g g) (list 'e w) (list x y) z ) out)
            (listofo twinso out)
            )
     )


;; 3.50

(defn loto [l]
  (listofo twinso l)
  )

(defn eq-car?
  [l a]
  (equal? (car l) a)
  )

(defn eq-caro
  [l x]
  (caro l x)
  )

;; 3.54

(defn -membero
  [x l]
  (conde 
   [(nullo l) fail] ;; unnecessary ?
   [(eq-caro l x) succeed]
   [succeed  (fresh [d]
                    (cdro l d)
                    (membero x d)
                    ) ]
   )
  )


(run* [q]
     (-membero q (list 1 2 3))
     )

(doc membero)

;; 3.57

(run* [q]
      (-membero 'olive (list 'virgin 'olive 'oil))
      (== true q)
      )

(run* [y]
      (-membero y '(hummus with pita))
      )

;; 3.65

(defn -identity
  [l]
  (run* [q]
         (-membero  q l)
        )
  )

(-identity '(1 2 3))

;; 3.69

(run 1 [x]
     (-membero 'e (list 'pasta 'e x 'fagioli))
     )



;; 3.70


(run 1 [x]
     (-membero 'e (list 'pasta x 'e 'fagioli)))


;; 3.71

(run* [r]
      (fresh [x y]
             (-membero 'e (list 'pasta x 'fagioli y))
             (== (list x y) r)
             
             )
      )

;; 3.73

(run 1 [l]
     (-membero 'tofu l)
     )


;; 3.76

(run 5 [l]
     (-membero 'tofu l))


;; 3.93

(doc pmembero)

(defn pmembero
  [x l]
  (conde
   [(eq-caro l x) (fresh [a d]
                         (cdro l (llist a d))
                         )]
   [(eq-caro l x) (cdro l '()) ]
   [succeed (fresh [d]
                   (cdro l d)
                   (pmembero x d)
                   )]
   )
  )

;; 3.94

(run 12 [l]
     (pmembero 'tofu l)
     )

;; 3.95

(defn first-value
  [l]
  (run 1 [y]
      (-membero y l) 
       )
  )

(first-value '( 2 3 4 ))

;; 3.98 3.101 not working, maybe lack of else statement

(defn memberrevo
  [x l]
  (conde
   [(nullo l) fail]
   [succeed  (fresh [d]
                    (cdro l d)
                    (memberrevo x d)
                    ) ]
   [succeed (eq-caro l x)  ]
   )
  )

(run* [q]
      (memberrevo q '(1 2 3)))

(run* [x]
      (memberrevo x '(pasta e fagioli)))

;; 3.101 not working
(defn reverse-list [l]
  (run* [y]
        (memberrevo y l)
        )
  )

;; chapter 4: members only


;; 4.7

(defn memo 
  [x l out]
  (conde 
   [(nullo l) fail]
   [(eq-caro l x) (== l out)]
   [succeed (fresh [d]
                   (cdro l d)
                   (memo x d out)
                   )]
   )
  )

;; 4.9

(run 1 [out]
     (memo 'tofu '(a b tofu d tofu e) out))


;; 4.11

(run 1 [out]
     (fresh [x]
            (memo 'tofu (list 'a 'b x 'd 'tofu 'e) out)
            )
     )

;; 4.15

(run* [x]
      (memo 'tofu '(tofu e) (list x 'e) )
      )


;; 4.17

(run* [out]
      (fresh [x]
             (memo 'tofu (list 'a 'b x 'd 'tofu 'e) out)
             )
      )

;; 4.18

(run 12 [z]
     (fresh [u]
            (memo 'tofu (llist 'a 'b 'tofu 'd 'tofu 'e z) u)
            )
     )


;; 4.21

(defn memo
  [x l out]
  (conde 
   [(eq-caro l x) (== l out)]
   [succeed (fresh [d]
                   (cdro l d)
                   (memo x d out)
                   )]
   )
  )

(run* [out]
      (fresh [x]
             (memo 'tofu (list 'a 'b x 'd 'tofu 'e) out)))


;; 4.24

(doc rembero)

(defn -rembero
  [x l out]
  (conde 
   [(emptyo l) (== '() out) ]
   [(eq-caro l x) (cdro l out)]
   [succeed  (fresh [res]
                    (fresh [d]
                           (cdro l d)
                           (-rembero x d res)
                           )
                    (fresh [a]
                           (caro l a)
                           (conso a res out)
                           )
                    
                    )  ]
   )
  )

(defn -rembero
  [x l out]
  (conde
   [(emptyo l) (== '() out)]
   [(eq-caro l x) (cdro l out)]
   [succeed  (fresh [res d a]
                    (cdro l d)
                    (-rembero x d res)
                    (caro l a)
                    (conso a res out))
    ]
   )
  )


(defn -rembero
  [x l out]
  (conde
   [(emptyo l) (== '() out)]
   [(eq-caro l x) (cdro l out)]
   [succeed  (fresh [res d a]
                    (conso a d l)
                    (-rembero x d res)
                    (conso a res out))]))


(run* [q]
      (-rembero 3 '(1 2 3) q )
      )

(run* [q]
      (-rembero q '(1 2 3) '(1 3)))

(run* [q]
      (-rembero 1 '(1 2 3) q))

;; 4.30

(run 1 [out]
     (fresh [y]
        (-rembero 'peas (list 'a 'b y 'd 'peas 'e) out)    
            )
     )

(run 2 [out]
     (fresh [y]
            (-rembero 'peas (list 'a 'b y 'd 'peas 'e) out)))

;; 4.31

(run* [out]
      (fresh [y z]
             (-rembero y (list 'a 'b y 'd z 'e) out)
             )
      )


;; 4.38

(run* [r]
      (fresh [y z]
             (-rembero y (list y 'd z 'e) (list y 'd 'e) )
             (== (list y z) r)
             )
      )

;; 4.57

(->>
 (run 13 [w]
      (fresh [y z out]
             (-rembero y (llist 'a 'b y 'd z w) out)))
 pp/pprint
 )

;; 4.68 

(defn surpriso [s]
  (-rembero s '(a b c) '(a b c) )
  )

;; 4.69

(run* [r]
      (== 'd r)
      (surpriso r)
      )

;; 4.70

(run* [r]
      (surpriso r)
      )

;; 4.72

(run* [r]
      (== 'b r)
      (surpriso r)
      )

(run* [r]
      (surpriso r)
      (== 'b r)
      )

(run* [q]
  (-rembero 'b '(a b c) '(a b c))
)


;; chapter 5 duble your fun

(doc append)

(defn append
  [l s]
  (cond
    (null? l) s
    :else (lcons (car l) (append (cdr l) s) )
    )
  )

(append '(1 2) '(3 4))

;; 5.6

(append '(d e ) 'a)

;; 5.9

(defn -appendo [l s out]
  (conde 
   [(emptyo l) (== s out) ]
   [succeed (fresh [a dl ds r]
                   (conso a dl l)
                   (conso a s ds)
                   (-appendo dl ds r)
                   (== r out)
                   )]
   )
  )

(run* [q]
      (-appendo '(1 2 3) '(4 5) q)
      )

(defn -appendo [l s out]
  (conde
   [(emptyo l) (== s out)]
   [succeed (fresh [a d res]
                   (conso a d l)
                   (-appendo d s res)
                   (conso a res out)
                   )]))

(run* [q]
      (-appendo '(1 2 3) '(4 5) q))

;; 5.12

(run* [x]
      (fresh [y]
             (-appendo '(a b c d) y x )
             )
      )

;; 5.13

(run 1 [x]
     (fresh [y]
            (-appendo (llist 'cake 'with 'ice y) '( d t) x )
            )
     )

;; 5.14


(run 1 [y]
     (fresh [x]
            (-appendo (llist 'cake 'with 'ice y) '(d t) x)))


;; 5.16

(->>
 (run 5 [x]
      (fresh [y]
             (-appendo (llist 'a 'b 'c y) '(d t) x)))
 pp/pprint
 )

;; 5.20

(->>
 (run 5 [x]
      (fresh [y]
             (-appendo (llist 'a 'b 'c y) (llist 'd 't y) x)))
 pp/pprint)



; 5.23

(->>
 (run 6 [x]
      (fresh [y]
             (appendo x y (list 'a 'b 'c 'd 'e) )))
 pp/pprint
 )

; 5.25

(->>
 (run 6 [y]
      (fresh [x]
             (appendo x y (list 'a 'b 'c 'd 'e))))
 pp/pprint)

; 5.27

(->>
 (run 6 [r]
      (fresh [x y]
             (appendo x y (list 'a 'b 'c 'd 'e))
             (== (list x y) r)
             )
      )
 pp/pprint)

; 5.31

(defn -appendo [l s out]
  (conde
   [(emptyo l) (== s out)]
   [succeed (fresh [a d res]
                   (conso a d l)
                   (conso a res out)
                   (-appendo d s res)
                   )]))


; 5.32
; works because we redefined -appedo, swapping last two goals
(run 7 [r]
     (fresh [x y]
            (-appendo x y '(a b c d e) )
            (== (list x y) r)
            )
     )

; 5.37

(->>
 (run 7 [r]
      (fresh [x y z]
             (-appendo x y z)
             (== (list x y z) r)))
 pp/pprint
 )

; 5.38 

(defn -swappendo 
  [l s out]
  (conde
   [succeed (fresh [a d res]
                   (conso a d l)
                   (conso a res out)
                   (-swappendo d s res))]
   [succeed (emptyo l) (== s out)]

   
   ))

; 5.39 - does have a value...

(run 1 [z]
     (fresh [x y]
            (-swappendo x y z )
            )
     )


; 5.41

(defn unwrap
  [x]
  (cond
    (a-pair? x) (unwrap (car x) )
    :else x
    ) 
  )

(unwrap '(((x))))


; 5.45

(defn unwrapo
  [x out]
  (conde 
   [(pairo x) (fresh [a]
                     (caro x a)
                     (unwrapo a out)
                     )]
   [succeed (== x out) ]
   
   )
  )

; 5.46

(run* [x]
      (unwrapo '((((P)))) x)
      )

; 5.48

(run 1 [x]
      (unwrapo x 'z))


; 5.52

(defn unwrapo
  [x out]
  (conde
   [succeed (== x out)]
   [succeed (fresh [a]
                     (caro x a)
                     (unwrapo a out))]
   
   ))

; 5.53

(run 5 [x]
     (unwrapo x 'z))

; 5.54

(run 5 [x]
     (unwrapo (list (list x)) 'z))


; 5.58 

(defn -null? [l]
  (cond
    ( and (coll? l) (empty? l) ) true
    :else false
    )
  )
(coll? 'x)

(defn append
  [l s]
  (cond
    (-null? l) s
    :else (cons (car l) (append (cdr l) s))))

(defn flatten [s]
  (cond 
    (-null? s) '()
    (a-pair? s) (append (flatten (car s)) (flatten (cdr s)) )
    (atom? s) (cons s '())
    :else (cons (car s) '())
    )
  )
; (a-pair? '((a b) c))

(flatten '((a b) c) )

; 5.59 

(defn flatteno
  [s out]
  (conde
   [ (nullo s) (== '() out) ]
   [ (pairo s) (fresh [a d res-a res-d]
                      (conso a d s)
                      (flatteno a res-a)
                      (flatteno d res-d)
                      (appendo res-a res-d out)
                      ) ]
   [succeed (conso s '() out) ]
   )
)

; 5.60

(run 1 [x]
     (flatteno (list (list 'a 'b) 'c) x)
     )

; 5.62

(run* [x]
     (flatteno '(a) x))

; 5.73

(defn flattenrevo
  [s out]
  (conde 
   [succeed (conso s '() out) ]
   [(nullo s) (== '() out) ]
   [succeed (fresh [a d res-a res-d]
                   (conso a d s)
                   (flattenrevo a res-a)
                   (flattenrevo d res-d)
                   (appendo res-a res-d out)
                   ) ]
   )
  
  )

(->>
 (run* [x]
       (flattenrevo '((a b) c) x)
       )
 pp/pprint
 )


; chapter 6 the fun never ends


(doc anyo)

; 6.1

(defn anyo
  [g]
  (conde 
   [g succeed]
   [succeed (anyo g)]
   )
  )

; 6.4

(def nevero (anyo fail))


; (run 1 [q]
;      nevero
;      (== true q)
;      )

(doc alwayso)

(def alwayso (anyo succeed))

(run 1 [q]
    alwayso
    (== true q)
    )

(run 5 [q]
     alwayso
     (== true q))

; 6.12

(defn salo ;; succeed at least once
  [g]
  (conde 
   [succeed succeed]
   [succeed g]
   )
  )

; 6.13

(run 1 [q]
     (salo alwayso)
     (== true q))


(run 1 [q]
     (salo nevero)
     (== true q))


(run 5 [q]
     (conde
      [(== false q) alwayso]
      [(== true q) alwayso]
      [succeed fail ]
      )
     (== true q))


;; condi alli - i stands for interleave
