
(ns re-frame.debug
    (:require [candy.api        :refer [return]]
              [format.api       :as format]
              [re-frame.context :as context]
              [re-frame.core    :as core]
              [re-frame.state   :as state]
              [time.api         :as time]))

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

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-debug-mode!
  ; @usage
  ; (set-debug-mode!)
  []
  (reset! state/DEBUG-MODE? true))

(defn quit-debug-mode!
  ; @usage
  ; (quit-debug-mode!)
  []
  (reset! state/DEBUG-MODE? false))
  
(defn toggle-debug-mode!
  ; @usage
  ; (toggle-debug-mode!)
  []
  (swap! state/DEBUG-MODE? not))
