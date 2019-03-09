(ns guides.spec
  (:require [clojure.repl :refer :all]
            [clojure.spec.alpha :as s]
            [clojure.pprint :as pp]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.test.alpha :as stest]
            
            )
  (:import java.util.Date )
  )

(defn hi [] "hi")

(comment
  
  (hi)
  
  )


(comment

  (s/conform even? 1000)

  (s/conform even? 1001)

  (s/valid? even? 10)

  (s/valid? nil? nil)

  (s/valid? string? "abc")

  (s/valid? #(> % 5) 10)

  (s/valid? #(> % 5) 0)

  (s/valid? inst? (Date.))

  (s/valid? #{:club :diamond :heart :spade} :club)

  (s/valid? #{:club :diamond :heart :spade} 42)

  (s/valid? #{42} 42)

  (s/def ::date inst?)
  (s/def ::suit #{:club :diamond :heart :spade})

  (s/valid? ::date (java.util.Date.))

  (s/valid? ::suit :spade)

  (doc ::date)

  (s/def ::big-even (s/and int? even? #(> % 1000)))

  (s/valid? ::big-even :foo)
  (s/valid? ::big-even 10)
  (s/valid? ::big-even 10000)


  (s/def ::name-or-id (s/or :name string? :id int?))

  (s/valid? ::name-or-id "abc")

  (s/valid? ::name-or-id 100)

  (s/valid? ::name-or-id :foo)

  (s/conform ::name-or-id "abc")

  (s/conform ::name-or-id 11)

  (s/valid? string? nil)

  (s/valid? (s/nilable string?) nil)

  (s/explain ::suit  42)

  (s/explain ::big-even  5)

  (s/explain ::name-or-id :foo)

  (s/explain ::name-or-id "abc")


  (doc re-matches)


  (def email-regex #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$")

  (s/def ::email-type (s/and string? #(re-matches email-regex %)))

  (s/def ::acctid int?)
  (s/def ::first-name string?)
  (s/def ::last-name string?)
  (s/def ::email ::email-type)

  (s/def ::person (s/keys :req [::first-name ::last-name ::email]
                          :opt [::phone]))

  (s/valid? ::person {::first-name "bugs"
                      ::last-name "bunny"
                      ::email "bugs@example.com"})

  (s/explain ::person {::first-name "bugs"})

  (s/explain ::person {::first-name "bugs"
                       ::last-name "bunyy"
                       ::email "n/a"})


  (s/explain ::person {::first-name "bugs"
                       ::last-name "bunyy"
                       :email "bugs@example.com"})


  (s/def :unq/person
    (s/keys :req-un [::first-name ::last-name ::email]
            :opt-un [::phone]))

  (s/conform :unq/person {:first-name "bugs"
                          :last-name "bunyy"
                          :email "bugs@example.com"})


  (s/explain :unq/person {:first-name "bugs"
                          :last-name "bunyy"
                          :email "n/a"})



  (s/explain :unq/person {:first-name "bugs"})


  (defrecord Person [first-name last-name email phone])

  (s/explain :unq/person (->Person "Bugs" nil nil nil))

  (s/conform :unq/person
             (->Person "Bugs" "Bunny" "bugs@example.com" nil))


  (s/def ::port number?)
  (s/def ::host string?)
  (s/def ::id keyword?)
  (s/def ::server
    (s/keys* :req [::id ::host] :opt [::port]))
  (s/conform ::server [::id :s1 ::host "example.com" ::port 5555])

  (s/def :animal/kind string?)
  (s/def :animal/says string?)
  (s/def :animal/common (s/keys :req [:animal/kind :animal/says]))
  (s/def :dog/tail? boolean?)
  (s/def :dog/breed string?)
  (s/def :animal/dog (s/merge :animal/common
                              (s/keys :req [:dog/tail? :dog/breed])))

  (s/valid? :animal/dog
            {:animal/kind "dog"
             :animal/says "woof"
             :dog/tail? true
             :dog/breed "retriever"})

  (s/explain :animal/dog
             {:animal/kind "dog"
              :animal/says "woof"
              :dog/tail? true
              :dog/breed "retriever"})



  ;; multi-spec
  
  (s/def :event/type keyword?)
  (s/def :event/timestamp int?)
  (s/def :search/url string?)
  (s/def :error/message string?)
  (s/def :error/code int?)

  (defmulti event-type :event/type)

  (defmethod event-type :event/search [_]
    (s/keys :req [:event/type :event/timestamp :search/url]))

  (defmethod event-type :event/error [_]
    (s/keys :req [:event/type :event/timestamp :error/message :error/code]))

  (s/def :event/event (s/multi-spec event-type :event/type))

  (s/valid? :event/event
            {:event/type :event/search
             :event/timestamp 1463970123000
             :search/url "https://clojure.org"})

  (s/valid? :event/event
            {:event/type :event/error
             :event/timestamp 1463970123000
             :error/message "invalid host"
             :error/code 500})

  (s/explain :event/event
             {:event/type :event/restart})



  (s/explain :event/event
             {:event/type :event/search
              :search/url 200})



  (s/conform (s/coll-of keyword?) [:a :b :c])

  (s/conform (s/coll-of number?) #{5 10 2})

  (s/def ::vnum3 (s/coll-of number? :kind vector? :count 3 :distinct true :into #{}))

  (s/conform  ::vnum3 [1 2 3])

  (s/explain ::vnum3 #{1 2 3})

  (s/explain ::vnum3 [1 1 1])

  (s/explain ::vnum3 [1 1 :a])

  (s/def ::point (s/tuple double? double? double?))

  (s/conform ::point [1.5 2.5 -0.5])

  (s/def ::scores  (s/map-of string? int?))

  (s/conform ::scores {"Sally" 1000 "Joe" 500})

  (s/def ::ingridient (s/cat :quantity number? :unit keyword?))

  (s/conform ::ingridient [2 :teaspoon])

  (s/explain ::ingridient  [11 "peaches"])

  (s/explain ::ingridient  [2])


  (s/def ::seq-of-keywords (s/* keyword?))
  (s/conform ::seq-of-keywords [:a :b :c])

  (s/explain ::seq-of-keywords [10 20])

  (s/def ::odds-then-maybe-even (s/cat :odds (s/+ odd?)
                                       :even (s/? even?)))

  (s/conform ::odds-then-maybe-even [1 3 5 100])

  (s/conform ::odds-then-maybe-even [1])

  (s/explain ::odds-then-maybe-even [100])


  (s/def ::opts (s/* (s/cat :opt keyword? :val boolean?)))

  (s/conform ::opts [:silent? false :verbose true])

  (s/def ::config (s/* (s/cat :prop string?
                              :val (s/alt :s string? :b boolean?))))

  (s/conform ::config ["-server" "foo" "-verbose" true "-user" "joe"])

  (s/describe ::seq-of-keywords)

  (s/describe ::odds-then-maybe-even)

  (s/describe ::opts)

  (s/def ::even-strings (s/& (s/* string?) #(even? (count %))))

  (s/valid? ::even-strings ["a"])
  (s/valid? ::even-strings ["a" "b"])

  (s/def ::nested
    (s/cat
     :names-kw #{:names}
     :names (s/spec (s/* string?))
     :nums-kw #{:nums}
     :nums (s/spec (s/* number?))))

  (s/conform ::nested [:names ["a" "b"] :nums [1 2 3]])


  (s/def ::unnested
    (s/cat :names-kw #{:names}
           :names (s/* string?)
           :nums-kw #{:nums}
           :nums (s/* number?)))

  (s/conform ::unnested [:names "a" "b" :nums 1 2 3])


  (defn person-name
    [person]
    {:pre [(s/valid? ::person person)]
     :post [(s/valid? string? %)]}
    (str (::first-name person) " " (::last-name person)))

  (person-name 42)

  (person-name
   {::first-name "bugs"
    ::last-name "bunny"
    ::email "bugs@example.com"})


  (defn person-name
    [person]
    (let [p (s/assert ::person person)]
      (str (::first-name p) " " (::last-name p))))

  (s/check-asserts true)

  (person-name 100)

  (defn set-config
    [prop val]
    (println "set" prop val))

  (defn configure [input]
    (let [parsed (s/conform ::config input)]
      (if (= parsed ::s/invalid)
        (throw (ex-info "Invalid input" (s/explain ::config input))))
      (for [{prop :prop [_ val] :val} parsed]
        (set-config (subs prop 1) val))))


  (configure ["-server" "foo" "-verbose" true "-user" "joe"])

  (configure ["-server" "foo" "-verbose" 1 "-user" "joe"])

  (defn ranged-rand
    "returns random int in range start <= rand < end"
    [start end]
    (+ start (long (rand (- end start)))))

  (ranged-rand 1 10)

  (s/fdef ranged-rand
    :args (s/and (s/cat :start int? :end int?)
                 #(< (:start %) (:end %)))
    :ret int?
    :fn (s/and #(>= (:ret %) (-> % :args :start))
               #(< (:ret %) (-> % :args :end))))


  (doc ranged-rand)
  (ranged-rand  10 1)



  ;; higher order functions
  
  (defn adder
    [x]
    #(+ x %))


  (s/fdef adder
    :args (s/cat :x number?)
    :ret (s/fspec :args (s/cat :y number?)
                  :ret number?)

    :fn #(= (-> % :args :x) ((:ret %) 0)))



  (s/fdef clojure.core/declare
    :args (s/cat :names (s/* simple-symbol?)))

  (declare 100)

  (def suit? #{:club :diamond :heart :spade})

  (def rank? (into #{:jack :queen :king :ace} (range 2 11)))

  (def deck (for [suit suit? rank rank?] [rank suit]))

  (s/def ::card (s/tuple rank? suit?))
  (s/def ::hand (s/* ::card))

  (s/def ::name string?)
  (s/def ::score int?)
  (s/def ::player (s/keys :req [::name ::score ::hand]))

  (s/def ::players (s/* ::player))

  (s/def ::deck (s/* ::card))

  (s/def ::game (s/keys :req [::players ::deck]))

  (def kenny
    {::name "kenny rogers"
     ::score 100
     ::hand []})
  
  (s/valid? ::player kenny)
  (s/explain ::player kenny)

  (s/explain ::game
             {::deck deck
              ::players [{::name "Kenny Rogers"
                          ::score 100
                          ::hand [[2 :banana]]}]})

  

  (defn total-cards [{:keys [::deck ::players] :as game}]
    (apply + (count deck)
           (map #(-> % ::hand count) players)))


  (defn deal [game] nil )

  (s/fdef deal
    :args (s/cat :game ::game)
    :ret ::game
    :fn #(= (total-cards (-> % :args :game))
            (total-cards (-> % :ret))))


  ;geenrators
  
  
  (gen/generate (s/gen int?))

  (gen/generate (s/gen nil?))

  (gen/sample (s/gen string?))
  
  
  (gen/sample (s/gen #{:club :diamond :heart :spade}))
  
  (gen/sample (s/gen (s/cat :k keyword? :ns (s/+ number?))))

  (gen/generate (s/gen ::player))

  (gen/generate (s/gen ::game))

  (-> 
   (s/exercise (s/cat :k keyword?  :ns (s/+ number?) ) 5)
   pp/pprint
   )

  
  (->
   (s/exercise (s/or :k keyword? :s string? :n number?) 5)
   pp/pprint)

  (s/exercise-fn `ranged-rand)

  
  (gen/generate (s/gen (s/and int? even?)))
  
  (defn divisible-by [n] #(zero? (mod % n)))
  
  (gen/sample (s/gen (s/and int?
                            #(> % 0)
                            (divisible-by 3))))

  ; (gen/sample (s/gen (s/and string? #(clojure.string/includes? % "hello"))))
  
  (s/def ::kws (s/and keyword? #(= (namespace %) "clojure.core")))

  (s/valid? ::kws :clojure.core/even?)

  (gen/sample (s/gen ::kws))

  (def kw-gen (s/gen #{:clojure.core/even? :clojure.core/odd? :clojure.core/vec?}))

  (gen/sample kw-gen 5)
  
  (s/def ::kws (s/with-gen (s/and keyword? #(= (namespace %) "clojure.core"))
                 #(s/gen #{:clojure.core/even? :clojure.core/odd? :clojure.core/vec?})))

  (s/valid? ::kws :clojure.core/int?)

  (gen/sample (s/gen ::kws))

  (def kw-gen-2 (gen/fmap #(keyword "clojure.core" %) (gen/string-alphanumeric)))

  (gen/sample kw-gen-2 5)
  
  (def kw-gen-3 (gen/fmap #(keyword "clojure.core" %)
                          (gen/such-that #(not= % "")
                                         (gen/string-alphanumeric))))

  (gen/sample kw-gen-3 5)

  (s/def ::hello
    (s/with-gen #(clojure.string/includes? % "hello")
      #(gen/fmap (fn [[s1 s2]] (str s1 "hello" s2))
                 (gen/tuple (gen/string-alphanumeric) (gen/string-alphanumeric)))))
  
  (gen/sample (s/gen ::hello))

  (s/def ::roll (s/int-in 0 11))

  (gen/sample (s/gen ::roll))

  (s/def ::the-aughts (s/inst-in #inst "2000" #inst "2010"))

  (drop 50 (gen/sample (s/gen ::the-aughts) 55))

  (s/def ::dubs (s/double-in :min -100.0 :max 100.0 :NaN? false :infinite? false))

  (s/valid? ::dubs 2.9)

  (s/valid? ::dubs Double/POSITIVE_INFINITY)

  (gen/sample (s/gen ::dubs))

  (stest/instrument `ranged-rand)

  ; (ranged-rand 8 5)
  
  (stest/unstrument `ranged-rand)

  ; (stest/check `ranged-rand)

  (defn ranged-rand  ;; BROKEN!
    "Returns random int in range start <= rand < end"
    [start end]
    (+ start (long (rand (- start end)))))

  (stest/abbrev-result (first (stest/check `ranged-rand)))
  
  
  (-> (stest/enumerate-namespace 'clojure.core) stest/check)
  
  ; (stest/check)
  
  (defn invoke-service [service request]
  ;; invokes remote service
    )

  (defn run-query [service query]
    (let [{::keys [result error]} (invoke-service service {::query query})]
      (or result error)))


  (s/def ::query string?)
  (s/def ::request (s/keys :req [::query]))
  (s/def ::result (s/coll-of string? :gen-max 3))
  (s/def ::error int?)
  (s/def ::response (s/or :ok (s/keys :req [::result])
                          :err (s/keys :req [::error])))

  (s/fdef invoke-service
    :args (s/cat :service any? :request ::request)
    :ret ::response)

  (s/fdef run-query
    :args (s/cat :service any? :query string?)
    :ret (s/or :ok ::result :err ::error))
  
  (stest/instrument `invoke-service {:stub #{`invoke-service}})

  ; (invoke-service nil {::query "test"})

  ; (invoke-service nil {::query "test"})

  ; (stest/summarize-results (stest/check `run-query))

  )