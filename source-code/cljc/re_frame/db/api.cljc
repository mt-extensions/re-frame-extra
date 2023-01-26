
(ns re-frame.db.api
    (:require [re-frame.db.events  :as events]
              [re-frame.db.helpers :as helpers]
              [re-frame.db.subs    :as subs]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; re-frame.db.events
(def empty-db!           events/empty-db!)
(def toggle-item!        events/toggle-item!)
(def toggle-item-value!  events/toggle-item-value!)
(def copy-item!          events/copy-item!)
(def move-item!          events/move-item!)
(def set-item!           events/set-item!)
(def set-vector-item!    events/set-vector-item!)
(def remove-item!        events/remove-item!)
(def remove-vector-item! events/remove-vector-item!)
(def remove-item-n!      events/remove-item-n!)
(def inc-item-n!         events/inc-item-n!)
(def dec-item-n!         events/dec-item-n!)
(def apply-item!         events/apply-item!)

; re-frame.db.helpers
(def subscribe-item  helpers/subscribe-item)
(def subscribed-item helpers/subscribed-item)

; re-frame.db.subs
(def get-db           subs/get-db)
(def get-item         subs/get-item)
(def item-exists?     subs/item-exists?)
(def get-item-count   subs/get-item-count)
(def get-applied-item subs/get-applied-item)
