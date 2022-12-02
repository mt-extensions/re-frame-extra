
# <strong>re-frame.api</strong> namespace

<strong>[README](../../../README.md) > [DOCUMENTATION](../../COVER.md) > </strong>source-code/cljc/re_frame/api.cljc

### apply-fx-params

```
@param (function) handler-f
@param (* or vector) params
```

```
@usage
(apply-fx-params (fn [a] ...) "a")
```

```
@usage
(apply-fx-params (fn [a] ...) ["a"])
```

```
@usage
(apply-fx-params (fn [a b] ...) ["a" "b"])
```

```
@return (*)
```

<details>
<summary>Source code</summary>

```
(defn apply-fx-params
  [handler-f params]
  (if (sequential?     params)
      (apply handler-f params)
      (handler-f       params)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [apply-fx-params]]))

(re-frame.api/apply-fx-params ...)
(apply-fx-params              ...)
```

</details>

---

### cofx->event-id

```
@param (map) cofx
{:event (vector)
  [(keyword) event-id]}
```

```
@example
(cofx->event-id {:event [:my-event ...]})
=>
:my-event
```

```
@return (keyword)
```

<details>
<summary>Source code</summary>

```
(defn cofx->event-id
  [cofx]
  (get-in cofx [:event 0]))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [cofx->event-id]]))

(re-frame.api/cofx->event-id ...)
(cofx->event-id              ...)
```

</details>

---

### cofx->event-vector

```
@param (map) cofx
{:event (vector)}
```

```
@example
(cofx->event-vector {:event [...]})
=>
[...]
```

```
@return (vector)
```

<details>
<summary>Source code</summary>

```
(defn cofx->event-vector
  [cofx]
  (get cofx :event))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [cofx->event-vector]]))

(re-frame.api/cofx->event-vector ...)
(cofx->event-vector              ...)
```

</details>

---

### context->db-after-effect

```
@param (map) context
{:effects (map)
 {:db (map)}}
```

```
@example
(context->db-after-effect {:effects {:db {...}}})
=>
{...}
```

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn context->db-after-effect
  [context]
  (get-in context [:effects :db]))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [context->db-after-effect]]))

(re-frame.api/context->db-after-effect ...)
(context->db-after-effect              ...)
```

</details>

---

### context->db-before-effect

```
@param (map) context
{:coeffects (map)
 {:db (map)}}
```

```
@example
(context->db-before-effect {:coeffects {:db {...}}})
=>
{...}
```

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn context->db-before-effect
  [context]
  (get-in context [:coeffects :db]))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [context->db-before-effect]]))

(re-frame.api/context->db-before-effect ...)
(context->db-before-effect              ...)
```

</details>

---

### context->event-id

```
@param (map) context
{:coeffects (map)
 {:event (vector)}}
```

```
@example
(context->event-id {:coeffects {:event [:my-event ...]}})
=>
:my-event
```

```
@return (keyword)
```

<details>
<summary>Source code</summary>

```
(defn context->event-id
  [context]
  (-> context context->event-vector event-vector/event-vector->event-id))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [context->event-id]]))

(re-frame.api/context->event-id ...)
(context->event-id              ...)
```

</details>

---

### context->event-vector

```
@param (map) context
{:coeffects (map)
 {:event (vector)}}
```

```
@example
(context->event-vector {:coeffects {:event [:my-event ...]}})
=>
[:my-event ...]
```

```
@return (vector)
```

<details>
<summary>Source code</summary>

```
(defn context->event-vector
  [context]
  (get-in context [:coeffects :event]))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [context->event-vector]]))

(re-frame.api/context->event-vector ...)
(context->event-vector              ...)
```

</details>

---

### dispatch

```
@param (metamorphic-event) event-handler
```

```
@usage
(dispatch [:my-event])
```

```
@usage
(dispatch {:dispatch [:my-event]})
```

```
@usage
(dispatch (fn [] {:dispatch [:my-event]}))
```

```
@usage
(dispatch nil)
```

<details>
<summary>Source code</summary>

