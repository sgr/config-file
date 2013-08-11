;; -*- coding: utf-8-unix -*-
(ns #^{:author "sgr"
       :doc "設定ファイルの読み込み、書き出しインタフェース"}
  config-file
  (:require [clojure.java.io :as io])
  (:import [java.io File]))

(defn system []
  (let [sys (.toLowerCase (System/getProperty "os.name"))]
    (condp re-find sys
      #"windows" :windows
      #"mac"     :mac
      #"linux"   :linux
      #"bsd"     :bsd
      #"solaris" :solaris
      :other)))

(defn- config-base-path-unix [uhome]
  (str uhome File/separator))

(defn- config-base-path-macosx [uhome]
  (str uhome File/separator "Library"))

(defn- config-base-path-windows [uhome]
  (if-let [appdata (System/getenv "AppData")]
    (str appdata File/separator)
    (str uhome File/separator)))

(defn ^String config-base-path []
  (let [uhome (System/getProperty "user.home")]
    (condp = (system)
      :windows (config-base-path-windows uhome)
      :mac     (config-base-path-macosx uhome)
      :linux   (config-base-path-unix uhome)
      :bsd     (config-base-path-unix uhome)
      :solaris (config-base-path-unix uhome)
      (config-base-path-unix uhome))))

(defn load-config [^File pfile]
  (if (and (.exists pfile) (.isFile pfile))
    (read-string (slurp pfile))
    nil))

(defn- ^File old-config-file [^File pfile] (File. (str (.getCanonicalPath pfile) ".old")))

(defn store-config [config ^File pfile]
  (when (and (.exists pfile) (.isFile pfile))
    (.renameTo pfile (old-config-file pfile)))
  (with-open [stream (io/writer pfile)]
    (.write stream (pr-str config))))
