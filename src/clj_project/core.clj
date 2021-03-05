(ns clj-project.core
  (:gen-class))

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
  ;; example 1: successfully returns the string.
  (as-some-> {:one 1, :two 2} mythings
             (:one mythings)
             (inc mythings)
             (str "These " mythings " things and more!"))

  ;; example 2: terminates early and returns nil due to the :one key not existing
  (as-some-> {:two 2} mythings
             (:one mythings)
             (inc mythings)
             (str "These " mythings " things and more!")))

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
               inc))
