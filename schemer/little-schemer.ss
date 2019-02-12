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

xyz

(x y z)

((x y) z)

(how are you doing so far)

(how are you doing so far) ;; 6 S-expressions in an S-expression

(((how) are) ((you) (doing so)) far) 

(((how) are) ((you) (doing so)) far) ;; list has 3 S-expressions

;; list & car cdr https://en.wikipedia.org/wiki/CAR_and_CDR

() ; empty list, not an atom

(() () () ()) ;; collection of S-expressions

(a b c) ;; a is a 'car' of list, because it is the frist atom

((a b c) x y z) ; (a b c) is the car


() ; cannot ask for car of an empty list. The Law of Car: the primitive car only for non-empty


(((hotdogs)) (and) (pickle) rel ish)  ; car is ((hotdogs))

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
            (else ( rember a
            ( cdr lat))))
        )
        )
    )) 

(rember 'a '(a b))


(define rember1
    (lambda (a lat)
        (cond
        ((null? lat) (quote ()))
        (else 
            (cond
            (( eq? ( car lat) a) ( cdr lat))
            (else (cons ( car lat)
                    (rember a
                    ( cdr lat))))
))))) 

(rember 'b '(a b c))

(define a '(x y z))
(rember 'y a) ; (x z)
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


(2 11 3 79 47 6) ; tuple (tup)

(1 2 8 apple 4 3) ; not a tuple (tup), lsit of atoms

()  ; tup

((null? '()) 0)

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

    ;; page 67

(define tup+
    (lambda (tup1 tup2)
        (cond 
            ((null? tup1) tup2 )
            ((null? tup2) tup1 )
            
            (else 
                (cons (o+ (  ) () ) '() )
                )
            )
    )
    
    )

   
