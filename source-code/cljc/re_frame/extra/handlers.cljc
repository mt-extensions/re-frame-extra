
(ns re-frame.extra.handlers
    (:require [re-frame.core      :as core]
              [re-frame.registrar :as registrar]
              [re-frame.extra.utils :as utils]
              [re-frame.extra.check :as check]
              [time.api :as time]
              [activity-listener.api :as activity-listener]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn dispatch
  ; @description
  ; Dispatches the given event handler.
  ;
  ; @param (metamorphic-event) metamorphic-event
  ;
  ; @usage
  ; (dispatch [:my-event])
  ;
  ; @usage
  ; (dispatch {:dispatch [:my-event]})
  ;
  ; @usage
  ; (dispatch (fn [] {:dispatch [:my-event]}))
  ;
  ; @usage
  ; {:dispatch [:my-event]}
  ;
  ; @usage
  ; {:dispatch {:dispatch [:my-event]}}
  ;
  ; @usage
  ; {:dispatch (fn [] {:dispatch [:my-event]})}
  [metamorphic-event]
  ; Re-Frame does not print errors in the Clojure environment(!) if an event is is dispatched but not registered.
  #?(:clj (check/check-metamorphic-event metamorphic-event))
  (if metamorphic-event (if (utils/event-vector? metamorphic-event)
                            (core/dispatch       metamorphic-event)
                            (core/dispatch       [:dispatch-metamorphic-event metamorphic-event]))))

(registrar/clear-handlers :fx      :dispatch)
(core/reg-fx              :dispatch dispatch)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn dispatch-f
  ; @description
  ; Executes the given 'f' function.
  ;
  ; @param (function) f
  ;
  ; @usage
  ; (dispatch-f (fn [] ...))
  ;
  ; @usage
  ; {:dispatch-f (fn [] ...)}
  [f]
  (if f (f)))

(core/reg-fx :dispatch-f dispatch-f)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn dispatch-later
  ; @description
  ; Dispatches the given list of effect maps delayed.
  ;
  ; @param (maps in vector) effect-map-list
  ; [(map) effect-map
  ;   {:dispatch (metamorphic-event)(opt)
  ;    :dispatch-f (function)(opt)
  ;    :dispatch-later (maps in vector)(opt)
  ;    :dispatch-n (metamorphic-events in vector)(opt)
  ;    :dispatch-once (map)(opt)
  ;    :dispatch-tick (maps in vector)(opt)
  ;    :fx (vector)(opt)
  ;    :fx-n (vectors in vector)(opt)
  ;    :ms (ms)
  ;    ...}]
  ;
  ; @usage
  ; (dispatch-later [{:ms 420 :dispatch   [:my-event]}
  ;                  {:ms 420 :dispatch-n [[:another-event]]
  ;                           :fx-n       [[:my-side-effect]]}])
  ;
  ; {:dispatch-later [{:ms 420 :dispatch   [:my-event]}
  ;                   {:ms 420 :dispatch-n [[:another-event]]
  ;                            :fx-n       [[:my-side-effect]]}]}
  [effect-map-list]
  ; The original 'dispatch-later' function does not set any timeout for the given events in the Clojure environment.
  (doseq [{:keys [ms] :as effect-map} effect-map-list]
         (if ms (letfn [(f0 [] (-> effect-map utils/delayed-effect-map->effect-map dispatch))]
                       (time/set-timeout! f0 ms)))))

(registrar/clear-handlers :fx            :dispatch-later)
(core/reg-fx              :dispatch-later dispatch-later)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn dispatch-n
  ; @description
  ; Dispatches the given list of event handlers.
  ;
  ; @param (metamorphic-events in vector) event-list
  ;
  ; @usage
  ; (dispatch-n [[:my-event]
  ;              {:dispatch [:another-event]}
  ;              (fn [_ _] {:fx [:my-side-effect]})])
  ;
  ; @usage
  ; {:dispatch-n [[:my-event]
  ;               {:dispatch [:another-event]}
  ;               (fn [_ _] {:fx [:my-side-effect]})]}
  [event-list]
  (doseq [event-vector event-list]
         (dispatch event-vector)))

