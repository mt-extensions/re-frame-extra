
(ns re-frame.extra.sub
    (:require [re-frame.core :as core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn subscribe
  ; @description
  ; Returns a Reagent Reaction computing a specific subscription handler.
  ;
  ; @param (vector) query-vector
  ;
  ; @usage
  ; (subscribe [:my-subscription])
  ; =>
  ; #object[Reagent.ratom.Reaction]
  ;
  ; @return (Reagent Reaction object)
  [query-vector]
  (core/subscribe query-vector))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn subscribed
  ; @description
  ; Returns a specific subscription handler computed.
  ;
  ; @param (vector) query-vector
  ;
  ; @usage
  ; (subscribed [:my-subscription])
  ;
  ; @return (*)
  [query-vector]
  (-> query-vector subscribe deref))
