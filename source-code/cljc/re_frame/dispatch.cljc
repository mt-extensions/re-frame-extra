
(ns re-frame.dispatch
    (:require [re-frame.core          :as core]
              [re-frame.event-handler :as event-handler]
              [re-frame.event-vector  :as event-vector]
              [re-frame.metamorphic   :as metamorphic]
              [re-frame.registrar     :as registrar]
              [re-frame.state         :as state]
              [time.api               :as time]))

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
  (fn [_ [_ n]] (metamorphic/metamorphic-event->effects-map n)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn dispatch
  ; @param (metamorphic-event) event-handler
  ;
  ; @usage
  ; (dispatch [:foo])
  ;
  ; @usage
  ; (dispatch {:dispatch [:foo]})
  ;
  ; @usage
  ; (dispatch nil)
  [event-handler]

  ; Szerver-oldalon a Re-Frame nem jelez hibát, nem regisztrált esemény meghívásakor.
  ; A szerver-oldalon nem történnek meg a nem regisztrált Re-Frame események, ezért nem lehetséges
  ; interceptor-ban vizsgálni az események regisztráltságát.
  #?(:clj (let [event-id      (event-vector/event-vector->event-id      event-handler)
                event-exists? (event-handler/event-handler-registrated? :event event-id)]
               (if-not event-exists? (println "re-frame: no :event handler registrated for:" event-id))))

  (if (vector? event-handler) (core/dispatch event-handler)
                              (core/dispatch [:dispatch-metamorphic-event event-handler])))

; @usage
;  {:dispatch ...}
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
  ; @param (event-vector) event-handler
  ;
  ; @usage
  ; (dispatch-sync [...])
  ;
  ; A dispatch-sync függvény a meghívási sebesség fontossága miatt nem kezeli
  ; a metamorphic-event kezelőket!
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
;  {:dispatch-n [[...] [...]}
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
  ; Az eredeti dispatch-later függvény clojure környezetben nem időzíti a dispatch-later eseményeket!
  (doseq [{:keys [ms] :as effects-map} (remove nil? effects-map-list)]
         (if ms (letfn [(f [] (dispatch (dissoc effects-map :ms)))]
                       (time/set-timeout! f ms)))))

; @usage
;  {:dispatch-later [{...} {...}]}
(registrar/clear-handlers :fx            :dispatch-later)
(core/reg-fx              :dispatch-later dispatch-later)

(defn dispatch-if
  ; @param (*) condition
  ; @param (metamorphic-event) if-event-handler
  ; @param (metamorphic-event)(opt) else-event-handler
  ;
  ; @usage
  ; (dispatch-if [true [:my-event] ...])
  ;
  ; @usage
  ; (dispatch-if [true {:dispatch [:my-event]} ...])
  ;
  ; @usage
  ; (dispatch-if [true (fn [_ _] {:dispatch [:my-event]}) ...])
  [[condition if-event-handler else-event-handler]]
  (if condition (dispatch if-event-handler)
                (if else-event-handler (dispatch else-event-handler))))

; @usage
;  {:dispatch-if [...]}
(core/reg-fx :dispatch-if dispatch-if)

(defn dispatch-cond
  ; @param (vector) conditional-events
  ; [(*) condition
  ; (metamorphic-event) if-event-handler
  ; ...]
  ;
  ; @usage
  ; (dispatch-cond [(some? "a") [:my-event]
  ;                 (nil?  "b") [:my-event]])
  ;
  ; @usage
  ; (dispatch-cond [(some? "a") {:dispatch [:my-event]}
  ;                 (nil?  "b") {:dispatch [:my-event]}])
  ;
  ; @usage
  ; (dispatch-cond [(some? "a") (fn [_ _] {:dispatch [:my-event]})
  ;                 (nil?  "b") (fn [_ _] {:dispatch [:my-event]})])
  [conditional-events]
  (letfn [(dispatch-cond-f [_ dex x]
                           (if (and (even? dex) x)
                               (let [event (nth conditional-events (inc dex))]
                                    (dispatch event))))]
         (reduce-kv dispatch-cond-f nil conditional-events)))

; @usage
;  {:dispatch-cond [...]}
(core/reg-fx :dispatch-cond dispatch-cond)

;; -- Low sample-rate dispatch functions --------------------------------------
;; ----------------------------------------------------------------------------

; Ritkított futású esemény-indítók: dispatch-last, dispatch-once
;
; Az dispatch-last és dispatch-once függvények kizárólag event-vector
; formátumban átadott eseményeket kezelnek, ugyanis más metamorphic-event
; formátumok nem rendelkeznek kizárólagos azonosítási lehetősséggel.

(defn- reg-event-lock
  ; @param (integer) timeout
  ; @param (keyword) event-id
  ;
  ; @return (?)
  [timeout event-id]
  (let [elapsed-time (time/elapsed)
        unlock-time  (+ timeout elapsed-time)]
       (swap! state/EVENT-LOCKS assoc event-id unlock-time)))

(defn- event-unlocked?
  ; @param (keyword) event-id
  ;
  ; @return (boolean)
  [event-id]
  (let [elapsed-time (time/elapsed)
        unlock-time  (get @state/EVENT-LOCKS event-id)]
       (> elapsed-time unlock-time)))

(defn- dispatch-unlocked?!
  ; Dispatch event if it is NOT locked
  ;
  ; @param (event-vector) event-vector
  ;
  ; @return (?)
  [event-vector]
  (let [event-id (event-vector/event-vector->event-id event-vector)]
       (if (event-unlocked? event-id)
           (core/dispatch   event-vector))))

(defn- delayed-try
  ; @param (integer) timeout
  ; @param (event-vector) event-vector
  ;
  ; @return (?)
  [timeout event-vector]
  (let [event-id (event-vector/event-vector->event-id event-vector)]
       (if (event-unlocked? event-id)
           (do (core/dispatch  event-vector)
               (reg-event-lock timeout event-id)))))

(defn dispatch-last
  ; Blokkolja az esemény-meghívásokat mindaddig, amíg az utolsó esemény-meghívás
  ; után letelik a timeout. Ekkor az utolsó esemény-meghívást engedélyezi,
  ; az utolsó előttieket pedig figyelmen kívül hagyja.
  ;
  ; @param (integer) timeout
  ; @param (event-vector) event-vector
  ;
  ; @usage
  ; (dispatch-last 500 [:foo-bar-baz])
  ;
  ; @return (?)
  [timeout event-vector]
  (let [event-id (event-vector/event-vector->event-id event-vector)]
       (reg-event-lock    timeout event-id)
       (letfn [(f [] (dispatch-unlocked?! event-vector))]
              (time/set-timeout! f timeout))))

(defn dispatch-once
  ; A megadott intervallumonként egy - az utolsó - esemény-meghívást engedélyezi,
  ; a többit figyelmen kívül hagyja.
  ;
  ; @param (integer) interval
  ; @param (event-vector) event-vector
  ;
  ; @usage
  ; (dispatch-once 500 [:foo-bar-baz])
  ;
  ; @return (?)
  [interval event-vector]
  (let [event-id (event-vector/event-vector->event-id event-vector)]
       (if (event-unlocked? event-id)
           (do (core/dispatch  event-vector)
               (reg-event-lock interval event-id))
           (letfn [(f [] (delayed-try interval event-vector))]
                  (time/set-timeout! f interval)))))
