# clj-contrib

 A library of functions to enhance clojure.core.

## Getting Started

Using the clj-contrib library.

### Installation

Clojure CLI/deps.edn

```clojure
clj-contrib/clj-contrib {:git/url "https://github.com/wdhowe/clj-contrib.git"
                         :sha "57e333ac5456d7f685feabdf9e76f8d6d983e516"}
```

### Include the Library

In the REPL

```clojure
(require '[clj-contrib.core :as cc])
(require '[clj-contrib.core.async :as cca])
```

In your application

```clojure
(ns my-app.core
  (:require [clj-contrib.core :as cc])
  (:require [clj-contrib.core.async :as cca]))
```

### Use the Library

A few usage examples.

clj-contrib.core

```clojure
;;; Simple Values
;; Numbers

; Check if two numbers are within a threshold.
(same-ish? 1.001 1.003 0.005)

;; Booleans
; Get the boolean value of the argument.
(bool "true")

;;; Operations
;; Flow Control

; as->, with the nil checking of some->.
(as-some-> {:one 1, :two 2} mythings
             (:one mythings)
             (inc mythings)
             (str "These " mythings " things and more!"))

;;; Collections
;; Maps

; count the errors in a collection.
(errors [{:error "foo"}, {:success "foo"}])
(errors {:success "foo"})

; count the success (non-errors) in a collection.
(success [{:success "foo"}, {:success "foo"}])
(success {:error "foo"})
```

clj-contrib.core.async

```clojure
;;; loop-until: Take from an async channel and execute a function against the value.
;;;             Stop if the function returns falsey or the max is exceeded.
;; Stops early when the :error is encountered.
(let [ch (async/chan)
      stop-on-error (fn [result] (println result) (not (contains? result :error)))]
  (async/put! ch {:success "some data"})
  (async/put! ch {:error "bad data"})
  (async/put! ch {:success "more data"})
  (async/put! ch {:success "data trifecta"})
  (loop-until ch stop-on-error 3))

;; Executes to the max of 3.
(let [ch (async/chan)
      stop-on-error (fn [result] (println result) (not (contains? result :error)))]
  (async/put! ch {:success "some data"})
  (async/put! ch {:success "more data"})
  (async/put! ch {:success "data trifecta"})
  (async/put! ch {:error "bad data"})
  (loop-until ch stop-on-error 3))
```
