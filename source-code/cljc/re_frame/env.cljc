
(ns re-frame.env
    (:require [re-frame.registrar :as registrar]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-event-handlers
  ; @param (keyword)(opt) event-kind
  ;
  ; @usage
  ; (get-event-handlers)
  ;
  ; @usage
  ; (get-event-handlers :sub)
  ;
  ; @return (map)
  ; {:cofx (map)
  ;  :event (map)
  ;  :fx (map)
  ;  :sub (map)}
  ([]                       (deref registrar/kind->id->handler))
  ([event-kind] (event-kind (deref registrar/kind->id->handler))))

(defn get-event-handler
  ; @param (keyword) event-kind
  ; :cofx, :event, :fx, :sub
  ; @param (keyword) event-id
  ;
  ; @usage
  ; (get-event-handler :sub :my-subscription)
  ;
  ; @return (maps in list)
  [event-kind event-id]
  (-> (get-event-handlers)
      (get-in [event-kind event-id])))

(defn event-handler-registered?
  ; @param (keyword) event-kind
  ; :cofx, :event, :fx, :sub
  ; @param (keyword) event-id
  ;
  ; @usage
  ; (event-handler-registered? :sub :my-subscription)
  ;
  ; @return (function)
  [event-kind event-id]
  (-> (get-event-handler event-kind event-id)
      (some?)))
