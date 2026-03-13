(ns clj-contrib.java.runtime-test
  (:require [clojure.test :refer [deftest is testing]]
            [clj-contrib.java.runtime :as runtime]))

(deftest add-shutdown-hook!-test
  (testing "Returns nil."
    (is (nil? (runtime/add-shutdown-hook! (fn [] nil)))))

  (testing "Does not throw when registering multiple hooks."
    (is (nil? (try
                (runtime/add-shutdown-hook! (fn [] nil))
                (runtime/add-shutdown-hook! (fn [] nil))
                nil
                (catch Exception e e))))))