```
(defn dispatch
  [event-handler]
  (letfn [(check! [] (let [event-id      (event-vector/event-vector->event-id      event-handler)
                           event-exists? (event-handler/event-handler-registrated? :event event-id)]
                          (when-not event-exists? (println "re-frame: no :event handler registrated for:" event-id))))]
         (if (vector? event-handler) #?(:clj (check!) :cljs nil))
         (if (vector? event-handler)         (core/dispatch event-handler)
                                             (core/dispatch [:dispatch-metamorphic-event event-handler]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [dispatch]]))

(re-frame.api/dispatch ...)
(dispatch              ...)
```

</details>

---

### dispatch-cond

```
@param (vector) conditional-events
[(*) condition
(metamorphic-event) if-event-handler
...]
```

```
@usage
(dispatch-cond [(some? "a") [:my-event]
                (nil?  "b") [:my-event]])
```

```
@usage
(dispatch-cond [(some? "a") {:dispatch [:my-event]}
                (nil?  "b") {:dispatch [:my-event]}])
```

```
@usage
(dispatch-cond [(some? "a") (fn [_ _] {:dispatch [:my-event]})
                (nil?  "b") (fn [_ _] {:dispatch [:my-event]})])
```

<details>
<summary>Source code</summary>

```
(defn dispatch-cond
  [conditional-events]
  (letfn [(dispatch-cond-f [_ dex x]
                           (if (and (even? dex) x)
                               (let [event (nth conditional-events (inc dex))]
                                    (dispatch event))))]
         (reduce-kv dispatch-cond-f nil conditional-events)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [dispatch-cond]]))

(re-frame.api/dispatch-cond ...)
(dispatch-cond              ...)
```

</details>

---

### dispatch-fx

```
@param (event-vector) event-handler
```

```
@usage
(dispatch-fx [:my-side-effect-event ...])
```

<details>
<summary>Source code</summary>

```
(defn dispatch-fx
  [event-handler]
  (dispatch {:fx event-handler}))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [dispatch-fx]]))

(re-frame.api/dispatch-fx ...)
(dispatch-fx              ...)
```

</details>

---

### dispatch-if

```
@param (*) condition
@param (metamorphic-event) if-event-handler
@param (metamorphic-event)(opt) else-event-handler
```

```
@usage
(dispatch-if [true [:my-event] ...])
```

```
@usage
(dispatch-if [true {:dispatch [:my-event]} ...])
```

```
@usage
(dispatch-if [true (fn [_ _] {:dispatch [:my-event]}) ...])
```

<details>
<summary>Source code</summary>

```
(defn dispatch-if
  [[condition if-event-handler else-event-handler]]
  (if condition (dispatch if-event-handler)
                (if else-event-handler (dispatch else-event-handler))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [dispatch-if]]))

(re-frame.api/dispatch-if ...)
(dispatch-if              ...)
```

</details>

---

### dispatch-last

```
@param (integer) timeout
@param (event-vector) event-vector
```

```
@usage
(dispatch-last 500 [:my-event])
```

```
@return (?)
```

<details>
<summary>Source code</summary>

```
(defn dispatch-last
  [timeout event-vector]
  (let [event-id (event-vector/event-vector->event-id event-vector)]
       (reg-event-lock    timeout event-id)
       (letfn [(f [] (dispatch-unlocked?! event-vector))]
              (time/set-timeout! f timeout))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [dispatch-last]]))

(re-frame.api/dispatch-last ...)
(dispatch-last              ...)
```

</details>

---

### dispatch-later

```
@param (maps in vector) effects-map-list
```

```
@usage
(dispatch-later [{:ms 500 :dispatch [...]}
                 {:ms 600 :fx [...]
                          :fx-n       [[...] [...]]
                          :dispatch-n [[...] [...]]}])
```

<details>
<summary>Source code</summary>

```
(defn dispatch-later
  [effects-map-list]
  (doseq [{:keys [ms] :as effects-map} (remove nil? effects-map-list)]
         (if ms (letfn [(f [] (dispatch (dissoc effects-map :ms)))]
                       (time/set-timeout! f ms)))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [dispatch-later]]))

(re-frame.api/dispatch-later ...)
(dispatch-later              ...)
```

