
; Namespace re-frame.core.dispatch clashes with var re-frame.core/dispatch

(ns re-frame.core.dispatch-set
    (:require [re-frame.core       :as core]
              [re-frame.core.env   :as env]
              [re-frame.core.state :as state]
              [re-frame.core.utils :as utils]
              [re-frame.registrar  :as registrar]
              [time.api            :as time]
              [vector.api          :as vector]))

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
  (fn [_ [_ n]] (utils/metamorphic-event->effects-map n)))

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
  ; By default the Re-Frame doesn't print errors on server-side when an event doesn't
  ; registered when it dispatched.
  (letfn [(check! [] (let [event-id      (utils/event-vector->event-id event-handler)
                           event-exists? (env/event-handler-registered? :event event-id)]
                          (when-not event-exists? (println "re-frame: no :event handler registered for:" event-id))))]
         (if (vector? event-handler) #?(:clj (check!) :cljs nil))
         (if (vector? event-handler)         (core/dispatch event-handler)
                                             (core/dispatch [:dispatch-metamorphic-event event-handler]))))

; @usage
; {:dispatch ...}
(registrar/clear-handlers :fx      :dispatch)
(core/reg-fx              :dispatch dispatch)

(defn dispatch-fx
  ; @param (event-vector) event-handler
  ;
  ; @usage
  ; (dispatch-fx [:my-side-effect-event ...])
  [event-handler]
  (dispatch {:fx event-handler}))

(defn dispatch-sync
  ; @description
  ; This function doesn't take metamoprhic handler (for performance reasons).
  ;
  ; @param (event-vector) event-handler
  ;
  ; @usage
  ; (dispatch-sync [...])
  ;
  [event-handler]
  (core/dispatch-sync event-handler))

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
         (if ms (letfn [(f [] (dispatch (dissoc effects-map :ms)))]
                       (time/set-timeout! f ms)))))

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
  (if (-> event-vector utils/event-vector->event-id event-unlocked?)
      (core/dispatch event-vector)))

(defn- delayed-try
  ; @ignore
  ;
  ; @param (integer) timeout
  ; @param (event-vector) event-vector
  ;
  ; @return (?)
  [timeout event-vector]
  (let [event-id (utils/event-vector->event-id event-vector)]
       (when (event-unlocked? event-id)
             (core/dispatch   event-vector)
             (reg-event-lock! timeout event-id))))

(defn dispatch-last
  ; @Warning
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
  (let [event-id (utils/event-vector->event-id event-vector)]
       (reg-event-lock! timeout event-id)
       (letfn [(f [] (dispatch-unlocked?! event-vector))]
              (time/set-timeout! f timeout))))

(defn dispatch-once
  ; @Warning
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
  (let [event-id (utils/event-vector->event-id event-vector)]
       (if (event-unlocked? event-id)
           (do (core/dispatch event-vector)
               (reg-event-lock! interval event-id))
           (letfn [(f [] (delayed-try interval event-vector))]
                  (time/set-timeout! f interval)))))

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
      (letfn [(f [merged-effects-map effects-map]
                 (if
                     ; Tick now?
                     (= 0 (:tick effects-map))

                     ; Tick now!
                     (utils/merge-effects-maps merged-effects-map effects-map)

                     ; Tick later!
                     (update merged-effects-map :dispatch-tick vector/conj-item (update effects-map :tick dec))))]

             (reduce f {} effects-maps-vector))))