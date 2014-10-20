(ns com.gfredericks.z-test
  (:refer-clojure :exclude [+ - * /])
  (:require [clojure.test.check :as check]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]]
            [com.gfredericks.z :refer [+ - * /] :as z]))

(defn =ish? [z1 z2] (< (z/separation z1 z2) 1e-9))

(def gen-complex
  (let [double (gen/fmap double gen/ratio)
        double-double (gen/tuple double double)]
    (gen/one-of
     [(gen/fmap (fn [[r i]]
                  (com.gfredericks.z.impl.RectComplex. r i))
                double-double)
      (gen/fmap (fn [[mag arg]]
                  (com.gfredericks.z.impl.PolarComplex. mag arg))
                double-double)])))

(defspec addition-associativity 10000
  (prop/for-all [z1 gen-complex
                 z2 gen-complex
                 z3 gen-complex]
    (=ish? (+ (+ z1 z2) z3)
           (+ z1 (+ z2 z3)))))

(defspec addition-commutativity 10000
  (prop/for-all [z1 gen-complex
                 z2 gen-complex]
    (=ish? (+ z1 z2) (+ z2 z1))))

(defspec multiplication-associativity 10000
  (prop/for-all [z1 gen-complex
                 z2 gen-complex
                 z3 gen-complex]
    (=ish? (* (* z1 z2) z3)
           (* z1 (* z2 z3)))))

(defspec multiplication-commutativity 10000
  (prop/for-all [z1 gen-complex
                 z2 gen-complex]
    (=ish? (* z1 z2) (* z2 z1))))

(defspec distributivity 10000
  (prop/for-all [z1 gen-complex
                 z2 gen-complex
                 z3 gen-complex]
    (=ish? (* z1 (+ z2 z3))
           (+ (* z1 z2) (* z1 z3)))))

(defspec negation 10000
  (prop/for-all [z gen-complex]
    (=ish? z/ZERO (+ z (- z)))))

(defspec inversion 10000
  (prop/for-all [z (gen/such-that #(not (zero? (z/magnitude %))) gen-complex)]
    (=ish? z/ONE (* z (/ z)))))

(defspec conjugation 10000
  (prop/for-all [z gen-complex]
    (-> z (z/conjugate) (+ z) (z/z->imag) (< 1e-9))))
