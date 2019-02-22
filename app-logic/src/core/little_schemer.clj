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
    (and (atom? a) (atom? b) ) (= a b)
    :else false
    )
  )

(eq? 'a (car '(a b c)))

(eq? 'ab 'ab)

; (eq? 1 1)

(number? 1)

; (eq? '() '())

;; 2. Do It, Do It Again

; (null? 'a)

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

(defn *=
  "equal"
  [n m]
  (cond 
    (and (zero? n) (zero? m) ) true
    (or (zero? n) (zero? m) ) false
    :else (*= (sub1 n) (sub1 m) )
    )
  )

(*= 0 1)

(defn *=
  "equal"
  [n m]
  (cond
    (zero? n)  (zero? m)
    (zero? m) false
    :else (*= (sub1 n) (sub1 m))))

(defn *=
  "equal"
  [n m]
  (cond
    (or (*< n m) (*> n m) ) false
    :else true
    )
  )

(*= 0 1)

(defn *expt
  "expt"
  [n m]
  (cond
    (zero? n) 0
    (zero? m) 1
    :else (** n (*expt n (sub1 m) ) )
    )
  )

(*expt 3 4)

(defn *d
  ""
  [n m]
  (cond 
    (< n m) 0
    :else (add1 (*d (*- n m) m))
    )
  )

(*d 4 2)

(defn length
  "count length"
  [lat]
  (cond
    (null? lat) 0
    :else (add1 (length (cdr lat))) 
    ) 
  )

(length '( 1 2 3 4 ))


(defn pick
  "get element by index"
  [n lat]
  (cond
    (zero? (sub1 n)) (car lat)
    :else (pick (sub1 n) (cdr lat))
    )
  )

(pick 3 '(1 2 3))

(defn rempick
  "remove by index"
  [n lat]
  (cond
    (null? lat) '()
    (zero? (sub1 n)) (cdr lat)
    :else (cons (car lat) (rempick (sub1 n) (cdr lat)))
    )
  )

(rempick 3 '(a b c d))

(defn no-nums
  "remove all numbers from lat"
  [lat]
  (cond
    (null? lat) '()
    (number? (car lat)) (no-nums (cdr lat))
    :else (cons (car lat) (no-nums (cdr lat)))
    
    )
  )

(no-nums '(a 1 b 2 c 3))

(defn all-nums
  "extract all numbers inot a tup"
  [lat]
  (cond
    (null? lat) '()
    (number? (car lat)) (cons (car lat) (all-nums (cdr lat)) )
    :else (all-nums (cdr lat))
    )
  )

(all-nums '(a 1 b 2 c 3))


(defn eqan? 
  "true of atoms(inlcuding numbers) are equal"
  [a1 a2]
  (cond
    (and (number? a1) (number? a2)) (*= a1 a2)
    (or (number? a1) (number? a2)) false
    :else (eq? a1 a2))
  
  )

(eqan? 3 'a)


(defn occur
  "count the number of times atom appears in list"
  [a lat]
  (cond
    (null? lat) 0
    (eqan? (car lat) a) ( add1 (occur a (cdr lat)) ) 
    :else (occur a (cdr lat))
    )
  )

(occur 3 '(1 2 3 4 3))

(defn one?
  "returns true if number is one"
  [n]
  (*= n 1)
  )

(one? 1)


(defn rempick
  "remove nth element from list"
  [n lat]
  (cond
    (null? lat) '()
    (one? n) (cdr lat)
    :else (cons (car lat) (rempick (sub1 n) (cdr lat) ))
    
    )
  )

(rempick 1 '(1 2 3))

(defn rember*
  "remove all occurances of a in list or nested lists"
  [a l]
  (cond 
    (null? l) '()
    (and (atom? (car l)) (eqan? (car l) a) ) (rember* a (cdr l))
     (atom? (car l)) (cons (car l) (rember* a (cdr l)) )
    :else (cons (rember* a (car l))  (rember* a (cdr l)) )
    
    )
  )

(rember* 'a '(a b c a (d a e)))

(defn insertR*
  "insert new to the right of every occurance of old in list of nested lists"
  [new old l]
  (cond
    (null? l) '()
    (and (atom? (car l)) (eqan? (car l) old)) (cons old (cons new (insertR* new old (cdr l) )))
    (atom? (car l)) (cons (car l)   (insertR* new old (cdr l)))
    :else (cons (insertR* new old (car l)) (insertR* new old (cdr l) ) )
    )
  
  )

(insertR* 'N 'z '(a z b z c (d z e)))


(defn occur*
  [a l]
  "count occurances in list of lists"
  (cond
    (null? l) 0
    (and (atom (car l)) (eqan? (car l) a) ) (add1 (occur* a (cdr l) ) )
    (atom? (car l)) (occur* a (cdr l))
    :else (*+ (occur* a (car l)) (occur* a (cdr l)) )
    )
  )

(occur* 'z '(a b z ( c d z (e z) )))

(defn subst* 
 "replce olds with new in LofL"
 [new old l]
 (cond
   (null? l) '()
   (and (atom? (car l)) (eqan? (car l) old ))  (cons new (subst* new old (cdr l) ))
   (atom? (car l)) (cons (car l) (subst* new old (cdr l)) )
   :else (cons (subst* new old (car l)) (subst* new old (cdr l)))
   )
 
 )

(subst* 'N 'z  '(a b z ( c d z (e z) )))

(defn insertL*
  "insert new to the right of every occurance of old in list of nested lists"
  [new old l]
  (cond
    (null? l) '()
    (and (atom? (car l)) (eqan? (car l) old)) (cons new (cons old (insertL* new old (cdr l))))
    (atom? (car l)) (cons (car l)   (insertL* new old (cdr l)))
    :else (cons (insertL* new old (car l)) (insertL* new old (cdr l)))))

(insertL* 'N 'z '(a z b z c (d z e)))

(defn member*
  "returns true if atom occurs in LofL"
  [a l]
  (cond
    (null? l) false
    (and (atom? (car l)) (eqan? (car l) a)) true
    (atom? (car l)) (member* a (cdr l))
    :else (or (member* a (car l)) (member* a (cdr l)) )
    
    ))

(member* 'N  '(a z b z c (d z e (N))))

(defn leftmost
  "finds the leftmost atom in a non-empty list of S-expressions
that does not contain an empty list"
  [l]
  (cond
    (atom? (car l)) (car l)
    :else (leftmost (car l))
    
    ))

(leftmost '(((a) b) c) )

(defn eqlist?
  "returns true if two LofL are same"
  [l1 l2]
  (cond
    (and (null? l1) (null? l2)) true
    (or (null? l1) (null? l2)) false
    (and (atom? (car l1) ) (atom?  (car l2))) (and (eqan? (car l1) (car l2)) (eqlist? (cdr l1) (cdr l2)))
    (or (atom? (car l1)) (atom?  (car l2))) false
    :else (and (eqlist? (car l1) (car l2)) (eqlist? (cdr l1) (cdr l2)))
    )
  )

(eqlist? '(a b (c d (e f))) '(a b (c d (e f))) )

(eqlist? '(a b (c d (e f))) '(a b (c d (e f g))))

(defn equal? 
  "returns true if two S-expressions are equal"
  [s1 s2]
  (cond
    (and (atom? s1) (atom? s2)) (eqan? s1 s2)
    (or (atom? s1) (atom? s2) ) false
     :else (eqlist? s1 s2)
    )
  )

(defn rember
  "remove any S-expression from the list"
  [s l]
  (cond
    (null? l) '()
    (equal? (car l) s) (cdr l)
    :else (cons (car l) (cdr l) )
    
    )
  
  )

(defn atom-to-operator
  "atom to op"
  [a]
  (cond
    (equal? a '+) *+
    (equal? a '*) **
    (equal? a 'expt) *expt
    )
  )
(def a '+)
(atom-to-operator a)


(defn operator?
  "returns true if op"
  [a]
  (cond
    (equal? a '+) true
    (equal? a '*) true
    (equal? a 'expt) true
    :else false
    )
  )

(defn numbered?
  "returs true if the representaion of an arithmetic expression contains only numbers besides + * expt"
  [aexp]
  (cond
    (atom? aexp) (number? aexp)
    (null? aexp) false
    ( and (operator? (car (cdr aexp)) ) (numbered? (car aexp) ) (numbered? (car (cdr (cdr aexp)))) ) true
    :else false
    )
  )

(numbered? '(1 expt 1))


(defn value
  "returns the natural value of a numbered arithmetic expression"
  [nexp]
  (cond
    (atom? nexp) nexp
    :else ((atom-to-operator (car (cdr nexp)) ) (value (car nexp)) (value (car (cdr (cdr nexp)))) )
    )
  )

(value '(2 expt (1 + 2)))

(defn fst-sub-exp
  "first of aexp"
  [aexp]
  (car (cdr aexp))
  )

(defn snd-sub-exp
  "snd of aexp"
  [aexp]
  (car (cdr (cdr aexp))))

(defn operator
  "arithmeic expression operator"
  [aexp]
  (car aexp)
  )

(defn value
  "returns the value of aexp"
  [nexp]
  (cond
    (atom? nexp) nexp
    :else ( (atom-to-operator (operator nexp)) (value (fst-sub-exp nexp)) (value (snd-sub-exp nexp)) )
    )
  )

(value '(expt 2 (+ 1 2)))

;; p 105