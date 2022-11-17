
(ns re-frame.coeffects
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

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cofx->event-vector
  ; @param (map) cofx
  ; {:event (vector)}
  ;
  ; @example
  ; (cofx->event-vector {:event [...]})
  ; =>
  ; [...]
  ;
  ; @return (vector)
  [cofx]
  (get cofx :event))

(defn cofx->event-id
  ; @param (map) cofx
  ; {:event (vector)
  ;   [(keyword) event-id]}
  ;
  ; @example
  ; (cofx->event-id {:event [:my-event ...]})
  ; =>
  ; :my-event
  ;
  ; @return (keyword)
  [cofx]
  (get-in cofx [:event 0]))
