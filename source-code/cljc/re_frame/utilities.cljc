
(ns re-frame.utilities
    (:require [fruits.map.api    :as map]
              [fruits.vector.api :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn r
  ; @param (function) f
  ; @param (map)(opt) context
  ; @param (list of *)(opt) params
  ;
  ; @usage
  ; (defn my-handler [db [event-id my-param]] ...)
  ; (r my-handler db "My value")
  ;
  ; @return (*)
  [f & [context & params]]
  (f context (vector/cons-item params nil)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn event-vector?
  ; @param (*) n
  ;
  ; @example
  ; (event-vector? [:my-event ...])
  ; =>
  ; true
  ;
  ; @return (boolean)
  [n]
  (and (-> n vector?)
       (-> n first keyword?)))

(defn event-vector<-params
  ; @param (event-vector) n
  ; @param (list of *) params
  ;
  ; @example
  ; (event-vector<-params [:my-event] "My param" "Another param")
  ; =>
  ; [:my-event "My param" "Another param"]
  ;
  ; @return (event-vector)
  [n & params]
  (vector/concat-items n params))

(defn event-vector->event-id
  ; @param (vector) event-vector
  ;
  ; @example
  ; (event-vector->event-id [:my-event ...])
  ; =>
  ; :my-event
  ;
  ; @return (vector)
  [event-vector]
  (first event-vector))

(defn event-vector->effects-map
  ; @param (vector) event-vector
  ;
  ; @example
  ; (event-vector->effects-map [...])
  ; =>
  ; {:dispatch [...]}
  ;
  ; @return (map)
  [event-vector]
  {:dispatch event-vector})

(defn event-vector->handler-f
  ; @param (vector) event-vector
  ;
  ; @example
  ; (event-vector->handler-f [...])
  ; =>
  ; (fn [_ _] {:dispatch [...]})
  ;
  ; @return (function)
  [event-vector]
  (fn [_ _] {:dispatch event-vector}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn effects-map<-event
  ; @param (map) effects-map
  ; @param (vector) event-vector
  ;
  ; @example
  ; (effects-map<-event {:dispatch [:my-event]} [:another-event])
  ; =>
  ; {:dispatch [:my-event] :dispatch-n [[:another-event]]}
  ;
  ; @return (map)
  [effects-map event-vector]
  (update effects-map :dispatch-n vector/conj-item event-vector))

(defn merge-effects-maps
  ; @param (map) a
  ; @param (map) b
  ;
  ; @example
  ; (merge-effects-maps {:dispatch [:a1]
  ;                      :dispatch-n [[:a2] [:a3]}]}
  ;                     {:dispatch [:b1]
  ;                      :dispatch-n [[:b2]]})
  ; =>
  ; {:dispatch [:a1] :dispatch-n [[:a2] [:a3] [:b1] [:b2]]}
  ;
  ; @return (map)
  [a b]
  (letfn [; Applies the given 'f' function ('conj-item' / 'concat-items') only if the passed value is not NIL.
          (f0 [a key f value] (if value (update a key f value) a))]
         (-> a (f0 :fx-n           vector/conj-item    (:fx             b))
               (f0 :fx-n           vector/concat-items (:fx-n           b))
               (f0 :dispatch-n     vector/conj-item    (:dispatch       b))
               (f0 :dispatch-n     vector/concat-items (:dispatch-n     b))
               (f0 :dispatch-later vector/concat-items (:dispatch-later b)))))

(defn effects-map->handler-f
  ; @param (map) effects-map
  ;
  ; @example
  ; (effects-map->handler-f {:dispatch [...]})
  ; =>
  ; (fn [_ _] {:dispatch [...]})
  ;
  ; @return (function)
  [effects-map]
  (fn [_ _] effects-map))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn context->event-vector
  ; @param (map) context
  ; {:coeffects (map)
  ;  {:event (vector)}}
  ;
  ; @example
  ; (context->event-vector {:coeffects {:event [:my-event ...]}})
  ; =>
  ; [:my-event ...]
  ;
  ; @return (vector)
  [context]
  (get-in context [:coeffects :event]))

(defn context->event-id
  ; @param (map) context
  ; {:coeffects (map)
  ;  {:event (vector)}}
  ;
  ; @example
  ; (context->event-id {:coeffects {:event [:my-event ...]}})
  ; =>
  ; :my-event
  ;
  ; @return (keyword)
  [context]
  (-> context context->event-vector event-vector->event-id))

(defn context->db-before-effect
  ; @param (map) context
  ; {:coeffects (map)
  ;  {:db (map)}}
  ;
  ; @example
  ; (context->db-before-effect {:coeffects {:db {...}}})
  ; =>
  ; {...}
  ;
  ; @return (map)
  [context]
  (get-in context [:coeffects :db]))

(defn context->db-after-effect
  ; @param (map) context
  ; {:effects (map)
  ;  {:db (map)}}
  ;
  ; @example
  ; (context->db-after-effect {:effects {:db {...}}})
  ; =>
  ; {...}
  ;
  ; @return (map)
  [context]
  (get-in context [:effects :db]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cofx->event-vector
  ; @param (map) cofx
  ; {:event (vector)}
  ;
  ; @example
  ; (cofx->event-vector {:event [...]})
  ; =>
  ; [...]
  ;
  ; @return (vector)
  [cofx]
  (get cofx :event))

(defn cofx->event-id
  ; @param (map) cofx
  ; {:event (vector)
  ;   [(keyword) event-id]}
  ;
  ; @example
  ; (cofx->event-id {:event [:my-event ...]})
  ; =>
  ; :my-event
  ;
  ; @return (keyword)
  [cofx]
  (get-in cofx [:event 0]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn metamorphic-handler->handler-f
  ; @param (metamorphic-event) n
  ;
  ; @example
  ; (metamorphic-handler->handler-f [...])
  ; =>
  ; (fn [_ _] {:dispatch [...]})
  ;
  ; @example
  ; (metamorphic-handler->handler-f {:dispatch [...]})
  ; =>
  ; (fn [_ _] {:dispatch [...]})
  ;
  ; @example
  ; (metamorphic-handler->handler-f (fn [_ _] ...))
  ; =>
  ; (fn [_ _] ...})
  ;
  ; @return (function)
  [n]
  (cond (map?    n) (effects-map->handler-f  n)
        (vector? n) (event-vector->handler-f n)
        :return  n))

(defn metamorphic-event->effects-map
  ; @param (metamorphic-event) n
  ;
  ; @example
  ; (metamorphic-event->effects-map [:my-event])
  ; =>
  ; {:dispatch [:my-event]}
  ;
  ; @example
  ; (metamorphic-event->effects-map {:dispatch [:my-event])
  ; =>
  ; {:dispatch [:my-event]}
  ;
  ; @return (map)
  [n]
  (cond (vector? n) (-> n event-vector->effects-map)
        (map?    n) (-> n)))

(defn metamorphic-event<-params
  ; @param (metamorphic-event) n
  ; @param (list of *) params
  ;
  ; @example
  ; (metamorphic-event<-params [:my-event] "My param" "Another param")
  ; =>
  ; [:my-event "My param" "Another param"]
  ;
  ; @example
  ; (metamorphic-event<-params {:dispatch [:my-event]} "My param" "Another param")
  ; =>
  ; {:dispatch [:my-event "My param" "Another param"]}
  ;
  ; @return (metamorphic-event)
  [n & params]
  ; A metamorphic event could be a vector ...
  ; ... as an event-vector:         [:my-event ...]
  ; ... as a dispatch-later vector: [{:ms   500 :dispatch [:my-event ...]}]
  ; ... as a dispatch-tick vector:  [{:tick 500 :dispatch [:my-event ...]}]
  (cond (event-vector? n) (vector/concat-items n params)
        (vector?       n) (vector/->items      n #(apply metamorphic-event<-params % params))
        (map?          n) (map/->values        n #(apply metamorphic-event<-params % params))
        :return        n))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn apply-fx-params
  ; @param (function) handler-f
  ; @param (* or vector) params
  ;
  ; @usage
  ; (apply-fx-params (fn [a] ...) "a")
  ;
  ; @usage
  ; (apply-fx-params (fn [a] ...) ["a"])
  ;
  ; @usage
  ; (apply-fx-params (fn [a b] ...) ["a" "b"])
  ;
  ; @return (*)
  [handler-f params]
  (if (sequential?     params)
      (apply handler-f params)
      (handler-f       params)))