</details>

---

### dispatch-n

```
@param (metamorphic-events in vector) event-list
```

```
@usage
(dispatch-n [[:event-a]
             {:dispatch [:event-b]}
             (fn [_ _] {:dispatch [:event-c]})])
```

<details>
<summary>Source code</summary>

```
(defn dispatch-n
  [event-list]
  (doseq [event (remove nil? event-list)]
         (dispatch event)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [dispatch-n]]))

(re-frame.api/dispatch-n ...)
(dispatch-n              ...)
```

</details>

---

### dispatch-once

```
@param (integer) interval
@param (event-vector) event-vector
```

```
@usage
(dispatch-once 500 [:my-event])
```

```
@return (?)
```

<details>
<summary>Source code</summary>

```
(defn dispatch-once
  [interval event-vector]
  (let [event-id (event-vector/event-vector->event-id event-vector)]
       (if (event-unlocked? event-id)
           (do (core/dispatch  event-vector)
               (reg-event-lock interval event-id))
           (letfn [(f [] (delayed-try interval event-vector))]
                  (time/set-timeout! f interval)))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [dispatch-once]]))

(re-frame.api/dispatch-once ...)
(dispatch-once              ...)
```

</details>

---

### dispatch-sync

```
@param (event-vector) event-handler
```

```
@usage
(dispatch-sync [...])
  ;
```

<details>
<summary>Source code</summary>

```
(defn dispatch-sync
  [event-handler]
  (core/dispatch-sync event-handler))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [dispatch-sync]]))

(re-frame.api/dispatch-sync ...)
(dispatch-sync              ...)
```

</details>

---

### dispatch-tick

```
@param (maps in vector) effects-maps-vector
[{ ... }
 {:tick 10
  :dispatch       [:my-event]
  :dispatch-n     [[:my-event]]
  :dispatch-later [ ... ]}
 { ... }]
```

```
@usage
(dispatch-tick [{:tick 42 :dispatch [:my-event]}])
```

<details>
<summary>Source code</summary>

```
(defn dispatch-tick
  [effects-maps-vector]
  (core/dispatch [:dispatch-tick effects-maps-vector]))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [dispatch-tick]]))

(re-frame.api/dispatch-tick ...)
(dispatch-tick              ...)
```

</details>

---

### effects-map->handler-f

```
@param (map) effects-map
```

```
@example
(effects-map->handler-f {:dispatch [...]})
=>
(fn [_ _] {:dispatch [...]})
```

```
@return (function)
```

<details>
<summary>Source code</summary>

```
(defn effects-map->handler-f
  [effects-map]
  (fn [_ _] effects-map))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [effects-map->handler-f]]))

(re-frame.api/effects-map->handler-f ...)
(effects-map->handler-f              ...)
```

</details>

---

### effects-map<-event

```
@param (map) effects-map
@param (vector) event-vector
```

```
@example
(effects-map<-event {:dispatch [:my-event]} [:your-event])
=>
{:dispatch [:my-event] :dispatch-n [[:your-event]]}
```

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn effects-map<-event
  [effects-map event-vector]
  (update effects-map :dispatch-n vector/conj-item event-vector))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [effects-map<-event]]))

(re-frame.api/effects-map<-event ...)
(effects-map<-event              ...)
```

</details>

---

### event-handler-registrated?

```
@param (keyword) event-kind
:cofx, :event, :fx, :sub
@param (keyword) event-id
```

```
@usage
(event-handler-registrated? :sub :my-subscription)
```

```
@return (function)
```

<details>
<summary>Source code</summary>

```
(defn event-handler-registrated?
  [event-kind event-id]
  (-> (get-event-handler event-kind event-id)
      (some?)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [event-handler-registrated?]]))

