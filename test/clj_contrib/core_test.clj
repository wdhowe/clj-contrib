(ns clj-contrib.core-test
  (:require [clojure.test :refer [are deftest is testing]]
            [clj-contrib.core :as core]))

;;; Simple Values
;; Numbers

(deftest same-ish?-test
  (testing "Values within threshold testing."
    (are [func result] (= func result)
      (core/same-ish? 1.001 1.003 0.005) true
      (core/same-ish? 1.003 1.001 0.005) true
      (core/same-ish? 1.001 1.009 0.005) false)))

;; Booleans

(deftest bool-test
  (testing "Bool value tests."
    (are [func result] (= func result)
      (core/bool "true") true
      (core/bool "True") true
      (core/bool "false") false
      (core/bool "False") false)))

;;; Operations
;; Flow Control

(deftest as-some->-test
  (testing "as-some threading test."

    (is (= "These 2 things and more!"
           (core/as-some->  {:one 1, :two 2} mythings
                            (:one mythings)
                            (inc mythings)
                            (str "These " mythings " things and more!"))))

    (is (nil? (core/as-some-> {:two 2} mythings
                              (:one mythings)
                              (inc mythings)
                              (str "These " mythings " things and more!"))))))

;;; Collections
;; Maps

(deftest update-keys-test
  (testing "Updating map key values."
    (let [kw (core/update-keys {:attr1 "value1"
                                :attr2 "value2"
                                :attr3 "value3"}
                               [:attr2 :attr3]
                               keyword)
          add (core/update-keys {:attr1 1
                                 :attr2 2
                                 :attr3 3}
                                [:attr1 :attr3]
                                inc)]
      (is (= "value1" (:attr1 kw)))
      (is (= :value2 (:attr2 kw)))
      (is (= :value3 (:attr3 kw)))

      (is (= 2 (:attr1 add)))
      (is (= 2 (:attr2 add)))
      (is (= 4 (:attr3 add))))))

(deftest update-keys2-test
  (testing "Updating map key values 2 - reduce-kv method."
    (let [kw (core/update-keys2 {:attr1 "value1"
                                 :attr2 "value2"
                                 :attr3 "value3"}
                                [:attr2 :attr3]
                                keyword)
          add (core/update-keys2 {:attr1 1
                                  :attr2 2
                                  :attr3 3}
                                 [:attr1 :attr3]
                                 inc)]
      (is (= "value1" (:attr1 kw)))
      (is (= :value2 (:attr2 kw)))
      (is (= :value3 (:attr3 kw)))

      (is (= 2 (:attr1 add)))
      (is (= 2 (:attr2 add)))
      (is (= 4 (:attr3 add))))))
