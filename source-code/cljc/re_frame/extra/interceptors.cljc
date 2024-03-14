
(ns re-frame.extra.interceptors
    (:require [re-frame.core :as core]
              [re-frame.tools.api :as re-frame.tools]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
; Interceptor for ensuring that the second item of the event vector is a keyword.
;
; @constant (map)
; {:after (function)
;  :before (function)
;  :id (keyword)}
;
; @usage
; (reg-event-fx :my-effects
;   [ensure-subject-id]
;   (fn [cofx [event-id subject-id {...}]] ...))
; (dispatch [:my-effects             {...}])
; (dispatch [:my-effects :my-subject {...}])
(def ensure-subject-id (core/->interceptor :id :re-frame/ensure-subject-id :before re-frame.tools/context<-subject-id))
