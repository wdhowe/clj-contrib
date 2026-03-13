(ns clj-contrib.core
  "A library of functions to enhance clojure.core."
  (:gen-class))

;;; Simple Values
;; Numbers

(defn same-ish?
  "Difference of `x` and `y` within `threshold`?

   Parameters:
   - x - first number
   - y - second number
   - threshold - acceptable difference between x and y

   Returns true or false."
  [x y threshold]
  (<= (Math/abs (- x y)) threshold))

(comment
  (same-ish? 1.001 1.003 0.005)
  (same-ish? 1.003 1.001 0.005)
  (same-ish? 1.001 1.009 0.005))

;; Booleans

(defn bool
  "Return the boolean value of `x`.

   Parameters:
   - x - value to convert to boolean

   Returns true or false."
  [x]
  (boolean (Boolean/valueOf x)))

(comment
  (bool "true")
  (bool "false"))

;;; Operations
;; Flow Control

(defmacro as-some->
  "as->, with the nil checking of some->.
   Binds name to expr. When name is not nil, evaluates the first
   form in the lexical context of that binding. When that result
   is not nil, then binds name to result, repeating for each
   successive form.

   Parameters:
   - expr - initial expression to bind to name
   - name - symbol to bind the threaded value to
   - forms - forms to thread, each evaluated only when name is not nil

   Returns the result of the last form, or nil if any step produces nil."
  [expr name & forms]
  (let [steps (map (fn [step] `(if (nil? ~name) nil ~step))
                   forms)]
    `(let [~name ~expr
           ~@(interleave (repeat name) (butlast steps))]
       ~(if (empty? steps)
          name
          (last steps)))))

(comment
  ; example 1: successfully returns the string.
  (as-some-> {:one 1, :two 2} mythings
             (:one mythings)
             (inc mythings)
             (str "These " mythings " things and more!"))

  ; example 2: terminates early and returns nil due to the :one key not existing
  (as-some-> {:two 2} mythings
             (:one mythings)
             (inc mythings)
             (str "These " mythings " things and more!")))

;;; Collections
;; Maps
(defprotocol Errors
  "A protocol for finding errors in a collection."
  (errors [coll] "Count entries with `:error` keys in coll.

   Parameters:
   - coll - collection or map to check

   Returns a map of {:errors count}."))

(extend-protocol Errors
  clojure.lang.Sequential
  (errors
    [coll]
    (->> (filter #(contains? % :error) coll)
         (count)
         (hash-map :errors)))

  clojure.lang.IPersistentMap
  (errors
    [coll]
    (if (contains? coll :error)
      {:errors 1}
      {:errors 0}))

  nil
  (errors [_] {:errors 0}))

(comment
  (errors [])
  (errors [{:error "foo"}, {:error "foo"}])
  (errors [{:error "foo"}, {:success "foo"}])
  (errors [{:success "foo"}, {:success "foo"}])
  (errors {})
  (errors {:success "foo"})
  (errors {:error "foo"})
  (errors nil))

(defprotocol Success
  "A protocol for finding success/non errors in a collection."
  (success [coll] "Count entries without `:error` keys in coll.

   Parameters:
   - coll - collection or map to check

   Returns a map of {:success count}."))

(extend-protocol Success
  clojure.lang.Sequential
  (success
    [coll]
    (->> (remove #(contains? % :error) coll)
         (count)
         (hash-map :success)))

  clojure.lang.IPersistentMap
  (success
    [coll]
    (if (and (not (contains? coll :error)) (not-empty coll))
      {:success 1}
      {:success 0}))

  nil
  (success [_] {:success 0}))

(comment
  (success [])
  (success [{:error "foo"}, {:error "foo"}])
  (success [{:error "foo"}, {:success "foo"}])
  (success [{:success "foo"}, {:success "foo"}])
  (success {})
  (success {:success "foo"})
  (success {:error "foo"})
  (success nil))
