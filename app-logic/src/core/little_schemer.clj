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

(defn multiinsertR
  "insert new to the right of all olds"
  [new old lat]
  (cond
    (null? lat) '()
    (eq? (car lat) old) (cons old (cons new (multiinsertR new old (cdr lat))  )) 
    :else  (cons (car lat) (multiinsertR new old (cdr lat) )) 
    )
  )

(multiinsertR 'N 'z '(a b z c z d))

(defn multiinsertL
  "insert new to the left of all olds"
  [new old lat]
  (cond
    (null? lat) '()
    (eq? (car lat) old) (cons new (cons old  (multiinsertL new old (cdr lat))))
    :else (cons (car lat) (multiinsertL new old (cdr lat)))
    )
  )

(multiinsertL 'N 'z '(a b z c z d))


(defn multisubst
  "replace all occurances of atom in lat"
  [a lat]
  (cond
    (null? lat) '()
    (eq? (car lat) a ) (multisubst a (cdr lat))
    :else (cons (car lat) (multisubst a (cdr lat)) )
    )
  )

(multisubst 'z '(a b z c z d))


(defn add1
  "increment"
  [n]
  (+ n 1)
  )

(defn sub1
  "increment"
  [n]
  (- n 1))

(sub1 0)

(defn *+
  "add two numbers"
  [n m]
  (cond
    (zero? m) n
    :else (add1 (*+ n (sub1 m) ))
    )
  )

(*+ 100000 2000)

(defn *-
  "subtract from a  number"
  [n m]
  (cond
    (zero? m) n
    :else (sub1  (*- n (sub1 m) ))
    )
  )

(*- 3 1)


(defn addtup
  "sum of a tuple"
  [tup]
  (cond
    (null? tup) 0
    :else (*+ (car tup) (addtup (cdr tup) ) )
    )
  )

(addtup '(1 2 3))

(defn ** 
  "multiply"
  [n m]
  (cond
    (zero? m) 0
    :else (*+ n (** n (sub1 m)))
    )
  )

(** 0 4)

(defn tup+
  "add two equal tups"
  [tup1 tup2]
  (cond
    (and (null? tup1) (null? tup2)) '()
    :else (cons (*+ (car tup1) (car tup2) ) (tup+ (cdr tup1) (cdr tup2)))
    )
  )

(tup+ '(1 2) '(3 4))

(defn tup+ 
  "add any two tups"
  [tup1 tup2]
  (cond
    (null? tup1) tup2
    (null? tup2) tup1
    :else (cons (*+ (car tup1) (car tup2)) (tup+ (cdr tup1) (cdr tup2) ) )
    ) 
  )

(tup+ '(1 2) '(3 4 6 7))


(defn *>
  "greater"
  [n m]
  (cond
    (zero? n) false
    (zero? m) true
    :else (*> (sub1 n) (sub1 m) )
    )
  )

(*> 3 2)


(defn *<
  "less"
  [n m]
  (cond
    (zero? m) false
    (zero? n) true
    :else (*< (sub1 n) (sub1 m))))

(*< 1 3)