(re-frame.api/event-handler-registrated? ...)
(event-handler-registrated?              ...)
```

</details>

---

### event-vector->effects-map

```
@param (vector) event-vector
```

```
@example
(event-vector->effects-map [...])
=>
{:dispatch [...]}
```

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn event-vector->effects-map
  [event-vector]
  {:dispatch event-vector})
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [event-vector->effects-map]]))

(re-frame.api/event-vector->effects-map ...)
(event-vector->effects-map              ...)
```

</details>

---

### event-vector->event-id

```
@param (vector) event-vector
```

```
@example
(event-vector->event-id [:my-event ...])
=>
:my-event
```

```
@return (vector)
```

<details>
<summary>Source code</summary>

```
(defn event-vector->event-id
  [event-vector]
  (first event-vector))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [event-vector->event-id]]))

(re-frame.api/event-vector->event-id ...)
(event-vector->event-id              ...)
```

</details>

---

### event-vector->handler-f

```
@param (vector) event-vector
```

```
@example
(event-vector->handler-f [...])
=>
(fn [_ _] {:dispatch [...]})
```

```
@return (function)
```

<details>
<summary>Source code</summary>

```
(defn event-vector->handler-f
  [event-vector]
  (fn [_ _] {:dispatch event-vector}))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [event-vector->handler-f]]))

(re-frame.api/event-vector->handler-f ...)
(event-vector->handler-f              ...)
```

</details>

---

### event-vector<-id-f

```
@param (map) context
```

```
@usage
(event-vector<-id-f {...})
```

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn event-vector<-id-f
  [context]
  (letfn [(f             [event-vector]
             (if (->     event-vector second keyword?)
                 (return event-vector)
                 (vec (concat [(first event-vector) (random/generate-keyword)] (rest event-vector)))))]
         (update-in context [:coeffects :event] f)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [event-vector<-id-f]]))

(re-frame.api/event-vector<-id-f ...)
(event-vector<-id-f              ...)
```

</details>

---

### event-vector<-params

```
@param (event-vector) n
@param (list of *) params
```

```
@example
(event-vector<-params [:my-event] "My param" "Your param")
=>
[:my-event "My param" "Your param"]
```

```
@return (event-vector)
```

<details>
<summary>Source code</summary>

```
(defn event-vector<-params
  [n & params]
  (vector/concat-items n params))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [event-vector<-params]]))

(re-frame.api/event-vector<-params ...)
(event-vector<-params              ...)
```

</details>

---

### event-vector?

```
@param (*) n
```

```
@example
(event-vector? [:my-event ...])
=>
true
```

```
@return (boolean)
```

<details>
<summary>Source code</summary>

```
(defn event-vector?
  [n]
  (and (-> n vector?)
       (-> n first keyword?)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [event-vector?]]))

(re-frame.api/event-vector? ...)
(event-vector?              ...)
```

</details>

---

### fx

```
@param (vector) effect-vector
```

```
@usage
(reg-fx :my-side-effect (fn [a b c]))
(fx [:my-side-effect "a" "b" "c"])
```

<details>
<summary>Source code</summary>

```
(defn fx
  [[effect-id & params :as effect-vector]]
  (when (= :db effect-id)
        (console :warn "re-frame: \":fx\" effect should not contain a :db effect"))
  (if-let [effect-f (registrar/get-handler :fx effect-id false)]
          (effect-f params)
          (console :warn "re-frame: in \":fx\" effect found " effect-id " which has no associated handler. Ignoring.")))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [fx]]))

(re-frame.api/fx ...)
(fx              ...)
```

</details>

---

### fx-n

```
@param (vectors in vector) effect-vector-list
```

```
@usage
(reg-fx :my-side-effect (fn [a b c]))
(fx-n [[:my-side-effect "a" "b" "c"]
       [...]])
```

<details>
<summary>Source code</summary>

```
(defn fx-n
  [effect-vector-list]
  (if-not (sequential? effect-vector-list)
          (console :warn "re-frame: \":fx\" effect expects a seq, but was given " (type effect-vector-list))
          (doseq [effect-vector (remove nil? effect-vector-list)]
                 (fx effect-vector))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [fx-n]]))

