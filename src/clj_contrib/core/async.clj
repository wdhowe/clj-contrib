(ns clj-contrib.core.async
  "A library of functions to enhance clojure.core.async."
  (:require [clojure.core.async :as async])
  (:gen-class))

(defn loop-until
  "Take from channel `c` and execute `f` against each value.
   Stops if `f` returns falsey or `max` iterations are exceeded.

   Parameters:
   - c - a core.async channel to take values from
   - f - function applied to each value; loop exits if it returns falsey
   - max - maximum number of iterations, regardless of f return value

   Returns nil."
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
