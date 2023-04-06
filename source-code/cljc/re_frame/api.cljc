
(ns re-frame.api
    (:require [re-frame.core         :as core]
              [re-frame.handlers     :as handlers]
              [re-frame.env          :as env]
              [re-frame.interceptors :as interceptors]
              [re-frame.reg          :as reg]
              [re-frame.side-effects :as side-effects]
              [re-frame.state        :as state]
              [re-frame.sub          :as sub]
              [re-frame.utilities    :as utilities]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; re-frame.core
(def ->interceptor core/->interceptor)
(def inject-cofx   core/inject-cofx)

; re-frame.env
(def get-event-handlers        env/get-event-handlers)
(def get-event-handler         env/get-event-handler)
(def event-handler-registered? env/event-handler-registered?)

; re-frame.handlers
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

; re-frame.interceptors
(def event-vector<-id interceptors/event-vector<-id)
(def debug!           interceptors/debug!)

; re-frame.reg
(def reg-cofx     reg/reg-cofx)
(def reg-sub      reg/reg-sub)
(def reg-event-db reg/reg-event-db)
(def reg-event-fx reg/reg-event-fx)
(def reg-fx       reg/reg-fx)

; re-frame.side-effects
(def set-debug-mode!    side-effects/set-debug-mode!)
(def quit-debug-mode!   side-effects/quit-debug-mode!)
(def toggle-debug-mode! side-effects/toggle-debug-mode!)

; re-frame.state
(def DEBUG-MODE? state/DEBUG-MODE?)

; re-frame.sub
(def subscribe  sub/subscribe)
(def subscribed sub/subscribed)

; re-frame.utilities
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
