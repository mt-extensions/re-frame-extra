
(ns re-frame.extra.api
    (:require [re-frame.extra.effects]
              [re-frame.extra.interceptors :as interceptors]
              [re-frame.extra.handlers :as handlers]
              [re-frame.extra.reg :as reg]
              [re-frame.extra.sub :as sub]
              [re-frame.extra.utils :as utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @tutorial Types
;
; @title Event vector
;
; [:my-event "My param"]
;
; @title Effect map
;
; {:dispatch [:my-event]
;  :dispatch-later [{:ms 420 :dispatch [:my-event]}]
;  ...}
;
; @title Metamorphic event
;
; The metamorphic event formula provides a flexible way to dispatch Re-Frame events.
; The extended [dispatch](#dispatch) function accepts not only event vectors but also effect maps and functions for dispatching.
;
; @usage
; (dispatch [:my-event])
; (dispatch {:dispatch [:my-event]})
; (dispatch (fn [] {:dispatch [:my-event]}))
;
; @title Metamorphic handler
;
; The metamorphic handler formula provides a flexible way to register effect handlers.
; The extended [reg-event-fx](#reg-event-fx) function accepts not only handler functions but also event vectors and effect maps.
;
; @usage
; (reg-event-fx :my-effects (fn [_ _] {:dispatch [:my-event]}))
; (reg-event-fx :my-effects {:dispatch [:my-event]})
; (reg-event-fx :my-effects [:my-event])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (re-frame.extra.handlers/*)
(def dispatch       handlers/dispatch)
(def dispatch-sync  handlers/dispatch-sync)
(def dispatch-n     handlers/dispatch-n)
(def dispatch-later handlers/dispatch-later)
(def dispatch-once  handlers/dispatch-once)
(def dispatch-tick  handlers/dispatch-tick)
(def fx             handlers/fx)
(def fx-n           handlers/fx-n)

; @redirect (re-frame.extra.interceptors/*)
(def ensure-subject-id interceptors/ensure-subject-id)

; @redirect (re-frame.extra.reg/*)
(def reg-cofx     reg/reg-cofx)
(def reg-sub      reg/reg-sub)
(def reg-event-db reg/reg-event-db)
(def reg-event-fx reg/reg-event-fx)
(def reg-fx       reg/reg-fx)

; @redirect (re-frame.extra.sub/*)
(def subscribe  sub/subscribe)
(def subscribed sub/subscribed)

; @redirect (re-frame.extra.utils/*)
(def r                              utils/r)
(def event-vector?                  utils/event-vector?)
(def event-vector<-params           utils/event-vector<-params)
(def event-vector<-subject-id       utils/event-vector<-subject-id)
(def event-vector->event-id         utils/event-vector->event-id)
(def event-vector->effect-map       utils/event-vector->effect-map)
(def event-vector->handler-f        utils/event-vector->handler-f)
(def side-effect-vector?            utils/side-effect-vector?)
(def side-effect-vector->effect-id  utils/side-effect-vector->effect-id)
(def delayed-effect-map->effect-map utils/delayed-effect-map->effect-map)
(def effect-map<-event-vector       utils/effect-map<-event-vector)
(def merge-effect-maps              utils/merge-effect-maps)
(def effect-map->handler-f          utils/effect-map->handler-f)
(def context->event-vector          utils/context->event-vector)
(def context->event-id              utils/context->event-id)
(def context->db-before-effect      utils/context->db-before-effect)
(def context->db-after-effect       utils/context->db-after-effect)
(def cofx->event-id                 utils/cofx->event-id)
(def cofx->event-vector             utils/cofx->event-vector)
(def metamorphic-handler->handler-f utils/metamorphic-handler->handler-f)
(def metamorphic-event->effect-map  utils/metamorphic-event->effect-map)
(def metamorphic-event<-params      utils/metamorphic-event<-params)
(def apply-fx-params                utils/apply-fx-params)
