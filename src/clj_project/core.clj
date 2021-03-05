(ns clj-project.core
  (:require [clj-config.core :as config])
  (:gen-class))

(defn -main
  "The -main function."
  [& args]
  (println "Hello world. My args are: " args)
  (println "I got these vars from config: "
           (:myvar config/env)
           (:myvar2 config/env)))
