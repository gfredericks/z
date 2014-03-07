(ns com.gfredericks.z
  "A simple implementation of complex numbers. The real/imaginary
  components can be anything that clojure's core arithmetic functions
  handle."
  (:refer-clojure :exclude [+ - * /])
  (:require [com.gfredericks.lib-4395 :as alg]
            [com.gfredericks.z.impl :as z]))

(defn real->z
  [r]
  (z/complex r 0))

(defn imag->z
  [i]
  (z/complex 0 i))

(defn z
  [r i]
  (z/complex r i))

(def ZERO (real->z 0))
(def ONE (real->z 1))
(def I (imag->z 1))

(alg/defs-keys + - * /
  (alg/compile-field ZERO
                     ONE
                     z/add
                     z/negate
                     z/multiply
                     z/invert))

(defn z->real
  [z]
  (z/real z))

(defn z->imag
  [z]
  (z/imag z))

(defn conjugate [z] (z/conjugate z))

(defn abs-squared
  "Returns the square of the absolute value of the argument."
  [z]
  (* z (z/conjugate z)))
