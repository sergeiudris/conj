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





    