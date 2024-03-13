
(ns re-frame.interceptors
    (:require
              [fruits.vector.api :as vector]
              [re-frame.core     :as core]
              [re-frame.dev.api  :as re-frame.dev]
              [re-frame.extra.utils :as utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (?)
(def ensure-subject-id (core/->interceptor :id :re-frame/ensure-subject-id :before utils/context<-subject-id))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn interceptors<-default-interceptors
  ; @ignore
  ;
  ; @description
  ; Appends the default interceptors to the given interceptor vector.
  ;
  ; @param (vector) interceptors
  ;
  ; @usage
  ; (interceptors<-default-interceptors [])
  ; =>
  ; [re-frame.dev/log-event!]
  ;
  ; @return (functions in vector)
  [interceptors]
  (vector/conj-item interceptors re-frame.dev/log-event!))
