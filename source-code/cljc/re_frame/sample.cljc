
(ns re-frame.sample
    (:require [re-frame.api :as r :refer [debug!]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Ha a debug! interceptort használod, akkor az esemény megtörténésekor
; az esemény-vektor kiíródik a console/terminálra.
(r/reg-event-fx :my-event [debug!] (fn [_ _] [:my-event]))
