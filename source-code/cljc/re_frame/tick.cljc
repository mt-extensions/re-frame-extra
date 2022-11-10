
(ns re-frame.tick
    (:require [mid-fruits.vector    :as vector]
              [re-frame.core        :as core]
              [re-frame.effects-map :as effects-map]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Ütemalapú esemény-időzítés
;
; A dispatch-later esemény-indítóhoz hasonlóan időzítve indítja az eseményeket,
; de nem ms-ban méri az időt, hanem a re-frame szerinti ütemekben (tick).
;
; @usage
;  (reg-event-fx :my-event
;    (fn [_ _]
;        {:dispatch-tick [{:dispatch [:my-event]
;                          :tick     10}]}))

(defn dispatch-tick
  ; @param (maps in vector) effects-maps-vector
  ;  [{ ... }
  ;   {:tick 10
  ;    :dispatch       [:my-event]
  ;    :dispatch-n     [[:my-event]]
  ;    :dispatch-later [ ... ]}
  ;   { ... }]
  [effects-maps-vector]
  (core/dispatch [:dispatch-tick effects-maps-vector]))

; @usage
;  (reg-event-fx :my-event
;    (fn [_ _]
;        {:dispatch-tick [{:dispatch [:my-event]
;                          :tick     10}]}))
(core/reg-fx :dispatch-tick dispatch-tick)

(core/reg-event-fx :dispatch-tick
  ; @param (maps in vector) effects-maps-vector
  ;  [{ ... }
  ;   {:tick 10
  ;    :dispatch       [:my-event]
  ;    :dispatch-n     [[:my-event]]
  ;    :dispatch-later [ ... ]}
  ;
  ;   { ... }]
  (fn [_ [_ effects-maps-vector]]
      (letfn [(f [merged-effects-map effects-map]
                 (if ; Tick now?
                     (= 0 (:tick effects-map))
                     ; Tick now!
                     (effects-map/merge-effects-maps merged-effects-map effects-map)
                     ; Tick later!
                     (update merged-effects-map :dispatch-tick vector/conj-item (update effects-map :tick dec))))]
             (reduce f {} effects-maps-vector))))
