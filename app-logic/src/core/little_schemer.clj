(ns core.little-schemer )

;; 1. TOYS

(defn atom? 
  [a]
  (not (coll? a)))

(atom? '())

;; https://en.wikipedia.org/wiki/CAR_and_CDR

;; Contents of the Address Register
(def car first)

;; Contents of the Decrement Register
(def cdr rest)


(cons 1 '( 2 3))

;; list composed of zero S-expressions
(def null? empty?)
; (defn null? [l]
;   (cond
;     ( and (coll? l) (empty? l) ) true
;     :else false
;     )
;   )

(null? '(1 2))

(defn eq? 
  [a b] 
  (cond
    (or (number? a) (number? b) ) (throw (Exception. "eq? takes two arguments. Both of them must be non-numeric atoms"))
    (and (atom? a) (atom? b) (= a b)) true
    :else false
    )
  )

(eq? 'a (car '(a b c)))

(eq? 'ab 'ab)

(eq? 1 1)

(number? 1)

(eq? '() '())

;; 2. Do It, Do It Again

(null? 'a)

;; is list a list of atoms
(defn lat? 
  [l]
  (cond 
    (null? l) true
    (atom? (car l)) (lat? (cdr l))
    :else false
    )
  )

(lat? '(a b c))
(lat? '(a 1))
(lat? '(a (1)))


(defn member?
  [a lat]
  (cond
    (null? lat) false
    (eq? (car lat) a) true
    :else (member? a (cdr lat))
    )
  )

(member? 'z '(a b c z))





