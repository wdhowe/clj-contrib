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
  "Seed namespaces that transitively load the full project.
   Required once (without reload) to ensure all project namespaces are loaded
   before we discover and reload them individually."
  '[clj-contrib.core
    clj-contrib.core.async
    clj-contrib.java.runtime
    clj-contrib.java.system
    clj-contrib.string])

(def ^:private own-ns-prefix
  "Namespace prefix for this project.
   Captured at compile time when *ns* is correctly bound."
  (str/join "." (drop-last (str/split (str *ns*) #"\."))))

(def ^:private own-ns-path-prefix
  "Classpath prefix for this project's namespaces.
   Used to filter reflection warnings by file path."
  (-> own-ns-prefix (str/replace "." "/") (str/replace "-" "_")))

(defn- own-namespace?
  "True if ns-sym belongs to this project."
  [ns-sym]
  (str/starts-with? (str ns-sym) (str own-ns-prefix ".")))

(defn- test-namespace?
  "True if ns-sym is a test namespace (ends with -test)."
  [ns-sym]
  (str/ends-with? (str ns-sym) "-test"))

(defn- project-ns-syms
  "Return all currently-loaded project source namespace symbols, sorted.
   Excludes test namespaces (reflection in tests is less critical) and
   this namespace (to avoid re-evaluating deftest forms during reload)."
  []
  (->> (all-ns)
       (map ns-name)
       (filter own-namespace?)
       (remove test-namespace?)
       sort))

(defn- collect-reflection-warnings
  "Reload only project namespaces with *warn-on-reflection* and capture warnings.
   Uses :reload (not :reload-all) to avoid reloading third-party namespaces
   whose protocol identities would be invalidated, breaking defs like
   router/malli-coercion that hold pre-reload protocol reify objects."
  []
  ;; Ensure all project namespaces are loaded (no reload, just trigger loading)
  (doseq [ns-sym project-namespaces]
    (require ns-sym))
  ;; Reload only our namespaces with reflection warnings enabled
  (let [warnings (StringWriter.)]
    (binding [*warn-on-reflection* true
              *err* (PrintWriter. warnings)]
      (doseq [ns-sym (project-ns-syms)]
        (require ns-sym :reload)))
    (str warnings)))

(defn- filter-own-warnings
  "Filter reflection warnings to only those from our own namespaces.
   Third-party library warnings are not actionable."
  [warnings]
  (->> (str/split-lines warnings)
       (filter #(str/includes? % own-ns-path-prefix))
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
