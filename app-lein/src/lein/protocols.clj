(ns lein.core)

(defprotocol P
 (foo [x])
 (bar-me [x] [x y]))

(deftype Foo [a b c]
 P
 (foo [x] a)
 (bar-me [x] b)
 (bar-me [x y] (+ c y)))

(bar-me (Foo. 1 2 3) 42)

(foo
 (let [x 42] 
   (reify P
     (foo [this] 17)
     (bar-me [this] x)
     (bar-me [this y] x))))

(bar-me
 (let [x 42]
   (reify P
     (foo [this] 17)
     (bar-me [this] x)
     (bar-me [this y] x))))


(+)
