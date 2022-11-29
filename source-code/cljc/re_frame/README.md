
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
(dispatch                   [:my-event])
(dispatch        {:dispatch [:my-event]})
(dispatch (fn [] {:dispatch [:my-event]}))
(dispatch (fn []            [:my-event]))
(dispatch (fn [] (fn []     [:my-event])))
(dispatch (fn [] (println "You can put side-effects here!")
                 {:dispatch [:my-event]}))

```

And of course you can do this if you want:

```
(dispatch (fn [] {:dispatch {:dispatch-n [{:dispatch-later [{:ms 420 :fx-n [:my-side-effect]}]}]}}))
```

### metamorphic-handler

The 'metamorphic-handler' is very similar to the previous formula.
But it allows you to registrate Re-Frame effect-handlers really flexible.
You can pass not just handler-functions but event-vectors or effect-maps to the
`reg-event-fx` function.

```
(reg-event-fx :my-effects                      [:my-event])
(reg-event-fx :my-effects           {:dispatch [:my-event]})
(reg-event-fx :my-effects (fn [_ _]            [:my-event]))
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

> Yes, the Re-Frame calculates the subscriptions every time when the state changes!
  It is part of the magic under the hood.

The solution is very simple. We have to decrease the number of writes in a specific
interval. So you can use the `dispatch-once` function which only fires an event once
in the given interval.

```
(dispatch-once 500 [:my-event])
```

If you call this a million times in every second the function will ignore it except
two times per second (500 ms interval).

### dispatch-last

It is quite similar to the previous function but the `dispatch-last` only fires
an event if you stop to calling it at least for the given timeout.

```
(dispatch-last 500 [:my-event])
```

If you call this the same way as in the previous sample the function will ignore
all of your calls and only fires the event (once) when you stop to calling it
at least for the given timeout.

### dispatch-later

I put some extra magic to the dispatch-later handler too. So you can use not just
the `:dispatch` handler delayed but all of the Re-Frame handlers or any of
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
queue is ordered in the way we want:

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
