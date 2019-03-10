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