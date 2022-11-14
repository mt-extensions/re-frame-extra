
(ns re-frame.log
    (:require [candy.api             :refer [return]]
              [re-frame.context      :as context]
              [re-frame.interceptors :as interceptors]
              [re-frame.state        :as state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn log-event-f
  ; @param (map) context
  ;
  ; @return (map)
  [context]
  (if @state/DEBUG-MODE? (-> context context/context->event-vector println))
  (return context))

; @constant (?)
(def LOG-EVENT! (interceptors/->interceptor :id :re-frame/log-event!
                                            :before log-event-f))