(re-frame.api/fx-n ...)
(fx-n              ...)
```

</details>

---

### get-event-handler

```
@param (keyword) event-kind
:cofx, :event, :fx, :sub
@param (keyword) event-id
```

```
@usage
(get-event-handler :sub :my-subscription)
```

```
@return (maps in list)
```

<details>
<summary>Source code</summary>

```
(defn get-event-handler
  [event-kind event-id]
  (-> (get-event-handlers)
      (get-in [event-kind event-id])))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [get-event-handler]]))

(re-frame.api/get-event-handler ...)
(get-event-handler              ...)
```

</details>

---

### get-event-handlers

```
@param (keyword)(opt) event-kind
```

```
@usage
(get-event-handlers)
```

```
@usage
(get-event-handlers :sub)
```

```
@return (map)
{:cofx (map)
 :event (map)
 :fx (map)
 :sub (map)}
```

<details>
<summary>Source code</summary>

```
(defn get-event-handlers
  ([]                       (deref registrar/kind->id->handler))
  ([event-kind] (event-kind (deref registrar/kind->id->handler))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [get-event-handlers]]))

(re-frame.api/get-event-handlers ...)
(get-event-handlers              ...)
```

</details>

---

### inject-cofx

```
@param (keyword) handler-id
@param (*) param
```

```
@usage
(inject-cofx :my-handler)
```

```
@usage
(inject-cofx :my-handler "My param")
```

<details>
<summary>Source code</summary>

```
(defn inject-cofx
  ([handler-id]
   (core/inject-cofx handler-id))

  ([handler-id param]
   (core/inject-cofx handler-id param)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [inject-cofx]]))

(re-frame.api/inject-cofx ...)
(inject-cofx              ...)
```

</details>

---

### merge-effects-maps

```
@param (map) a
@param (map) b
```

```
@example
(merge-effects-maps {:dispatch [:a1]
                     :dispatch-n [[:a2] [:a3]}]}
                    {:dispatch [:b1]
                     :dispatch-n [[:b2]]})
=>
{:dispatch [:a1] :dispatch-n [[:a2] [:a3] [:b1] [:b2]]}
```

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn merge-effects-maps
  [a b]
  (-> a (update-some :fx-n           vector/conj-item    (:fx             b))
        (update-some :fx-n           vector/concat-items (:fx-n           b))
        (update-some :dispatch-n     vector/conj-item    (:dispatch       b))
        (update-some :dispatch-n     vector/concat-items (:dispatch-n     b))
        (update-some :dispatch-later vector/concat-items (:dispatch-later b))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [merge-effects-maps]]))

(re-frame.api/merge-effects-maps ...)
(merge-effects-maps              ...)
```

</details>

---

### metamorphic-event->effects-map

```
@param (metamorphic-event) n
```

```
@example
(metamorphic-event->effects-map [:my-event])
=>
{:dispatch [:my-event]}
```

```
@example
(metamorphic-event->effects-map {:dispatch [:my-event])
=>
{:dispatch [:my-event]}
```

```
@example
(metamorphic-event->effects-map (fn [] {:dispatch [:my-event]))
=>
{:dispatch [:my-event]}
```

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn metamorphic-event->effects-map
  [n]
  (cond (vector? n) (event-vector/event-vector->effects-map n)
        (map?    n) (return                                 n)
        (fn?     n) (metamorphic-event->effects-map        (n))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [metamorphic-event->effects-map]]))

(re-frame.api/metamorphic-event->effects-map ...)
(metamorphic-event->effects-map              ...)
```

</details>

---

### metamorphic-event<-params

```
@param (metamorphic-event) n
@param (list of *) params
```

```
@example
(metamorphic-event<-params [:my-event] "My param" "Your param")
=>
[:my-event "My param" "Your param"]
```

```
@example
(metamorphic-event<-params {:dispatch [:my-event]} "My param" "Your param")
=>
{:dispatch [:my-event "My param" "Your param"]}
```

```
@return (metamorphic-event)
```

<details>
<summary>Source code</summary>

```
(defn metamorphic-event<-params
  [n & params]
  (cond (types/event-vector? n) (vector/concat-items n params)
        (map?                n) (map/->values        n #(apply metamorphic-event<-params % params))
        :return              n))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [metamorphic-event<-params]]))

