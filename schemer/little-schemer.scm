;; https://github.com/frankitox/the-little-javascripter/blob/master/The%20Little%20Schemer%204th%20Ed.pdf
;; https://scheme.com/csug8/use.html

;; use (load "path") for loading file



(define atom?
    (lambda (x)
    (and (not (pair? x)) (not (null? x))))) 
        


(atom? (quote ()))


;; #f


;; atoms 
(quote atom) 

'turkey

'1492

'u

'*abc$

;; list

'(atom)

'(atom turkey or) 

'((atom tu rkey) or)

;; S-expression https://en.wikipedia.org/wiki/S-expression

'xyz

'(x y z)

'((x y) z)

'(how are you doing so far)

'(how are you doing so far) ;; 6 S-expressions in an S-expression

'(((how) are) ((you) (doing so)) far) 

'(((how) are) ((you) (doing so)) far) ;; list has 3 S-expressions

;; list & car cdr https://en.wikipedia.org/wiki/CAR_and_CDR

'() ; empty list, not an atom

'(() () () ()) ;; collection of S-expressions

'(a b c) ;; a is a 'car' of list, because it is the frist atom

'((a b c) x y z) ; (a b c) is the car


'() ; cannot ask for car of an empty list. The Law of Car: the primitive car only for non-empty


'(((hotdogs)) (and) (pickle) rel ish)  ; car is ((hotdogs))

