
(ns re-frame.effects-map
    (:require [mid-fruits.map    :refer [update-some]]
              [mid-fruits.vector :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn effects-map<-event
  ; @param (map) effects-map
  ; @param (vector) event-vector
  ;
  ; @example
  ;  (effects-map<-event {:dispatch [:foo]} [:bar])
  ;  =>
  ;  {:dispatch [:foo] :dispatch-n [[:bar]]}
  ;
  ; @return (map)
  [effects-map event-vector]
  (update effects-map :dispatch-n vector/conj-item event-vector))

(defn merge-effects-maps
  ; @param (map) a
  ; @param (map) b
  ;
  ; @example
  ;  (merge-effects-maps {:dispatch [:a1]
  ;                       :dispatch-n [[:a2] [:a3]}]}
  ;                      {:dispatch [:b1]
  ;                       :dispatch-n [[:b2]]})
  ;  =>
  ;  {:dispatch [:a1] :dispatch-n [[:a2] [:a3] [:b1] [:b2]]}
  ;
  ; @return (map)
  [a b]
  (-> a (update-some :dispatch-n     vector/conj-item    (:dispatch       b))
        (update-some :dispatch-n     vector/concat-items (:dispatch-n     b))
        (update-some :dispatch-later vector/concat-items (:dispatch-later b))))

(defn effects-map->handler-f
  ; @param (map) effects-map
  ;
  ; @example
  ;  (effects-map->handler-f {:dispatch [...]})
  ;  =>
  ;  (fn [_ _] {:dispatch [...]})
  ;
  ; @return (function)
  [effects-map]
  (fn [_ _] effects-map))
