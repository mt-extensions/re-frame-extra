
(ns re-frame.debug.interceptors
    (:require [noop.api             :refer [return]]
              [re-frame.core        :as core]
              [re-frame.debug.state :as state]
              [vector.api           :as vector]))

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
  (if @state/DEBUG-MODE? (-> context :coeffects :event println))
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
  (-> context :coeffects :event println)
  (return context))

; @constant (?)
(def debug! (core/->interceptor :id :re-frame/debug! :after debug-f))
