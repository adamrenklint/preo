# preo

Clojure/Script library for validation of arguments and return value in :pre and :post hooks, with readable Expound errors

## Installation

### Leiningen/Boot

`[com.adamrenklint/preo "0.1.0"]`

#### deps.edn

`com.adamrenklint/preo {:mvn/version "0.1.0"}`

## Usage

```clj
(ns example
  (:require [cljs.spec.alpha :as s]
            [preo.core :as p]))

(s/def ::msg string?)
(s/def ::thing (s/keys :req-un [::msg]))

; Use predicate fns or specs, both works
(defn my-fn [msg]
  {:pre [(p/arg! string? msg)]
   :post [(p/ret! ::thing %)]}
  {:msg msg})
```

## Develop

- Watch and run Clojure tests with socket REPL on port `9000`: `clj -M:dev-clj`
- Run ClojureScript tests with Shadow CLJS in browser with socket REPL on port `9000`: `clj -M:dev-cljs`

## Test

- Run Clojure tests once: `clj -X:test-clj`
- Run ClojureScript tests once: `clj -M:test-cljs`
- Run tests on both platforms: `clj -T:build run-tests`

## Release

- Deploy to Clojars: `clj -T:build release`
