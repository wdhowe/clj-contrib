(ns clj-contrib.string
  "String manipulation utilities that complement clojure.string.

   Provides functions for splitting strings by first or last occurrence
   of a pattern."
  (:require [clojure.string :as str])
  (:gen-class))

(defn split-at-first
  "Split the string into groups based on the first regular expression match.

   Args:
     s: string to split
     re: regular expression pattern to match

   Returns:
     A vector of strings."
  [s re]
  (str/split s (re-pattern re) 2))

(defn split-at-last
  "Split the string into groups based on the last regular expression match.

   Args:
     s: string to split
     re: regular expression pattern to match

   Returns:
     A vector of strings."
  [s re]
  (let [pattern (re-pattern (str re "(?!.*" re ")"))]
    (split-at-first s pattern)))
