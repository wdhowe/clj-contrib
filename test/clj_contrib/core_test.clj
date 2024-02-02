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

(deftest errors-test
  (testing "errors collection testing." 
    (is (= (core/errors []) {:errors 0}))
    (is (= (core/errors [{:error "foo"}, {:error "foo"}]) {:errors 2}))
    (is (= (core/errors [{:error "foo"}, {:success "foo"}]) {:errors 1}))
    (is (= (core/errors [{:success "foo"}, {:success "foo"}]) {:errors 0}))
    (is (= (core/errors {}) {:errors 0}))
    (is (= (core/errors {:success "foo"}) {:errors 0}))
    (is (= (core/errors {:error "foo"}) {:errors 1}))
    (is (= (core/errors nil) {:errors 0}))))

(deftest success-test
  (testing "success collection testing."
    (is (= (core/success []) {:success 0}))
    (is (= (core/success [{:error "foo"}, {:error "foo"}]) {:success 0}))
    (is (= (core/success [{:error "foo"}, {:success "foo"}]) {:success 1}))
    (is (= (core/success [{:success "foo"}, {:success "foo"}]) {:success 2}))
    (is (= (core/success {}) {:success 0}))
    (is (= (core/success {:success "foo"}) {:success 1}))
    (is (= (core/success {:error "foo"}) {:success 0}))
    (is (= (core/success nil) {:success 0}))))
