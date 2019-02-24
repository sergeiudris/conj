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

(defn sero?
  "returns true if number represented by () is zero"
  [n]
  (null? n)
  )

(sero? '())

(defn edd1
  "edd () to ()"
  [n]
  (cons '() n)
  )

(edd1 '(() ()))

(defn zub1
  "decrement by one"
  [n]
  (cdr n)
  )

(defn blus
  [n m]
  (cond
    (sero? m) n
    ; :else (blus (cons (car m) n) (cdr m) ) 
    :else (edd1 (blus n (zub1 m) ) )
    )
  )

(blus '(()) '(() () ()))


(defn member?
  "list contains atom"
  [a lat]
  (cond
    (null? lat) false
    :else (or
           (equal? (car lat) a)
           (member? a (cdr lat)))))


(defn set??
  [l]
  (cond 
    (null? l) true
    (member? (car l) (cdr l) ) false
    :else (set?? (cdr l) )
    )
  )

(set?? '(1 2 3 3))

(defn multirember
  "remove all occurances of a"
  [a lat]
  (cond
    (null? lat) '()
    (equal? (car lat) a) (multirember a (cdr lat))
    :else (cons (car lat) (multirember a (cdr lat)))))

(defn makeset
  [l]
  (cond 
    (null? l) '()
    :else (cons (car l) (makeset (multirember (car l) (cdr l))))
    )
  )

(makeset '(apple peach pear peach plum apple lemon peach 1 1))

(defn subset??
  "returns true if subset"
  [set1 set2]
  (cond
    (null? set1) true
    :else (and (member? (car set1) set2 ) (subset?? (cdr set1) (cdr set2) ) )
    )
  
  )

(subset?? '(1 2) '(a b 0 2 3))

(defn eqset?
  "returns true of sets are same"
  [set1 set2]
  (cond
    (and (null? set1) (null? set2)) true
    (or (null? set1) (null? set2)) false
    :else (and (equal? (car set1) (car set2) ) (eqset? (cdr set1) (cdr set2) ) )
    )
  )

(eqset? '(a 1 b 2) '(a 1 b 2) )
(eqset? '(a 1 b 2) '(a 1 b 2 3))



(defn eqset?
  "returns true of sets are same"
  [set1 set2]
  (and (subset?? set1 set2) (subset?? set2 set1))
  )


(eqset? '(a 1 b 2) '(a 1 b 2))
(eqset? '(a 1 b 2) '(a 1 b 2 3))

(defn intersect?
  "returns true if at least one atom in set1 is in set2"
  [set1 set2]
  (cond
    (or (null? set1) (null? set2)) false
    :else (or (member? (car set1) set2) (intersect? (cdr set1) set2) )
    
    )
  )

(intersect? '(a b c)  '(d e f a) )

(defn intersect 
  "return list of set1 and set2 intersection"
  [set1 set2]
  (cond
    (or (null? set1) (null? set2)) '()
    (member? (car set1) set2) (cons (car set1) (intersect (cdr set1) set2 ) )
    :else (intersect (cdr set1) set2)
    )
  )

(intersect '(1 2) '(3 4 1 1 2  2))


(clojure.set/union #{1 2} #{3})

(defn union
  "union of two sets"
  [set1 set2]
  (cond
    (null? set1) set2
    (null? set2) set1
    :else (cons (car set1) (union (cdr set1) (multirember (car set1) set2)  ) )
    
    )
  
  )

(union '(1 2 a b) '(3 4 1 2))


(defn union
  "union of set1 and set2"
  [set1 set2]
  (cond
    (null? set1) set2
    (member? (car set1) set2) (union (cdr set1) set2 )
    :else (cons (car set1) (union (cdr set1) set2) )
    
    )
  
  )

(union '(1 2) '(3 4 1) )

(defn intersectall 
  "intersect all sublists-sets of a l-set"
  [l-set]
  (cond 
    (null? l-set) '()
    (null? (cdr l-set)) (car l-set)
    :else (intersect (car l-set) (intersectall (cdr l-set) ))
    )
  )

(intersectall '( (a b c f g) (d e f a) (g f) ))

(defn a-pair?
  "returns true if pair"
  [x]
  (cond 
    (or (atom? x) (null? x) (null? (cdr x))) false
    :else (null? (cdr (cdr x)) )
    )
  )

(a-pair? '( 3))

(defn first 
  "first element"
  [p]
  (car p)
  )

(first '(2))

(defn second
  "first element"
  [p]
  (car (cdr p)))

(defn build
  "build a pair"
  [s1 s2]
  (cons s1 (cons s2 '()) )
  )

(build 1 2)

(defn third 
  "third element"
  [l]
  (second (cdr l))
  )

(third '(a b c))

(defn fun?
  "returns true if relation is a fucntion"
  [rel]
      (set?? (firsts rel))
  )

(fun? '((1 2) (3 4) (5 6)) )

(defn revrel
  "reverse pairs in rel"
  [rel]
  (cond 
    (null? rel) '()
    :else (cons (build (second (car rel)) (first (car rel))) (revrel (cdr rel)))
    )
  )

(revrel '((1 2) (3 4) (5 6)))

(defn revpair
  "reverse a pair"
  [p]
  (build (second p) (first p) )
  )

(defn revrel
  "reverse pairs in rel"
  [rel]
  (cond
    (null? rel) '()
    :else (cons (revpair (car rel)) (revrel (cdr rel)))))

(revrel '((1 2) (3 4) (5 6) (7 6)))

(defn fullfun?
  "one input, one output"
  [fun]
  (set?? (firsts  (revrel fun)))
  )

(fullfun? '((1 2) (3 4) (5 6) (7 6) ))

(defn one-to-one?
  "is fun full"
  [fun]
  (fun? (revrel fun))
  )

(one-to-one? '((1 2) (3 4) (5 6) (7 6)))


(defn rember-f
  "remove meber w/ lambda"
  [test? a l]
  (cond
    (null? l) '()
    (test? a (car l)) (cdr l)
    :else (cons (car l) (rember-f test? a (cdr l) ))
    )
  )

(rember-f equal? 'a '(b c a d) )

(defn equal?-c
  "curried equal?"
  [a]
  (fn [x] (equal? a x ) )
  )
((equal?-c 'a) (car '(a)))

(defn rember-f
  "remove meber w/ lambda"
  [test?]
    (fn [a l]
      (cond
        (null? l) '()
        (test? a (car l)) (cdr l)
        :else (cons (car l) ((rember-f test?) a (cdr l))))
      )
  )

((rember-f equal?) 'a '(b a c) )

(defn insertL-f
  "curried insertleft"
  [test?]
  (fn [new old l]
    (cond
      (null? l) '()
      (test? (car l) old) (cons new (cons old ((insertL-f test?) new old (cdr l) ) ))
      :else (cons (car l) ((insertL-f test?) new old (cdr l)))
      )
    )
  )

((insertL-f equal?) 'N 'a '(b a c))

(defn seqR
  [new old l]
  (cons old (cons new l))
  )

(defn seqL
  [new old l]
  (cons new (cons old l)))

(defn insert-g
  "iether left or right"
  [seq]
  (fn [new old l]
    (cond
      (null? l) '()
      (equal? (car l) old ) ( seq new old ((insert-g seq) new old (cdr l) ) ) 
      :else (cons (car l)  ((insert-g seq) new old (cdr l)))
      
      )
    
    )
  )

((insert-g seqR) 'N 'a '(b a c))
((insert-g seqL) 'N 'a '(b a c))

(def insertL (insert-g seqL) )
(def insertR (insert-g seqR))

(insertL 'N 'a '(b a c))

(defn seqS
  [new old l]
  (cons new l)
  )

(def subst (insert-g seqS) )

(subst 'N 'a '(b a c))

(defn seqrem
  [new old l]
  l
  )

(defn rember
  [old l]
  ((insert-g seqrem) false old l )
  )

(rember  'a '(b a c))


(defn multirember&co
  [a l col]
  (cond
    (null? l) (col '() '())
    (equal? (car l) a) (multirember&co a (cdr l) (fn [newl seen]
                                      (col newl (cons (car l) seen ))
                                                   ))
    :else (multirember&co a (cdr l) (fn [newl seen]
                                      (col (cons (car l) newl) seen)
                                      )
                          )
    )
  )

(multirember&co 'a '(b c a d e) (fn [newl seen]
                                  (cons newl (cons seen '()))
                                  ) )


(defn multiinsertLR
  "insert either to the left or right"
  [new oldL oldR l]
  (cond
    (null? l) '()
    (equal? (car l) oldL) (cons new (cons oldL (multiinsertLR new oldL oldR (cdr l))))
    (equal? (car l) oldR) (cons oldR (cons new (multiinsertLR new oldL oldR (cdr l))))
    :else (cons (car l) (multiinsertLR new oldL oldR (cdr l)))
    )
  )

(multiinsertLR 'N 'a 'b '(c d e f a g b e) )

(defn multiinsertLR&co
  "use collectors to implement multiinsertLR"
  [new oldL oldR l col]
  (cond
    (null? l) (col '() 0 0)
    (equal? (car l) oldL) (multiinsertLR&co new oldL oldR (cdr l) (fn [newl L R]
                                                                 (col (cons new (cons oldL newl)) (add1 L) R )
                                                                 ))
    (equal? (car l) oldR) (multiinsertLR&co new oldL oldR (cdr l) (fn [newl L R]
                                                                 (col (cons oldR (cons new newl)) L (add1 R))))
    :else (multiinsertLR&co new oldL oldR (cdr l) (fn [newl L R]
                                                 (col (cons (car l) newl) L R )
                                                 ))
    
    )
  )

(multiinsertLR&co  'N 'a 'b '(c d e f a g b e) (fn [newl L R]
                                                 (cons newl (cons L (cons R '())) )
                                                 ))


(defn even??
  [n]
  (*= n (** (*d n 2) 2 ))
  )

(defn evens-only*
  "evens only"
  [l]
  (cond
    (null? l) '()
    (and (atom? (car l) ) (even?? (car l)) ) (cons (car l) (evens-only* (cdr l)))
    (atom? (car l)) (evens-only* (cdr l))
    :else (cons (evens-only* (car l)) (evens-only* (cdr l)))
    )
  )

(evens-only*  '( 1 2 3 4 (5 6 7 8 (9 10))) )

(defn evens-only*&co
  "builds a nested list of even numbers, multiplies even numbers and sums odd"
  [l col]
  (cond
    (null? l) (col '() 0 0)
    (and (atom? (car l)) (even?? (car l)) ) (evens-only*&co (cdr l) (fn [newl even odd]
                                                                      (col (cons (car l) newl) (*+ even (car l)) odd )
                                                                      ))
    (atom? (car l)) (evens-only*&co (cdr l) (fn [newl even odd]
                                              (col newl even  (*+ odd (car l))) ))
    
    :else      (evens-only*&co (car l) (fn [newl2 even2 odd2]
                                         (evens-only*&co (cdr l)  
                                                         (fn [newl even odd] 
                                                           (col (cons newl2 newl) (*+ even even2) (*+ odd odd2) ) )
                                                         ))) 
    
    )
  )

(evens-only*&co  '(1 2 3 4 (5 6 7 8 (9 10)))  (fn [newl even odd]
                                                (cons newl (cons even (cons odd '())))
                                                
                                                ) )


;; and again, and again..

(defn keep-looking
  "keep looking for a in lat"
  [a sorn lat]
  (cond
    (number? sorn) (keep-looking a (pick sorn lat) lat )
    :else (equal? a sorn)
    )
  
  )


(defn lokking 
  "look for a using breadcrumbs"
  [a lat]
  (keep-looking a (pick 1 lat) lat)
  )

(defn eternity
  [x]
  (eternity x)
  )


(defn shift
  "shift pair"
  [pair]
  (build (first (first pair) ) (build (second (first pair)) (second pair) ) )
  )

(shift '((a b) (c d) ))
(shift '((a b) c))

(defn align
  "keep shifting pora "
  [pora]
  (cond 
    (atom? pora) pora
    (a-pair? (first pora)) (align (shift pora))
    :else (build (first pora)
                 (align (second pora))
                 )
    
    )
  
  )

(defn length*
  "length of pora"
  [pora]
  (cond
    (atom? pora) 1
    :else (*+  (length* (first pora)) (length* (second pora)) )
    )
  )


(length* '((a b) (c d) ))

(defn weight* 
  "weight of pora"
  [pora]
  (atom? pora) 1
  :else (*+ (** (weight* (first pora) ) 2 ) (weight* (second pora)) )
  )

(defn shuffle
  "keep shifting pora "
  [pora]
  (cond
    (atom? pora) pora
    (a-pair? (first pora)) (shuffle (revpair pora))
    :else (build (first pora)
                 (shuffle (second pora)))))

(shuffle '((a b) (c d)))


(defn C
  [n]
  (cond 
    (one? n) 1
    (even? n) (C (/ n 2))
    :else (C (add1 (* 3 n)))
    )
  )

(defn A
  [n m]
  (cond 
    (zero? n) (add1 m)
    (zero? m) (A (sub1 n) 1)
    :else (A (sub1 n) (A n (sub1 m)) )
    
    
    )
  
  )

(A 1 0)
(A 2 2)


(defn length
  "length of a list"
  [l]
  (cond
    (null? l) 0
    :else (add1 (length (cdr l) ))
    
    )
  )

(length '(1 2 3))

(def length<=1
  (fn [l] 
    (cond 
      (null? l) 0
      :else (add1 
             ((fn [l]
                (cond
                  (null? l) 0
                  :else (add1 (eternity (cdr l)) )
                  )
                
                )(cdr l))
              
             )
      )
    )
  )

(length<=1 '())

(def length<=2
  (fn [l]
    (cond
      (null? l) 0
      :else (add1
             ((fn [l]
                (cond
                  (null? l) 0
                  :else (add1 ((fn [l]
                                 (cond
                                   (null? l) 0
                                   :else (add1 (eternity (cdr l))))) (cdr l))
                              ))) (cdr l)))
      
      )))

(length<=2 '(1 2))

(def length0-c
  (fn [length]
    (fn [l]
      (cond 
        (null? l) 0
        :else (add1 (length (cdr l)))
        
        )
      )
    )
  )

(length0-c eternity)

(def length<=1-c
  ((fn [f]
     (fn [l]
       (cond 
         (null? l) 0
         :else (add1 (f (cdr l)))
         )
       )
     )
   ((fn [g]
      (fn [l]
        (cond
          (null? l) 0
          :else (add1 (g (cdr l)))))) 
    eternity)
   )
  )

(length<=1-c '(1))

(def length<=2-c
  ((fn [length]
     (fn [l]
       (cond
         (null? l) 0
         :else (add1 (length (cdr l))))))
   ((fn [length]
      (fn [l]
        (cond
          (null? l) 0
          :else (add1 (length (cdr l))))))
    ((fn [length]
       (fn [l]
         (cond
           (null? l) 0
           :else (add1 (length (cdr l))))))
     eternity
     )
    
    )))

(length<=2-c '(1 2))


(def length0-c
  ((fn [mk-length]
     (mk-length eternity)
     )
   (fn [length]
     (fn [l]
       (cond
         (null? l) 0
         :else (add1 (length (cdr l))))))
   )
  )

(length0-c '())



(def length<=1-c
  ((fn [mk-length]
     (mk-length (mk-length eternity)))
   (fn [length]
     (fn [l]
       (cond
         (null? l) 0
         :else (add1 (length (cdr l)))))))
  )

(length<=1-c '(1))


(def length<=2-c
  ((fn [mk-length]
     (mk-length (mk-length (mk-length eternity))))
   (fn [length]
     (fn [l]
       (cond
         (null? l) 0
         :else (add1 (length (cdr l))))))))

(length<=2-c '(1 2))

(def length<=3-c
  ((fn [mk-length]
     (mk-length (mk-length (mk-length (mk-length eternity)))))
   (fn [length]
     (fn [l]
       (cond
         (null? l) 0
         :else (add1 (length (cdr l))))))))

(defn eternity
  [l]
  Double/NaN
  )

(length<=3-c '(1 2 3 4))

;; https://stackoverflow.com/questions/310974/what-is-tail-call-optimization

;; not tail recursive because of tracking multiply
(defn fact 
  [x]
  (cond 
    (= x 0) 1
    :else (* x (fact (- x 1)))
    )
  )

(fact 20)

;; tail recursive
(defn fact
  [x]
  (defn fact-tail
    [x accum]
    (cond
      (= x 0) accum
      :else (fact-tail (- x 1) (* x accum))))
  (fact-tail x 1)
  )
(fact 20)



(def length
  ((fn [mk-length]
     (mk-length mk-length))
   (fn [mk-length]
     (fn [l]
       (cond
         (null? l) 0
         :else (add1 ((mk-length mk-length) (cdr l)))))))
  )

(length '(1 2 3))

;; infinite loop, stackoverflow
(def len
  ((fn [mk-length]
     (mk-length mk-length))
   (fn [mk-length]
     ((fn [length]
        (fn [l]
          (cond
            (null? l) 0
            :else (add1 (length (cdr l))))))
      (mk-length mk-length)))))

;; now works
(def len
  ((fn [mk-length]
     (mk-length mk-length))
   (fn [mk-length]
     ((fn [length]
        (fn [l]
          (cond
            (null? l) 0
            :else (add1 (length (cdr l))))))
      (fn [x]
        ((mk-length mk-length) x))))))

(len '(1 2 3))

(def len
  "one more level of separation"
  ((fn [le]
     ((fn [mk-length]
          (mk-length mk-length))
        (fn [mk-length] 
          (le (fn [x]
                 ((mk-length mk-length) x))
                ))
          )
     )
   (fn [length]
     (fn [l]
       (cond
         (null? l) 0
         :else (add1 (length (cdr l))))))
   )
  )

(len '(1 2 3))

(def mk-length
  (fn [le]
    ((fn [mk-length]
       (mk-length mk-length))
     (fn [mk-length]
       (le (fn [x]
             ((mk-length mk-length) x)
             ) )
       )
     )
    )
  )

(def applicative-order-Y-combinator
  (fn [le]
    ((fn [f]
       (f f)
       )
     (fn [f]
       (le (fn [x]
             ((f f) x)
             ))
       )
     )
    )
  )




