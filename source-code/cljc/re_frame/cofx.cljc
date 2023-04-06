
(ns re-frame.cofx
    (:require [re-frame.core :as core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn inject-cofx
  ; @param (keyword) handler-id
  ; @param (*)(opt) param
  ;
  ; @usage
  ; (inject-cofx :my-handler)
  ;
  ; @usage
  ; (inject-cofx :my-handler "My param")
  ([handler-id]
   (core/inject-cofx handler-id))

  ([handler-id param]
   (core/inject-cofx handler-id param)))
