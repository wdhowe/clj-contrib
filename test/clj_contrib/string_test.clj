(ns clj-contrib.string-test
  (:require [clojure.test :refer [are deftest testing]]
            [clj-contrib.string :as string]))

(deftest split-at-first-test
  (testing "Splits at first occurrence of pattern."
    (are [func result] (= func result)
      (string/split-at-first "first:second:third" ":")  ["first" "second:third"]
      (string/split-at-first "a.b.c.d" "\\.")           ["a" "b.c.d"]
      (string/split-at-first "no-match" ":")             ["no-match"]
      (string/split-at-first ":leading" ":")             ["" "leading"]
      (string/split-at-first "trailing:" ":")            ["trailing" ""])))

(deftest split-at-last-test
  (testing "Splits at last occurrence of pattern."
    (are [func result] (= func result)
      (string/split-at-last "first:second:third" ":")  ["first:second" "third"]
      (string/split-at-last "a.b.c.d" "\\.")           ["a.b.c" "d"]
      (string/split-at-last "no-match" ":")             ["no-match"]
      (string/split-at-last "only:" ":")                ["only" ""])))
