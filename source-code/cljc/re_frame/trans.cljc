
(ns re-frame.trans
    (:require [vector.api :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; A Re-Frame események függvényei a db paraméter után egy paraméter-vektort fogadnak,
; amelynek az első eleme az esemény azonosítója, ami szinte kivétel nélkül soha
; nincs használva.
;
; A Re-Frame függvényeket a következő formula szerint lehetséges használni:
; (db/remove-item! db [:my-event [:my-item]])
;
; Egyszerűbb, ha nem kell ezzel a vektorral baszakodni, ezért létezik
; az r nevű függvény, amelynek az egyetlen feladata, hogy egy másik
; formula szerint is használhatóvá teszi a Re-frame események függvényeit:
; (r db/remove-item! db [:my-item])
(defn r
  ; @param (function) f
  ; @param (*) params
  ;
  ; @example
  ; (r db/remove-item! db [:my-item])
  ; =>
  ; (db/remove-item! db [nil [:my-item]])
  ;
  ; @return (*)
  [f & params]
  (let [context      (first params)
        event-vector (vector/cons-item (rest params) nil)]
       (f context event-vector)))
