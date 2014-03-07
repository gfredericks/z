(ns com.gfredericks.z.impl)

;; Keep it simple at first

(defprotocol IComplex
  (add [z1 z2])
  (negate [z])
  (multiply [z1 z2])
  (invert [z])
  (conjugate [z])
  (real [z])
  (imag [z]))

(deftype RectComplex [r i]
  IComplex
  (add [_ rc]
    (RectComplex. (+ r (.-r ^RectComplex rc))
                  (+ i (.-i ^RectComplex rc))))
  (negate [_]
    (RectComplex. (- r) (- i)))
  (multiply [_ rc]
    (let [r' (.-r ^RectComplex rc)
          i' (.-i ^RectComplex rc)]
      (RectComplex. (+ (* r r')
                       (- (* i i')))
                    (+ (* r i')
                       (* i r')))))
  (invert [_]
    (let [div (+ (* r r) (* i i))]
      (RectComplex. (/ r div) (- (/ i div)))))
  (conjugate [_]
    (RectComplex. r (- i)))
  (real [_] r)
  (imag [_] i)
  Object
  (toString [_]
    (if (neg? i)
      (str r "-" (- i) "i")
      (str r "+" i "i")))
  (hashCode [_] (.hashCode [r i]))
  (equals [_ o]
    (and (instance? RectComplex o)
         (= r (.-r ^RectComplex o))
         (= i (.-i ^RectComplex o)))))

(defn complex [r i] (RectComplex. r i))

(defmethod print-method RectComplex
  [rc pw]
  (.write pw (str rc)))
