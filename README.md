# clj-contrib

[![Build Status][gh-actions-badge]][gh-actions] [![Clojars Project][clojars-badge]][clojars] [![cljdoc][cljdoc-badge]][cljdoc-link] [![Clojure version][clojure-v]](deps.edn)

A library of functions to enhance clojure.core.

## Getting Started

Using the clj-contrib library.

### Installation

See [Clojars for adding this library][clojars] to various project types (lein, boot, deps.edn, etc).

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

```clojure
;; Threading with nil checking (like some-> but with as-> binding)
(cc/as-some-> {:one 1, :two 2} mythings
  (:one mythings)
  (inc mythings)
  (str "These " mythings " things and more!"))

;; Get the boolean value of an argument
(cc/bool "true")
```

For complete API documentation and examples, see the [cljdoc documentation][cljdoc-link].

<!-- Named page links below: /-->

[gh-actions-badge]: https://github.com/wdhowe/clj-contrib/workflows/ci%2Fcd/badge.svg
[gh-actions]: https://github.com/wdhowe/clj-contrib/actions
[cljdoc-badge]: https://cljdoc.org/badge/com.github.wdhowe/clj-contrib
[cljdoc-link]: https://cljdoc.org/d/com.github.wdhowe/clj-contrib/CURRENT
[clojure-v]: https://img.shields.io/badge/clojure-1.12.4-blue.svg
[clojars]: https://clojars.org/com.github.wdhowe/clj-contrib
[clojars-badge]: https://img.shields.io/clojars/v/com.github.wdhowe/clj-contrib.svg
