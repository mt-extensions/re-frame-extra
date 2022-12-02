
# re-frame-api

> "A person had to work hard for it, but a minute of perfection was worth the effort.
   A moment was the most you could ever expect from perfection." – Tyler Durden

### Overview

The <strong>re-frame-api</strong> library contains the whole functionality
of the great and majestic Re-Frame Clojure/ClojureScript framework with some
extra features like the metamorphic-events (which allows you to registrate
and dispatch your events in a very-very freestyle way).

### deps.edn

```
{:deps {bithandshake/re-frame-api {:git/url "https://github.com/bithandshake/re-frame-api"
                                   :sha     "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"}}
```

### Current version

Check out the latest commit on the [release branch](https://github.com/bithandshake/re-frame-api/tree/release).

### Documentation

The <strong>re-frame-api</strong> documentation is [available here](documentation/COVER.md).

### Changelog

You can track the changes of the <strong>re-frame-api</strong> library [here](CHANGES.md).

# What's in this library?

### event-vector

Just to clarify what we called as an 'event-vector':

```
[:my-event "My param"]
```

### effects-map

And how an 'effects-map' looks like:

```
{:dispatch-later [{:ms 500 :dispatch [:my-event "My param"]}]}
```

### metamorphic-event

Yeah! The metamorphic stuff! Let's see how the magic works!

The 'metamorphic-event' formula provides you a more flexible way to dispatch
Re-Frame events.
You can pass the event to the dispatch function (or the dispatching side-effects)
as an event-vector, an effects-map or a function which returns with another
metamorphic-event ooor a function which returns a function which returns
an event-vector (for example).

```
(dispatch [:my-event])
```

```
(dispatch {:dispatch [:my-event]})
```

```
(dispatch (fn [] {:dispatch [:my-event]}))
```

```
(dispatch (fn [] [:my-event]))
```

```
(dispatch (fn [] (fn [] [:my-event])))
```

```
(dispatch (fn [] (println "You can put some side-effects here!")
                 {:dispatch [:my-event]}))

```

And of course you can do this if you want:

```
(dispatch (fn [] {:dispatch {:dispatch-n [{:dispatch-later [{:ms 420 :fx-n [[:my-side-effect]]}]}]}}))
```

### metamorphic-handler

The 'metamorphic-handler' is very similar to the previous formula.
But it allows you to registrate Re-Frame effect-handlers really flexible.
You can pass not just a handler-function but an event-vector or an effects-map to the
`reg-event-fx` function.

```
(reg-event-fx :my-effects [:my-event])
```

```
(reg-event-fx :my-effects {:dispatch [:my-event]})
```

```
(reg-event-fx :my-effects (fn [_ _] [:my-event]))
```

```
(reg-event-fx :my-effects (fn [_ _] {:dispatch [:my-event]}))
```

And yes, it is possible too ...

```
(reg-event-fx :my-effects (fn [_ _] {:dispatch {:dispatch-n [{:dispatch-later [{:ms 420 :dispatch [:my-event]}]}]}}))
```

### dispatch-once

Do you remember the first time when you tried to store the scroll-position
in the Re-Frame database? Yes you do! Your poor CPU was melting down slowly when
it tried to calculate all of your subscriptions at least hundred times per every
second when you scrolled the viewport.

> The Re-Frame calculates the subscriptions every time when the state changes!
  It is part of the magic under the hood.

The solution is very simple. We have to decrease the number of writes in a specific
interval. So you can use the `dispatch-once` function which only fires an event once
in the given interval.

```
(dispatch-once 500 [:my-event])
```

If you call this a million times in every second the function will ignore the
callings except two times per second (500 ms interval).

### dispatch-last

It is quite similar to the previous function but the `dispatch-last` only fires
an event if you stop to calling it at least for the given timeout.

```
(dispatch-last 500 [:my-event])
```

If you call this the same way as in the previous sample the function will ignore
all of your callings and only fires the event (once) when you stop to calling it
at least for the given timeout.

### dispatch-later

I put some extra magic to the dispatch-later handler too. So you can use not just
the `:dispatch` handler delayed but all of the Re-Frame handlers and any of
your own handlers.

```
{:dispatch-later [{:ms 100 :dispatch [:my-event]}
                  {:ms 200 :dispatch-n [[:my-event] [:your-event]]}
                  {:ms 300 :my-fx nil}]}
```

### dispatch-tick

And what about the event-delaying based not on the time? What if you want to add
just a little slip into the event queue? When you composing your effect-events,
sometimes you face with the fact that the Re-Frame uses a very logical but strict
event queue.
So you can delay your events in the queue easily with the `:dispatch-tick` handler.

```
(reg-event-fx :init-app! [:import-data!])
(reg-event-fx :import-data! ...)

(reg-event-fx :render-app! [:render-surface!])
(reg-event-fx :render-surface! ...)

(reg-event-fx :boot-app! {:dispatch-n [[:init-app!]
                                       [:render-app!]]})
```

When you dispatch the `[:boot-app!]` event the queue will looks like this:

`[:boot-app!]`
`[:init-app!]`
`[:render-app!]`
`[:import-data!]`
`[:render-surface!]`

If you want the `[:import-data!]` to happen before the rendering started, you
need to add a slippage. So let's try again with the `:dispatch-tick` handler!

```
(reg-event-fx :init-app! [:import-data!])
(reg-event-fx :import-data! ...)

(reg-event-fx :render-app! [:render-surface!])
(reg-event-fx :render-surface! ...)

(reg-event-fx :boot-app! {:dispatch-tick [{:tick 0 :dispatch [:init-app!]}
                                          {:tick 1 :dispatch [:render-app!]}]})
```

Now the `[:import-data!]` event happens before the rendering started and the
queue is ordered in the way we wanted:

`[:boot-app!]`
`[:init-app!]`
`[:import-data!]`
`[:render-app!]`
`[:render-surface!]`

### dispatch-if

Just another way to dispatch an event conditional.

```
{:dispatch-if [(= x 42)            [:my-event]]}
{:dispatch-if [(= x 42) {:dispatch [:my-event]}]}
```

### dispatch-cond

And one more ...

```
{:dispatch-cond [(keyword? x) [:my-event   x]
                 (string?  x) [:your-event x]]}
```

### r

The `r` function helps you not to care about the event-id when you stacking handler-functions.

Case 1. When you disregard the event-id parameter:

```
(defn store-data!
  [db [_ data]]
  (assoc db :my-data data))

(defn import-data!
  [db _]
  (store-data! db [nil 420]))
```

Case 2. When you pass the event-id parameter:

```
(defn store-data!
  [db [_ data]]
  (assoc db :my-data data))

(defn import-data!
  [db [event-id]]
  (store-data! db [event-id 420]))
```

Case 3. When you use the `r` function:

```
(defn store-data!
  [db [_ data]]
  (assoc db :my-data data))

(defn import-data!
  [db _]
  (r store-data! db 420))
```

You can apply functions with `r` one after another if you use the as-> function:

```
(defn store-data!
  [db [_ data]]
  (assoc db :my-data data))

(defn update-data!
  [db _]
 (update db :my-data inc))

(defn import-data!
  [db _]
  (as-> db % (r store-data!  % 420)
             (r update-data! %)))
```

And you can pass more than one parameter by using the `r`:

```
(defn add-user!
  [db [_ name age]]
  (assoc db :my-user {:name name :age age}))

(defn init-app!
  [db _]
  (r add-user! db "John" 42))
```

### Stacking handler-functions

Stacking handler-functions helps you to decrease the Re-Frame database writes.

In the following sample, when you dispatch the `[:handle-data!]` event it followed
by two db writes.

```
(reg-event-db
  :store-data!  
  (fn [db [_ data]] (assoc db :my-data data)))

(reg-event-db
  :update-data!
  (fn [db _] (update db :my-data inc)))

(reg-event-fx
  :handle-data!
  (fn [_ _] {:dispatch-n [[:store-data! 420]
                          [:update-data!]]}))
```

If you want to do the same thing in only one db write you have several choices:

Case 1:

```
(defn store-data!
  [db [_ data]]
  (assoc  db :my-data data))

(defn update-data!
  [db _]
  (update db :my-data inc))

(reg-event-fx
  :handle-data!
  (fn [_ _] {:db (as-> db % (r store-data!  % 420)
                            (r update-data! %))}))

; In case if you need your handlers to be registrated:
; (reg-event-db :store-data! store-data!)  
; (reg-event-db :update-data! update-data!)
```

Case 2:

```
(defn store-data!
  [db [_ data]]
  (assoc  db :my-data data))

(defn update-data!
  [db _]
  (update db :my-data inc))

(defn handle-data!
  [db _]
  (as-> db % (r store-data!  % 420)
             (r update-data! %)))

; In case if you need your handlers to be registrated:
; (reg-event-db  :store-data!  store-data!)
; (reg-event-db :update-data! update-data!)
; (reg-event-db :handle-data! handle-data!)
```

### Subscribing in an effect event or in a db event, how?

If you are registrating named functions as subscription handlers, you can easily
apply them in effect events and db events.

```
(defn get-data
  [db _]
  (get db :my-data))

(defn check-data!
  [db _]
  (let [my-data (r get-data db)]))
       (assoc db :my-data-valid? (number? my-data))

; In case if you need your handler to be registrated:
; (reg-sub :get-data get-data)
```

```
(defn get-data
  [db _]
  (get db :my-data))

(reg-event-fx
  :check-data!
  (fn [_ _]
      (let [my-data (r get-data db)])
           (println my-data))
```
