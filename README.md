# clj-contrib

 A library of Clojure functions to enhance clojure.core.

## Getting Started

Using the clj-contrib library.

### Installation

Clojure CLI/deps.edn

```clojure
clj-contrib/clj-contrib {:git/url "https://github.com/wdhowe/clj-contrib.git"
                         :sha "f76ea1d9fba24557f16fd220a4830810f63bfbdb"}
```

### Include the Library

In the REPL

```clojure
(require '[clj-contrib.core :as contrib])
```

In your application

```clojure
(ns my-app.core
  (:require [clj-contrib.core :as contrib]))
```

### Use the Library

A few usage examples.

```clojure
;; update-keys examples

; update selected keys' values with the inc function
(contrib/update-keys {:attr1 1
                      :attr2 2
                      :attr3 3}
                     [:attr1 :attr3]
                     inc)

; update all values with the inc function
(let [mymap {:attr1 1
             :attr2 2
             :attr3 3}]
     (contrib/update-keys mymap
                          (keys mymap)
                          inc))
```
