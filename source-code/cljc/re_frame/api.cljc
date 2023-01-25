
; README-BEN ÁTIRNI AZ UJ NAMESPACEKRE

(ns re-frame.api
    (:require [re-frame.core               :as core]
              [re-frame.core.cofx          :as core.cofx]
              [re-frame.core.dispatch-set :as core.dispatch-set]
              [re-frame.core.env           :as core.env]
              [re-frame.core.fx            :as core.fx]
              [re-frame.core.interceptors  :as core.interceptors]
              [re-frame.core.reg           :as core.reg]
              [re-frame.core.sub           :as core.sub]
              [re-frame.core.utils         :as core.utils]
              [re-frame.debug.interceptors :as debug.interceptors]
              [re-frame.debug.side-effects :as debug.side-effects]
              [re-frame.debug.state        :as debug.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; re-frame.core
(def ->interceptor core/->interceptor)

; re-frame.core.cofx
(def inject-cofx core.cofx/inject-cofx)

; re-frame.core.env
(def get-event-handlers        core.env/get-event-handlers)
(def get-event-handler         core.env/get-event-handler)
(def event-handler-registered? core.env/event-handler-registered?)

; re-frame.core.interceptors
(def event-vector<-id core.interceptors/event-vector<-id)

; re-frame.core.dispatch-set
(def dispatch       core.dispatch-set/dispatch)
(def dispatch-fx    core.dispatch-set/dispatch-fx)
(def dispatch-sync  core.dispatch-set/dispatch-sync)
(def dispatch-n     core.dispatch-set/dispatch-n)
(def dispatch-later core.dispatch-set/dispatch-later)
(def dispatch-last  core.dispatch-set/dispatch-last)
(def dispatch-once  core.dispatch-set/dispatch-once)
(def dispatch-tick  core.dispatch-set/dispatch-tick)

; re-frame.core.fx
(def fx   core.fx/fx)
(def fx-n core.fx/fx-n)

; re-frame.core.reg
(def reg-cofx     core.reg/reg-cofx)
(def reg-sub      core.reg/reg-sub)
(def reg-event-db core.reg/reg-event-db)
(def reg-event-fx core.reg/reg-event-fx)
(def reg-fx       core.reg/reg-fx)

; re-frame.core.sub
(def subscribe  core.sub/subscribe)
(def subscribed core.sub/subscribed)

; re-frame.core.utils
(def r                              core.utils/r)
(def event-vector?                  core.utils/event-vector?)
(def event-vector<-params           core.utils/event-vector<-params)
(def event-vector->event-id         core.utils/event-vector->event-id)
(def event-vector->effects-map      core.utils/event-vector->effects-map)
(def event-vector->handler-f        core.utils/event-vector->handler-f)
(def effects-map<-event             core.utils/effects-map<-event)
(def merge-effects-maps             core.utils/merge-effects-maps)
(def effects-map->handler-f         core.utils/effects-map->handler-f)
(def context->event-vector          core.utils/context->event-vector)
(def context->event-id              core.utils/context->event-id)
(def context->db-before-effect      core.utils/context->db-before-effect)
(def context->db-after-effect       core.utils/context->db-after-effect)
(def cofx->event-id                 core.utils/cofx->event-id)
(def cofx->event-vector             core.utils/cofx->event-vector)
(def metamorphic-handler->handler-f core.utils/metamorphic-handler->handler-f)
(def metamorphic-event->effects-map core.utils/metamorphic-event->effects-map)
(def metamorphic-event<-params      core.utils/metamorphic-event<-params)
(def apply-fx-params                core.utils/apply-fx-params)

; re-frame.debug.interceptors
(def debug! debug.interceptors/debug!)

; re-frame.debug.side-effects
(def set-debug-mode!    debug.side-effects/set-debug-mode!)
(def quit-debug-mode!   debug.side-effects/quit-debug-mode!)
(def toggle-debug-mode! debug.side-effects/toggle-debug-mode!)

; re-frame.debug.state
(def DEBUG-MODE? debug.state/DEBUG-MODE?)
