
(ns re-frame.extra.api
    (:require [re-frame.core         :as core]
              [re-frame.handlers     :as handlers]
              [re-frame.interceptors :as interceptors]
              [re-frame.extra.reg :as reg]
              [re-frame.extra.sub :as sub]
              [re-frame.extra.utils :as utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (re-frame.extra.core/*)
(def ->interceptor core/->interceptor)
(def inject-cofx   core/inject-cofx)

; @redirect (re-frame.extra.handlers/*)
(def dispatch       handlers/dispatch)
(def dispatch-fx    handlers/dispatch-fx)
(def dispatch-sync  handlers/dispatch-sync)
(def dispatch-n     handlers/dispatch-n)
(def dispatch-later handlers/dispatch-later)
(def dispatch-last  handlers/dispatch-last)
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
(def event-vector->effects-map      utils/event-vector->effects-map)
(def event-vector->handler-f        utils/event-vector->handler-f)
(def effects-map<-event-vector      utils/effects-map<-event-vector)
(def merge-effects-maps             utils/merge-effects-maps)
(def effects-map->handler-f         utils/effects-map->handler-f)
(def context->event-vector          utils/context->event-vector)
(def context->event-id              utils/context->event-id)
(def context->db-before-effect      utils/context->db-before-effect)
(def context->db-after-effect       utils/context->db-after-effect)
(def cofx->event-id                 utils/cofx->event-id)
(def cofx->event-vector             utils/cofx->event-vector)
(def metamorphic-handler->handler-f utils/metamorphic-handler->handler-f)
(def metamorphic-event->effects-map utils/metamorphic-event->effects-map)
(def metamorphic-event<-params      utils/metamorphic-event<-params)
(def apply-fx-params                utils/apply-fx-params)
