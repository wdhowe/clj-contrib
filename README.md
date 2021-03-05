# clj-project

A template for starting a clj tools based project.

## Overview

Batteries included features:

- Project dirs/file basics
  - src and test directory structure.
  - .gitignore, CHANGELOG, config.edn, deps.edn, LICENSE, README.
- Config: Configuration/Env variables via 'clj-config'.
- Dependencies: Dependency version checks with 'deps-ancient'.
- Deploy: Publish uberjars to clojars via 'deps-deploy'.
- Tests: Clojure 'test-runner' from Cognitect.
- Packaging: Create uberjars with 'cambada'.

Some features above selected from the clj-tools list [available here](https://github.com/clojure/tools.deps.alpha/wiki/Tools).

## Execute the -main function

Run the -main function in the namespace/core.clj file:

- Method 1: Implied -main function in the namespace:

```clojure
clj -M -m clj-project.core
```

- Method 2: Explicit namespace+function:

```clojure
clj -X clj-project.core/-main
```

## Config

Config is loaded in the following order:

- A :config map in the deps.edn file. (included in this project's deps.edn)
- config.edn on the classpath. (included in this project's dir)
- Environment variables.
- Java system properties.

[clj-config details here.](https://gitlab.com/orangefoxcollective/clj-config)

## Dependencies

Check for outdated dependencies:

```clojure
clj -M:ancient
```

## Deploy

Deploy your uberjars to clojars:

```clojure
;; env vars for clojars
CLOJARS_USERNAME=username
CLOJARS_PASSWORD=clojars-token

clj -M:deploy
```

## Tests

In the project root directory, run the tests:

```clojure
clj -M:test
```

## Packaging

Create an uberjar:

```clojure
clj -M:uberjar
```
