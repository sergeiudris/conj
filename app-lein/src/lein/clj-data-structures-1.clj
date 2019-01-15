(ns lein.core)

(+ 3 4)

(foo 1)

#{:hey 3}



; (hello)

; (h1)

; raeding vs evaluation
{ :e 3 :d 45 }

(* 100000000000000000 10000000)

1.2

22/7

21/2

(* 5/20 4)

(* 5/20 4.0)
(* 5/20 (. Math PI))

(/ (* 22/7 3) 3)

(+)

(*)

(* 2)

(+ 1 2 3)

(< 4 5 6)

(< 4 5 3)

(min 3 1 2 1/3)

(dec 1/3)

(zero? 2)

(pos? 1/3)

(neg? -2.2)

(bit-and 1 2)
(bit-or 2 4)
(bit-xor 2 4)

(bit-not 3)

(bit-shift-right 2 2)

(bit-shift-left 2 2)

"fred"

(= 1 1)

; keywords are analog to enumerations in other lagunages
; keywords evaluate to themselves

(= :ab :ab)

:foo

; sttring pooling
(identical? "foo" "foo")

(= "foo" "foo")

(identical? :foo :foo)

(def m {:foo "a"})

m

(= (:foo m) "a")

(name :foo)

(= (name :foo) "foo")

'w ; quote

(quote w)

'foo/bar

(name 'foo/bar)

(namespace 'foo/bar)

(namespace 'foo/key/bar)

(name 'foo/key/bar)

(namespace `bar)

(gensym)

(gensym "id_")




