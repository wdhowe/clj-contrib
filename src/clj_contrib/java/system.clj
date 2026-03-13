(ns clj-contrib.java.system
  "JVM system utilities that complement java.lang.System.

   Provides helpers for accessing system properties."
  (:gen-class))

(defn home
  "Returns the user home directory.

   Returns:
     The user home directory path as a string."
  []
  (System/getProperty "user.home"))
