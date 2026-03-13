(ns clj-contrib.core.async-test
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.core.async :as async]
            [clj-contrib.core.async :as cca]))

(deftest loop-until-test
  (testing "Stops early when f returns falsey."
    (let [ch (async/chan 4)
          results (atom [])
          record! (fn [v] (swap! results conj v) (not (contains? v :error)))]
      (async/put! ch {:success "one"})
      (async/put! ch {:error "stop"})
      (async/put! ch {:success "two"})
      (cca/loop-until ch record! 4)
      (is (= [{:success "one"} {:error "stop"}] @results))))

  (testing "Executes to max when f always returns truthy."
    (let [ch (async/chan 4)
          results (atom [])
          record! (fn [v] (swap! results conj v) true)]
      (async/put! ch {:success "one"})
      (async/put! ch {:success "two"})
      (async/put! ch {:success "three"})
      (cca/loop-until ch record! 3)
      (is (= [{:success "one"} {:success "two"} {:success "three"}] @results))))

  (testing "Does nothing when max is 0."
    (let [ch (async/chan 1)
          results (atom [])
          record! (fn [v] (swap! results conj v) true)]
      (async/put! ch {:success "one"})
      (cca/loop-until ch record! 0)
      (is (= [] @results)))))
