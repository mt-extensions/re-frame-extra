
(ns re-frame.sub
    (:require [re-frame.core :as core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn subscribe
  ; @param (vector) query-vector
  ;
  ; @usage
  ; (subscribe [:my-subscription])
  ;
  ; @return (atom)
  [query-vector]
  (core/subscribe query-vector))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn subscribed
  ; @description
  ; Returns the actual derefed value of the given subscription.
  ;
  ; @param (vector) query-vector
  ;
  ; @usage
  ; (subscribed [:my-subscription])
  ;
  ; @return (*)
  [query-vector]
  (-> query-vector subscribe deref))
