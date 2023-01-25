
(ns re-frame.debug.side-effects
    (:require [re-frame.debug.state :as state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-debug-mode!
  ; @description
  ; Turns on the debug mode.
  ;
  ; @usage
  ; (set-debug-mode!)
  []
  (reset! state/DEBUG-MODE? true))

(defn quit-debug-mode!
  ; @description
  ; Turns off the debug mode.
  ;
  ; @usage
  ; (quit-debug-mode!)
  []
  (reset! state/DEBUG-MODE? false))

(defn toggle-debug-mode!
  ; @description
  ; Toggles on/off the debug mode.
  ;
  ; @usage
  ; (toggle-debug-mode!)
  []
  (swap! state/DEBUG-MODE? not))
