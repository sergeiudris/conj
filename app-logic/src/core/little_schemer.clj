(ns core.little-schemer )

;; 1. TOYS

(defn atom? 
  "is arg not a list"
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
  "equality only for two non-numeric atoms"
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

(defn lat? 
  "is list a list of atoms"
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
  "list contains atom"
  [a lat]
  (cond
    (null? lat) false
    :else (or  
           (eq? (car lat) a) 
           (member? a (cdr lat)) 
           )
    )
  )

(member? 'z '(a b c z))

(defn rember
  "remove first appearance of the atom fro list"
  [a lat]
  (cond
    (null? lat) '()
    (eq? (car lat) a) (cdr lat)
    :else (cons (car lat) (rember a (cdr lat)))
    )
  )


(rember 'a '(b c a) )

(defn firsts [l]
  "list of first elements of list of lists"
  (cond
    (null? l) '()
    :else (cons (car (car l)) (firsts (cdr l) ) )
    )
  )

(firsts '( (1 2) (3 4) (a b) ))


(defn insertR 
  "inserts 'new' to the right of 'old'"
  [new old lat]
  (cond
    (null? lat) '()
    (eq? (car lat) old) (cons old (cons new (cdr lat)))
    :else (cons (car lat) (insertR new old (cdr lat) ))
    )
  )

(insertR 'z 'b '(a b c))
(insertR 'z 'c '(a b c))

(defn insertL
  "inserts 'new' to the left of 'old'"
  [new old lat]
  (cond
    (null? lat) '()
    (eq? (car lat) old ) (cons new (cons old (cdr lat)))
    :else (cons (car lat) (insertL new old (cdr lat)) )
    )
  )

(insertL 'z 'c '(a b c))


(defn subst 
  "replaces first occurance of old int he lat with new"
  [new old lat]
  (cond
    (null? lat) '()
    (eq? (car lat) old) (cons new (cdr lat))
    :else (cons (car lat) (subst new old (cdr lat)) )
  )
)

(subst 'z 'a '(a b c a))

(defn subst2 
  "replace the first occurance of either o1 or o2"
  [new o1 o2 lat]
  (cond
    (null? lat) '()
    (or (eq? (car lat) o1) (eq? (car lat) o2)) (cons new (cdr lat))
    :else (cons (car lat) (subst2 new o1 o2 (cdr lat)))
    )
  )

(subst2 'z 'a 'b '(c a b m))

(defn multirember
  "remove all occurances of a"
  [a lat]
  (cond
    (null? lat) '()
    (eq? (car lat) a) (multirember a (cdr lat))
    :else (cons (car lat) (multirember a (cdr lat)))
    )
  
  )

(multirember 'z '(a b z c z d))

