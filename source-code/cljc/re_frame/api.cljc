
(ns re-frame.api
    (:require [re-frame.core               :as core]
              [re-frame.core.helpers       :as core.helpers]
              [re-frame.core.interceptors  :as core.interceptors]
              [re-frame.core.side-effects  :as core.side-effects]
              [re-frame.core.sub           :as core.sub]
              [re-frame.db                 :as db]
              [re-frame.debug.interceptors :as debug.interceptors]
              [re-frame.debug.side-effects :as debug.side-effects]
              [re-frame.debug.state        :as debug.state]
              [re-frame.reg.helpers        :as reg.helpers]
              [re-frame.reg.side-effects   :as reg.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------
 
; re-frame.core
(def ->interceptor core/->interceptor)

; re-frame.core.helpers
(def r                              core.helpers/r)
(def event-vector?                  core.helpers/event-vector?)
(def event-vector<-params           core.helpers/event-vector<-params)
(def event-vector->event-id         core.helpers/event-vector->event-id)
(def event-vector->effects-map      core.helpers/event-vector->effects-map)
(def event-vector->handler-f        core.helpers/event-vector->handler-f)
(def effects-map<-event             core.helpers/effects-map<-event)
(def merge-effects-maps             core.helpers/merge-effects-maps)
(def effects-map->handler-f         core.helpers/effects-map->handler-f)
(def context->event-vector          core.helpers/context->event-vector)
(def context->event-id              core.helpers/context->event-id)
(def context->db-before-effect      core.helpers/context->db-before-effect)
(def context->db-after-effect       core.helpers/context->db-after-effect)
(def cofx->event-id                 core.helpers/cofx->event-id)
(def cofx->event-vector             core.helpers/cofx->event-vector)
(def metamorphic-handler->handler-f core.helpers/metamorphic-handler->handler-f)
(def metamorphic-event->effects-map core.helpers/metamorphic-event->effects-map)
(def metamorphic-event<-params      core.helpers/metamorphic-event<-params)

; re-frame.core.interceptors
(def event-vector<-id core.interceptors/event-vector<-id)

; re-frame.core.side-effects
(def inject-cofx    core.side-effects/inject-cofx)
(def dispatch       core.side-effects/dispatch)
(def dispatch-fx    core.side-effects/dispatch-fx)
(def dispatch-sync  core.side-effects/dispatch-sync)
(def dispatch-n     core.side-effects/dispatch-n)
(def dispatch-later core.side-effects/dispatch-later)
(def dispatch-last  core.side-effects/dispatch-last)
(def dispatch-once  core.side-effects/dispatch-once)
(def fx             core.side-effects/fx)
(def fx-n           core.side-effects/fx-n)
(def dispatch-tick  core.side-effects/dispatch-tick)

; re-frame.core.sub
(def subscribe  core.sub/subscribe)
(def subscribed core.sub/subscribed)

; re-frame.db
(def app-db db/app-db)

; re-frame.debug.interceptors
(def debug! debug.interceptors/debug!)

; re-frame.debug.side-effects
(def set-debug-mode!    debug.side-effects/set-debug-mode!)
(def quit-debug-mode!   debug.side-effects/quit-debug-mode!)
(def toggle-debug-mode! debug.side-effects/toggle-debug-mode!)

; re-frame.debug.state
(def DEBUG-MODE? debug.state/DEBUG-MODE?)

; re-frame.reg.helpers
(def apply-fx-params           reg.helpers/apply-fx-params)
(def get-event-handlers        reg.helpers/get-event-handlers)
(def get-event-handler         reg.helpers/get-event-handler)
(def event-handler-registered? reg.helpers/event-handler-registered?)

; re-frame.reg.side-effects
(def reg-cofx     reg.side-effects/reg-cofx)
(def reg-sub      reg.side-effects/reg-sub)
(def reg-event-db reg.side-effects/reg-event-db)
(def reg-event-fx reg.side-effects/reg-event-fx)
(def reg-fx       reg.side-effects/reg-fx)
