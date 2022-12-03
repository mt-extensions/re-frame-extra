
(ns re-frame.api
    (:require [re-frame.coeffects     :as coeffects]
              [re-frame.context       :as context]
              [re-frame.db            :as db]
              [re-frame.trans         :as trans]
              [re-frame.debug         :as debug]
              [re-frame.dispatch      :as dispatch]
              [re-frame.effects-map   :as effects-map]
              [re-frame.event-handler :as event-handler]
              [re-frame.event-vector  :as event-vector]
              [re-frame.id            :as id]
              [re-frame.interceptors  :as interceptors]
              [re-frame.metamorphic   :as metamorphic]
              [re-frame.reg           :as reg]
              [re-frame.side-effects  :as side-effects]
              [re-frame.state         :as state]
              [re-frame.sub           :as sub]
              [re-frame.tick          :as tick]
              [re-frame.types         :as types]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; re-frame.coeffects
(def cofx->event-vector coeffects/cofx->event-vector)
(def cofx->event-id     coeffects/cofx->event-id)
(def inject-cofx        coeffects/inject-cofx)

; re-frame.context
(def context->event-vector     context/context->event-vector)
(def context->event-id         context/context->event-id)
(def context->db-before-effect context/context->db-before-effect)
(def context->db-after-effect  context/context->db-after-effect)

; re-frame.db
(def app-db db/app-db)

; re-frame.debug
(def debug!             debug/debug!)
(def set-debug-mode!    debug/set-debug-mode!)
(def quit-debug-mode!   debug/quit-debug-mode!)
(def toggle-debug-mode! debug/toggle-debug-mode!)

; re-frame.dispatch
(def dispatch       dispatch/dispatch)
(def dispatch-fx    dispatch/dispatch-fx)
(def dispatch-sync  dispatch/dispatch-sync)
(def dispatch-n     dispatch/dispatch-n)
(def dispatch-later dispatch/dispatch-later)
(def dispatch-if    dispatch/dispatch-if)
(def dispatch-cond  dispatch/dispatch-cond)
(def dispatch-last  dispatch/dispatch-last)
(def dispatch-once  dispatch/dispatch-once)

; re-frame.effects-map
(def effects-map<-event     effects-map/effects-map<-event)
(def merge-effects-maps     effects-map/merge-effects-maps)
(def effects-map->handler-f effects-map/effects-map->handler-f)

; re-frame.event-handler
(def get-event-handlers        event-handler/get-event-handlers)
(def get-event-handler         event-handler/get-event-handler)
(def event-handler-registered? event-handler/event-handler-registered?)

; re-frame.event-vector
(def event-vector->event-id    event-vector/event-vector->event-id)
(def event-vector->effects-map event-vector/event-vector->effects-map)
(def event-vector->handler-f   event-vector/event-vector->handler-f)
(def event-vector<-params      event-vector/event-vector<-params)

; re-frame.id
(def event-vector<-id-f id/event-vector<-id-f)
(def event-vector<-id   id/event-vector<-id)

; re-frame.interceptors
(def ->interceptor interceptors/->interceptor)

; re-frame.metamorphic
(def metamorphic-handler->handler-f metamorphic/metamorphic-handler->handler-f)
(def metamorphic-event->effects-map metamorphic/metamorphic-event->effects-map)
(def metamorphic-event<-params      metamorphic/metamorphic-event<-params)

; re-frame.reg
(def reg-cofx        reg/reg-cofx)
(def reg-sub         reg/reg-sub)
(def reg-event-db    reg/reg-event-db)
(def reg-event-fx    reg/reg-event-fx)
(def apply-fx-params reg/apply-fx-params)
(def reg-fx          reg/reg-fx)

; re-frame.side-effects
(def fx   side-effects/fx)
(def fx-n side-effects/fx-n)

; re-frame.state
(def DEBUG-MODE? state/DEBUG-MODE?)

; re-frame.sub
(def subscribe  sub/subscribe)
(def subscribed sub/subscribed)

; re-frame.tick
(def dispatch-tick tick/dispatch-tick)

; re-frame.trans
(def r trans/r)

; re-frame.types
(def event-vector? types/event-vector?)
(def query-vector? types/query-vector?)
