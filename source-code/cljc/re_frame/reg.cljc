
(ns re-frame.reg
    (:require [mid-fruits.vector    :as vector]
              [re-frame.core        :as core]
              [re-frame.log         :as log]
              [re-frame.metamorphic :as metamorphic]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; re-frame.core
(def reg-cofx core/reg-cofx)
(def reg-sub  core/reg-sub)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- interceptors<-system-interceptors
  ; @param (vector) interceptors
  ;
  ; @return (vector)
  [interceptors]
  (vector/conj-item interceptors log/LOG-EVENT!))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-event-db
  ; @param (keyword) event-id
  ; @param (vector)(opt) interceptors
  ; @param (metamorphic-event) event-handler
  ;
  ; @usage
  ;  (defn my-handler-f [db _])
  ;  (reg-event-db :my-event my-handler-f)
  ;
  ; @usage
  ;  (defn my-handler-f [db _])
  ;  (reg-event-db :my-event [...] my-handler-f)
  ([event-id event-handler]
   (reg-event-db event-id nil event-handler))

  ([event-id interceptors event-handler]
   (let [interceptors (interceptors<-system-interceptors interceptors)]
        (core/reg-event-db event-id interceptors event-handler))))

(defn reg-event-fx
  ; You can registrate metamorphic-events, not only handler-functions
  ;
  ; @param (keyword) event-id
  ; @param (vector)(opt) interceptors
  ; @param (metamorphic-event) event-handler
  ;
  ; @usage
  ;  (reg-event-fx :my-event [:your-event])
  ;
  ; @usage
  ;  (reg-event-fx :my-event {:dispatch [:your-event]})
  ;
  ; @usage
  ;  (reg-event-fx :my-event (fn [cofx event-vector] [:your-event]})
  ;
  ; @usage
  ;  (reg-event-fx :my-event (fn [cofx event-vector] {:dispatch [:your-event]})
  ([event-id event-handler]
   (reg-event-fx event-id nil event-handler))

  ([event-id interceptors event-handler]
   (let [handler-f    (metamorphic/metamorphic-handler->handler-f event-handler)
         interceptors (interceptors<-system-interceptors          interceptors)]
        (core/reg-event-fx event-id interceptors #(metamorphic/metamorphic-event->effects-map (handler-f %1 %2))))))

(defn apply-fx-params
  ; @param (function) handler-f
  ; @param (* or vector) params
  ;
  ; @usage
  ;  (apply-fx-params (fn [a] ...) "a")
  ;
  ; @usage
  ;  (apply-fx-params (fn [a] ...) ["a"])
  ;
  ; @usage
  ;  (apply-fx-params (fn [a b] ...) ["a" "b"])
  ;
  ; @return (*)
  [handler-f params]
  (if (sequential?     params)
      (apply handler-f params)
      (handler-f       params)))

(defn reg-fx
  ; @param (keyword) event-id
  ; @param (function) handler-f
  ;
  ; @usage
  ;  (defn my-side-effect-f [a])
  ;  (reg-fx       :my-side-effect my-side-effect-f)
  ;  (reg-event-fx :my-effect {:my-my-side-effect-f "A"})
  ;
  ; @usage
  ;  (defn your-side-effect-f [a b])
  ;  (reg-fx       :your-side-effect your-side-effect-f)
  ;  (reg-event-fx :your-effect {:your-my-side-effect-f ["a" "b"]})
  [event-id handler-f]
  (core/reg-fx event-id #(apply-fx-params handler-f %)))
