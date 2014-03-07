(ns com.gfredericks.z-test
  (:refer-clojure :exclude [+ - * /])
  (:require [clojure.test.check :as check]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]]
            [com.gfredericks.z :refer [+ - * /] :as z]))

(def gen-complex
  (gen/fmap (fn [[r i]]
              (z/z r i))
            (gen/tuple gen/int gen/int)))

(defspec addition-associativity 10000
  (prop/for-all [z1 gen-complex
                 z2 gen-complex
                 z3 gen-complex]
    (= (+ (+ z1 z2) z3)
       (+ z1 (+ z2 z3)))))

(defspec addition-commutativity 10000
  (prop/for-all [z1 gen-complex
                 z2 gen-complex]
    (= (+ z1 z2) (+ z2 z1))))

(defspec multiplication-associativity 10000
  (prop/for-all [z1 gen-complex
                 z2 gen-complex
                 z3 gen-complex]
    (= (* (* z1 z2) z3)
       (* z1 (* z2 z3)))))

(defspec multiplication-commutativity 10000
  (prop/for-all [z1 gen-complex
                 z2 gen-complex]
    (= (* z1 z2) (* z2 z1))))

(defspec distributivity 10000
  (prop/for-all [z1 gen-complex
                 z2 gen-complex
                 z3 gen-complex]
    (= (* z1 (+ z2 z3))
       (+ (* z1 z2) (* z1 z3)))))

(defspec negation 10000
  (prop/for-all [z gen-complex]
    (= z/ZERO (+ z (- z)))))

(defspec inversion 10000
  (prop/for-all [z (gen/such-that #(not= % z/ZERO) gen-complex)]
    (= z/ONE (* z (/ z)))))

(defspec conjugation 10000
  (prop/for-all [z gen-complex]
    (-> z (z/conjugate) (+ z) (z/z->imag) (zero?))))
