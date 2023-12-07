
(ns re-frame.api
    (:require [re-frame.core         :as core]
              [re-frame.handlers     :as handlers]
              [re-frame.interceptors :as interceptors]
              [re-frame.reg          :as reg]
              [re-frame.sub          :as sub]
              [re-frame.utilities    :as utilities]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (re-frame.core)
(def ->interceptor core/->interceptor)
(def inject-cofx   core/inject-cofx)

; @redirect (re-frame.handlers)
(def dispatch       handlers/dispatch)
(def dispatch-f     handlers/dispatch-f)
(def dispatch-fx    handlers/dispatch-fx)
(def dispatch-sync  handlers/dispatch-sync)
(def dispatch-n     handlers/dispatch-n)
(def dispatch-later handlers/dispatch-later)
(def dispatch-last  handlers/dispatch-last)
(def dispatch-once  handlers/dispatch-once)
(def dispatch-tick  handlers/dispatch-tick)
(def fx             handlers/fx)
(def fx-n           handlers/fx-n)

; @redirect (re-frame.interceptors)
(def event-vector<-id interceptors/event-vector<-id)

; @redirect (re-frame.reg)
(def reg-cofx     reg/reg-cofx)
(def reg-sub      reg/reg-sub)
(def reg-event-db reg/reg-event-db)
(def reg-event-fx reg/reg-event-fx)
(def reg-fx       reg/reg-fx)

; @redirect (re-frame.sub)
(def subscribe  sub/subscribe)
(def subscribed sub/subscribed)

; @redirect (re-frame.utilities)
(def r                              utilities/r)
(def event-vector?                  utilities/event-vector?)
(def event-vector<-params           utilities/event-vector<-params)
(def event-vector->event-id         utilities/event-vector->event-id)
(def event-vector->effects-map      utilities/event-vector->effects-map)
(def event-vector->handler-f        utilities/event-vector->handler-f)
(def effects-map<-event             utilities/effects-map<-event)
(def merge-effects-maps             utilities/merge-effects-maps)
(def effects-map->handler-f         utilities/effects-map->handler-f)
(def context->event-vector          utilities/context->event-vector)
(def context->event-id              utilities/context->event-id)
(def context->db-before-effect      utilities/context->db-before-effect)
(def context->db-after-effect       utilities/context->db-after-effect)
(def cofx->event-id                 utilities/cofx->event-id)
(def cofx->event-vector             utilities/cofx->event-vector)
(def metamorphic-handler->handler-f utilities/metamorphic-handler->handler-f)
(def metamorphic-event->effects-map utilities/metamorphic-event->effects-map)
(def metamorphic-event<-params      utilities/metamorphic-event<-params)
(def apply-fx-params                utilities/apply-fx-params)
