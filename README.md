# preo

Validate Clojure/Script function arguments and return value in `:pre` and `:post` hooks, with readable [Expound](https://github.com/bhb/expound) errors.

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

### Example errors

```clj
(defn arg-example [v]
  {:pre [(p/arg! string? v)]}
  v)

(arg-example 123)

; java.lang.AssertionError: Invalid argument: v
; -- Spec failed --------------------
;
;   123
;
; should satisfy
;
;   string?
;
; -------------------------
; Detected 1 error
;
; at preo.core$throw_error_BANG_.invokeStatic (core.cljc:7)
;    preo.core$throw_error_BANG_.invoke (core.cljc:7)
;    preo.example/fn (example.cljc:0)
```

```clj
(s/def ::pos-int (s/and integer? pos?))

(defn ret-example [v]
  {:post [(p/ret! ::pos-int %)]}
  v)

(ret-example "asdf")

; java.lang.AssertionError: Invalid return value
; -- Spec failed --------------------
;
;   "asdf"
;
; should satisfy
;
;   integer?
;
; -- Relevant specs -------
;
; :preo.core-test/pos-int:
; (clojure.spec.alpha/and clojure.core/integer? clojure.core/pos?)
;
; -------------------------
; Detected 1 error
;
; at preo.core$throw_error_BANG_.invokeStatic (core.cljc:7)
;    preo.core$throw_error_BANG_.invoke (core.cljc:7)
;    preo.example/fn (example.cljc:2)
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

## License

Copyright © 2025 Adam Renklint

This project is licensed under the terms of the MIT license.
