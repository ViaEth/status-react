(ns status-im.utils.image-processing
  (:require [goog.object :as object]
            [status-im.utils.fs :as fs]
            ["react-native-image-resizer" :as image-resizer]))

(defn- resize [path max-width max-height on-resize on-error]
  (-> (.createResizedImage image-resizer path max-width max-height "JPEG" 75 0 nil)
      (.then on-resize)
      (.catch on-error)))

(defn- image-base64-encode [path on-success on-error]
  (fs/read-file path "base64" on-success #(on-error :base64 %)))

(defn img->base64 [path on-success on-error max-width max-height]
  (let [on-resized (fn [image]
                     (let [path (object/get image "path")]
                       (image-base64-encode path on-success on-error)))
        on-error   (fn [error]
                     (on-error :resize error))]
    (resize path max-width max-height on-resized on-error)))
