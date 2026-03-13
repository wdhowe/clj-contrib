(ns clj-contrib.java.runtime)

(defn add-shutdown-hook!
  "Adds a shutdown hook that executes the given function when the JVM is shutting down.

   Parameters:
   - f - function to execute on shutdown

   Returns nil."
  [f]
  (.addShutdownHook (Runtime/getRuntime) (Thread. ^Runnable f)))
