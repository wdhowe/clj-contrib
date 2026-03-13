(ns test-runner
  "Convenience wrapper for running tests with simpler CLI syntax.
   Calls System/exit after tests to ensure the JVM shuts down cleanly
   even when non-daemon threads (core.async) are still alive."
  (:require [cognitect.test-runner.api :as tr]))

(defn- exit-with-results
  "Exit the JVM with code 0 on success, 1 on any failures or errors.
   cognitect test-runner returns nil on success, a map with :fail/:error on failure."
  [results]
  (let [{:keys [fail error] :or {fail 0 error 0}} results]
    (System/exit (if (zero? (+ fail error)) 0 1))))

(defn match
  "Run tests matching pattern substring.
   Usage: clj -X:test-m :p pattern

   Parameters:
   - opts - map with :p (pattern substring to match)"
  [{:keys [p]}]
  (-> (tr/test {:patterns [(str ".*" p ".*")]
                :excludes [:integration]})
      exit-with-results))

(defn match-integration
  "Run integration tests matching pattern substring.
   Usage: clj -X:test-integration-m :p pattern

   Parameters:
   - opts - map with :p (pattern substring to match)"
  [{:keys [p]}]
  (-> (tr/test {:patterns [(str ".*" p ".*")]
                :includes [:integration]})
      exit-with-results))
