
(ns re-frame.state.events
    (:require [map.api       :refer [dissoc-in]]
              [noop.api      :refer [return]]
              [re-frame.core :as core]
              [vector.api    :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn empty-db!
  ; @return (map)
  [_ _]
  (return {}))

(defn toggle-item!
  ; @param (vector) item-path
  ;
  ; @usage
  ; (r toggle-item! [:my-item])
  ;
  ; @return (map)
  [db [_ item-path]]
  (update-in db item-path not))

(defn toggle-item-value!
  ; @param (vector) item-path
  ; @param (*) item-value
  ;
  ; @usage
  ; (r toggle-item-value! [:my-item] :my-value)
  ;
  ; @return (map)
  [db [_ item-path item-value]]
  (let [stored-value (get-in db item-path)]
       (if (= stored-value item-value)
           (dissoc-in db item-path)
           (assoc-in  db item-path item-value))))

(defn copy-item!
  ; @param (vector) from-item-path
  ; @param (vector) to-item-path
  ;
  ; @usage
  ; (r copy-item! [:move-from] [:move-to])
  ;
  ; @return (map)
  [db [_ from-item-path to-item-path]]
  (if-let [item (get-in db from-item-path)]
          (assoc-in  db to-item-path item)
          (dissoc-in db to-item-path)))

(defn move-item!
  ; @param (vector) from-item-path
  ; @param (vector) to-item-path
  ;
  ; @usage
  ; (r move-item! [:move-from] [:move-to])
  ;
  ; @return (map)
  [db [_ from-item-path to-item-path]]
  (if-let [item (get-in db from-item-path)]
          (-> db (assoc-in  to-item-path item)
                 (dissoc-in from-item-path))
          (dissoc-in db to-item-path)))

(defn set-item!
  ; @param (vector) item-path
  ; @param (*) item
  ;
  ; @usage
  ; (r set-item! [:my-item] :item-value)
  ;
  ; @return (map)
  [db [_ item-path item]]
  (if item (assoc-in  db item-path item)
           (dissoc-in db item-path)))

(defn set-vector-item!
  ; @warning
  ; Last item of the item path must be an integer!
  ;
  ; @param (vector) item-path
  ; @param (*) item
  ;
  ; @example
  ; (def db {})
  ; (r set-vector-item! [:my-item 0] :item-value)
  ; =>
  ; {:my-item [:item-value]}
  ;
  ; @example
  ; (def db {})
  ; (r set-vector-item! [:my-item 2] :item-value)
  ; =>
  ; {:my-item [:item-value]}
  ;
  ; @example
  ; (def db {:my-item {}})
  ; (r set-vector-item! [:my-item 0] :item-value)
  ; =>
  ; {:my-item [:item-value]}
  ;
  ; @example
  ; (def db {:my-item [])
  ; (r set-vector-item! [:my-item 0] :item-value)
  ; =>
  ; {:my-item [:item-value]}
  ;
  ; @example
  ; (def db {:my-item [:first-value :second-value])
  ; (r set-vector-item! [:my-item 0] :item-value)
  ; =>
  ; {:my-item [:item-value :second-value]}
  ;
  ; @return (map)
  [db [_ item-path item]]
  (let [item-parent-path (vector/remove-last-item item-path)
        item-dex         (vector/last-item        item-path)
        item-parent      (get-in db item-parent-path)]
       (if (vector/nonempty? item-parent)
           (let [updated-item-parent (vector/replace-nth-item item-parent item-dex item)]
                (assoc-in db item-parent-path updated-item-parent))
           (let [updated-item-parent [item]]
                (assoc-in db item-parent-path updated-item-parent)))))

(defn remove-item!
  ; @param (vector) item-path
  ;
  ; @usage
  ; (r remove-item! [:my-item])
  ;
  ; @return (map)
  [db [_ item-path]]
  (dissoc-in db item-path))

(defn remove-vector-item!
  ; @warning
  ; Last item of the item path must be an integer!
  ;
  ; @param (vector) item-path
  ;
  ; @usage
  ; (r remove-vector-item! [:my-item 0])
  ;
  ; @return (map)
  [db [_ item-path]]
  (let [parent-path         (vector/remove-last-item item-path)
        item-dex            (vector/last-item        item-path)
        parent-item         (get-in db parent-path)
        updated-parent-item (vector/remove-nth-item parent-item item-dex)]
       (assoc-in db parent-path updated-parent-item)))

(defn remove-item-n!
  ; @param (vectors in vector) item-paths
  ;
  ; @usage
  ; (r remove-item-n! [[:my-item] [...]])
  ;
  ; @return (map)
  [db [_ & item-paths]]
  (letfn [(f [db item-path] (dissoc-in db item-path))]
         (reduce f db item-paths)))

(defn inc-item-n!
  ; @param (vectors in vector) item-paths
  ;
  ; @usage
  ; (r inc-item-n! [[:my-item] [...]])
  ;
  ; @return (map)
  [db [_ & item-paths]]
  (letfn [(f [db item-path] (update-in db item-path inc))]
         (reduce f db item-paths)))

(defn dec-item-n!
  ; @param (vectors in vector) item-paths
  ;
  ; @usage
  ; (r dec-item-n! [[:my-item] [...]])
  ;
  ; @return (map)
  [db [_ & item-paths]]
  (letfn [(f [db item-path] (update-in db item-path dec))]
         (reduce f db item-paths)))

(defn apply-item!
  ; @param (vector) item-path
  ; @param (function) f
  ; @param (list of *) params
  ;
  ; @usage
  ; (r apply-item! db [:my-item] not)
  ;
  ; @usage
  ; (r apply-item! db [:my-item] conj :apple)
  ;
  ; @return (map)
  [db [_ item-path f & params]]
  (let [item   (get-in db item-path)
        params (cons item params)]
       (assoc-in db item-path (apply f params))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
; [:empty-db!]
(core/reg-event-db :empty-db! empty-db!)

; @usage
; [:toggle-item! [:my-item]]
(core/reg-event-db :toggle-item! toggle-item!)

; @usage
; [:toggle-item-value! [:my-item] :my-value]
(core/reg-event-db :toggle-item-value! toggle-item-value!)

; @usage
; [:move-item! [:move-from] [:move-to]]
(core/reg-event-db :move-item! move-item!)

; @usage
; [:set-item! [:my-item] "My value"]
(core/reg-event-db :set-item! set-item!)

; @usage
; [:set-vector-item! [:my-item :0] "My value"]
(core/reg-event-db :set-vector-item! set-vector-item!)

; @usage
; [:remove-item! [:my-item]]
(core/reg-event-db :remove-item! remove-item!)

; @usage
; [:remove-vector-item! [:my-item 0]]
(core/reg-event-db :remove-vector-item! remove-vector-item!)

; @usage
; [:remove-item-n! [[:my-item ] [...]]]
(core/reg-event-db :remove-item-n! remove-item-n!)

; @usage
; [:inc-item-n! [[:my-item] [...]]]
(core/reg-event-db :inc-item-n! inc-item-n!)

; @usage
; [:dec-item-n! [[:my-item] [...]]]
(core/reg-event-db :dec-item-n! dec-item-n!)

; @usage
; [:apply-item! [:my-item] merge {}]
(core/reg-event-db :apply-item! apply-item!)
