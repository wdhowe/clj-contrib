(ns clj-contrib.core
  (:gen-class))

;;; Simple Values
;; Numbers

(defn same-ish?
  "Difference of `x` and `y` whithin `threshold`?
   Returns true or false."
  [x y threshold]
  (<= (Math/abs (- x y)) threshold))

(comment
  (same-ish? 1.001 1.003 0.005)
  (same-ish? 1.003 1.001 0.005)
  (same-ish? 1.001 1.009 0.005))

;; Booleans

(defn bool
  "Return the boolean value of `x`."
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
   successive form."
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

(defn update-keys
  "Update a map(`m`) keys(`ks`) values via the passed in function(`f`)."
  [m ks f]
  (->> ks
       (map #(f (get m %)))
       (zipmap ks)
       (merge m)))

(comment
  (update-keys {:attr1 "value1"
                :attr2 "value2"
                :attr3 "value3"}
               [:attr2 :attr3]
               keyword)
  (update-keys {:attr1 1
                :attr2 2
                :attr3 3}
               [:attr1 :attr3]
               inc)
  (let [mymap {:attr1 1
               :attr2 2
               :attr3 3}]
    (update-keys mymap
                 (keys mymap)
                 inc)))

; Same as update-keys, but uses reduce-kv instead.
(defn update-keys2
  "Update a map(`m`) keys(`ks`) values via the passed in function(`f`)."
  [m ks f]
  (reduce-kv (fn [m2 k v] (assoc m2 k (f v)))
             m
             (select-keys m ks)))

(comment
  (update-keys2 {:attr1 "value1"
                 :attr2 "value2"
                 :attr3 "value3"}
                [:attr2 :attr3]
                keyword))
