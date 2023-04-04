
(ns re-frame.core.api
    (:require [re-frame.core              :as core]
              [re-frame.core.cofx         :as cofx]
              [re-frame.core.dispatch-set :as dispatch-set]
              [re-frame.core.env          :as env]
              [re-frame.core.fx           :as fx]
              [re-frame.core.interceptors :as interceptors]
              [re-frame.core.reg          :as reg]
              [re-frame.core.sub          :as sub]
              [re-frame.core.utils        :as utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; re-frame.core
(def ->interceptor core/->interceptor)

; re-frame.core.cofx
(def inject-cofx cofx/inject-cofx)

; re-frame.core.env
(def get-event-handlers        env/get-event-handlers)
(def get-event-handler         env/get-event-handler)
(def event-handler-registered? env/event-handler-registered?)

; re-frame.core.interceptors
(def event-vector<-id interceptors/event-vector<-id)

; re-frame.core.dispatch-set
(def dispatch       dispatch-set/dispatch)
(def dispatch-fx    dispatch-set/dispatch-fx)
(def dispatch-sync  dispatch-set/dispatch-sync)
(def dispatch-n     dispatch-set/dispatch-n)
(def dispatch-later dispatch-set/dispatch-later)
(def dispatch-last  dispatch-set/dispatch-last)
(def dispatch-once  dispatch-set/dispatch-once)
(def dispatch-tick  dispatch-set/dispatch-tick)

; re-frame.core.fx
(def fx   fx/fx)
(def fx-n fx/fx-n)

; re-frame.core.reg
(def reg-cofx     reg/reg-cofx)
(def reg-sub      reg/reg-sub)
(def reg-event-db reg/reg-event-db)
(def reg-event-fx reg/reg-event-fx)
(def reg-fx       reg/reg-fx)

; re-frame.core.sub
(def subscribe  sub/subscribe)
(def subscribed sub/subscribed)

; re-frame.core.utils
(def r                              utils/r)
(def event-vector?                  utils/event-vector?)
(def event-vector<-params           utils/event-vector<-params)
(def event-vector->event-id         utils/event-vector->event-id)
(def event-vector->effects-map      utils/event-vector->effects-map)
(def event-vector->handler-f        utils/event-vector->handler-f)
(def effects-map<-event             utils/effects-map<-event)
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
