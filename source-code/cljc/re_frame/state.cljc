
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns re-frame.state)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (map)
; A letiltott esemény-azonosítók és a tiltásuk feloldásainak idejei
(def EVENT-LOCKS (atom {}))
