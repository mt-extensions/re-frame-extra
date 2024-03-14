
(ns re-frame.extra.utils
    (:require [fruits.vector.api :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn r
  ; @description
  ; Takes a handler function, a context map, and a list of custom parameters in shorthand form
  ; where the parameters are NOT wrapped in an event vector, and applies the given handler function
  ; in longhand form using a NIL value to replace the missing handler ID.
  ;
  ; @param (function) handler-f
  ; @param (map)(opt) context
  ; @param (list of *)(opt) params
  ;
  ; @usage
  ; ;; Handler functions take their parameters in a vector starting with the event ID.
  ; (defn my-handler [db [event-id my-param]] ...)
  ; ;; Applying the handler function in longhand form:
  ; (my-handler db [:my-event "My value"])
  ; ;; Applying the handler function in shorthand form:
  ; (r my-handler db "My value")
  ;
  ; @return (*)
  [handler-f & [context & params]]
  (handler-f context (vector/cons-item params nil)))
