# config-file

A tiny Clojure library designed to handle configuration file easily.

## Install

config-file is available in [Clojars.org](https://clojars.org/config-file).
Your leiningen project.clj:

  [config-file "0.1.0"]

## Usage

```clojure
(let [config (atom nil)
      default-config {:x 100, :y 50}
      config-dir  (File. (config-base-path) ".yourapp")
      config-file (File. config-dir "config.clj")]
  (reset! config (if-let [c (load-config config-file)] c default-config)) ; load config
  (swap! config assoc :x 250) ; update config
  ;;
  (store-config @config config-file) ; store {:x 250, y: 50} to file
)
```

## License

Copyright (C) Shigeru Fujiwara All Rights Reserved.

Distributed under the Eclipse Public License, the same as Clojure.
