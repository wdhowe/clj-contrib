(ns clj-contrib.core.async
  "Async channel utilities that complement clojure.core.async.

   Provides loop constructs for consuming channel values with
   early termination support."
  (:require [clojure.core.async :as async])
  (:gen-class))

(defn loop-until
  "Take from channel `c` and execute `f` against each value.
   Stops if `f` returns falsey or `max` iterations are exceeded.

   Args:
     c: a core.async channel to take values from
     f: function applied to each value; loop exits if it returns falsey
     max: maximum number of iterations, regardless of f return value

   Returns:
     nil."
  [c f max]
  (loop [i 0]
    (when (< i max)
      (-> c
          async/<!!
          f
          (when (recur (inc i)))))))
