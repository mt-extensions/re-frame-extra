
# event-vector

Just to clarify what we called as an 'event-vector':

```
[:my-event "My param"]
```

# effects-map

And how an 'effects-map' looks like:

```
{:dispatch-later [{:ms 500 :dispatch [:my-event "My param"]}]}
```

# metamorphic-event

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

# metamorphic-handler

The 'metamorphic-handler' is very similar to the previous formula.
But it allows you to registrate Re-Frame effect-handlers in a really flexible way.
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

# dispatch

...

# dispatch-fx

...

# dispatch-sync

...

# dispatch-n

...

# dispatch-last

...

# dispatch-once

...

# dispatch-tick

...

# dispatch-later

I put some extra magic to the dispatch-later handler too. So you can use not just
the `:dispatch` handler delayed but all of the Re-Frame handlers or any of
your own handlers.

```
{:dispatch-later [{:ms 100 :dispatch [:my-event]}
                  {:ms 200 :dispatch-n [[:my-event] [:your-event]]}
                  {:ms 300 :my-fx nil}]}
```

# dispatch-if

Just another way to dispatch an event conditional.

```
{:dispatch-if [(= x 42)            [:my-event]]}
{:dispatch-if [(= x 42) {:dispatch [:my-event]}]}
```

# dispatch-cond

And one more ...

```
{:dispatch-cond [(keyword? x) [:my-event   x]
                 (string?  x) [:your-event x]]}
```
