
(ns re-frame.log
    (:require [mid-fruits.candy      :refer [return]]
              [re-frame.context      :as context]
              [re-frame.interceptors :as interceptors]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (?)
(def LOG-EVENT! (interceptors/->interceptor :id :re-frame/log-event!
                                            :before #(let [event-vector (context/context->event-vector %1)]
                                                         ; Szükséges korlátozni a fájl maximális méretét!
                                                         ; (write events to log file ...)
                                                          (return %1))))
