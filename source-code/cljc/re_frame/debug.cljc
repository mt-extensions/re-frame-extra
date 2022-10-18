
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns re-frame.debug
    (:require [mid-fruits.candy  :refer [return]]
              [mid-fruits.format :as format]
              [re-frame.context  :as context]
              [re-frame.core     :as core]
              [time.api          :as time]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn debug-f
  ; @param (map) context
  ;
  ; @return (map)
  [context]
  (let [event-vector (context/context->event-vector context)]
       #?(:cljs (let [timestamp (-> js/performance .now time/ms->s format/decimals)]
                     (println timestamp "\n" event-vector)))
       (return context)))

; @constant (?)
(def debug! (core/->interceptor :id :re-frame/debug!
                                :after debug-f))
