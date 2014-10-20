(ns com.gfredericks.z.impl)

(def ^:const PI Math/PI)
(def ^:const TAU (* 2 PI))

;; Keep it simple at first

(defprotocol IComplex
  (add [z1 z2])
  (negate [z])
  (multiply [z1 z2])
  (invert [z])
  (conjugate [z])
  (real [z])
  (imag [z])
  (mag [z])
  (arg [z]))

(deftype RectComplex [r i]
  IComplex
  (add [_ z]
    (RectComplex. (+ r (real z))
                  (+ i (imag z))))
  (negate [_]
    (RectComplex. (- r) (- i)))
  (multiply [_ rc]
    (let [r' (real rc)
          i' (imag rc)]
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
  (mag [_] (Math/sqrt (+ (* r r) (* i i))))
  ;; TODO: Check that this is right. Wikipedia has good pseudopsuedocode in
  ;; the Complex Number article
  (arg [_]
    (if (zero? r)
      (if (pos? i) (/ PI 2) (* PI 1.5))
      (let [theta (Math/atan (/ i r))]
        (if (neg? r)
          (+ PI theta)
          theta))))
  Object
  (toString [_]
    (cond
     (zero? i) (str r)
     (zero? r) (str (case i 1 "" -1 "-" i) "i")
     (neg? i)  (str r "-" (if (= -1 i) "" (- i)) "i")
     :else     (str r "+" (if (= 1 i) "" i) "i")))
  (hashCode [_] (.hashCode [r i]))
  (equals [_ o]
    (and (instance? RectComplex o)
         (= r (.-r ^RectComplex o))
         (= i (.-i ^RectComplex o)))))

(deftype PolarComplex [m a]
  IComplex
  (real [this] (* m (Math/cos a)))
  (imag [this] (* m (Math/sin a)))
  (mag  [this] m)
  (arg  [this] a)
  (add  [this other]
    (add (RectComplex. (real this) (imag this)) other))
  (negate [this]
    (PolarComplex. m (rem (+ a PI) TAU)))
  (multiply [this other]
    (PolarComplex. (* m (mag other))
                   (+ a (arg other))))
  (invert [this]
    (PolarComplex. (/ m) (- a)))
  (conjugate [this]
    (PolarComplex. m (rem (- TAU a) TAU)))
  Object
  (toString [this]
    (str "#<" m " e^i(" a ")>"))
  (hashCode [_] (.hashCode [m a]))
  (equals [_ o]
    (and (instance? PolarComplex o)
         (= m (.-m ^PolarComplex o))
         (= a (.-a ^PolarComplex a)))))

(defn complex [r i] (RectComplex. r i))

(defmethod print-method RectComplex
  [rc pw]
  (.write pw (str rc)))
