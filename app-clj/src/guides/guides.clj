(ns guides.guides
  (:require [clojure.repl :refer :all]
            [clojure.spec.alpha :as s]
            [clojure.pprint :as pp]
            )
)


(comment
  
  ;; https://clojure.org/guides/weird_characters
  
  
    ; (+ 1 ##Inf)
  
  (type #inst "2014-05-19T19:12:37.925-00:00")


  (read-string "#inst \"2014-05-19T19:12:37.925-00:00\"")


  (def x (atom 1))

  x

  @x

  (deref x)

  (def ^{:debug true} five 5)

  (meta #'five)

  (def ^:debug five 5)

  (meta #'five)

  (def ^Integer five 5)

  (meta #'five)

  (def ^Integer ^:debug ^:private five 5)

  (meta #'five)

;   (import (basex.core BaseXClient$EventNotifier)
  
; (defn- build-notifier [notifier-action]
;   (reify BaseXClient$EventNotifier
;     (notify [this value]
;       (notifier-action value))))
  
  (defmacro debug [body]
    #_=>   `(let [val# ~body]
              #_=>      (println "DEBUG: " val#)
              #_=>      val#))


  (debug (+ 2 2))



  (def five 5)

  `five

  `~five

  (def three-and-four (list 3 4))

  `(1 ~three-and-four)

  `(1 ~@three-and-four)

  (defmacro m [] `(let [x 1] x))

  ;  (m)
  
  (defmacro m [] `(let [x# 1] x#))

  (m)
  
  
  
  )


(comment

  ;; https://clojure.org/guides/destructuring
  

  (def my-line [[5 10] [10 20]])

  (let [p1 (first my-line)
        p2 (second my-line)
        x1 (first p1)
        y1 (second p1)
        x2 (first p2)
        y2 (second p2)]
    (println "Line from (" x1 "," y1 ") to (" x2 ", " y2 ")"))

  (let [[p1 p2] my-line
        [x1 y1] p1
        [x2 y2] p2]
    (println "Line from (" x1 "," y1 ") to (" x2 ", " y2 ")"))

  (def my-vector [1 2 3])
  (def my-list '(1 2 3))
  (def my-string "abc")

  (let [[x y z] my-vector]
    (println x y z))

  (let [[x y z] my-list]
    (println x y z))

  (let [[x y z] my-string]
    (println x y z)
    (map type [x y z]))

  (def small-list '(1 2 3))
  (let [[a b c d e f g] small-list]
    (println a b c d e f g))

  (def large-list '(1 2 3 4 5 6 7 8 9 10))
  (let [[a b c] large-list]
    (println a b c))

  (def names ["Michael" "Amber" "Aaron" "Nick" "Earl" "Joe"])

  (let [[item1 & remaining] names]
    (println item1)
    (apply println remaining))

  (let [[item1 _ item3 _ item5 _] names]
    (println "Odd names:" item1 item3 item5))

  (let [[item1 :as all] names]
    (println "The first name from" all "is" item1))

  (def numbers [1 2 3 4 5])

  (let [[x & remaining :as all] numbers]
    (apply prn [remaining all]))

  (def word "Clojure")
  (let [[x & remaining :as all] word]
    (apply prn [x remaining all]))

  (def fruits ["apple" "orange" "strawberry" "peach" "pear" "lemon"])

  (let [[item1 _ item3 & remaining :as all-fruits] fruits]
    (println "The first and third fruits are" item1 "and" item3)
    (println "These were taken from" all-fruits)
    (println "The fruits after them are" remaining))


  (let [[[x1 y1] [x2 y2]] my-line]
    (println "Line from (" x1 "," y1 ") to (" x2 ", " y2 ")"))

  (let [[[a b :as group1] [c d :as group2]] my-line]
    (println a b group1)
    (println c d group2))

  (def client {:name "Super Co."
               :location "Philadelphia"
               :description "The worldwide leader in plastic tableware."})

  (let [{name :name
         location :location
         description :description} client]
    (println name location "-" description))

  (let [{category :category} client]
    (println category))

  (let [{category :category, :or {category "Category not found"}} client]
    (println category))

  (let [{name :name :as all} client]
    (println "The name from" all "is" name))


  (def my-map {:a "A" :b "B" :c 3 :d 4})
  (let [{a :a, x :x, :or {x "Not found!"}, :as all} my-map]
    (println "I got" a "from" all)
    (println "Where is x?" x))


  (let [{:keys [name location description]} client]
    (println name location "-" description))

  (let [{:keys [name location description]} client]
    (println name location "-" description))


  (def string-keys {"first-name" "Joe" "last-name" "Smith"})

  (let [{:strs [first-name last-name]} string-keys]
    (println first-name last-name))

  (def symbol-keys {'first-name "Jane" 'last-name "Doe"})

  (let [{:syms [first-name last-name]} symbol-keys]
    (println first-name last-name))

  (def multiplayer-game-state
    {:joe {:class "Ranger"
           :weapon "Longbow"
           :score 100}
     :jane {:class "Knight"
            :weapon "Greatsword"
            :score 140}
     :ryan {:class "Wizard"
            :weapon "Mystic Staff"
            :score 150}})

  (let [{{:keys [class weapon]} :joe} multiplayer-game-state]
    (println "Joe is a" class "wielding a" weapon))

  (defn configure [val options]
    (let [{:keys [debug verbose] :or {debug false, verbose false}} options]
      (println "val =" val " debug =" debug " verbose =" verbose)))

  (configure 12 {:debug true})

  (defn configure [val & {:keys [debug verbose]
                          :or {debug false, verbose false}}]
    (println "val =" val " debug =" debug " verbose =" verbose))

  (configure 10)

  (configure 5 :debug true)

  (configure 12 :verbose true :debug true)

 ;;The use of keyword arguments has fallen in and out of fashion in the Clojure community over the years.
 ;; They are now mostly used when presenting interfaces that people are expected to type at the REPL or the outermost layers of an API.
 ;; In general, inner layers of the code find it easier to pass options as an explicit map.
  
  (def human {:person/name "Franklin"
              :person/age 25
              :hobby/hobbies "running"})
  
  (let [{:keys [hobby/hobbies]
         :person/keys [name age]} human]
    (println name "is" age "and likes" hobbies))

  
  (let [{:person/keys [age]
         hobby-name :hobby/name
         person-name :person/name} human]
    (println person-name "is" age "and likes" hobby-name))

  (defn f-with-options
    [a b & {:keys [opt1]}]
    (println "Got" a b opt1))

  (f-with-options 1 2 :opt1 true)

  (defn print-coordinates-1 [point]
    (let [x (first point)
          y (second point)
          z (last point)]
      (println "x:" x ", y:" y ", z:" z)))

  (defn print-coordinates-2 [point]
    (let [[x y z] point]
      (println "x:" x ", y:" y ", z:" z)))

  (defn print-coordinates-3 [[x y z]]
    (println "x:" x ", y:" y ", z:" z))


  (def john-smith {:f-name "John"
                   :l-name "Smith"
                   :phone "555-555-5555"
                   :company "Functional Industries"
                   :title "Sith Lord of Git"})

  (defn print-contact-info [{:keys [f-name l-name phone company title]}]
    (println f-name l-name "is the" title "at" company)
    (println "You can reach him at" phone))

  (print-contact-info john-smith)

  (def john-smith {:f-name "John"
                   :l-name "Smith"
                   :phone "555-555-5555"
                   :address {:street "452 Lisp Ln."
                             :city "Macroville"
                             :state "Kentucky"
                             :zip "81321"}
                   :hobbies ["running" "hiking" "basketball"]
                   :company "Functional Industries"
                   :title "Sith Lord of Git"})

  
  (defn print-contact-info
    [{:keys [f-name l-name phone company title]
      {:keys [street city state zip]} :address
      [fav-hobby second-hobby] :hobbies}]
    (println f-name l-name "is the" title "at" company)
    (println "You can reach him at" phone)
    (println "He lives at" street city state zip)
    (println "Maybe you can write to him about" fav-hobby "or" second-hobby))

  (print-contact-info john-smith)

  
  (destructure '[[x & remaining :as all] numbers])

  )


(comment
  
  ;;https://clojure.org/guides/threading_macros
  
  (defn transform [person]
    (update (assoc person :hair-color :gray) :age inc))
  
  
  
  (transform {:name "Socrates", :age 39})
  
  
  (defn transform* [person]
    (-> person
        (assoc :hair-color :gray)
        (update :age inc)))

  (def person {:name "Socrates", :age 39})
  
  (-> person :hair-color name clojure.string/upper-case)


  (-> person (:hair-color) (name) (clojure.string/upper-case))

  (defn calculate []
    (reduce + (map #(* % %) (filter odd? (range 10)))))
  
  (defn calculate* []
    (->> (range 10)
         (filter odd?)
         (map #(* % %))
         (reduce +)))
  
  (-> "hello" clojure.string/lower-case (.startsWith "prefix"))
  
  (as-> [:foo :bar] v
    (map name v)
    (first v)
    (.substring v 1))
  
  (defn some-fn [] (vector :foo :bar))
  
  (some-fn)
  
  (as-> (some-fn) v
    (map name v)
    (first v)
    (.substring v 1))
  
  (when-let [counter (:counter a-map)]
    (inc (Long/parseLong counter)))
  
  (some-> a-map :counter Long/parseLong inc)
  
  
  
  (some-> (compute) Long/parseLong)

;; equivalent to
  
  (when-let [a-str (compute)]
    (Long/parseLong a-str))
  
  (defn describe-number [n]
    (cond-> []
      (odd? n) (conj "odd")
      (even? n) (conj "even")
      (zero? n) (conj "zero")
      (pos? n) (conj "positive")))
  
  (describe-number 3)
  
  (describe-number 4)
  
  )


(comment
  
  ;; equality
  
  (= (float 314.0) (double 314.0))
  
  (= 3 3N)
  
  (= 2 2.0)
  
  (range 3)
  
  (= [0 1 2] (range 3))
  
  (= [0 1 2] '(0 1 2))
  
  (= [0 1 2] [0 2 1])
  
  (= [0 1] [0 1 2])
  
  (= '(0 1 2) '(0 1 2.0))
  
  
  (def s1 #{1999 2001 3001})
  
  (def s2 (sorted-set 1999 2001 3001))
  
  (= s1 s2)
  
  (def m1 (sorted-map-by > 3 -7 5 10 15 20))
  
  (def m2 {3 -7 5 10 15 20})
  
  (= m1 m2)
  
  (def v1 ["a" "b" "c"])
  
  (def m1 {0 "a" 1 "b" 2 "c"})
  
  (v1 0)
  
  (m1 0)
  
  (= m1 v1)
  
  (def s1 (with-meta #{1 2 3} {:key1 "set 1"}))
  
  (def s2 (with-meta #{1 2 3} {:key1 "set 2 here"}))
  
  (binding [*print-meta* true] (pr-str s1) )
  
  (binding [*print-meta* true] (pr-str s2))
  
  (= s1 s2)
  
  (= (meta s1) (meta s2))
  
  (defrecord MyRec1 [a b])
  
  (def r1 (->MyRec1 1 2))
  
  r1
  
  (defrecord MyRec2 [a b])
  
  (def r2 (->MyRec2 1 2))
  
  r2
  
  (def m1 {:a 1 :b 2})
  
  (= r1 r2)
  
  (= r1 m1)
  
  (into {} r1)
  
  (= (into {} r1) m1)
  
  (= 1 1.0)
  
  (== 1 1.0)
  
  (def d1 (apply + (repeat 100 0.1)))
  
  d1
  
  (== d1 10.0)
  (Math/sqrt -1)

  ; (= ##NaN ##NaN)
  
  ; (== ##NaN ##NaN)
  

  ; (def s1 #{1.0 2.0 ##NaN})
  
  ; (s1 1.0)
  
  ; (s1 1.5)
  
  ; (s1 ##NaN)
  
  ; (disj s1 2.0)
  
  ; (disj s1 ##NaN)
  
  ; (= [1 ##NaN] [2 ##NaN])
  

  ; (def s2 #{ ##NaN 2.0 1.0})
  
  ; (= s1 s2)
  
  ; (identical? ##NaN ##NaN)
  
  ; (.equals ##NaN ##NaN)
  

  (hash ["a" 5 :c])

  (hash (seq ["a" 5 :c]))

  (hash '("a" 5 :c))
  
  (hash (conj clojure.lang.PersistentQueue/EMPTY "a" 5 :c))

  (def java-list (java.util.ArrayList. [1 2 3]))

  (def clj-vec [1 2 3])
  
  (= java-list clj-vec)

  (class java-list)

  (class clj-vec)
  
  (hash java-list)
  
  (hash clj-vec)
  
  (= [java-list] [clj-vec])
  
  (class {java-list 5})
  
  (= {java-list 5} {clj-vec 5})

  (assoc {} java-list 5 clj-vec 3)

  (class (hash-map java-list 5))
  
  (= (hash-map java-list 5) (hash-map clj-vec 5) )

  (= (hash-set java-list) (hash-set clj-vec))
  
  (get (hash-map java-list 5) java-list)
  (get (hash-map java-list 5) clj-vec)

  (conj #{} java-list clj-vec)

  (hash-map java-list 5 clj-vec 3)
  
  (def grid-keys (for [x (range 100), y (range 100)] [x y] ))

  (count grid-keys)
  
  (take 5 grid-keys)
  
  (take-last 5 grid-keys)
  
  (count (group-by #(.hashCode %) grid-keys ))

  (= (float 1.0e9) (double 1.0e9))
  
  (map hash [(float 1.0e9) (double 1.0e9)])

  (hash-map (float 1.0e9) :float-one (double 1.0e9) :oops )
  
  )


(comment
  
  (sort [22/7 2.71828 ##-Inf 1 55 3N])
  
  (sorted-set "aardvark" "boo" "a" "Antelope" "bar")
  
  (sorted-set 'clojure.core/pprint 'bar 'clojure.core/apply 'user/zz)
  (sorted-map :map-key 10 :amp [3 2 1] :blammo "kaboom")
  (sort [[-8 2 5] [-5 -1 20] [1 2] [1 -5] [100000]])
  
  (import '(java.util UUID))
  
  (sort [(UUID. 0xa 0) (UUID. 5 0x11) (UUID. 5 0xb)])
  
  (sort [:ns2/kw1 :ns2/kw2 :ns1/kw2 :kw2 nil])
  
  (sort [4 2 3 1])
  
  (defn reverse-cmp [a b]
    (compare b a)
    )
  
  (sort reverse-cmp [4 2 3 1 ])
  
  (sort #(compare %2 %1) [4 11 5 1] )
  
  
  (def john1 {:name "John" :salary 35000.00 :company "Acme"})
  (def mary {:name "Mary" :salary 35000.00 :company "Mars Inc"})
  (def john2 {:name "John" :salary 40000.00 :company "Venus Co"})
  (def john3 {:name "John" :salary 30000.00 :company "Asteroids-R-Us"})
  (def people [john1 mary john2 john3])
  
  (defn by-salary-name-co [x y]
    (let [c (compare (:salary y) (:salary x) ) ]
      (if (not= c 0)
        c
        (let [c (compare (:name x) (:name y) )]
          (if (not= c 0)
            c
            (let [c (compare (:company x) (:company y))]
              c
              )
            )
          )
        )
      ))
  
  ; (defn by-salary-name-co [x y]
  ; ;; :salary values sorted in decreasing order because x and y
  ; ;; swapped in this compare.
  ;   (let [c (compare (:salary y) (:salary x))]
  ;     (if (not= c 0)
  ;       c
  ;     ;; :name and :company are sorted in increasing order
  ;       (let [c (compare (:name x) (:name y))]
  ;         (if (not= c 0)
  ;           c
  ;           (let [c (compare (:company x) (:company y))]
  ;             c))))))
  
  (pp/pprint (sort by-salary-name-co people))
  
  (defn by-salary-name-co2 [x y]
    (compare [(:salary y) (:name x) (:company x) ]
             [(:salary x) (:name y) (:company y)]
             )
    )

  (pp/pprint (sort by-salary-name-co2 people))
  
  (defn my-< [a b]
    (println "(my-<" a b ") returns " (< a b))
    (< a b)
    )
  
  (. my-< (compare 1 2))

  (. my-< (compare 2 1 ))

  (. my-< (compare 1 1 ))

  (. my-< (invoke 2 1))
  
  
  
  (defn by-2nd [a b]
    (compare (second a) (second b)))
  
  (sorted-set-by by-2nd ["a" 1] ["b" 1] ["c" 1])
  
  (defn by-2nd-<= [a b]
    (<= (second a) (second b)))
  
  (def sset (sorted-set-by by-2nd-<= ["a" 1] ["b" 1] ["c" 1]))
  
  sset
  
  (sset ["c" 1])  
  (sset ["b" 1])
  (sset ["a" 1])
  
  (defn by-number-then-string [[a-str a-num] [b-str b-num]]
    (compare [a-num a-str]
             [b-num b-str]))
  
  (defn by-number-then-whatever [a-vec b-vec]
    (compare [(second a-vec) a-vec]
             [(second b-vec) b-vec]))
  
  (sort by-number-then-whatever [["a" 2] ["c" 3] [:b 2]])

  (doc cc-cmp)
  
  )


(comment 
  ;; https://clojure.org/guides/higher_order_functions
  
  
  
  
  )