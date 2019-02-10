(ns exp.logic
  (:refer-clojure :exclude [==])
  (:require [clojure.repl :refer :all]
            [clojure.core.logic.pldb :refer [db with-db db-rel db-fact]]
            [clojure.core.logic.unifier :refer [unifier unifier*]]
            [clojure.core.logic.fd  :as fd]
            [clojure.core.logic :exclude [is] :refer :all :as l]
            [clojure.core.logic.nominal :exclude [fresh hash] :as nom]
        [clojure.test]
            ))


(comment
  (+)
  (doc fresh)
  (doc project)


  (run* [q]
        (== q true))

  ;; features
  
  ;; simple in memeory database
  
  ((db-rel man p)
   (db-rel woman p)
   (db-rel likes ^:index p1 ^:index p2)
   (db-rel fun p)

   (def facts0
     (db
      [man 'Bob]
      [man 'John]
      [man 'Ricky]

      [woman 'Mary]
      [woman 'Martha]
      [woman 'Lucy]

      [likes 'Bob 'Mary]
      [likes 'John 'Martha]
      [likes 'Ricky 'Lucy]
      [likes 'Ricky 'Mary]))

   (def facts1 (-> facts0 (db-fact fun  'Lucy) (db-fact fun  'Mary)))

   (with-db facts1
     (run* [q]
           (fresh [x y]
                  (fun y)
                  (likes x y)
                  (== q [x y])))))


  ;; a la carte unifier
  
  (unifier '(?x ?y ?z) '(1 2 ?y))

  (unifier '(?x ?y ?z) '(1 2 ?y)) ; (1 2 _.0)
  

  ;; constraint logic programming (CLP)
  
  ;; CLP (tree)
  
  (run* [q]
        (!= q 1))

  (run* [q]
        (fresh [x y]
               (!= [1 x] [y 2])
               (== q [x y])))


  ;; CLP (FD) finite domains
  
  (run* [q]
        (fd/in q (fd/interval 1 5)))

  (run* [q]
        (fresh [x y]
               (fd/in x y (fd/interval 1 10))
               (fd/+ x y 10)
               (== q [x y])))

  (run* [q]
        (fresh [x y]
               (fd/in x y (fd/interval 0 9))
               (fd/eq
                (= (+ x y) 9)
                (= (+ (* x 2) (* y 4)) 24))
               (== q [x y])))

  (run* [q]
        (fresh [x y]
               (fd/in x y (fd/interval 1 10))
               (fd/+ x y 10)
               (fd/distinct [x y]) ;; [5 5] no longer in the set of returned solutions
               (== q [x y])))

  ;; tabling
  
  (defne arco [x y]
    ([:a :b])
    ([:b :a])
    ([:b :d]))

  (def patho
    (tabled [x y]
            (conde
             [(arco x y)]
             [(fresh [z]
                     (arco x z)
                     (patho z y))])))

   ;; (:b :a :d)
  (run* [q] (patho :a q))


  ;; nominal
  

  (defn substo [e new a out]
    (conde
     [(== ['var a] e) (== new out)]
     [(fresh [y]
             (== ['var y] e)
             (== ['var y] out)
             (nom/hash a y))]
     [(fresh [rator ratorres rand randres]
             (== ['app rator rand] e)
             (== ['app ratorres randres] out)
             (substo rator new a ratorres)
             (substo rand new a randres))]
     [(fresh [body bodyres]
             (nom/fresh [c]
                        (== ['lam (nom/tie c body)] e)
                        (== ['lam (nom/tie c bodyres)] out)
                        (nom/hash c a)
                        (nom/hash c new)
                        (substo body new a bodyres)))]))

  (run* [q]
        (nom/fresh [a b]
                   (substo ['lam (nom/tie a ['app ['var a] ['var b]])]
                           ['var b] a q)))
;; => [['lam (nom/tie 'a_0 '(app (var a_0) (var a_1)))]]
  
  (run* [q]
        (nom/fresh [a b]
                   (substo ['lam (nom/tie a ['var b])]
                           ['var a]
                           b
                           q)))
;; => [['lam (nom/tie 'a_0 '(var a_1))]]
  

  ;; definite clause grammars
  (
   (def-->e verb [v]
     ([[:v 'eats]] '[eats]))

   (def-->e noun [n]
     ([[:n 'bat]] '[bat])
     ([[:n 'cat]] '[cat]))

   (def-->e det [d]
     ([[:d 'the]] '[the])
     ([[:d 'a]] '[a]))

   (def-->e noun-phrase [n]
     ([[:np d n]] (det d) (noun n)))

   (def-->e verb-phrase [n]
     ([[:vp v np]] (verb v) (noun-phrase np)))

   (def-->e sentence [s]
     ([[:s np vp]] (noun-phrase np) (verb-phrase vp)))

   (run* [parse-tree]
         (sentence parse-tree '[the bat eats a cat] []))
   
   )

  )


;; wiki: a core.logic Primer

(comment

  (run* [q]
        (membero q [1 2 3])
        (membero q [2 3 4]))

  (run* [q]
        (== q 1))

  (run* [q]
        (== q {:a 1 :b 2}))

  (run* [q]
        (== {:a q :b 2} {:a 1 :b 2}))

  (run* [q]
        (== 1 q))


  (run* [q]
        (== q '(1 2 3)))


  (run* [q]
        (== q 1)
        (== q 2))

  (run* [q]
        (== q 1/2)
        (== q 2/4))

  ;; Unification of two lvars  (intersection of sets)
  
  (run* [q]
        (membero q [1 2 3]))

  (run* [q]
        (membero q [1 2 3])
        (membero q [3 4 5]))

  (run* [q]
        (fresh [a]
               (membero a [1 2 3])
               (membero q [3 4 5])
               (== a q)))

  ;; core.logic is declarative - order of expressions does not matter
  (run* [q]
        (fresh [a]
               (membero q [1 2 3])
               (membero a [3 4 5])
               (== q a)))
  (run* [q]
        (fresh [a]
               (membero a [3 4 5])
               (== q a)
               (membero q [1 2 3])))
  (run* [q]
        (fresh [a]
               (== q a)
               (membero a [3 4 5])
               (membero q [1 2 3])))


  ;; conde (OR) diejunction
  

  (run* [q]
        (conde
         [succeed succeed succeed succeed]))

  (run* [q]
        (conde
         [succeed succeed fail succeed]))

  (run* [q]
        (conde
         [succeed]
         [succeed]))


  (run* [q]
        (conde
         [succeed]
         [fail]))

  (run* [q]
        (conde
         [succeed (== q 1)]))

  (doc succeed)

  (run* [q]
        (conde
         [(== q 2) (== q 1)]))

  (run* [q]
        (conde
         [(== q 1)]
         [(== q 2)]))

  ;; conso (the magnificent)
  
  (run* [q]
        (conso 1 [2 3] q))
  
  (run* [q]
        (conso q [ 2 3] [1 2 3]))

  (run* [q]
        (conso 1 q [1 2 3]))

  (run* [q]
        (conso 1 [2 q] [1 2 3]))

  ;; resto  (complement of conso)
  
  (run* [q]
        (resto [1 2 3 4] q))
  


  ;; membero
  
  (run* [q]
        (membero q [1 2 3]))


  (run* [q]
        (membero 7 [1 3 8 q]))

  ;; summary - lvars, goals and expressions

  )

(comment 
  
  (doc defne)
  
  ;; examples
  
  ;; a classic AI program
  
  (defne moveo [before action after]
    ([[:middle :onbox :middle :hasnot]
      :grasp
      [:middle :onbox :middle :has]])
    ([[pos :onfloor pos has]
      :climb
      [pos :onbox pos has]])
    ([[pos1 :onfloor pos1 has]
      :push
      [pos2 :onfloor pos2 has]])
    ([[pos1 :onfloor box has]
      :walk
      [pos2 :onfloor box has]])
    )
  
  (defne cangeto [state out]
    ([[_ _ _ :has] true])
    ([_ _] (fresh [action next]
                  (moveo state action next)
                  (cangeto next out))))
  (run 1 [q]
       (cangeto [:atdoor :onfloor :atwindow :hasnot] q))
  
  ;; sudoku
  (defn get-square [rows x y]
    (for [x (range x (+ x 3))
          y (range y (+ y 3))]
      (get-in rows [x y])))

  (defn init [vars hints]
    (if (seq vars)
      (let [hint (first hints)]
        (all
         (if-not (zero? hint)
           (== (first vars) hint)
           succeed)
         (init (next vars) (next hints))))
      succeed))

  (defn sudokufd [hints]
    (let [vars (repeatedly 81 lvar)
          rows (->> vars (partition 9) (map vec) (into []))
          cols (apply map vector rows)
          sqs  (for [x (range 0 9 3)
                     y (range 0 9 3)]
                 (get-square rows x y))]
      (run 1 [q]
           (== q vars)
           (everyg #(fd/in % (fd/domain 1 2 3 4 5 6 7 8 9)) vars)
           (init vars hints)
           (everyg fd/distinct rows)
           (everyg fd/distinct cols)
           (everyg fd/distinct sqs))))

  (def hints
    [2 0 7 0 1 0 5 0 8
     0 0 0 6 7 8 0 0 0
     8 0 0 0 0 0 0 0 6
     0 7 0 9 0 6 0 5 0
     4 9 0 0 0 0 0 1 3
     0 3 0 4 0 1 0 2 0
     5 0 0 0 0 0 0 0 1
     0 0 0 2 9 4 0 0 0
     3 0 6 0 8 0 4 0 9])

  (sudokufd hints)
  
  )





(comment

;; type inferencer for the simply typed lambda calculus
  
  (defna findo [x l o]
    ([_ [[y :- o] . _] _]
     (project [x y] (== (= x y) true)))
    ([_ [_ . c] _] (findo x c o)))

  (defn typedo [c x t]
    (conda
     [(lvaro x) (findo x c t)]
     [(matche [c x t]
              ([_ [[y] :>> a] [s :> t]]
               (fresh [l]
                      (conso [y :- s] c l)
                      (typedo l a t)))
              ([_ [:apply a b] _]
               (fresh [s]
                      (typedo c a [s :> t])
                      (typedo c b s))))]))

  ;; ([_.0 :> _.1])
  (run* [q]
        (fresh [f g a b t]
               (typedo [[f :- a] [g :- b]] [:apply f g] t)
               (== q a)))

  ;; ([:int :> _.0])
  (run* [q]
        (fresh [f g a t]
               (typedo [[f :- a] [g :- :int]] [:apply f g] t)
               (== q a)))

  ;; (:int)
  (run* [q]
        (fresh [f g a t]
               (typedo [[f :- [:int :> :float]] [g :- a]]
                       [:apply f g] t)
               (== q a)))

  ;; ()
  (run* [t]
        (fresh [f a b]
               (typedo [f :- a] [:apply f f] t)))

  ;; ([_.0 :> [[_.0 :> _.1] :> _.1]])
  (run* [t]
        (fresh [x y]
               (typedo []
                       [[x] :>> [[y] :>> [:apply y x]]] t)))
  
  
  
  )


(comment 
  
  
  
  
;   ((db-rel man p)
;    (db-rel woman p)
;    (db-rel likes ^:index p1 ^:index p2)
;    (db-rel fun p)
  
;    (def facts0
;      (db
;       [man 'Bob]
;       [man 'John]
;       [man 'Ricky]
  
;       [woman 'Mary]
;       [woman 'Martha]
;       [woman 'Lucy]
  
;       [likes 'Bob 'Mary]
;       [likes 'John 'Martha]
;       [likes 'Ricky 'Lucy]
;       [likes 'Ricky 'Mary]))
  
;    (def facts1 (-> facts0 (db-fact fun  'Lucy) (db-fact fun  'Mary)))
  
;    (with-db facts1
;      (run* [q]
;            (fresh [x y]
;                   (fun y)
;                   (likes x y)
;                   (== q [x y])))))
  
  
  ;; actions
  ;; unit
  ;; structure
  ;; environment
  
  
  (db-rel unit p)
  (db-rel structure p)
  (db-rel terrain p)
  (db-rel action p)
  
  (db-rel allowed ^:index p1 ^:index p2)
  
  (def facts0
    (db
     [unit "hero"]
     [unit "lizard"]
     
     [structure "market"]
     [structure "engineering-hub"]
     
     [terrain "stone"]
     [terrain "sand"]
     
     [action "observe"]
     [action "move"]
     [action "build"]
     [action "attack"]
     [action "buy"]

     [allowed "market" "buy"]
     [allowed "engineering-hub" "build"]     

     ; query what actions are allowed when multiple entities are selected
     
     
     
     ))
  
  
  
  
  )