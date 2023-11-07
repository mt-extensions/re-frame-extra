
# re-frame-api

### Overview

The <strong>re-frame-api</strong> library is an implementation of the Re-Frame
Clojure/ClojureScript framework with extra features such as metamorphic-events
that allows you to register and dispatch your events in a freestyle way.

> This library implements the 1.2.0 version of Re-Frame.

### deps.edn

```
{:deps {bithandshake/re-frame-api {:git/url "https://github.com/bithandshake/re-frame-api"
                                   :sha     "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"}}
```

### Current version

Check out the latest commit on the [release branch](https://github.com/bithandshake/re-frame-api/tree/release).

### Documentation

The <strong>re-frame-api</strong> functional documentation is [available here](documentation/COVER.md).

### Changelog

You can track the changes of the <strong>re-frame-api</strong> library [here](CHANGES.md).

# Usage

> Some parameters of the following functions and some further functions are not discussed in this file.
  To learn more about the available functionality, check out the [functional documentation](documentation/COVER.md)!

### Index

- [event-vector](#event-vector)

- [effects-map](#effects-map)

- [metamorphic-event](#metamorphic-event)

- [metamorphic-handler](#metamorphic-handler)

- [dispatch-once](#dispatch-once)

- [dispatch-last](#dispatch-last)

- [dispatch-later](#dispatch-later)

- [dispatch-tick](#dispatch-tick)

- [r function](#r-function)

- [Stacking handler functions](#stacking-handler-functions)

- [How to use subscription handlers in effect events and db events](#how-to-use-subscription-handlers-in-effect-events-and-db-events)

- [How to use the debug interceptor?](#how-to-use-the-debug-interceptor)

- [How to use the debug mode?](#how-to-use-the-debug-mode)

### event-vector

To clarify what we called as an 'event-vector':

```
[:my-event "My param"]
```

### effects-map

And how an 'effects-map' looks like:

```
{:dispatch-later [{:ms 500 :dispatch [:my-event "My param"]}]}
```

### metamorphic-event

The 'metamorphic-event' formula provides a more flexible way to dispatch Re-Frame events.
You can pass an event to the [`re-frame.api/dispatch`](documentation/cljc/re-frame/API.md#dispatch)
function (or to the dispatching side-effects) as an event-vector, an effects-map
or a function that returns another 'metamorphic-event' ooor a function that returns
a function that returns an event-vector and so on.

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
But it allows you to register Re-Frame effect-handlers really flexible.
You can pass not just a handler function but an event-vector or an effects-map
to the `reg-event-fx` function.

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

To spare some CPU when dispatching an event in high frequency we we can use the `dispatch-once` function
which only fires an event once in the given interval.

> Re-Frame calculates the subscriptions every time when the state changes!

```
(dispatch-once 500 [:my-event])
```

If you call this a million times in every second the function will ignore the
callings except two times per second (500 ms interval).

### dispatch-last

It is quite similar to the previous function but the `dispatch-last` function only
fires an event if you stop calling it at least for the given timeout.

```
(dispatch-last 500 [:my-event])
```

If you call this the same way as in the previous sample the function will ignore
all of your callings and only fires the event (once) when you stop calling it
at least for the given timeout.

### dispatch-later

I put some extra magic into the dispatch-later handler too. So you can use not just
the `:dispatch` handler delayed but all of the Re-Frame handlers and any of
your own handlers.

```
{:dispatch-later [{:ms 100 :dispatch [:my-event]}
                  {:ms 200 :dispatch-n [[:my-event] [:your-event]]}
                  {:ms 300 :my-fx nil}
                  {:ms 400 :dispatch-n [[:my-event] {:dispatch-later [{:fx [:my-side-effect] :ms 500}]}]}]}
```

### dispatch-tick

If you need event delaying based on not the time you can delay your events in the event queue
easily with the `:dispatch-tick` handler.

> One tick in the Re-Frame queue is about 16 ms.

Here is an example for the `:dispatch-tick` handler:

```
(reg-event-fx :init-app! [:import-data!])
(reg-event-fx :import-data! ...)

(reg-event-fx :render-app! [:render-surface!])
(reg-event-fx :render-surface! ...)

(reg-event-fx :boot-app! {:dispatch-n [[:init-app!]
                                       [:render-app!]]})
```

When you dispatch the `[:boot-app!]` event in the example above the queue will
look like this:

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

### r function

The `r` function helps you not to care about the event ID when you stacking handler functions.

Case 1: Disregarding the event-id parameter:

```
(defn store-data!
  [db [_ data]]
  (assoc db :my-data data))

(defn import-data!
  [db _]
  (store-data! db [nil 420]))
```

Case 2: Passing the event-id parameter:

```
(defn store-data!
  [db [_ data]]
  (assoc db :my-data data))

(defn import-data!
  [db [event-id]]
  (store-data! db [event-id 420]))
```

Case 3: Useing the `r` function:

```
(defn store-data!
  [db [_ data]]
  (assoc db :my-data data))

(defn import-data!
  [db _]
  (r store-data! db 420))
```

You can apply functions with `r` one after another by using the `as->` function:

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

And also you can pass more than one parameter by using the `r`:

```
(defn add-user!
  [db [_ name age]]
  (assoc db :my-user {:name name :age age}))

(defn init-app!
  [db _]
  (r add-user! db "John" 42))
```

### Stacking handler functions

Stacking handler functions helps you to decrease the Re-Frame database writes.

In the following example, when you dispatch the `[:handle-data!]` event that is
followed by two other db writes by the `[:store-data!]` and the `[:update-data!]` events.

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

If you want to do the same thing but with only one db write you have several choices:

Case 1: Keep the `[:handle-data!]` event as an effect event and use the `:db`
handler to stack the `store-data!` and `update-data!` functions which were anonymous
functions in the previous example and they are named functions in the next example.

```
; Defining the 'store-data!' handler as a named function:
(defn store-data!
  [db [_ data]]
  (assoc  db :my-data data))

(reg-event-db :store-data! store-data!)  


; Defining the 'update-data!' handler as a named function:
(defn update-data!
  [db _]
  (update db :my-data inc))

(reg-event-db :update-data! update-data!)


(reg-event-fx
  :handle-data!
  (fn [_ _] {:db (as-> db % (r store-data!  % 420)
                            (r update-data! %))}))
```

Case 2: The key is the same as in the previous example but the only difference is
that the `handle-data!` function is not an effect event.

```
; Defining the 'store-data!' handler as a named function:
(defn store-data!
  [db [_ data]]
  (assoc  db :my-data data))

(reg-event-db :store-data! store-data!)


; Defining the 'update-data!' handler as a named function:
(defn update-data!
  [db _]
  (update db :my-data inc))

(reg-event-db :update-data! update-data!)


; Defining the 'handle-data!' handler as a named function:
(defn handle-data!
  [db _]
  (as-> db % (r store-data!  % 420)
             (r update-data! %)))

(reg-event-db :handle-data! handle-data!)
```

### How to use subscription handlers in effect events and db events?

If you register named functions as subscription handlers, you can use them in
effect events and db events.

```
; Defining the 'get-data' handler as a named function:
(defn get-data
  [db _]
  (get db :my-data))

(reg-sub :get-data get-data)


; Defining the 'check-data!' handler as a named function:
(defn check-data!
  [db _]
  (let [my-data (r get-data db)]))
       (assoc db :my-data-valid? (number? my-data))

(reg-event-db :check-data! check-data!)
```

```
; Defining the 'get-data' handler as a named function:
(defn get-data
  [db _]
  (get db :my-data))

(reg-sub :get-data get-data)


(reg-event-fx
  :check-data!
  (fn [_ _]
      (let [my-data (r get-data db)])
           (println my-data))
```

### How to use the debug interceptor?

By using the `re-frame.api/debug!` interceptor your event will be printed to
the console when it get dispatched.

```
(reg-event-db
  :my-event
  [debug!]
  (fn [db _] ...))

(reg-event-fx
  :my-event
  [debug!]
  (fn [cofx _] ...))
```

### How to use the debug mode?

You can turn on/off the debug mode by using the following functions:

```
(ns my-namespace
    (:require [re-frame.api]))

(re-frame.api/set-debug-mode!)
(re-frame.api/quit-debug-mode!)
(re-frame.api/toggle-debug-mode!)
```

In debug mode, the event handler prints all the dispatched events and
their parameters to the console and you can track what happens under the hood.
