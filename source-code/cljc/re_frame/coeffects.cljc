
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns re-frame.coeffects
    (:require [re-frame.core :as core]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; re-frame.core
(def inject-cofx core/inject-cofx)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cofx->event-vector
  ; @param (map) cofx
  ;  {:event (vector)}
  ;
  ; @example
  ;  (r/cofx->event-vector {:event [...]})
  ;  =>
  ;  [...]
  ;
  ; @return (vector)
  [cofx]
  (get cofx :event))

(defn cofx->event-id
  ; @param (map) cofx
  ;  {:event (vector)
  ;    [(keyword) event-id]}
  ;
  ; @example
  ;  (r/cofx->event-vector {:event [:my-event ...]})
  ;  =>
  ;  :my-event
  ;
  ; @return (keyword)
  [cofx]
  (get-in cofx [:event 0]))
