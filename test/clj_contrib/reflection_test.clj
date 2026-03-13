(ns clj-contrib.reflection-test
  "Tests for detecting reflection warnings in the codebase.

   Usage:
     clj -X:test-m :p reflection
       Lenient - prints warnings but passes

     clj -X:test-m :p reflection :includes '[:strict]'
       Strict - fails on any warnings"
  (:require [clojure.string :as str]
            [clojure.test :refer [deftest is testing]])
  (:import [java.io PrintWriter StringWriter]))

(def ^:private project-namespaces
  "All project namespaces to check for reflection warnings.
   Include core (which loads its dependencies via :reload-all)
   and any other namespaces not reached transitively via core."
  '[clj-contrib.core
    clj-contrib.core.async
    clj-contrib.java.runtime
    clj-contrib.java.system
    clj-contrib.string])

(defn- collect-reflection-warnings
  "Reload all namespaces with *warn-on-reflection* and capture warnings.

   Returns a string containing any reflection warnings."
  []
  (let [warnings (StringWriter.)]
    (binding [*warn-on-reflection* true
              *err* (PrintWriter. warnings)]
      (doseq [ns-sym project-namespaces]
        (require ns-sym :reload-all)))
    (str warnings)))

(def ^:private own-ns-prefix
  "Classpath prefix for this project's namespaces.
   Captured at compile time when *ns* is correctly bound."
  (str/join "/" (-> (str *ns*)
                    (str/replace "-" "_")
                    (str/split #"\.")
                    drop-last)))

(defn- filter-own-warnings
  "Filter reflection warnings to only those from our own namespaces.
   Third-party library warnings are not actionable."
  [warnings]
  (->> (str/split-lines warnings)
       (filter #(str/includes? % own-ns-prefix))
       (str/join "\n")))

(deftest check-reflection-warnings
  (testing "Compile all namespaces and print any reflection warnings"
    (let [warnings (filter-own-warnings (collect-reflection-warnings))]
      (when (seq warnings)
        (println "Reflection warnings:\n" warnings)))))

(deftest ^:strict check-reflection-warnings-strict
  (testing "Compile all namespaces and fail if reflection warnings exist"
    (let [warnings (filter-own-warnings (collect-reflection-warnings))]
      (is (empty? warnings)
          (str "Reflection warnings found:\n" warnings)))))
