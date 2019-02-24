(ns core.seasoned-schemer
  (:use core.little-schemer)
  )

(defn hello [] "hello")


(value '(+ 3 3))
(evens-only*  '(1 2 3 4 (5 6 7 8 (9 10))))



(member? 'x '(x y z))

(defn two-in-a-row?
  [lat]
  (cond
    (or (null? lat) (null? (cdr lat)) ) false
    (equal? (car lat) (car (cdr lat)) ) true
    :else (two-in-a-row? (cdr lat) )
    )
  )

(two-in-a-row? '( a b c))
(two-in-a-row? '(a b b c))

(defn is-first? 
  [a l]
  (cond
    (null? l) false
    :else (equal? (car l) a )
    )
  )

(defn two-in-a-row?
  [lat]
  (cond
    (null? lat) false
    
    :else (or (is-first? (car lat) (cdr lat) ) (two-in-a-row? (cdr lat))) ) 
  )

(two-in-a-row? '(a b b c))

;; i think this is a bad idea - tight couling, hard to reason about
(defn is-first-b?
  [a l]
  (cond
    (null? l) false
    :else (or (equal? a (car l)) (two-in-a-row? l) )
    )
  )

(defn two-in-a-row?
  [l]
  (cond 
    (null? l) false
    :else (is-first-b? (car l) (cdr l) )
    )
  
  )

(two-in-a-row? '(a b b c))


(defn two-in-a-row-b?
  [preceding l]
  (cond
    (null? l) false
    :else (or (equal? preceding (car l)) (two-in-a-row-b? (car l) (cdr l)))
    )
  )