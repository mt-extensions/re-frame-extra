
(ns re-frame.reg.helpers
    (:require [re-frame.debug.interceptors :as debug.interceptors]
              [re-frame.registrar          :as registrar]
              [vector.api                  :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn apply-fx-params
  ; @param (function) handler-f
  ; @param (* or vector) params
  ;
  ; @usage
  ; (apply-fx-params (fn [a] ...) "a")
  ;
  ; @usage
  ; (apply-fx-params (fn [a] ...) ["a"])
  ;
  ; @usage
  ; (apply-fx-params (fn [a b] ...) ["a" "b"])
  ;
  ; @return (*)
  [handler-f params]
  (if (sequential?     params)
      (apply handler-f params)
      (handler-f       params)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn interceptors<-system-interceptors
  ; @ignore
  ;
  ; @param (vector) interceptors
  ;
  ; @return (vector)
  [interceptors]
  (vector/conj-item interceptors debug.interceptors/LOG-EVENT!))

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
