
(ns re-frame.debug.interceptors
    (:require [format.api            :as format]
              [noop.api              :refer [return]]
              [re-frame.core         :as core]
              [re-frame.core.helpers :as core.helpers]
              [re-frame.debug.state  :as debug.state]
              [time.api              :as time]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn log-event-f
  ; @ignore
  ;
  ; @description
  ; Prints the event vector to the console only in debug mode.
  ;
  ; @param (map) context
  ;
  ; @return (map)
  [context]
  (if @debug.state/DEBUG-MODE? (-> context core.helpers/context->event-vector println))
  (return context))

; @constant (?)
(def LOG-EVENT! (core/->interceptor :id :re-frame/log-event! :before log-event-f))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn debug-f
  ; @ignore
  ;
  ; @description
  ; Prints the event vector to the console.
  ;
  ; @param (map) context
  ;
  ; @return (map)
  [context]
  (-> context core.helpers/context->event-vector println)
  (return context))

; @constant (?)
(def debug! (core/->interceptor :id :re-frame/debug! :after debug-f))
