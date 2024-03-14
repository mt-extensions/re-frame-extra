
(ns re-frame.extra.interceptors
    (:require [re-frame.core :as core]
              [re-frame.extra.utils :as utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
; {:after (function)
;  :before (function)
;  :id (keyword)}
(def ensure-subject-id (core/->interceptor :id :re-frame/ensure-subject-id :before utils/context<-subject-id))
