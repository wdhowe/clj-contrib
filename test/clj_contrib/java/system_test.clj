(ns clj-contrib.java.system-test
  (:require [clojure.test :refer [deftest is testing]]
            [clj-contrib.java.system :as system]))

(deftest home-test
  (testing "Returns a non-empty string."
    (is (string? (system/home)))
    (is (seq (system/home)))))
