(ns guides.reader-cond
  (:require [clojure.repl :refer :all]
            [clojure.spec.alpha :as s]
            [clojure.pprint :as pp]))


(comment
 ;; https://clojure.org/guides/reader_conditionals
  

  #?(
     :clj (Clojure expression)
     :cljs (ClojureScript expression)
     :cljr (Clojure CLR expression)
     :default (fallthrough expression)
     )
  
  (defn build-list []
    (list #?@(:clj  [5 6 7 8]
              :cljs [1 2 3 4])))
  
  (build-list)
  
  (defn build-list []
    (list 5 6 7 8))
  
  
  (defn str->int [s]
    #?(:clj  (java.lang.Integer/parseInt s)
       :cljs (js/parseInt s)))
  
  (str->int "3")
  
  (ns route-ccrs.schema.ids.part-no-test
    (:require #?(:clj  [clojure.test :refer :all]
                 :cljs [cljs.test :refer-macros [is]])
              #?(:cljs [cljs.test.check :refer [quick-check]])
              #?(:clj  [clojure.test.check.properties :as prop]
                 :cljs [cljs.test.check.properties :as prop
                        :include-macros true])
              [schema.core :as schema :refer [check]]))
  
  
  
  (ns rethinkdb.query
    (:require [clojure.walk :refer [postwalk postwalk-replace]]
              #?(:clj [rethinkdb.net :as net])))
  
  
  #?(:clj (defn run [query conn]
            (let [token (get-token conn)]
              (net/send-start-query conn token (replace-vars query)))))
  
  (defn message-container-test [f]
    (fn [mc]
      (passed?
       (let [failed* (failed mc)]
         (try
           (let [x (:data mc)]
             (if (f x) mc failed*))
           (catch #?(:clj Exception :cljs js/Object) _ failed*))))))
  
  (deftest reader-conditionals
     ;; snip
    (testing "splicing"
      (is (= [] [#?@(:clj [])]))
      (is (= [:a] [#?@(:clj [:a])]))
      (is (= [:a :b] [#?@(:clj [:a :b])]))
      (is (= [:a :b :c] [#?@(:clj [:a :b :c])]))
      (is (= [:a :b :c] [#?@(:clj [:a :b :c])]))))
  
  
  ;; https://clojure.org/guides/reader_conditionals#_file_organisation
  
  
  )