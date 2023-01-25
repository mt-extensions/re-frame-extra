
(ns re-frame.debug.api
    (:require [re-frame.debug.interceptors :as interceptors]
              [re-frame.debug.side-effects :as side-effects]
              [re-frame.debug.state        :as state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; re-frame.debug.interceptors
(def debug! interceptors/debug!)

; re-frame.debug.side-effects
(def set-debug-mode!    side-effects/set-debug-mode!)
(def quit-debug-mode!   side-effects/quit-debug-mode!)
(def toggle-debug-mode! side-effects/toggle-debug-mode!)

; re-frame.debug.state
(def DEBUG-MODE? state/DEBUG-MODE?)
