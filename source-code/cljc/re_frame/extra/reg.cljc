
(ns re-frame.extra.reg
    (:require [fruits.vector.api     :as vector]
              [re-frame.core         :as core]
              [re-frame.interceptors :as interceptors]
              [re-frame.extra.utils :as utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-cofx
  ; @description
  ; Registers a coeffect handler.
  ;
  ; @param (keyword) event-id
  ; @param (function) handler-f
  ;
  ; @usage
  ; (defn my-handler-f [cofx _])
  ; (reg-cofx :my-event my-handler-f)
  [event-id handler-f]
  (core/reg-cofx event-id handler-f))

(defn reg-sub
  ; @description
  ; Registers a subscription handler.
  ;
  ; @param (keyword) query-id
  ; @param (list of functions) handler-fns
  ;
  ; @usage
  ; (defn my-handler-f [db _])
  ; (reg-sub :my-subscription my-handler-f)
  ;
  ; @usage
  ; (defn my-signal-f         [db _])
  ; (defn another-signal-f    [db _])
  ; (defn my-computation-f [[my-signal another-signal] _])
  ; (reg-sub :my-subscription my-signal-f another-signal-f my-computation-f)
  [query-id & handler-fns]
  (apply core/reg-sub query-id handler-fns))

(defn reg-event-db
  ; @description
  ; Registers a db event handler.
  ;
  ; @param (keyword) event-id
  ; @param (vector)(opt) interceptors
  ; @param (function) handler-f
  ;
  ; @usage
  ; (defn my-handler-f [db _])
  ; (reg-event-db :my-event my-handler-f)
  ([event-id handler-f]
   (reg-event-db event-id nil handler-f))

  ([event-id interceptors handler-f]
   (let [interceptors (interceptors/interceptors<-default-interceptors interceptors)]
        (core/reg-event-db event-id interceptors handler-f))))

(defn reg-event-fx
  ; @description
  ; Registers an effect handler that can be either a handler function, which returns an effect map, or a metamorphic handler in various forms.
  ;
  ; @param (keyword) event-id
  ; @param (vector)(opt) interceptors
  ; @param (function or metamorphic-handler) metamorphic-handler
  ;
  ; @usage
  ; (reg-event-fx :my-event (fn [cofx event-vector] {:dispatch [:another-event]}))
  ;
  ; @usage
  ; (reg-event-fx :my-event (fn [cofx event-vector] [:another-event]))
  ;
  ; @usage
  ; (reg-event-fx :my-event {:dispatch [:another-event]})
  ;
  ; @usage
  ; (reg-event-fx :my-event [:another-event])
  ([event-id metamorphic-handler]
   (reg-event-fx event-id nil metamorphic-handler))

  ([event-id interceptors metamorphic-handler]
   (let [handler-f    (utils/metamorphic-handler->handler-f metamorphic-handler)
         interceptors (interceptors/interceptors<-default-interceptors interceptors)]
        (core/reg-event-fx event-id interceptors handler-f))))

(defn reg-fx
  ; @description
  ; Registers a side effect handler that takes its parameters [individually](#apply-fx-params),
  ; regardless of how they are structured in the effect map.
  ;
  ; @param (keyword) event-id
  ; @param (function) handler-f
  ;
  ; @usage
  ; (defn my-side-effect-f [a])
  ; (reg-fx       :my-side-effect my-side-effect-f)
  ; (reg-event-fx :my-effect {:my-side-effect-f "A"})
  ;
  ; @usage
  ; (defn my-side-effect-f [a b])
  ; (reg-fx       :my-side-effect my-side-effect-f)
  ; (reg-event-fx :my-effect {:my-side-effect-f ["a" "b"]})
  [event-id handler-f]
  (core/reg-fx event-id #(utils/apply-fx-params handler-f %)))