(re-frame.api/metamorphic-event<-params ...)
(metamorphic-event<-params              ...)
```

</details>

---

### metamorphic-handler->handler-f

```
@param (metamorphic-event) n
```

```
@example
(metamorphic-handler->handler-f [...])
=>
(fn [_ _] {:dispatch [...]})
```

```
@example
(metamorphic-handler->handler-f {:dispatch [...]})
=>
(fn [_ _] {:dispatch [...]})
```

```
@example
(metamorphic-handler->handler-f (fn [_ _] ...))
=>
(fn [_ _] ...})
```

```
@return (function)
```

<details>
<summary>Source code</summary>

```
(defn metamorphic-handler->handler-f
  [n]
  (cond (map?    n) (effects-map/effects-map->handler-f   n)
        (vector? n) (event-vector/event-vector->handler-f n)
        :return  n))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [metamorphic-handler->handler-f]]))

(re-frame.api/metamorphic-handler->handler-f ...)
(metamorphic-handler->handler-f              ...)
```

</details>

---

### query-vector?

```
@param (*) n
```

```
@example
(query-vector? [:my-namespace/get-something ...])
=>
true
```

```
@example
(query-vector? [:my-namespace/something-happened? ...])
=>
true
```

```
@example
(query-vector? [:div ...])
=>
false
```

```
@return (boolean)
```

<details>
<summary>Source code</summary>

```
(defn query-vector?
  [n]
  (and (-> n vector?)
       (and (-> n first keyword?)
            (or (-> n first name (string/starts-with? "get-"))
                (-> n first name (string/ends-with?   "?"))))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [query-vector?]]))

(re-frame.api/query-vector? ...)
(query-vector?              ...)
```

</details>

---

### quit-debug-mode!

```
@usage
(quit-debug-mode!)
```

<details>
<summary>Source code</summary>

```
(defn quit-debug-mode!
  []
  (reset! state/DEBUG-MODE? false))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [quit-debug-mode!]]))

(re-frame.api/quit-debug-mode!)
(quit-debug-mode!)
```

</details>

---

### r

```
@param (function) f
@param (*) params
```

```
@example
(r db/remove-item! db [:my-item])
=>
(db/remove-item! db [nil [:my-item]])
```

```
@return (*)
```

<details>
<summary>Source code</summary>

```
(defn r
  [f & params]
  (let [context      (first params)
        event-vector (vector/cons-item (rest params) nil)]
       (f context event-vector)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [r]]))

(re-frame.api/r ...)
(r              ...)
```

</details>

---

### reg-cofx

```
@param (keyword) event-id
@param (metamorphic-event) event-handler
```

```
@usage
(defn my-handler-f [cofx _])
(reg-cofx :my-event my-handler-f)
```

<details>
<summary>Source code</summary>

```
(defn reg-cofx
  [event-id event-handler]
  (core/reg-cofx event-id event-handler))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [reg-cofx]]))

(re-frame.api/reg-cofx ...)
(reg-cofx              ...)
```

</details>

---

### reg-event-db

```
@param (keyword) event-id
@param (vector)(opt) interceptors
@param (metamorphic-event) event-handler
```

```
@usage
(defn my-handler-f [db _])
(reg-event-db :my-event my-handler-f)
```

```
@usage
(defn my-handler-f [db _])
(reg-event-db :my-event [...] my-handler-f)
```

<details>
<summary>Source code</summary>

```
(defn reg-event-db
  ([event-id event-handler]
   (reg-event-db event-id nil event-handler))

  ([event-id interceptors event-handler]
   (let [interceptors (interceptors<-system-interceptors interceptors)]
        (core/reg-event-db event-id interceptors event-handler))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [reg-event-db]]))

