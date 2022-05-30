(ns preo.core-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :refer [deftest is testing]]
            [preo.core :as p]))

(s/def ::pos-int (s/and integer? pos?))

(deftest arg!
  (testing "predicate fn"
    (let [expect-string (fn [v] {:pre [(p/arg! string? v)]} v)]
      (is (expect-string "asdf"))
      (is (thrown-with-msg? #?(:clj AssertionError :cljs js/TypeError)
                            #"^Invalid argument: v(.|\n)+ Spec failed(.|\n)+123(.|\n)+should satisfy(.|\n)+string\?"
                            (expect-string 123)))))
  (testing "spec"
    (let [expect-pos-int (fn [v] {:pre [(p/arg! ::pos-int v)]} v)]
      (is (expect-pos-int 123))
      (is (thrown-with-msg? #?(:clj AssertionError :cljs js/TypeError)
                            #"^Invalid argument: v(.|\n)+ Spec failed(.|\n)+asdf(.|\n)+should satisfy(.|\n)+integer\?"
                            (expect-pos-int "asdf")))
      (is (thrown-with-msg? #?(:clj AssertionError :cljs js/TypeError)
                            #"^Invalid argument: v(.|\n)+ Spec failed(.|\n)+\-123(.|\n)+should satisfy(.|\n)+pos\?"
                            (expect-pos-int -123))))))

(deftest ret!
  (testing "predicate fn"
    (let [ret-string (fn [v] {:post [(p/ret! string? %)]} v)]
      (is (ret-string "asdf"))
      (is (thrown-with-msg? #?(:clj AssertionError :cljs js/TypeError)
                            #"^Invalid return value(.|\n)+ Spec failed(.|\n)+123(.|\n)+should satisfy(.|\n)+string\?"
                            (ret-string 123)))))
  (testing "spec"
    (let [ret-pos-int (fn [v] {:post [(p/ret! ::pos-int %)]} v)]
      (is (ret-pos-int 123))
      (is (thrown-with-msg? #?(:clj AssertionError :cljs js/TypeError)
                            #"^Invalid return value(.|\n)+ Spec failed(.|\n)+asdf(.|\n)+should satisfy(.|\n)+integer\?"
                            (ret-pos-int "asdf")))
      (is (thrown-with-msg? #?(:clj AssertionError :cljs js/TypeError)
                            #"^Invalid return value(.|\n)+ Spec failed(.|\n)+-123(.|\n)+should satisfy(.|\n)+pos\?"
                            (ret-pos-int -123))))))
