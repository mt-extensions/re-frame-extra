
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns re-frame.side-effects
    (:require [re-frame.core      :as core]
              [re-frame.loggers   :refer [console]]
              [re-frame.registrar :as registrar]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn fx
  ; @param (vector) effect-vector
  ;
  ; @usage
  ;  (r/reg-fx :my-side-effect (fn [a b c]))
  ;  (r/fx [:my-side-effect "a" "b" "c"])
  [[effect-id & params :as effect-vector]]
  (when (= :db effect-id)
        (console :warn "re-frame: \":fx\" effect should not contain a :db effect"))
  (if-let [effect-f (registrar/get-handler :fx effect-id false)]
          (effect-f params)
          (console :warn "re-frame: in \":fx\" effect found " effect-id " which has no associated handler. Ignoring.")))

; @usage
;  {:fx [...]}
(registrar/clear-handlers :fx :fx)
(core/reg-fx              :fx  fx)

(defn fx-n
  ; @param (vectors in vector) effect-vector-list
  ;
  ; @usage
  ;  (r/reg-fx :my-side-effect (fn [a b c]))
  ;  (r/fx-n [[:my-side-effect "a" "b" "c"]
  ;           [...]])
  [effect-vector-list]
  (if-not (sequential? effect-vector-list)
          (console :warn "re-frame: \":fx\" effect expects a seq, but was given " (type effect-vector-list))
          (doseq [effect-vector (remove nil? effect-vector-list)]
                 (fx effect-vector))))

; @usage
;  {:fx-n [[...] [...]]}
(core/reg-fx :fx-n fx-n)