(re-frame.api/reg-event-db ...)
(reg-event-db              ...)
```

</details>

---

### reg-event-fx

```
@param (keyword) event-id
@param (vector)(opt) interceptors
@param (metamorphic-event) event-handler
```

```
@usage
(reg-event-fx :my-event [:your-event])
```

```
@usage
(reg-event-fx :my-event {:dispatch [:your-event]})
```

```
@usage
(reg-event-fx :my-event (fn [cofx event-vector] [:your-event]))
```

```
@usage
(reg-event-fx :my-event (fn [cofx event-vector] {:dispatch [:your-event]}))
```

<details>
<summary>Source code</summary>

```
(defn reg-event-fx
  ([event-id event-handler]
   (reg-event-fx event-id nil event-handler))

  ([event-id interceptors event-handler]
   (let [handler-f    (metamorphic/metamorphic-handler->handler-f event-handler)
         interceptors (interceptors<-system-interceptors          interceptors)]
        (core/reg-event-fx event-id interceptors #(metamorphic/metamorphic-event->effects-map (handler-f %1 %2))))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [reg-event-fx]]))

(re-frame.api/reg-event-fx ...)
(reg-event-fx              ...)
```

</details>

---

### reg-fx

```
@param (keyword) event-id
@param (function) handler-f
```

```
@usage
(defn my-side-effect-f [a])
(reg-fx       :my-side-effect my-side-effect-f)
(reg-event-fx :my-effect {:my-my-side-effect-f "A"})
```

```
@usage
(defn your-side-effect-f [a b])
(reg-fx       :your-side-effect your-side-effect-f)
(reg-event-fx :your-effect {:your-my-side-effect-f ["a" "b"]})
```

<details>
<summary>Source code</summary>

```
(defn reg-fx
  [event-id handler-f]
  (core/reg-fx event-id #(apply-fx-params handler-f %)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [reg-fx]]))

(re-frame.api/reg-fx ...)
(reg-fx              ...)
```

</details>

---

### reg-sub

```
@param (keyword) query-id
@param (list of functions) fs
```

```
@usage
(defn my-handler-f [db _])
(reg-sub :my-subscription my-handler-f)
```

```
@usage
(defn my-signal-f      [db _])
(defn your-signal-f    [db _])
(defn my-computation-f [[my-signal your-signal] _])
(reg-sub :my-subscription my-signal-f your-signal-f my-computation-f)
```

<details>
<summary>Source code</summary>

```
(defn reg-sub
  [query-id & fs]
  (apply core/reg-sub query-id fs))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [reg-sub]]))

(re-frame.api/reg-sub ...)
(reg-sub              ...)
```

</details>

---

### set-debug-mode!

```
@usage
(set-debug-mode!)
```

<details>
<summary>Source code</summary>

```
(defn set-debug-mode!
  []
  (reset! state/DEBUG-MODE? true))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [set-debug-mode!]]))

(re-frame.api/set-debug-mode!)
(set-debug-mode!)
```

</details>

---

### subscribe

```
@param (vector) query-vector
```

```
@usage
(subscribe [:my-subscription])
```

```
@return (atom)
```

<details>
<summary>Source code</summary>

```
(defn subscribe
  [query-vector]
  (core/subscribe query-vector))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [subscribe]]))

(re-frame.api/subscribe ...)
(subscribe              ...)
```

</details>

---

### subscribed

```
@param (vector) query-vector
```

```
@usage
(subscribed [:my-subscription])
```

```
@return (*)
```

<details>
<summary>Source code</summary>

```
(defn subscribed
  [query-vector]
  (-> query-vector subscribe deref))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [subscribed]]))

(re-frame.api/subscribed ...)
(subscribed              ...)
```

</details>

---

### toggle-debug-mode!

```
@usage
(toggle-debug-mode!)
```

<details>
<summary>Source code</summary>

```
(defn toggle-debug-mode!
  []
  (swap! state/DEBUG-MODE? not))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.api :refer [toggle-debug-mode!]]))

(re-frame.api/toggle-debug-mode!)
(toggle-debug-mode!)
```

</details>
