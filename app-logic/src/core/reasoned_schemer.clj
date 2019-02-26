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




