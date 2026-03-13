(ns user
  "Dev namespace with REPL helpers. Loaded automatically in dev mode.")

;;; REPL Helpers ;;;

;; Conditionally load criterium for benchmarking (only available in dev, not test)
(defn- criterium-available? []
  (try
    (require 'criterium.core)
    true
    (catch Exception _ false)))

(defmacro bench
  "Quick benchmark an expression using criterium.

   Example: (bench (reduce + (range 1000)))

   Args:
     expr: Expression to benchmark.

   Note:
     Only available when criterium is on the classpath (dev mode)."
  [expr]
  (if (criterium-available?)
    `((requiring-resolve 'criterium.core/quick-bench) ~expr)
    `(throw (ex-info "Criterium not available. Use 'clj -M:dev' to access benchmarking."
                     {:expr '~expr}))))

(defn help
  "Show available REPL commands."
  []
  (println "
REPL Commands
======================

Benchmarking (dev mode only):
  (bench <expr>)         Quick benchmark an expression with criterium
"))

;;; Rich Comment Block ;;;

(comment
  ;; REPL helpers - Evaluate these forms in your editor

  ;; Show help
  (help))