(registrar/clear-handlers :fx        :dispatch-n)
(core/reg-fx              :dispatch-n dispatch-n)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn dispatch-once
  ; @description
  ; Dispatches the given event handler throttled (rate limited).
  ;
  ; @param (vector) event-vector
  ; @param (ms) timeout
  ;
  ; @usage
  ; (dispatch-once [:my-event] 420)
  ;
  ; @usage
  ; {:dispatch-once [[:my-event] 420]}
  [event-vector timeout]
  (let [event-id (utils/event-vector->event-id event-vector)]
       (when (activity-listener/activity-not-locked? event-id)
             (activity-listener/lock-activity!       event-id timeout)
             (dispatch                               event-vector))))

(registrar/clear-handlers :fx           :dispatch-once)
(core/reg-fx              :dispatch-once dispatch-once)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn dispatch-sync
  ; @description
  ; Dispatches the given event handler immediately.
  ;
  ; @param (vector) event-vector
  ;
  ; @usage
  ; (dispatch-sync [:my-event])
  [event-vector]
  (if event-vector (core/dispatch-sync event-vector)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn dispatch-tick
  ; @description
  ; Dispatches the given list of delayed event handlers.
  ;
  ; @param (maps in vector) effect-map-list
  ; [(map) effect-map
  ;   {:dispatch (metamorphic-event)(opt)
  ;    :dispatch-f (function)(opt)
  ;    :dispatch-later (maps in vector)(opt)
  ;    :dispatch-n (metamorphic-events in vector)(opt)
  ;    :dispatch-once (map)(opt)
  ;    :dispatch-tick (maps in vector)(opt)
  ;    :fx (vector)(opt)
  ;    :fx-n (vectors in vector)(opt)
  ;    :tick (integer)
  ;    ...}]
  ;
  ; @usage
  ; (dispatch-tick [{:tick 420 :dispatch   [:my-event]}
  ;                 {:tick 420 :dispatch-n [[:another-event]]
  ;                            :fx-n       [[:my-side-effect]]}])
  ;
  ; {:dispatch-tick [{:tick 420 :dispatch   [:my-event]}
  ;                  {:tick 420 :dispatch-n [[:another-event]]
  ;                             :fx-n       [[:my-side-effect]]}]}
  [effect-map-list]
  (core/dispatch [:dispatch-tick effect-map-list]))

(core/reg-fx :dispatch-tick dispatch-tick)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn fx
  ; @description
  ; Dispatches the given side effect handler.
  ;
  ; @param (vector) side-effect-vector
  ;
  ; @usage
  ; (fx [:my-side-effect])
  ;
  ; @usage
  ; {:fx [:my-side-effect]}
  [[effect-id & params :as side-effect-vector]]
  (check/check-side-effect-vector side-effect-vector)
  (if side-effect-vector (if-let [effect-f (registrar/get-handler :fx effect-id false)]
                                 (effect-f params))))

(registrar/clear-handlers :fx :fx)
(core/reg-fx              :fx  fx)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn fx-n
  ; @description
  ; Dispatches the given list of side effect handlers.
  ;
  ; @param (vectors in vector) side-effect-list
  ;
  ; @usage
  ; (fx-n [[:my-side-effect]
  ;        [:another-side-effect]])
  ;
  ; @usage
  ; {:fx-n [[:my-side-effect]
  ;         [:another-side-effect]]}
  [side-effect-list]
  (check/check-side-effect-list side-effect-list)
  (if (sequential? side-effect-list)
      (doseq [side-effect-vector side-effect-list]
             (fx side-effect-vector))))

(core/reg-fx :fx-n fx-n)
