# z

A simple implementation of complex numbers in Clojure. Requires
Clojure >= 1.6.

## Obtention

Leiningen: `[com.gfredericks/z "0.1.3"]`

## Usage

``` clojure
(ns my.things
  (:refer-clojure :exclude [+ - * /])
  (:require [com.gfredericks.z :as z :refer [+ - * /]]))

(+ (*) (*)) ;; => 2+0i
```

## License

Copyright © 2014 Gary Fredericks

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
