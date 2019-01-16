
(ns learn.repl-enhanced
  (:require [nrepl.server :refer [start-server stop-server]]
            [clojure.pprint :refer [pprint pp print-table]]
            [clojure.repl :refer :all]))




(defn -main []
  (println "running learn repl")
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))


(comment

  (non-existing-fn  1 2 3)
  (/ 1 0)

  (doc prn)
  (doc pr)
  (doc println)
  
  (defn average
    "a buggy function for computing the average of some numbers."
    [numbers]
    (let [sum (first numbers)
          n (count numbers)]
      (prn sum) ;; HERE printing an intermediary value
      (/ sum n)))
  
  (average [12 14])
  
  (defn average
    "a buggy function for computing the average of some numbers."
    [numbers]
    (let [sum (first numbers)
          n (count numbers)]
      (/
       (doto sum prn) ;; HERE
       n)))
  
  (average [12 14])
  
  (defn average
    [numbers]
    (let [sum (apply + numbers)
          n (count numbers)]
      (def n n) ;; FIXME remove when you're done debugging
      (/ sum n)))
  
  (average [1 2 3])
  
  n ; defined intermediate value
  
  ; reproducing the context fo an expression
  
  (def G 6.67408e-11)
  (def earth-radius 6.371e6)
  (def earth-mass 5.972e24)
  
  (defn earth-gravitational-force
    "Computes (an approximation of) the gravitational force between Earth and an object
  of mass `m`, at distance `r` of Earth's center."
    [m r]
    (/
     (*
      G
      m
      (if (>= r earth-radius)
        earth-mass
        (*
         earth-mass
         (Math/pow (/ r earth-radius) 3.0))))
     (* r r)))
  
  (earth-gravitational-force 80 5e6)
  
  (def m 80)
  (def r 5e6)
  
  (* r r)
  (>= r earth-radius)
  (Math/pow (/ r earth-radius) 3.0)
  
  
  
  
  )


(comment
;; Each of these 4 code examples start a loop in another thread
;; which prints numbers at a regular time interval.

;;;; 1. NOT REPL-friendly
;; We won't be able to change the way numbers are printed without restarting the REPL.
(future
  (run!
   (fn [i]
     (println i "green bottles, standing on the wall. ♫")
     (Thread/sleep 1000))
   (range)))

;;;; 2. REPL-friendly
;; We can easily change the way numbers are printed by re-defining print-number-and-wait.
;; We can even stop the loop by having print-number-and-wait throw an Exception.
(defn print-number-and-wait
  [i]
  (println i "green bottles, standing on the wall. ♫")
  (Thread/sleep 1000))

(future
  (run!
   (fn [i] (print-number-and-wait i))
   (range)))

;;;; 3. NOT REPL-friendly
;; Unlike the above example, the loop can't be altered by re-defining print-number-and-wait,
;; because the loop uses the value of print-number-and-wait, not the #'print-number-and-wait Var.
(defn print-number-and-wait
  [i]
  (println i "green bottles, standing on the wall. ♫")
  (Thread/sleep 1000))

(future
  (run!
   print-number-and-wait
   (range)))

;;;; 4. REPL-friendly
;; The following works because a Clojure Var is (conveniently) also a function,
;; which consist of looking up its value (presumably a function) and calling it.
(defn print-number-and-wait
  [i]
  (println i "green bottles, standing on the wall. ♫")
  (Thread/sleep 1000))

(future
  (run!
   #'print-number-and-wait ;; mind the #' - the expression evaluates to the #'print-number-and-wait Var, not its value.
   (range)))
)


(comment 
  
  ;;; NOT REPL-friendly
;; if you re-define `solar-system-planets`, you have to think of re-defining `n-planets` too.
(def solar-system-planets
  "The set of planets which orbit the Sun."
  #{"Mercury" "Venus" "Earth" "Mars" "Jupiter" "Saturn" "Uranus" "Neptune"})

(def n-planets
  "The number of planets in the solar system"
  (count solar-system-planets))


;;;; REPL-friendly
;; if you re-define `solar-system-planets`, the behaviour of `n-planets` will change accordingly.
(def solar-system-planets
  "The set of planets which orbit the Sun."
  #{"Mercury" "Venus" "Earth" "Mars" "Jupiter" "Saturn" "Uranus" "Neptune"})

(defn n-planets
  "The number of planets in the solar system"
  []
  (count solar-system-planets))
  
  )