(ns build
  "build script.
  
   Test
   clj -T:build test

   Test, Write POM, Build
   clj -T:build ci

   Deploy to Clojars
   clj -T:build deploy"
  (:refer-clojure :exclude [test])
  (:require [clojure.tools.build.api :as b]
            [deps-deploy.deps-deploy :as dd]))

(def lib 'com.github.wdhowe/clj-contrib)
(defn- the-version [patch] (format "1.0.%s" patch))
(def version (the-version (b/git-count-revs nil)))
(def snapshot (the-version "999-SNAPSHOT"))
(def class-dir "target/classes")

(defn test
  "Run all the tests."
  [opts]
  (let [basis    (b/create-basis {:aliases [:test]})
        cmds     (b/java-command
                  {:basis     basis
                   :main      'clojure.main
                   :main-args ["-m" "cognitect.test-runner"]})
        {:keys [exit]} (b/process cmds)]
    (when-not (zero? exit) (throw (ex-info "Tests failed" {}))))
  opts)

(defn- pom-template
  [version]
  [[:description "A library of functions to enhance clojure.core."]
   [:url "https://github.com/wdhowe/clj-contrib"]
   [:licenses
    [:license
     [:name "Eclipse Public License"]
     [:url "http://www.eclipse.org/legal/epl-v10.html"]]]
   [:developers
    [:developer
     [:name "Bill Howe"]]]
   [:scm
    [:url "https://github.com/wdhowe/clj-contrib"]
    [:connection "scm:git:https://github.com/wdhowe/clj-contrib.git"]
    [:developerConnection "scm:git:ssh:git@github.com:wdhowe/clj-contrib.git"]
    [:tag (str "v" version)]]])

(defn- jar-opts
  [opts]
  (let [version (if (:snapshot opts) snapshot version)]
    (assoc opts
           :lib lib   :version version
           :jar-file  (format "target/%s-%s.jar" lib version)
           :basis     (b/create-basis {})
           :class-dir class-dir
           :target    "target"
           :src-dirs  ["src"]
           :pom-data  (pom-template version))))

(defn ci
  "Run the CI pipeline of tests (and build the JAR)."
  [opts]
  (test opts)
  (b/delete {:path "target"})
  (let [opts (jar-opts opts)]
    (println "\nWriting pom.xml...")
    (b/write-pom opts)
    (println "\nCopying source...")
    (b/copy-dir {:src-dirs ["resources" "src"] :target-dir class-dir})
    (println "\nBuilding" (:jar-file opts) "...")
    (b/jar opts))
  opts)

(defn deploy
  "Deploy the JAR to Clojars."
  [opts]
  (let [{:keys [jar-file] :as opts} (jar-opts opts)]
    (dd/deploy {:installer :remote :artifact (b/resolve-path jar-file)
                :pom-file (b/pom-path (select-keys opts [:lib :class-dir]))}))
  opts)
