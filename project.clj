(defproject clj-contrib "0.1.0"

  ;;; Project Metadata
  :description "A library of functions to enhance clojure.core."
  :url "https://github.com/wdhowe/clj-contrib"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}

  ;;; Dependencies, Plugins
  :dependencies [[org.clojure/clojure "1.11.2"]
                 [org.clojure/core.async "1.6.681"]]

  ;;; Profiles
  :profiles {:uberjar {:aot :all}}

  ;;; Running Project Code
  :repl-options {:init-ns clj-contrib.core})
