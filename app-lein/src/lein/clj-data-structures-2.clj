(ns lein.core)

(defn choice [] (println "choice"))

(count [])

(count nil)

(conj [] "3")

(conj '() "3")

'(1 2 3)

(def a 1)
(def b 2)
(def c 3)

'(a b c)

(conj '(a b c) :x)

(def lst '(a b c))

(conj lst :x)

lst

(identical? (rest (conj lst :x)) lst)

(def vec [1 2 3])

(conj vec 4)

([1 2] 0)

(def m {:a 1 :b 2})

m

(conj m {:c 3 :d 4})

{ 1 2 3 4}

(def sm (sorted-map :a 1 :b 2))

(conj sm {:b 5 :c 3 :d 4})

sm

(seq lst)

(identical? (seq lst) lst)

(seq vec)

(type (seq vec))

(seq m)

(count m)

(count lst)

(list* 1 2 3 [4 5 6])

(peek lst)

(peek '(1 2 3))

(pop '(1 2 3))

(peek [1 2 3])

(pop [1 2 3])

(first [1 2 3])

(rest [1 2 3])

(rest [1])

(rest [])

(first nil)

(rest nil)

; count O(1) seq conj

(rseq [1 2 3])

(assoc [1 2 3] 1 5)

(assoc [1 2 3] 3 5)

(assoc [1 2 3] 13 5)

(assoc [1 2 3] 3 5)

(get [1 2 3] 13)

(nth [1 2 3] 13)

(pop [])

(subvec [1 2 3 4 5] 2 4 )

(apply vector (seq "fred"))

(assoc m :c 1 :d 2 :e 3)

(dissoc m :b :e)

m

(get m :b)

(def mn {:a 1 :b nil })

mn

(get mn :b)

(get mn :c)

(contains? mn :c)

(get mn :c :not-found)

(get mn :b :not-found)

(find mn :b)

(seq m)

(key (first (seq m)))

(val (first (seq m)))

(keys m)

(vals m)

(merge {:a 1 :b 2} {:b 4 :c 3} {:a 5} )

(conj {:a 1 :b 2} {:b 4 :c 3} {:a 5})

(merge-with + {:a 1 :b 2} {:b 4 :c 3} {:a 5} )












(+ 1 1)
