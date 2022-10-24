
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns re-frame.metamorphic
    (:require [mid-fruits.candy      :refer [return]]
              [mid-fruits.map        :as map]
              [mid-fruits.vector     :as vector]
              [re-frame.effects-map  :as effects-map]
              [re-frame.event-vector :as event-vector]
              [re-frame.types        :as types]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn metamorphic-handler->handler-f
  ; @param (metamorphic-event) n
  ;
  ; @example
  ;  (metamorphic-handler->handler-f [...])
  ;  =>
  ;  (fn [_ _] {:dispatch [...]})
  ;
  ; @example
  ;  (metamorphic-handler->handler-f {:dispatch [...]})
  ;  =>
  ;  (fn [_ _] {:dispatch [...]})
  ;
  ; @example
  ;  (metamorphic-handler->handler-f (fn [_ _] ...))
  ;  =>
  ;  (fn [_ _] ...})
  ;
  ; @return (function)
  [n]
  (cond (map?    n) (effects-map/effects-map->handler-f   n)
        (vector? n) (event-vector/event-vector->handler-f n)
        :return  n))

(defn metamorphic-event->effects-map
  ; @param (metamorphic-effects) n
  ;
  ; @example
  ;  (metamorphic-event->effects-map [:my-event])
  ;  =>
  ;  {:dispatch [:my-event]}
  ;
  ; @example
  ;  (metamorphic-event->effects-map {:dispatch [:my-event])
  ;  =>
  ;  {:dispatch [:my-event]}
  ;
  ; @return (map)
  [n]
  (cond (vector? n) (event-vector/event-vector->effects-map n)
        (map?    n) (return                                 n)))

(defn metamorphic-event<-params
  ; @param (metamorphic-event) n
  ; @param (list of *) params
  ;
  ; @example
  ;  (metamorphic-event<-params [:my-event] "My param" "Your param")
  ;  =>
  ;  [:my-event "My param" "Your param"]
  ;
  ; @example
  ;  (metamorphic-event<-params {:dispatch [:my-event]} "My param" "Your param")
  ;  =>
  ;  {:dispatch [:my-event "My param" "Your param"]}
  ;
  ; @return (metamorphic-event)
  [n & params]
  ; Szükséges megkülönböztetni az esemény vektort a dispatch-later és dispatch-tick esemény csoport vektortól!
  ; [:my-event ...]
  ; [{:ms 500 :dispatch [:my-event ...]}]
  (cond (types/event-vector? n) (vector/concat-items n params)
        (map?                n) (map/->values        n #(apply metamorphic-event<-params % params))
        :return              n))
