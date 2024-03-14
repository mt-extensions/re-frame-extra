
(ns re-frame.extra.check
    (:require [re-frame.extra.utils :as utils]
              [re-frame.dev.api :as re-frame.dev]
              [re-frame.loggers :refer [console]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn check-metamorphic-event
  ; @ignore
  ;
  ; @description
  ; Checks the given value whether it is provided as an event vector;
  ; to derive the event ID and check whether it is registered.
  ;
  ; @param (*) n
  ;
  ; @usage
  ; (check-metamorphic-event [:my-event {...}])
  [n]
  (if (utils/event-vector? n)
      (let [event-id (utils/event-vector->event-id n)]
           (when-not (re-frame.dev/event-handler-registered? :event event-id)
                     (println "re-frame: no :event handler registered for:" event-id)
                     (println n)))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn check-side-effect-vector
  ; @ignore
  ;
  ; @description
  ; Checks the given value whether it is provided as a side effect vector;
  ; to derive the effect ID and check whether it is registered.
  ;
  ; @param (*) n
  ;
  ; @usage
  ; (check-side-effect-vector [:my-side-effect {...}])
  [n]
  (if (utils/side-effect-vector? n)
      (let [effect-id (utils/side-effect-vector->effect-id n)]
           (when     (= :db effect-id)
                     (console :warn "re-frame: \":fx\" effect should not contain a :db effect"))
           (when-not (re-frame.dev/event-handler-registered? :fx effect-id)
                     (console :warn "re-frame: in \":fx\" effect found " effect-id " which has no associated handler. Ignoring.")))))

(defn check-side-effect-list
  ; @ignore
  ;
  ; @description
  ; Checks the given value whether it is provided as a vector.
  ;
  ; @param (*) n
  ;
  ; @usage
  ; (check-side-effect-list [[:my-side-effect {...}]])
  [n]
  (if-not (sequential? n)
          (console :warn "re-frame: \":fx\" effect expects a seq, but was given " (type n))))
