(ns clj-contrib.java.runtime
  "JVM runtime utilities that complement java.lang.Runtime.

   Provides helpers for registering shutdown hooks."
  (:gen-class))

(defn add-shutdown-hook!
  "Adds a shutdown hook that executes the given function when the JVM is shutting down.

   Args:
     f: function to execute on shutdown

   Returns:
     nil."
  [f]
  (.addShutdownHook (Runtime/getRuntime) (Thread. ^Runnable f)))
