
(ns re-frame.state)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (map)
; A letiltott esemény-azonosítók és a tiltásuk feloldásainak idejei
(def EVENT-LOCKS (atom {}))

; @atom (boolean)
(def DEBUG-MODE? (atom false))
