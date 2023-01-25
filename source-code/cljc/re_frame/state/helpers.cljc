
(ns re-frame.state.helpers
    (:require [re-frame.core :as core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn subscribe-item
  ; @param (vector) item-path
  ; @param (*)(opt) default-value
  ;
  ; @usage
  ; (subscribe-item [:my-item])
  ;
  ; @return (atom)
  ([item-path]
   (-> [:get-item item-path] core/subscribe))

  ([item-path default-value]
   (-> [:get-item item-path default-value] core/subscribe)))

(defn subscribed-item
  ; @description
  ; Returns the actual derefed value of a db item.
  ;
  ; @param (vector) item-path
  ; @param (*)(opt) default-value
  ;
  ; @usage
  ; (subscribed-item [:my-item])
  ;
  ; @return (*)
  ([item-path]
   (-> [:get-item item-path] core/subscribe deref))

  ([item-path default-value]
   (-> [:get-item item-path default-value] core/subscribe deref)))
