
# re-frame.core.api isomorphic namespace

##### [README](../../../../README.md) > [DOCUMENTATION](../../../COVER.md) > re-frame.core.api

### Index

- [apply-fx-params](#apply-fx-params)

- [cofx->event-id](#cofx-event-id)

- [cofx->event-vector](#cofx-event-vector)

- [context->db-after-effect](#context-db-after-effect)

- [context->db-before-effect](#context-db-before-effect)

- [context->event-id](#context-event-id)

- [context->event-vector](#context-event-vector)

- [dispatch](#dispatch)

- [dispatch-fx](#dispatch-fx)

- [dispatch-last](#dispatch-last)

- [dispatch-later](#dispatch-later)

- [dispatch-n](#dispatch-n)

- [dispatch-once](#dispatch-once)

- [dispatch-sync](#dispatch-sync)

- [dispatch-tick](#dispatch-tick)

- [effects-map->handler-f](#effects-map-handler-f)

- [effects-map<-event](#effects-map-event)

- [event-handler-registered?](#event-handler-registered)

- [event-vector->effects-map](#event-vector-effects-map)

- [event-vector->event-id](#event-vector-event-id)

- [event-vector->handler-f](#event-vector-handler-f)

- [event-vector<-params](#event-vector-params)

- [event-vector?](#event-vector)

- [fx](#fx)

- [fx-n](#fx-n)

- [get-event-handler](#get-event-handler)

- [get-event-handlers](#get-event-handlers)

- [inject-cofx](#inject-cofx)

- [merge-effects-maps](#merge-effects-maps)

- [metamorphic-event->effects-map](#metamorphic-event-effects-map)

- [metamorphic-event<-params](#metamorphic-event-params)

- [metamorphic-handler->handler-f](#metamorphic-handler-handler-f)

- [r](#r)

- [subscribe](#subscribe)

- [subscribed](#subscribed)

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
(ns my-namespace (:require [re-frame.core.api :refer [apply-fx-params]]))

(re-frame.core.api/apply-fx-params ...)
(apply-fx-params                   ...)
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
(ns my-namespace (:require [re-frame.core.api :refer [cofx->event-id]]))

(re-frame.core.api/cofx->event-id ...)
(cofx->event-id                   ...)
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
(ns my-namespace (:require [re-frame.core.api :refer [cofx->event-vector]]))

(re-frame.core.api/cofx->event-vector ...)
(cofx->event-vector                   ...)
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
(ns my-namespace (:require [re-frame.core.api :refer [context->db-after-effect]]))

(re-frame.core.api/context->db-after-effect ...)
(context->db-after-effect                   ...)
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
(ns my-namespace (:require [re-frame.core.api :refer [context->db-before-effect]]))

(re-frame.core.api/context->db-before-effect ...)
(context->db-before-effect                   ...)
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
  (-> context context->event-vector event-vector->event-id))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.core.api :refer [context->event-id]]))

(re-frame.core.api/context->event-id ...)
(context->event-id                   ...)
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
(ns my-namespace (:require [re-frame.core.api :refer [context->event-vector]]))

(re-frame.core.api/context->event-vector ...)
(context->event-vector                   ...)
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
  (letfn [(check! [] (let [event-id      (utils/event-vector->event-id event-handler)
                           event-exists? (env/event-handler-registered? :event event-id)]
                          (when-not event-exists? (println "re-frame: no :event handler registered for:" event-id))))]
         (if (vector? event-handler) #?(:clj (check!) :cljs nil))
         (if (vector? event-handler)         (core/dispatch event-handler)
                                             (core/dispatch [:dispatch-metamorphic-event event-handler]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.core.api :refer [dispatch]]))

(re-frame.core.api/dispatch ...)
(dispatch                   ...)
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
(ns my-namespace (:require [re-frame.core.api :refer [dispatch-fx]]))

(re-frame.core.api/dispatch-fx ...)
(dispatch-fx                   ...)
```

</details>

---

### dispatch-last

```
@description
The 'dispatch-last' function only fires an event if you stop calling it
at least for the given timeout.
It ignores dispatching the event until the timout elapsed since the last calling.
```

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
  (let [event-id (utils/event-vector->event-id event-vector)]
       (reg-event-lock! timeout event-id)
       (letfn [(f [] (dispatch-unlocked?! event-vector))]
              (time/set-timeout! f timeout))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.core.api :refer [dispatch-last]]))

(re-frame.core.api/dispatch-last ...)
(dispatch-last                   ...)
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
(ns my-namespace (:require [re-frame.core.api :refer [dispatch-later]]))

(re-frame.core.api/dispatch-later ...)
(dispatch-later                   ...)
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
(ns my-namespace (:require [re-frame.core.api :refer [dispatch-n]]))

(re-frame.core.api/dispatch-n ...)
(dispatch-n                   ...)
```

</details>

---

### dispatch-once

```
@description
The 'dispatch-once' function only fires an event once in the given interval.
It ignores dispatching the event except one time per interval.
```

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
  (let [event-id (utils/event-vector->event-id event-vector)]
       (if (event-unlocked? event-id)
           (do (core/dispatch event-vector)
               (reg-event-lock! interval event-id))
           (letfn [(f [] (delayed-try interval event-vector))]
                  (time/set-timeout! f interval)))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.core.api :refer [dispatch-once]]))

(re-frame.core.api/dispatch-once ...)
(dispatch-once                   ...)
```

</details>

---

### dispatch-sync

```
@description
This function doesn't take metamoprhic handler (for performance reasons).
```

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
(ns my-namespace (:require [re-frame.core.api :refer [dispatch-sync]]))

(re-frame.core.api/dispatch-sync ...)
(dispatch-sync                   ...)
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
(ns my-namespace (:require [re-frame.core.api :refer [dispatch-tick]]))

(re-frame.core.api/dispatch-tick ...)
(dispatch-tick                   ...)
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
(ns my-namespace (:require [re-frame.core.api :refer [effects-map->handler-f]]))

(re-frame.core.api/effects-map->handler-f ...)
(effects-map->handler-f                   ...)
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
(ns my-namespace (:require [re-frame.core.api :refer [effects-map<-event]]))

(re-frame.core.api/effects-map<-event ...)
(effects-map<-event                   ...)
```

</details>

---

### event-handler-registered?

```
@param (keyword) event-kind
:cofx, :event, :fx, :sub
@param (keyword) event-id
```

```
@usage
(event-handler-registered? :sub :my-subscription)
```

```
@return (function)
```

<details>
<summary>Source code</summary>

```
(defn event-handler-registered?
  [event-kind event-id]
  (-> (get-event-handler event-kind event-id)
      (some?)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.core.api :refer [event-handler-registered?]]))

(re-frame.core.api/event-handler-registered? ...)
(event-handler-registered?                   ...)
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
(ns my-namespace (:require [re-frame.core.api :refer [event-vector->effects-map]]))

(re-frame.core.api/event-vector->effects-map ...)
(event-vector->effects-map                   ...)
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
(ns my-namespace (:require [re-frame.core.api :refer [event-vector->event-id]]))

(re-frame.core.api/event-vector->event-id ...)
(event-vector->event-id                   ...)
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
(ns my-namespace (:require [re-frame.core.api :refer [event-vector->handler-f]]))

(re-frame.core.api/event-vector->handler-f ...)
(event-vector->handler-f                   ...)
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
(ns my-namespace (:require [re-frame.core.api :refer [event-vector<-params]]))

(re-frame.core.api/event-vector<-params ...)
(event-vector<-params                   ...)
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
(ns my-namespace (:require [re-frame.core.api :refer [event-vector?]]))

(re-frame.core.api/event-vector? ...)
(event-vector?                   ...)
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
(ns my-namespace (:require [re-frame.core.api :refer [fx]]))

(re-frame.core.api/fx ...)
(fx                   ...)
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
(ns my-namespace (:require [re-frame.core.api :refer [fx-n]]))

(re-frame.core.api/fx-n ...)
(fx-n                   ...)
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
(ns my-namespace (:require [re-frame.core.api :refer [get-event-handler]]))

(re-frame.core.api/get-event-handler ...)
(get-event-handler                   ...)
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
(ns my-namespace (:require [re-frame.core.api :refer [get-event-handlers]]))

(re-frame.core.api/get-event-handlers ...)
(get-event-handlers                   ...)
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
(ns my-namespace (:require [re-frame.core.api :refer [inject-cofx]]))

(re-frame.core.api/inject-cofx ...)
(inject-cofx                   ...)
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
(ns my-namespace (:require [re-frame.core.api :refer [merge-effects-maps]]))

(re-frame.core.api/merge-effects-maps ...)
(merge-effects-maps                   ...)
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
  (cond (vector? n) (event-vector->effects-map       n)
        (map?    n) (return                          n)
        (fn?     n) (metamorphic-event->effects-map (n))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.core.api :refer [metamorphic-event->effects-map]]))

(re-frame.core.api/metamorphic-event->effects-map ...)
(metamorphic-event->effects-map                   ...)
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
  (cond (event-vector? n) (vector/concat-items n params)
        (vector?       n) (vector/->items      n #(apply metamorphic-event<-params % params))
        (map?          n) (map/->values        n #(apply metamorphic-event<-params % params))
        :return        n))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.core.api :refer [metamorphic-event<-params]]))

(re-frame.core.api/metamorphic-event<-params ...)
(metamorphic-event<-params                   ...)
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
  (cond (map?    n) (effects-map->handler-f  n)
        (vector? n) (event-vector->handler-f n)
        :return  n))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.core.api :refer [metamorphic-handler->handler-f]]))

(re-frame.core.api/metamorphic-handler->handler-f ...)
(metamorphic-handler->handler-f                   ...)
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
  [f & [context & params]]
  (f context (vector/cons-item params nil)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.core.api :refer [r]]))

(re-frame.core.api/r ...)
(r                   ...)
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
(ns my-namespace (:require [re-frame.core.api :refer [subscribe]]))

(re-frame.core.api/subscribe ...)
(subscribe                   ...)
```

</details>

---

### subscribed

```
@description
Returns the actual derefed value of the given subscription.
```

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
(ns my-namespace (:require [re-frame.core.api :refer [subscribed]]))

(re-frame.core.api/subscribed ...)
(subscribed                   ...)
```

</details>

---

This documentation is generated by the [docs-api](https://github.com/bithandshake/docs-api) engine

