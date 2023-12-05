
(ns re-frame.handlers
    (:require [re-frame.core      :as core]
              [re-frame.dev.api   :as re-frame.dev]
              [re-frame.loggers   :refer [console]]
              [re-frame.registrar :as registrar]
              [re-frame.state     :as state]
              [re-frame.utilities :as utilities]
              [time.api           :as time]
              [vector.api         :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(core/reg-event-fx :dispatch-metamorphic-event
  ; @param (metamorphic-event) n
  ;
  ; @usage
  ; [:dispatch-metamorphic-event [...]]
  ;
  ; @usage
  ; [:dispatch-metamorphic-event {:dispatch [...]}]
  (fn [_ [_ n]] (utilities/metamorphic-event->effects-map n)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn dispatch
  ; @param (metamorphic-event) event-handler
  ;
  ; @usage
  ; (dispatch [:my-event])
  ;
  ; @usage
  ; (dispatch {:dispatch [:my-event]})
  ;
  ; @usage
  ; (dispatch (fn [] {:dispatch [:my-event]}))
  ;
  ; @usage
  ; (dispatch nil)
  [event-handler]
  ; By default the Re-Frame doesn't print errors on server-side if an event isn't registered when it is dispatched.
  (letfn [(check! [] (let [event-id      (utilities/event-vector->event-id event-handler)
                           event-exists? (re-frame.dev/event-handler-registered? :event event-id)]
                          (when-not event-exists? (println "re-frame: no :event handler registered for:" event-id)
                                                  (println event-handler))))]
         (if (vector? event-handler) #?(:clj (check!) :cljs nil))
         (if (vector? event-handler)         (core/dispatch event-handler)
                                             (core/dispatch [:dispatch-metamorphic-event event-handler]))))

; @usage
; {:dispatch ...}
(registrar/clear-handlers :fx      :dispatch)
(core/reg-fx              :dispatch dispatch)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn dispatch-f
  ; @description
  ; Applies the given 'f' function.
  ;
  ; @param (function) f
  ;
  ; @usage
  ; (dispatch-f (fn [] ...))
  [f]
  (f))

; @usage
; {:dispatch-f (fn [] ...)}
(core/reg-fx :dispatch-f dispatch-f)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn dispatch-fx
  ; @param (event-vector) event-handler
  ;
  ; @usage
  ; (dispatch-fx [:my-side-effect-event ...])
  [event-handler]
  (dispatch {:fx event-handler}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn dispatch-sync
  ; @description
  ; This function doesn't take metamoprhic handlers (for performance reasons).
  ;
  ; @param (event-vector) event-handler
  ;
  ; @usage
  ; (dispatch-sync [...])
  ;
  [event-handler]
  (core/dispatch-sync event-handler))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn dispatch-n
  ; @param (metamorphic-events in vector) event-list
  ;
  ; @usage
  ; (dispatch-n [[:event-a]
  ;              {:dispatch [:event-b]}
  ;              (fn [_ _] {:dispatch [:event-c]})])
  [event-list]
  (doseq [event (remove nil? event-list)]
         (dispatch event)))

; @usage
; {:dispatch-n [[...] [...]}
(registrar/clear-handlers :fx        :dispatch-n)
(core/reg-fx              :dispatch-n dispatch-n)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn dispatch-later
  ; @param (maps in vector) effects-map-list
  ;
  ; @usage
  ; (dispatch-later [{:ms 500 :dispatch [...]}
  ;                  {:ms 600 :fx [...]
  ;                           :fx-n       [[...] [...]]
  ;                           :dispatch-n [[...] [...]]}])
  [effects-map-list]
  ; The original dispatch-later function of Re-Frame doesn't set a timeout on
  ; events in server-side (Clojure) environment.
  (doseq [{:keys [ms] :as effects-map} (remove nil? effects-map-list)]
         (if ms (letfn [(f0 [] (dispatch (dissoc effects-map :ms)))]
                       (time/set-timeout! f0 ms)))))

; @usage
; {:dispatch-later [{...} {...}]}
(registrar/clear-handlers :fx            :dispatch-later)
(core/reg-fx              :dispatch-later dispatch-later)

;; -- Low sample-rate dispatch functions --------------------------------------
;; ----------------------------------------------------------------------------

(defn- reg-event-lock!
  ; @ignore
  ;
  ; @param (integer) timeout
  ; @param (keyword) event-id
  ;
  ; @return (?)
  [timeout event-id]
  (let [elapsed-time (time/elapsed)
        unlock-time  (+ timeout elapsed-time)]
       (swap! state/EVENT-LOCKS assoc event-id unlock-time)))

(defn- event-unlocked?
  ; @ignore
  ;
  ; @param (keyword) event-id
  ;
  ; @return (boolean)
  [event-id]
  (let [elapsed-time (time/elapsed)
        unlock-time  (get @state/EVENT-LOCKS event-id)]
       (> elapsed-time unlock-time)))

(defn- dispatch-unlocked?!
  ; @ignore
  ;
  ; @description
  ; Dispatches the event if it's NOT locked.
  ;
  ; @param (event-vector) event-vector
  ;
  ; @return (?)
  [event-vector]
  (if (-> event-vector utilities/event-vector->event-id event-unlocked?)
      (core/dispatch event-vector)))

(defn- delayed-try
  ; @ignore
  ;
  ; @param (integer) timeout
  ; @param (event-vector) event-vector
  ;
  ; @return (?)
  [timeout event-vector]
  (let [event-id (utilities/event-vector->event-id event-vector)]
       (when (event-unlocked? event-id)
             (core/dispatch   event-vector)
             (reg-event-lock! timeout event-id))))

(defn dispatch-last
  ; @warning
  ; The 'dispatch-last' function only handles standard event vectors, because
  ; the metamorphic events don't have unique identifiers!
  ;
  ; @description
  ; The 'dispatch-last' function only fires an event if you stop calling it
  ; at least for the given timeout.
  ; It ignores dispatching the event until the timout elapsed since the last calling.
  ;
  ; @param (integer) timeout
  ; @param (event-vector) event-vector
  ;
  ; @usage
  ; (dispatch-last 500 [:my-event])
  ;
  ; @return (?)
  [timeout event-vector]
  (let [event-id (utilities/event-vector->event-id event-vector)]
       (reg-event-lock! timeout event-id)
       (letfn [(f0 [] (dispatch-unlocked?! event-vector))]
              (time/set-timeout! f0 timeout))))

(defn dispatch-once
  ; @warning
  ; The 'dispatch-once' function only handles standard event vectors, because
  ; a metamorphic event doesn't have unique identifier!
  ;
  ; @description
  ; The 'dispatch-once' function only fires an event once in the given interval.
  ; It ignores dispatching the event except one time per interval.
  ;
  ; @param (integer) interval
  ; @param (event-vector) event-vector
  ;
  ; @usage
  ; (dispatch-once 500 [:my-event])
  ;
  ; @return (?)
  [interval event-vector]
  (let [event-id (utilities/event-vector->event-id event-vector)]
       (if (event-unlocked? event-id)
           (do (core/dispatch event-vector)
               (reg-event-lock! interval event-id))
           (letfn [(f0 [] (delayed-try interval event-vector))]
                  (time/set-timeout! f0 interval)))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn dispatch-tick
  ; @param (maps in vector) effects-maps-vector
  ; [{ ... }
  ;  {:tick 10
  ;   :dispatch       [:my-event]
  ;   :dispatch-n     [[:my-event]]
  ;   :dispatch-later [ ... ]}
  ;  { ... }]
  ;
  ; @usage
  ; (dispatch-tick [{:tick 42 :dispatch [:my-event]}])
  [effects-maps-vector]
  (core/dispatch [:dispatch-tick effects-maps-vector]))

; @usage
; (reg-event-fx :my-event
;   (fn [_ _]
;       {:dispatch-tick [{:dispatch [:my-event]
;                         :tick     10}]}))
(core/reg-fx :dispatch-tick dispatch-tick)

(core/reg-event-fx :dispatch-tick
  ; @param (maps in vector) effects-maps-vector
  ; [{ ... }
  ;  {:tick 10
  ;   :dispatch       [:my-event]
  ;   :dispatch-n     [[:my-event]]
  ;   :dispatch-later [ ... ]}
  (fn [_ [_ effects-maps-vector]]
      (letfn [(f0 [merged-effects-map effects-map]
                  (if ; Tick now?
                      (-> effects-map :tick zero?)
                      ; Tick now!
                      (utilities/merge-effects-maps merged-effects-map effects-map)
                      ; Tick later!
                      (update merged-effects-map :dispatch-tick vector/conj-item (update effects-map :tick dec))))]
             (reduce f0 {} effects-maps-vector))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn fx
  ; @param (vector) effect-vector
  ;
  ; @usage
  ; (reg-fx :my-side-effect (fn [a b c]))
  ; (fx [:my-side-effect "a" "b" "c"])
  [[effect-id & params :as effect-vector]]
  (when (= :db effect-id)
        (console :warn "re-frame: \":fx\" effect should not contain a :db effect"))
  ; This function ...
  ; ... ignores dispatching the effect when the effect-vector is NIL.
  ; ... warns when the effect-vector is NOT NIL, but no registered effect found with the given ID.
  (if effect-vector (if-let [effect-f (registrar/get-handler :fx effect-id false)]
                            (effect-f params)
                            (console :warn "re-frame: in \":fx\" effect found " effect-id " which has no associated handler. Ignoring."))))

; @usage
; {:fx [...]}
(registrar/clear-handlers :fx :fx)
(core/reg-fx              :fx  fx)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn fx-n
  ; @param (vectors in vector) effect-vector-list
  ;
  ; @usage
  ; (reg-fx :my-side-effect (fn [a b c]))
  ; (fx-n [[:my-side-effect "a" "b" "c"]
  ;        [...]])
  [effect-vector-list]
  (if-not (sequential? effect-vector-list)
          (console :warn "re-frame: \":fx\" effect expects a seq, but was given " (type effect-vector-list))
          (doseq [effect-vector (remove nil? effect-vector-list)]
                 (fx effect-vector))))

; @usage
; {:fx-n [[...] [...]]}
(core/reg-fx :fx-n fx-n)
