(ns clj-contrib.string-test
  (:require [clojure.test :refer [are deftest testing]]
            [clj-contrib.string :as str]))

(deftest split-at-first-test
  (testing "Splits at first occurrence of pattern."
    (are [func result] (= func result)
      (str/split-at-first "first:second:third" ":")  ["first" "second:third"]
      (str/split-at-first "a.b.c.d" "\\.")           ["a" "b.c.d"]
      (str/split-at-first "no-match" ":")             ["no-match"]
      (str/split-at-first ":leading" ":")             ["" "leading"]
      (str/split-at-first "trailing:" ":")            ["trailing" ""])))

(deftest split-at-last-test
  (testing "Splits at last occurrence of pattern."
    (are [func result] (= func result)
      (str/split-at-last "first:second:third" ":")  ["first:second" "third"]
      (str/split-at-last "a.b.c.d" "\\.")           ["a.b.c" "d"]
      (str/split-at-last "no-match" ":")             ["no-match"]
      (str/split-at-last "only:" ":")                ["only" ""])))
