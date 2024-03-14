
(ns re-frame.extra.check
    (:require [re-frame.extra.utils :as utils]
              [re-frame.dev.api :as re-frame.dev]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn check-event-handler
  ; @ignore
  ;
  ; @note
  ; By default, Re-Frame does not print errors in the Clojure environment if an event is is dispatched but not registered.
  ;
  ; @description
  ; Prints a warning to the console in case ...
  ; ... the given event handler is an event vector containing an event ID that is not registered.
  ;
  ; @param (metamorphic-event or vector) event-handler
  ;
  ; @usage
  ; (check-event-handler [:my-event {...}])
  [event-handler]
  (if (utils/event-vector? event-handler)
      (let [event-id      (utils/event-vector->event-id event-handler)
            event-exists? (re-frame.dev/event-handler-registered? :event event-id)]
           (when-not event-exists? (println "re-frame: no :event handler registered for:" event-id)
                                   (println event-handler)))))