(car '(1 2))

(cdr '(1 2)) ; list without car (basically, first and rest)

(cons 1 '(2 3))

(null? '()) ;; #t  (symbolic value)

(null? (quote ())) 

(atom? 'hey)

(eq? 'Harry 'Harry) ; #t

(define lat?
    (lambda (l)
        (cond
        ((null? l) #t )
        (( atom? ( car l)) (lat? ( cdr l)))
        (else #f ))
    ))


(lat? '(a b c))


(or (null? '()) (atom? '(a b)))


(define member?
    (lambda (a lat)
    (cond
    ((null? lat) #f )
    (else (or ( eq? ( car lat) a)
        (member? a ( cdr lat))
        )
    )))) 


(member? 'a '(a b))

(define rember0
    (lambda (a lat)
        (cond
        ( (null? lat) (quote ()))
        (else 
            (cond
            ( ( eq? ( car lat) a) ( cdr lat))
            (else ( rember0 a
            ( cdr lat))))
        )
        )
    )) 

(rember0 'a '(a b))


(define rember1
    (lambda (a lat)
        (cond
        ((null? lat) (quote ()))
        (else 
            (cond
            (( eq? ( car lat) a) ( cdr lat))
            (else (cons ( car lat)
                    (rember1 a
                    ( cdr lat))))
))))) 

(rember1 'b '(a b c))

(define a '(x y z))
(rember1 'y a) ; (x z)
a ; (x y z)

(define rember
    (lambda (a lat)
    (cond
    ((null? lat) (quote ()))
    ((eq? ( car lat) a) ( cdr lat))
    (else ( cons ( car lat)(rember a ( cdr lat))))
    ))) 




(define firsts
    (lambda (l)
    (cond
    ( (null? l) '() )
    (else ( cons (car (car l)) (firsts ( cdr l))))))) 

(firsts '((a) (b))) ; (a b)

(define seconds
    (lambda (l)
    (cond
    ( (null? l) '() )
    (else ( cons (car (cdr (car l))) (seconds ( cdr l))))))) 

(seconds '((a b) (c d)))

(define insertR
    (lambda (new old lat)
    (cond  
        ((null? lat) '())
        ((eq? (car lat) old) (cons old (cons new (cdr lat))))
        (else ( cons (car lat) (insertR new old (cdr lat)) ))
        ))) 

(insertR 'z 'a '(a b c))

(insertR 'z 'b '(a b c)) ; (a b z c)


(define insertL
    (lambda (new old lat)
    (cond  
        ((null? lat) '())
        ((eq? (car lat) old) (cons new lat))
        (else ( cons (car lat) (insertL new old (cdr lat)) ))
        ))) 

(insertL 'z 'c '(a b c)) ; (a b z c)


(define subst
    (lambda (new old lat)
    (cond  
        ((null? lat) '())
        ((eq? (car lat) old) (cons new (cdr lat)))
        (else ( cons (car lat) (subst new old (cdr lat)) ))
        ))) 

(subst 'z 'c '(a b c)) ; (a b z)


(define subst2
    (lambda (new o1 o2 lat)
    (cond  
        ((null? lat) '())
        ((or (eq? (car lat) o1) (eq? (car lat) o2)) (cons new (cdr lat))  )
        (else ( cons (car lat) (subst2 new o1 o2 (cdr lat)) ))
        ))) 

(subst2 'z 'd 'c '(a b c)) ; (a b z)



(define multirember
    (lambda (a lat)
    (cond  
        ((null? lat) '())
        ((eq? (car lat) a) (multirember a (cdr lat)))
        (else ( cons (car lat) (multirember a (cdr lat)) ))
        ))) 

(multirember 'b '(a b c b d b e)) ; (a c d e)



(define multiinsertR
    (lambda (new old lat)
    (cond  
        ((null? lat) '())
        ((eq? (car lat) old) (cons (car lat) (cons new (multiinsertR new old  (cdr lat))) ))
        (else ( cons (car lat) (multiinsertR new old (cdr lat)) ))
        ))) 

(multiinsertR 'z 'b '(a b c b)) ; (a b z c b z)


(define multiinsertL
    (lambda (new old lat)
    (cond  
        ((null? lat) '())
        ((eq? (car lat) old) (cons new (cons (car lat) (multiinsertL new old  (cdr lat))) ))
        (else ( cons (car lat) (multiinsertL new old (cdr lat)) ))
        ))) 

(multiinsertL 'z 'b '(a b c b)) ; (a z b c z b)


(define multisubst
    (lambda (new old lat)
    (cond  
        ((null? lat) '())
        ((eq? (car lat) old) (cons new (multisubst new old  (cdr lat))))
        (else ( cons (car lat) (multisubst new old (cdr lat)) ))
        ))) 

(multisubst 'z 'c '(c a b c d c)) ; (a b z)


(define add1
    (lambda (n)
    (+ n 1)))

(add1 67)

(define sub1 
    (lambda (n)
    (- n 1)))

(sub1 1)


(zero? 0)


; Yes! cons builds lists and add1 builds numbers.  p.60

(define o+
    (lambda (n m)
    (cond 
        ((zero? m) n)
        (else  (add1 (+ n (sub1 m))))
        )
    ))

(define o-
    (lambda (n m)
    (cond 
        ((zero? m) n)
        (else (sub1 (- n (sub1 m))))
        )
    )
)

(o- 3 1)


'(2 11 3 79 47 6) ; tuple (tup)

'(1 2 8 apple 4 3) ; not a tuple (tup), lsit of atoms

'()  ; tup


'((null? '()) 0)

(define addtup
    (lambda (tup)
    (cond 
        ((null? tup) 0)
        (else ( o+ (car tup) (addtup (cdr tup)) ) )        
        )
    )
)

(addtup '(1 2 3 ))

(define o* 
    (lambda (n m)
    (cond
    ( (zero? m) 0)
    ( (zero? n) 0)
    (else ( o+ n (o* n (sub1 m))  ) )
        )
    ; (else  )

    )
    )

    ;; page 68

(define tup+
    (lambda (tup1 tup2)
        (cond 
            ; ( (and (null? tup1) (null? tup2) ) '() ) ; not neccessary!
            ((null? tup1) tup2 )
            ((null? tup2) tup1 )
            (else
                (cons (o+ (car tup1) (car tup2))  (tup+ (cdr tup1) (cdr tup2)))
            )   
            )
    )
    
)


(define o> 
    (lambda (n m)
    (cond 
        ((zero? n) #f)
        ((zero? m) #t)
        (else 
            (o> (sub1 n) (sub1 m) )
        )
        )
    )
)



(define o< 
    (lambda (n m)
    (cond 
        ((zero? m) #f)
        ((zero? n) #t)
        (else 
            (o< (sub1 n) (sub1 m) )
        )
        )
    )
)

(define o=
    (lambda (n m)
    (cond
        ((and (zero? n) (zero? m)) #t)
        ((or (zero? n) (zero? m)) #f)
        (else (o= (sub1 n) (sub1 m)) )
        )
    )
    )


(define o=2
    (lambda (n m)
    (cond
        ((zero? n) (zero? m))
        ((zero? n) #f)
        (else (o= (sub1 n) (sub1 m)) )
        )
    )
)

(define o=3
    (lambda (n m)
    (cond
        ((o> n m) #t)
        ((o< n m) #f)
        (else #t)
        )
    )
)

(define o^ 
    (lambda (n m)
    (cond 
        ((zero? n) 0)
        ((zero? m) 1)
        ((o= m 1) n)
        (else (o* n (o^ n (sub1 m) )  ))
        )
    )
    
)

(define o^2
    (lambda (n m)
    (cond 
        ; ((zero? n) 0)
        ((zero? m) 1)
        ; ((o= m 1) n)
        (else (o* n (o^ n (sub1 m) )  ))
        )
    )
    
)

(define o/ 
    (lambda (n m)
        (cond 
            ((< n m) 0)
            (else (add1 (o/ (o- n m) m) ))

            )
    )
)

(define length 
    (lambda (lat)
        (cond 
            ((null? lat) 0)
            (else (add1 (length (cdr lat))) )
            )
    )
    
)


(define pick 
    (lambda (n lat)
        (cond
            ((zero? n) (car lat))
            ; ((null? lat) #f)
            (else  (pick (sub1 n) (cdr lat) ) )
            )
    )
)

(define pick2
    (lambda (n lat)
        (cond
            ((zero? (sub1 n)) (car lat))
            ; ((null? lat) #f)
            (else  (pick (sub1 n) (cdr lat) ) )
            )
    )
)

(define rempick 
    (lambda (n lat)
        (cond 
            ((null? lat) '())
            ((zero? (sub1 n)) (cdr lat) )
            (else (cons (car lat) (rempick (sub1 n) (cdr lat) ) ) )
            )
    )
    )

(define no-nums 
    (lambda (lat)
        (cond 
            ((null? lat) '())
            ((number? (car lat)) (no-nums (cdr lat)) )
            (else (cons (car lat) (no-nums (cdr lat)) ) ) 
            )
    )
)

(define all-nums
    (lambda (lat)
        (cond 
            ((null? lat) '())
            ((number? (car lat)) (cons (car lat) (all-nums (cdr lat)) )) 
            (else (all-nums (cdr lat) ))
            )
    )
)

(define eqan? 
    (lambda (a1 a2)
    (cond 
        ; ((and (atom? a1) (atom? a2)) )
        ((and (number? a1) (number? a2)) (o= a1 a2) )
        ((or (number? a1) (number? a2)) #f)
        (else (eq? a1 a2 ))
        )
    )
)

(define occur
    (lambda (a lat)
        (cond 
            ((null? lat) 0)
            ((eqan? (car lat) a) (add1 (occur a (cdr lat))) )
            (else  (occur a (cdr lat)) )
            )
    )
    
)

(occur 'a '(a b c a a)) ; 3

(define one?
    (lambda (n)
        (cond 
            ((zero? n) #f)
            ((o= n 1) #t)
            (else #f)
            )
    )
)

(define one?2
    (lambda (n)
        (cond 
            ((zero? n) #f)
            (else (zero? (sub1 n)))
            )
    )
)

(define one?3
    (lambda (n)
        (cond 
            (else (o= n 1))
            )
    )
)

(define one?4
    (lambda (n)
        (o= n 1)
    )
)

(define rempick2
    (lambda (n lat)
        (cond 
            ((one? n) (cdr lat))
            (else  (cons (car lat) (rempick2 (sub1 n) (cdr lat) ) ) )
            )
    )
)

(rempick2 3 '(a b c d))

(define rember* 
    (lambda (a l)
        (cond 
            ((null? l) '())
            ((atom? (car l)) 
                (cond 
                    ((eq? (car l) a) (rember* a (cdr l))  )
                    (else (cons (car l)  (rember* a (cdr l) ) ))
                    )
                )
            (else  (cons (rember* a (car l))  (rember* a (cdr l) )  ) )
            )
    )
)



(rember* 'a '(b a c (a d  (x y a z)  )))


(define insertR* 
    (lambda (new old l)
        (cond 
            ((null? l) '())
            ((atom? (car l))
                (cond 
                    ((eq? (car l) old) ( cons old (cons new  (insertR* new old (cdr l))  )))
                    (else  (cons (car l) (insertR* new old (cdr l) ) ) )
                    )
            
            )
            (else ( cons (insertR* new old (car l) )  (insertR* new old (cdr l))  ) )

    )
    
    )
)

(insertR* 'N 'a '(b a c (a d  (x y a z)  )))

'("
The First Commandment: 
- when recurring on a list of atoms, lat, ask two questions about it: (null? lat) and else
- when recurring on a number, ask two questions: zero? and else
- when recurring on a list of S-expressions ask three questions: (null? l) (atom? (car l)) else
")

(define occur* 
    (lambda (a l)
        (cond
            ((null? l) 0)
            ( (atom? (car l)) 
                (cond
                    ( (eq? a (car l)) (add1 (occur* a (cdr l) ) ) )
                    (else  (occur* a (cdr l))   )
                    )
            )
            (else (o+ (occur* a (car l) ) (occur* a (cdr l))  ) )
            )
    )
    
)
(occur* 'a  '(a b c (d e a (z (a) (a)))) )


(define subst* 
    (lambda (new old l)
        (cond
            ((null? l) '())
            ((atom? (car l)) 
                (cond 
                    ((eq? (car l) old )  (cons new ( subst* new old (cdr l))) )
                    (else (cons (car l) ( subst* new old (cdr l)) ) )                    
                )  
            )
            (else (cons (subst* new old (car l))   ( subst* new old (cdr l))) )
        )
    )
)

(subst* 'N 'a '(a b c (d e a (z (a) (a)))) )

(define insertL* 
    (lambda (new old l)
        (cond 
            ((null? l) '())
        ((atom? (car l))
            (cond
                ((eq? (car l) old) (cons new (cons old (insertL* new old (cdr l) )))   )
                (else  (cons (car l)  (insertL* new old (cdr l))  ) )
            )
        )
        (else 
            (cons (insertL* new old (car l)) (insertL* new old (cdr l)) )
            )
            
            
            )
    )
)

(insertL* 'N 'a '(a b c (d e a (z (a) (a)))) ) ;(N a b c (d e N a (z (N a) (N a))))


(define member* 
    (lambda (a l)
        (cond 
            ((null? l) #f)
            ((atom? (car l)) 
                (cond 
                    ((eq? a (car l)) #t )
                    (else (member* a (cdr l) ) )
                    )
            )
            (else 
                (or (member* a (car l)) (member* a (cdr l))   )
            )
            )

    )
    
)

(member* 'a '( b c d '(z x '(b a))))

(define leftmost 
    (lambda (l)
        (cond 
            ((atom? (car l)) (car l))
            (else (leftmost (car l)) )
        )
    )
)


(leftmost '(((x y z)) b c) )


(define eqlist? 
    (lambda (l1 l2)
        (cond 
            ((and (null? l1) (null? l2) ) #t )
            ( (null? l1) #f )
            ( (null? l2) #f )
            (else 
                (and 
                    (eq? (car l1) (car l2) )
                    (eqlist? (cdr l1) (cdr l2))
                )
            )
        )
    )
)

(eqlist? '(a b c) '(a b c))

(eqlist? '(a (b (z)) c) '(a (b (z)) c))

; (define eqlist?* 
;     (lambda (l1 l2)
;         (cond 
;             ((and (null? l1) (null? l2) ) #t )
;             ((or (null? l1) (null? l2) ) #f )
;             ((atom? (car l1))
;                 (cond 
;                   ((atom? (car l2)) (and (eqan? (car l1) (car l2) ) (eqlist?* (cdr l1) (cdr l2)) ) )
;                   (else   )
;                 )
;             )
;             (else 
;                 (and 
;                     (eq? (car l1) (car l2) )
;                     (eqlist? (cdr l1) (cdr l2))
;                 )
;             )
;         )
;     )
; )

(define eqlist?* 
    (lambda (l1 l2)
        (cond 
            ((and (null? l1) (null? l2) ) #t )
            ((or (null? l1) (null? l2) ) #f )
            ((and (atom? (car l1)) (atom? (car l2)) )
                (and (eqan? (car l1) (car l2) ) (eqlist?* (cdr l1) (cdr l2)) )
            )
            ((or (atom? (car l1)) (atom? (car l2)) ) #f)
            (else (and  (eqlist?* (car l1) (car l2) )  (eqlist?* (cdr l1) (cdr l2) ) ))
        )
    )
)

(eqlist?* '(a (b (z)) c) '(a (b (z)) c))

(define equal?*
    (lambda (s1 s2) 
        (cond 
            ( (and (atom? s1 ) (atom? s2 ) ) (eqan? s1 s2))
            ((or (atom?  s1) (atom? s2) ) #f)
            (else (eqlist?* s1 s2) )
        )
    
    )
)

(equal?* '(a (b (z)) c) '(a (b (z)) c))
(equal?* 'a 'a)
(equal?* 1 1)

(define rember-any
    (lambda (s l)
        (cond 
            ((null? l) '())
            ((equal?* s (car l)) (rember-any s (cdr l)) )
            ((atom? (car l)) (cons (car l) (rember-any s (cdr l)) ) )
            (else (cons (rember-any s (car l) ) (rember-any s (cdr l)) )   )   
        )
    )
)

(rember-any '(a b) '(a b (z (a b)) c ))

;; shadows

(define numbered? 
    (lambda (aexp)
        (cond 
            ((atom? aexp) (number? aexp))
            (else (and 
                (or (eq? (car (cdr aexp)) '+) (eq? (car (cdr aexp)) '*) (eq? (car (cdr aexp)) 'expt)) 
                (and (numbered? (car aexp) ) (numbered? (car (cdr (cdr aexp)) )))
                ))
            )
    )
    
)

(numbered? '(1 + (2 expt (3 * 4))) )

(define a (lambda (a b c) (c a b) )  )

(a 2 3 expt)

(define value 
    (lambda (nexp)
        (cond 
            ((atom? nexp) nexp)
            ((eq? (car (cdr nexp)) '+) (+  (value (car nexp)) (value (car (cdr (cdr nexp))))   ))
            ((eq? (car (cdr nexp)) '*) (*  (value (car nexp)) (value (car (cdr (cdr nexp))))   ))
            ((eq? (car (cdr nexp)) '^) (expt  (value (car nexp)) (value (car (cdr (cdr nexp))))   ))

            ; (else ( 
            ;     (car (cdr nexp))  
            ;         (value (car nexp)) 
            ;         (value (car (cdr (cdr nexp)))  ) 
            ;     )   
            ; )           
        )
    )
)

(value '(1 + 2))
(value '(2 ^ (1 + (1 * 2))))
(value '(1 + (3 ^ 4)))

(define no-else 
    (lambda (a)
        (cond 
            ((eq? a 'a) 1)
            ((eq? a 'b) 2)
            ((eq? a 'c) 3)
        )
    )
)
;value-declarative, using (+ 1 1) notatino
(define value-d
    (lambda (nexp)
        (cond 
            ((atom? nexp) nexp)
            ((eq? (car nexp) '+ ) (+ (value-d (car (cdr nexp) )) (value-d (car (cdr (cdr nexp)) )  ) ) )
            ((eq? (car nexp) '* ) (* (value-d (car (cdr nexp) )) (value-d (car (cdr (cdr nexp)) )) ) )
            ((eq? (car nexp) '^ ) (expt (value-d (car (cdr nexp) )) (value-d (car (cdr (cdr nexp)) )) ) )
        )
    )
)
(value-d '(+ 1 2))
(value-d '(+ 1 (^ 3 4)))
(value-d '(^ 2  (+ 1 (* 1 2))))


(define 1st-sub-exp 
    (lambda (aexp)
        (car (cdr aexp))
    )
)

(define 2st-sub-exp 
    (lambda (aexp)
        (car (cdr (cdr aexp)))
    )
)

(define operator 
    (lambda (aexp)
        (car aexp)
    )
)

; eighth commandment: use helper functions to abstract from representations

(define value-d2
    (lambda (nexp)
        (cond 
            ((atom? nexp) nexp)
            ((eq? (operator nexp) '+ ) (+ (value-d2 (1st-sub-exp nexp) ) (value-d2 (2st-sub-exp nexp )  ) ) )
            ((eq? (operator nexp) '* ) (* (value-d2 (1st-sub-exp nexp)) (value-d2 (2st-sub-exp nexp ) ) ) )
            ((eq? (operator nexp) '^ ) (expt (value-d2 (1st-sub-exp nexp)) (value-d2 (2st-sub-exp nexp ) ) ) )
        )
    )
)

(value-d2 '(+ 1 2))
(value-d2 '(+ 1 (^ 3 4)))
(value-d2 '(^ 2  (+ 1 (* 1 2))))


; how many primitives we need for numbers ?  A - four:  zero? sub1 add1 number?

(define sero? 
    (lambda (n)
        (null? n)
    )
)

(define edd1 
    (lambda (n)
        (cons '() n)    
    )
)

(define zub1 
    (lambda (n)
        (cdr n)    
    )
)

(define blus 
    (lambda (n m)
        (cond 
            ((null? m) n)
            ((cons (car m) (blus n (cdr m)) ))
            )
    )
    
)

(define blus2 
    (lambda (n m)
        (cond 
            ((sero? m) n)
            (else (edd1 (blus n (zub1 m)) ))
        )
    )
    
)

(blus2 '(() () ()) '(() ()))


;; Friends and Relations


(define eqan? 
    (lambda (a1 a2)
    (cond 
        ; ((and (atom? a1) (atom? a2)) )
        ((and (number? a1) (number? a2)) (= a1 a2) )
        ((or (number? a1) (number? a2)) #f)
        (else (eq? a1 a2 ))
        )
    )
)

(define member?
    (lambda (a lat)
    (cond
    ((null? lat) #f )
    (else (or ( equal?* ( car lat) a)
        (member? a ( cdr lat))
        )
    )))) 


(define set?
    (lambda (lat)
        (cond 
            ((null? lat) #t)
            ((member? (car lat) (cdr lat) ) #f)
            (else (set? (cdr lat)))
        )
    )
)

(set? '(a a b c ) )

(define makeset 
    (lambda (lat)
        (cond 
            ((null? lat) lat)
            ((member? (car lat) (cdr lat) ) (makeset (cons (car lat) (rember (car lat) (cdr lat) ) )  ) )
            (else (cons (car lat) (makeset (cdr lat)) ))
        )
    )
    
)

(define multirember
    (lambda (a lat)
    (cond  
        ((null? lat) '())
        ((equal? (car lat) a) (multirember a (cdr lat)))
        (else ( cons (car lat) (multirember a (cdr lat)) ))
        ))) 

(define makeset 
        (lambda (lat)
            (cond 
                ((null? lat) '())
                ( (cons (car lat) (makeset (multirember (car lat) (cdr lat) ) ) ) )
                
                )
        )
)

(define subset? 
    (lambda (set1 set2)
        (cond 
            ; ((and (null? set1) (null? set2) ) #t )
            ((null? set1) #t)
            (else (and (member? (car set1) set2 ) (subset? (cdr set1) set2) ) )
        )
    )
    
)

(define eqset? 
    (lambda (set1 set2)
        ( (and (subset? set1 set2) (subset? set2 set1) ))
    )
)

(define intersect?
        (lambda (set1 set2)
            (cond
                ((null? set1) #f)
                (else (or (member? (car set1) set2 ) (intersect? (cdr set1) set2) )    
            )
        
            )
        )
)


(define intersect 
    (lambda (set1 set2)
        (cond 
            ((null? set1) '())
            ((member? (car set1) set2) (cons (car set1) (intersect (cdr set1) set2) ) )  
            (else (intersect (cdr set1) set2))  
        )
    
    )    
)

(intersect '(a b) '(x y a))

(define union 
    (lambda (set1 set2)
        (cond 
            ((null? set1) set2)
            ((member? (car set1) set2) (union (cdr set1) set2 ) )
            (else (cons (car set1) (union (cdr set1) set2) ) )
        )
    )
    
)
(union '(a b c) '(x y a))


(define set-diff 
    (lambda (set1 set2)
        (cond 
            ((null? set1) '())
            ((member? (car set1) set2) (set-diff (cdr set1) set2 ) )
            (else (cons (car set1) (set-diff (cdr set1) set2) ) )
        )
    )
    
)

(define intersectall 
    (lambda (l-set)
        (cond 
            ((null? l-set) '())
            ((null? (cdr l-set)) (car l-set))
            (else (intersect (car l-set)  (intersectall (cdr l-set))  ) )
        )    
    )
    
)

(intersectall '( (6 p a) (3 pe a 6 pp) (8 p a 6 pl) (a 6 pr w s ap) ))

(intersectall '((1 2 3) (2) (1 2 3)))


(define a-pair? 
    (lambda (x)
        (cond 
            ((atom? x) #f)
            ((or (null? x) (null? (cdr x) ) ) #f)
            ((null? (cdr (cdr x))) #t)
            (else #f)
        )
    )
    
)

(define first 
    (lambda (p)
        (car p)
    )    
    
)
(define second 
    (lambda (p)
        (car (cdr p))
    )    
    
)

(define build 
    (lambda (s1 s2)
        (cons s1 (cons s2 '()))
    )    
)

(define third 
    (lambda (l)
        (car (cdr (cdr l)))
    )
)

(define fun? 
    
    (lambda (rel)
        (set? (firsts rel))    
    )
    
)

; finite function here will be: a list of apirs where firsts are unique
; it means, that any input has one output

(define revrel 
    (lambda (rel)
        (cond 
            ((null? rel) '())
            ; (else (cons
            ;     (cons (second (first rel)) (cons (first (first rel)) '()))
            ;     (revrel (cdr rel))
            ; ))
            (else 
               (cons (build (second (car rel)) (first (car rel)))     (revrel (cdr rel)) )
            )  
        )  
    )
)
(revrel '((1 2) (3 4)))

(define revpair 
    (lambda (pair)
        (build (second pair) (first pair) )
    )
)

(define revrel 
    (lambda (rel)
        (cond 
            ((null? rel) '())
            ; (else (cons
            ;     (cons (second (first rel)) (cons (first (first rel)) '()))
            ;     (revrel (cdr rel))
            ; ))
            (else 
               (cons  (revpair (car rel))    (revrel (cdr rel)) )
            )  
        )  
    )
)

(define fullfun? 
    (lambda (fun)
        (set? (seconds fun))
    )
)

(fullfun? '( (1 2) (3 4) (5 6) ))
(fullfun? '( (1 2) (3 4) (5 2) ))

(define one-to-one? 
    (lambda (fun)
        (fun? (revrel fun))
    )
    
)

(one-to-one? '( (1 2) (3 4) (5 2) ))


;; Lambda the Ultimate

(define rember-f 
    (lambda (test? a l)
        (cond 
            ((null? l) '())
            ((test? a (car l)) (rember-f test? a (cdr l)) )
            (else (cons (car l) (rember-f test? a (cdr l)) ) 
                )
            )
    )    
)
(rember-f o= 2 '(2 3))

(rember-f eq? 'a '(a b a))

(rember-f equal?* '(1 2) '(a (1 2) a))

