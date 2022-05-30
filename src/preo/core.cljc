(ns preo.core
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as str]
            [expound.alpha :as expound])
  #?(:cljs (:require-macros [preo.core :refer [arg! ret!]])))

(defn throw-error! [msg]
  #?(:clj (throw (AssertionError. msg))
     :cljs (throw (js/TypeError. msg))))

(defn- assert! [pred val prefix]
  `(let [msg# (with-out-str (expound/expound ~pred ~val))]
     (if (str/includes? msg# "Success!")
       true
       (throw-error! (str ~prefix "\n" msg#)))))

#?(:clj
   (defmacro arg!
     "Assert argument value in :pre hook. `pred` can be predicate fn or spec."
     [pred val]
     (assert! pred val (str "Invalid argument: " val))))

#?(:clj
   (defmacro ret!
     "Assert return value in :post hook. `pred` can be predicate fn or spec."
     [pred val]
     (assert! pred val (str "Invalid return value"))))
