
# metamorphic-event
  A metamorphic-event olyan formula amely lehetővé teszi, hogy egy eseményt vagy
  esemény-csoportot event-vector vagy effects-map formában is meghatározhass.
  `(dispatch [...])`
  `(dispatch {:dispatch [...]})`



# event-vector
  ...
  `[:my-event "My param"]`



# effects-map
  ...
  `{:dispatch-later [{:ms 500 :dispatch [:my-event "My param"]}]}`



# metamorphic-handler
  A metamorphic-handler olyan formula amely lehetővé teszi, hogy egy handler-f
  függvény helyett regisztrálhass event-vector vektort vagy effects-map térképet,
  illetve a handler-f függvény visszatérési értéke lehet event-vector vagy
  effects-map egyaránt.
  `(reg-event-fx :my-effects            [...])`
  `(reg-event-fx :my-effects {:dispatch [...]})`
  `(reg-event-fx :my-effects (fn [_ _]            [...]))`
  `(reg-event-fx :my-effects (fn [_ _] {:dispatch [...]}))`



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
  ...
  `{:dispatch-later [{:ms 100 :dispatch [...]}
                     {:ms 200 :dispatch-n [[...] [...]]}]}`



# dispatch-if
  ...



# dispatch-cond
  ...
