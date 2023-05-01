
(ns re-frame.side-effects
    (:require [re-frame.state :as state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-debug-mode!
  ; @description
  ; Turns the debug mode on.
  ;
  ; @usage
  ; (set-debug-mode!)
  []
  (reset! state/DEBUG-MODE? true))

(defn quit-debug-mode!
  ; @description
  ; Turns the debug mode off.
  ;
  ; @usage
  ; (quit-debug-mode!)
  []
  (reset! state/DEBUG-MODE? false))

(defn toggle-debug-mode!
  ; @description
  ; Toggles the debug mode on/off.
  ;
  ; @usage
  ; (toggle-debug-mode!)
  []
  (swap! state/DEBUG-MODE? not))
