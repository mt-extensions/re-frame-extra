
(ns re-frame.extra.utils
    (:require [fruits.map.api    :as map]
              [fruits.vector.api :as vector]
              [fruits.random.api :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn r
  ; @description
  ; Takes a handler function, a context map, and a list of custom parameters in shorthand form
  ; where the parameters are not wrapped in an event vector, and applies the given handler function
  ; in longhand form using a NIL value to replace the missing handler ID.
  ;
  ; @param (function) handler-f
  ; @param (map)(opt) context
  ; @param (list of *)(opt) params
  ;
  ; @usage
  ; ;; Handler functions take their parameters in a vector starting with the event ID.
  ; (defn my-handler [db [event-id my-param]] ...)
  ; ;; Applying the handler function in longhand form:
  ; (my-handler db [:my-event "My value"])
  ; ;; Applying the handler function in shorthand form:
  ; (r my-handler db "My value")
  ;
  ; @return (*)
  [handler-f & [context & params]]
  (handler-f context (vector/cons-item params nil)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn event-vector?
  ; @description
  ; Returns TRUE if the given 'n' value is a vector containing the first element as a keyword (indicating it might be an event vector).
  ;
  ; @param (*) n
  ;
  ; @usage
  ; (event-vector? [:my-event ...])
  ; =>
  ; true
  ;
  ; @return (boolean)
  [n]
  (and (-> n vector?)
       (-> n first keyword?)))

(defn event-vector<-params
  ; @description
  ; Appends the given parameters to the given event vector.
  ;
  ; @param (event-vector) event-vector
  ; @param (list of *) params
  ;
  ; @usage
  ; (event-vector<-params [:my-event] "My param" "Another param")
  ; =>
  ; [:my-event "My param" "Another param"]
  ;
  ; @return (event-vector)
  [event-vector & params]
  (vector/concat-items event-vector params))

(defn event-vector<-subject-id
  ; @note
  ; The first item of event vectors is always a keyword representing the handler ID.
  ; The second item of event vectors is the first parameter of the handler, that as a keyword can represent a subject ID.
  ;
  ; @description
  ; Ensures that the second item of the given event vector is a keyword.
  ;
  ; @param (vector) event-vector
  ;
  ; @usage
  ; (event-vector<-subject-id [:my-event :my-subject {...}])
  ; =>
  ; [:my-event :my-subject {...}]
  ;
  ; @usage
  ; (event-vector<-subject-id [:my-event {...}])
  ; =>
  ; [:my-event :xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx {...}]
  ;
  ; @return (vector)
  [event-vector]
  (if (-> event-vector second keyword?)
      (-> event-vector)
      (-> event-vector (vector/insert-item 1 (random/generate-keyword)))))

(defn event-vector->event-id
  ; @description
  ; Returns the event ID from the given event vector.
  ;
  ; @param (vector) event-vector
  ;
  ; @usage
  ; (event-vector->event-id [:my-event {...}])
  ; =>
  ; :my-event
  ;
  ; @return (vector)
  [event-vector]
  (first event-vector))

(defn event-vector->effects-map
  ; @description
  ; Converts the given event vector into an effect map.
  ;
  ; @param (vector) event-vector
  ;
  ; @usage
  ; (event-vector->effects-map [:my-event {...}])
  ; =>
  ; {:dispatch [:my-event {...}]}
  ;
  ; @return (map)
  [event-vector]
  {:dispatch event-vector})

(defn event-vector->handler-f
  ; @description
  ; Converts the given event vector into an effect handler function.
  ;
  ; @param (vector) event-vector
  ;
  ; @usage
  ; (event-vector->handler-f [...])
  ; =>
  ; (fn [_ _] {:dispatch [...]})
  ;
  ; @return (function)
  [event-vector]
  (fn [_ _] {:dispatch event-vector}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn effects-map<-event-vector
  ; @description
  ; Inserts the given event vector into the given effect map.
  ;
  ; @param (map) effects-map
  ; @param (vector) event-vector
  ;
  ; @usage
  ; (effects-map<-event-vector {:dispatch [:my-event]} [:another-event])
  ; =>
  ; {:dispatch [:my-event] :dispatch-n [[:another-event]]}
  ;
  ; @return (map)
  [effects-map event-vector]
  (update effects-map :dispatch-n vector/conj-item event-vector))

(defn merge-effects-maps
  ; @description
  ; Merges the the given effect maps.
  ;
  ; @param (map) a
  ; @param (map) b
  ;
  ; @usage
  ; (merge-effects-maps {:dispatch [:a1] :dispatch-n [[:a2] [:a3]}]}
  ;                     {:dispatch [:b1] :dispatch-n [[:b2]]})
  ; =>
  ; {:dispatch [:a1] :dispatch-n [[:a2] [:a3] [:b1] [:b2]]}
  ;
  ; @return (map)
  [a b]
  (letfn [; Applies the given 'f' function ('conj-item' / 'concat-items') only if the provided value is not NIL.
          (f0 [a key f value] (if value (update a key f value) a))]
         (-> a (f0 :fx-n           vector/conj-item    (:fx             b))
               (f0 :fx-n           vector/concat-items (:fx-n           b))
               (f0 :dispatch-n     vector/conj-item    (:dispatch       b))
               (f0 :dispatch-n     vector/concat-items (:dispatch-n     b))
               (f0 :dispatch-later vector/concat-items (:dispatch-later b)))))

(defn effects-map->handler-f
  ; @description
  ; Converts the given effect map into an effect handler function.
  ;
  ; @param (map) effects-map
  ;
  ; @usage
  ; (effects-map->handler-f {:dispatch [:my-event {...}]})
  ; =>
  ; (fn [_ _] {:dispatch [:my-event {...}]})
  ;
  ; @return (function)
  [effects-map]
  (fn [_ _] effects-map))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn context<-subject-id
  ; @note
  ; The first item of event vectors is always a keyword representing the handler ID.
  ; The second item of event vectors is the first parameter of the handler, that as a keyword can represent a subject ID.
  ;
  ; @description
  ; Ensures that the second item of the event vector (in the given context map) is a keyword.
  ;
  ; @param (map) context
  ;
  ; @usage
  ; (context<-subject-id {:coeffects {:event [:my-event :my-subject {...}]}})
  ; =>
  ; {:coeffects {:event [:my-event :my-subject {...}]}}
  ;
  ; @usage
  ; (context<-subject-id {:coeffects {:event [:my-event {...}]}})
  ; =>
  ; {:coeffects {:event [:my-event :xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx {...}]}}
  ;
  ; @return (map)
  [context]
  (update-in context [:coeffects :event] event-vector<-subject-id))

(defn context->event-vector
  ; @description
  ; Returns the event vector from the given context map.
  ;
  ; @param (map) context
  ; {:coeffects (map)
  ;  {:event (vector)
  ;   ...}
  ;  ...}
  ;
  ; @usage
  ; (context->event-vector {:coeffects {:event [:my-event {...}]}})
  ; =>
  ; [:my-event {...}]
  ;
  ; @return (vector)
  [context]
  (get-in context [:coeffects :event]))

(defn context->event-id
  ; @description
  ; Returns the event ID from the given context map.
  ;
  ; @param (map) context
  ; {:coeffects (map)
  ;  {:event (vector)
  ;   ...}
  ;  ...}
  ;
  ; @usage
  ; (context->event-id {:coeffects {:event [:my-event {...}]}})
  ; =>
  ; :my-event
  ;
  ; @return (keyword)
  [context]
  (-> context context->event-vector event-vector->event-id))

(defn context->db-before-effect
  ; @description
  ; Returns the unchanged db from the given context map.
  ;
  ; @param (map) context
  ; {:coeffects (map)
  ;  {:db (map)
  ;   ...}
  ;  ...}
  ;
  ; @usage
  ; (context->db-before-effect {:coeffects {:db {...}}})
  ; =>
  ; {...}
  ;
  ; @return (map)
  [context]
  (get-in context [:coeffects :db]))

(defn context->db-after-effect
  ; @description
  ; Returns the updated db from the given context map.
  ;
  ; @param (map) context
  ; {:effects (map)
  ;  {:db (map)
  ;   ...}
  ;  ...}
  ;
  ; @usage
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
  ; @description
  ; Returns the event vector from the given coeffect map.
  ;
  ; @param (map) cofx
  ; {:event (vector)
  ;  ...}
  ;
  ; @usage
  ; (cofx->event-vector {:event [:my-event {...}]})
  ; =>
  ; [:my-event {...}]
  ;
  ; @return (vector)
  [cofx]
  (get cofx :event))

(defn cofx->event-id
  ; @description
  ; Returns the event ID from the given coeffect map.
  ;
  ; @param (map) cofx
  ; {:event (vector)
  ;   [(keyword) event-id]
  ;  ...}
  ;
  ; @usage
  ; (cofx->event-id {:event [:my-event {...}]})
  ; =>
  ; :my-event
  ;
  ; @return (keyword)
  [cofx]
  (get-in cofx [:event 0]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn metamorphic-event->effects-map
  ; @description
  ; Converts the given metamorphic event into an effect map.
  ;
  ; @param (metamorphic-event or vector) metamorphic-event
  ;
  ; @usage
  ; (metamorphic-event->effects-map [:my-event])
  ; =>
  ; {:dispatch [:my-event]}
  ;
  ; @usage
  ; (metamorphic-event->effects-map {:dispatch [:my-event])
  ; =>
  ; {:dispatch [:my-event]}
  ;
  ; @return (map)
  [metamorphic-event]
  (cond (vector? metamorphic-event) (-> metamorphic-event event-vector->effects-map)
        (map?    metamorphic-event) (-> metamorphic-event)))

(defn metamorphic-event<-params
  ; @description
  ; @description
  ; Appends the given parameters to each event vector in the given effect map.
  ;
  ; @param (metamorphic-event or vector) metamorphic-event
  ; @param (list of *) params
  ;
  ; @usage
  ; (metamorphic-event<-params [:my-event] "My param" "Another param")
  ; =>
  ; [:my-event "My param" "Another param"]
  ;
  ; @usage
  ; (metamorphic-event<-params {:dispatch [:my-event]} "My param" "Another param")
  ; =>
  ; {:dispatch [:my-event "My param" "Another param"]}
  ;
  ; @return (metamorphic-event)
  [metamorphic-event & params]
  ; A metamorphic event can be a vector ...
  ; ... as an event-vector:         [:my-event {...}]
  ; ... as a dispatch-later vector: [{:ms   500 :dispatch [:my-event {...}]}]
  ; ... as a dispatch-tick vector:  [{:tick 500 :dispatch [:my-event {...}]}]
  (cond (event-vector? metamorphic-event) (vector/concat-items metamorphic-event params)
        (vector?       metamorphic-event) (vector/->items      metamorphic-event #(apply metamorphic-event<-params % params))
        (map?          metamorphic-event) (map/->values        metamorphic-event #(apply metamorphic-event<-params % params))
        :return        metamorphic-event))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn metamorphic-handler->handler-f
  ; @description
  ; Converts the given metamorphic handler into an effect handler function.
  ;
  ; @param (function or metamorphic-handler) metamorphic-handler
  ;
  ; @usage
  ; (metamorphic-handler->handler-f [:my-event {...}])
  ; =>
  ; (fn [_ _] {:dispatch [:my-event {...}]})
  ;
  ; @usage
  ; (metamorphic-handler->handler-f {:dispatch [:my-event {...]})
  ; =>
  ; (fn [_ _] {:dispatch [:my-event {...]})
  ;
  ; @usage
  ; (metamorphic-handler->handler-f (fn [_ _] ...))
  ; =>
  ; (fn [_ _] ...})
  ;
  ; @return (function)
  [metamorphic-handler]
  (cond (map?    metamorphic-handler) (-> metamorphic-handler event-vector->handler-f)
        (vector? metamorphic-handler) (-> metamorphic-handler event-vector->handler-f)
        (fn?     metamorphic-handler) (fn [cofx event-vector] (metamorphic-event->effects-map (metamorphic-handler cofx event-vector)))
        :return  metamorphic-handler))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn apply-fx-params
  ; @description
  ; Applies the given side effect function using the provided parameters as a list of arguments;
  ; regardless of whether they are provided as a single value or multiple values within a vector.
  ;
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
