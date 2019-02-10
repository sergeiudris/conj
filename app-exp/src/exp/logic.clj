(ns exp.logic
  (:refer-clojure :exclude [==])
  (:require [clojure.repl :refer :all]
            [clojure.core.logic.pldb :refer [db with-db db-rel db-fact]]
            [clojure.core.logic :refer [all == != lvar run run* defne fresh]]
            ))


(comment
  (+)
  (doc fresh)
  
  (run* [q]
        (== q true))
  
  ;; features
  
  ;; simple in memeory database
  
  (
   (db-rel man p)
   (db-rel woman p)
   (db-rel likes p1 p2)
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
      [likes 'Ricky 'Mary]
      )
     )
   
   (def facts1 (-> facts0 (db-fact fun  'Lucy) (db-fact fun  'Mary)))
   
   (with-db facts1
     (run* [q]
           (fresh [x y]
                  (fun y)
                  (likes x y)
                  (== q [x y])
                  ))
     )
   
   )
  
  
  
  
  
  )