
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
  ; @param (vector) query-vector
  ;
  ; @usage
  ; (subscribed [:my-subscription])
  ;
  ; @return (*)
  [query-vector]
  (-> query-vector subscribe deref))
