(ns clj-contrib.core.async
  (:gen-class)
  (:require [clojure.core.async :as async]))

(defn loop-until
  "Pulls results from a `c` async channel and executes `f` function against
   the result. Stops if the `f` function returns false/nil or the `max` is
   exceeded.
   - c: A core.async channel.
   - f: Function applied to the value taken from the channel. If this function's
        return value is falsey, the loop exits.
   - max: The max number of loops to execute, regardless of f return value."
  [c f max]
  (loop [i 0]
    (when (< i max)
      (-> c
          async/<!!
          f
          (when (recur (inc i)))))))

(comment
  ;; stops early when the :error is encountered.
  (let [ch (async/chan)
        stop-on-error (fn [result] (println result) (not (contains? result :error)))]
    (async/put! ch {:success "some data"})
    (async/put! ch {:error "bad data"})
    (async/put! ch {:success "more data"})
    (async/put! ch {:success "data trifecta"})
    (loop-until ch stop-on-error 3))

  ;; executes to the max of 3
  (let [ch (async/chan)
        stop-on-error (fn [result] (println result) (not (contains? result :error)))]
    (async/put! ch {:success "some data"})
    (async/put! ch {:success "more data"})
    (async/put! ch {:success "data trifecta"})
    (async/put! ch {:error "bad data"})
    (loop-until ch stop-on-error 3)))
