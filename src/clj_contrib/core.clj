(ns clj-contrib.core
  "Core utilities that complement clojure.core.

   Provides value helpers (approximate equality, boolean coercion),
   flow control macros (nil-safe threading), and collection protocols
   for counting errors and successes."
  (:gen-class))

;;; Simple Values
;; Numbers

(defn same-ish?
  "Difference of `x` and `y` within `threshold`?

   Args:
     x: first number
     y: second number
     threshold: acceptable difference between x and y

   Returns:
     true or false."
  [x y threshold]
  (<= (Math/abs (double (- x y))) threshold))

;; Booleans

(defn bool
  "Return the boolean value of `x`.

   Args:
     x: value to convert to boolean

   Returns:
     true or false."
  [x]
  (boolean (Boolean/valueOf (str x))))

;;; Operations
;; Flow Control

(defmacro as-some->
  "as->, with the nil checking of some->.
   Binds name to expr. When name is not nil, evaluates the first
   form in the lexical context of that binding. When that result
   is not nil, then binds name to result, repeating for each
   successive form.

   Args:
     expr: initial expression to bind to name
     name: symbol to bind the threaded value to
     forms: forms to thread, each evaluated only when name is not nil

   Returns:
     The result of the last form, or nil if any step produces nil."
  [expr name & forms]
  (let [steps (map (fn [step] `(if (nil? ~name) nil ~step))
                   forms)]
    `(let [~name ~expr
           ~@(interleave (repeat name) (butlast steps))]
       ~(if (empty? steps)
          name
          (last steps)))))

;;; Collections
;; Maps
(defprotocol CountErrors
  "A protocol for counting errors in a collection."
  (count-errors [coll] "Count entries with `:error` keys in coll.

   Args:
     coll: collection or map to check

   Returns:
     A map of {:errors count}."))

(extend-protocol CountErrors
  clojure.lang.Sequential
  (count-errors
    [coll]
    (->> (filter #(contains? % :error) coll)
         (count)
         (hash-map :errors)))

  clojure.lang.IPersistentMap
  (count-errors
    [coll]
    (if (contains? coll :error)
      {:errors 1}
      {:errors 0}))

  nil
  (count-errors [_] {:errors 0}))

(defprotocol CountSuccess
  "A protocol for counting successes in a collection."
  (count-success [coll] "Count entries without `:error` keys in coll.

   Args:
     coll: collection or map to check

   Returns:
     A map of {:success count}."))

(extend-protocol CountSuccess
  clojure.lang.Sequential
  (count-success
    [coll]
    (->> (remove #(contains? % :error) coll)
         (count)
         (hash-map :success)))

  clojure.lang.IPersistentMap
  (count-success
    [coll]
    (if (and (not (contains? coll :error)) (not-empty coll))
      {:success 1}
      {:success 0}))

  nil
  (count-success [_] {:success 0}))
