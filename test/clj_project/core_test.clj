(ns clj-project.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [clj-project.core :as core]))

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
