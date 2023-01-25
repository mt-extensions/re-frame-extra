
# re-frame.state.api isomorphic namespace

##### [README](../../../../README.md) > [DOCUMENTATION](../../../COVER.md) > re-frame.state.api

### Index

- [apply-item!](#apply-item)

- [copy-item!](#copy-item)

- [dec-item-n!](#dec-item-n)

- [empty-db!](#empty-db)

- [get-applied-item](#get-applied-item)

- [get-db](#get-db)

- [get-item](#get-item)

- [get-item-count](#get-item-count)

- [inc-item-n!](#inc-item-n)

- [item-exists?](#item-exists)

- [move-item!](#move-item)

- [remove-item!](#remove-item)

- [remove-item-n!](#remove-item-n)

- [remove-vector-item!](#remove-vector-item)

- [set-item!](#set-item)

- [set-vector-item!](#set-vector-item)

- [subscribe-item](#subscribe-item)

- [subscribed-item](#subscribed-item)

- [toggle-item!](#toggle-item)

- [toggle-item-value!](#toggle-item-value)

### apply-item!

```
@param (vector) item-path
@param (function) f
@param (list of *) params
```

```
@usage
(r apply-item! db [:my-item] not)
```

```
@usage
(r apply-item! db [:my-item] conj :apple)
```

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn apply-item!
  [db [_ item-path f & params]]
  (let [item   (get-in db item-path)
        params (cons item params)]
       (assoc-in db item-path (apply f params))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.state.api :refer [apply-item!]]))

(re-frame.state.api/apply-item! ...)
(apply-item!                    ...)
```

</details>

---

### copy-item!

```
@param (vector) from-item-path
@param (vector) to-item-path
```

```
@usage
(r copy-item! [:move-from] [:move-to])
```

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn copy-item!
  [db [_ from-item-path to-item-path]]
  (if-let [item (get-in db from-item-path)]
          (assoc-in  db to-item-path item)
          (dissoc-in db to-item-path)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.state.api :refer [copy-item!]]))

(re-frame.state.api/copy-item! ...)
(copy-item!                    ...)
```

</details>

---

### dec-item-n!

```
@param (vectors in vector) item-paths
```

```
@usage
(r dec-item-n! [[:my-item] [...]])
```

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn dec-item-n!
  [db [_ & item-paths]]
  (letfn [(f [db item-path] (update-in db item-path dec))]
         (reduce f db item-paths)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.state.api :refer [dec-item-n!]]))

(re-frame.state.api/dec-item-n! ...)
(dec-item-n!                    ...)
```

</details>

---

### empty-db!

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn empty-db!
  [_ _]
  (return {}))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.state.api :refer [empty-db!]]))

(re-frame.state.api/empty-db!)
(empty-db!)
```

</details>

---

### get-applied-item

```
@param (vector) item-path
@param (function) f
@param (list of *) params
```

```
@usage
(r get-applied-item [:my-item] inc)
```

```
@usage
(r get-applied-item [:my-item] + 42)
```

```
@return (integer)
```

<details>
<summary>Source code</summary>

```
(defn get-applied-item
  [db [_ item-path f & params]]
  (let [item   (get-in db item-path)
        params (cons item params)]
       (apply f params)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.state.api :refer [get-applied-item]]))

(re-frame.state.api/get-applied-item ...)
(get-applied-item                    ...)
```

</details>

---

### get-db

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn get-db
  [db _]
  (return db))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.state.api :refer [get-db]]))

(re-frame.state.api/get-db)
(get-db)
```

</details>

---

### get-item

```
@param (vector) item-path
@param (*)(opt) default-value
```

```
@usage
(r get-item [:my-item])
```

```
@usage
(r get-item [:my-item] "Default value")
```

```
@return (*)
```

<details>
<summary>Source code</summary>

```
(defn get-item
  [db [_ item-path default-value]]
  (get-in db item-path default-value))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.state.api :refer [get-item]]))

(re-frame.state.api/get-item ...)
(get-item                    ...)
```

</details>

---

### get-item-count

```
@param (vector) item-path
```

```
@usage
(r get-item-count [:my-item])
```

```
@return (integer)
```

<details>
<summary>Source code</summary>

```
(defn get-item-count
  [db [_ item-path]]
  (let [item (get-in db item-path)]
       (count item)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.state.api :refer [get-item-count]]))

(re-frame.state.api/get-item-count ...)
(get-item-count                    ...)
```

</details>

---

### inc-item-n!

```
@param (vectors in vector) item-paths
```

```
@usage
(r inc-item-n! [[:my-item] [...]])
```

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn inc-item-n!
  [db [_ & item-paths]]
  (letfn [(f [db item-path] (update-in db item-path inc))]
         (reduce f db item-paths)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.state.api :refer [inc-item-n!]]))

(re-frame.state.api/inc-item-n! ...)
(inc-item-n!                    ...)
```

</details>

---

### item-exists?

```
@param (vector) item-path
```

```
@usage
(r item-exists? [:my-item])
```

```
@return (boolean)
```

<details>
<summary>Source code</summary>

```
(defn item-exists?
  [db [_ item-path]]
  (some? (get-in db item-path)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.state.api :refer [item-exists?]]))

(re-frame.state.api/item-exists? ...)
(item-exists?                    ...)
```

</details>

---

### move-item!

```
@param (vector) from-item-path
@param (vector) to-item-path
```

```
@usage
(r move-item! [:move-from] [:move-to])
```

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn move-item!
  [db [_ from-item-path to-item-path]]
  (if-let [item (get-in db from-item-path)]
          (-> db (assoc-in  to-item-path item)
                 (dissoc-in from-item-path))
          (dissoc-in db to-item-path)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.state.api :refer [move-item!]]))

(re-frame.state.api/move-item! ...)
(move-item!                    ...)
```

</details>

---

### remove-item!

```
@param (vector) item-path
```

```
@usage
(r remove-item! [:my-item])
```

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn remove-item!
  [db [_ item-path]]
  (dissoc-in db item-path))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.state.api :refer [remove-item!]]))

(re-frame.state.api/remove-item! ...)
(remove-item!                    ...)
```

</details>

---

### remove-item-n!

```
@param (vectors in vector) item-paths
```

```
@usage
(r remove-item-n! [[:my-item] [...]])
```

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn remove-item-n!
  [db [_ & item-paths]]
  (letfn [(f [db item-path] (dissoc-in db item-path))]
         (reduce f db item-paths)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.state.api :refer [remove-item-n!]]))

(re-frame.state.api/remove-item-n! ...)
(remove-item-n!                    ...)
```

</details>

---

### remove-vector-item!

```
@warning
Last item of the item path must be an integer!
```

```
@param (vector) item-path
```

```
@usage
(r remove-vector-item! [:my-item 0])
```

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn remove-vector-item!
  [db [_ item-path]]
  (let [parent-path         (vector/remove-last-item item-path)
        item-dex            (vector/last-item        item-path)
        parent-item         (get-in db parent-path)
        updated-parent-item (vector/remove-nth-item parent-item item-dex)]
       (assoc-in db parent-path updated-parent-item)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.state.api :refer [remove-vector-item!]]))

(re-frame.state.api/remove-vector-item! ...)
(remove-vector-item!                    ...)
```

</details>

---

### set-item!

```
@param (vector) item-path
@param (*) item
```

```
@usage
(r set-item! [:my-item] :item-value)
```

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn set-item!
  [db [_ item-path item]]
  (if item (assoc-in  db item-path item)
           (dissoc-in db item-path)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.state.api :refer [set-item!]]))

(re-frame.state.api/set-item! ...)
(set-item!                    ...)
```

</details>

---

### set-vector-item!

```
@warning
Last item of the item path must be an integer!
```

```
@param (vector) item-path
@param (*) item
```

```
@example
(def db {})
(r set-vector-item! [:my-item 0] :item-value)
=>
{:my-item [:item-value]}
```

```
@example
(def db {})
(r set-vector-item! [:my-item 2] :item-value)
=>
{:my-item [:item-value]}
```

```
@example
(def db {:my-item {}})
(r set-vector-item! [:my-item 0] :item-value)
=>
{:my-item [:item-value]}
```

```
@example
(def db {:my-item [])
(r set-vector-item! [:my-item 0] :item-value)
=>
{:my-item [:item-value]}
```

```
@example
(def db {:my-item [:first-value :second-value])
(r set-vector-item! [:my-item 0] :item-value)
=>
{:my-item [:item-value :second-value]}
```

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn set-vector-item!
  [db [_ item-path item]]
  (let [item-parent-path (vector/remove-last-item item-path)
        item-dex         (vector/last-item        item-path)
        item-parent      (get-in db item-parent-path)]
       (if (vector/nonempty? item-parent)
           (let [updated-item-parent (vector/replace-nth-item item-parent item-dex item)]
                (assoc-in db item-parent-path updated-item-parent))
           (let [updated-item-parent [item]]
                (assoc-in db item-parent-path updated-item-parent)))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.state.api :refer [set-vector-item!]]))

(re-frame.state.api/set-vector-item! ...)
(set-vector-item!                    ...)
```

</details>

---

### subscribe-item

```
@param (vector) item-path
@param (*)(opt) default-value
```

```
@usage
(subscribe-item [:my-item])
```

```
@return (atom)
```

<details>
<summary>Source code</summary>

```
(defn subscribe-item
  ([item-path]
   (-> [:get-item item-path] core/subscribe))

  ([item-path default-value]
   (-> [:get-item item-path default-value] core/subscribe)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.state.api :refer [subscribe-item]]))

(re-frame.state.api/subscribe-item ...)
(subscribe-item                    ...)
```

</details>

---

### subscribed-item

```
@description
Returns the actual derefed value of a db item.
```

```
@param (vector) item-path
@param (*)(opt) default-value
```

```
@usage
(subscribed-item [:my-item])
```

```
@return (*)
```

<details>
<summary>Source code</summary>

```
(defn subscribed-item
  ([item-path]
   (-> [:get-item item-path] core/subscribe deref))

  ([item-path default-value]
   (-> [:get-item item-path default-value] core/subscribe deref)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.state.api :refer [subscribed-item]]))

(re-frame.state.api/subscribed-item ...)
(subscribed-item                    ...)
```

</details>

---

### toggle-item!

```
@param (vector) item-path
```

```
@usage
(r toggle-item! [:my-item])
```

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn toggle-item!
  [db [_ item-path]]
  (update-in db item-path not))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.state.api :refer [toggle-item!]]))

(re-frame.state.api/toggle-item! ...)
(toggle-item!                    ...)
```

</details>

---

### toggle-item-value!

```
@param (vector) item-path
@param (*) item-value
```

```
@usage
(r toggle-item-value! [:my-item] :my-value)
```

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn toggle-item-value!
  [db [_ item-path item-value]]
  (let [stored-value (get-in db item-path)]
       (if (= stored-value item-value)
           (dissoc-in db item-path)
           (assoc-in  db item-path item-value))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [re-frame.state.api :refer [toggle-item-value!]]))

(re-frame.state.api/toggle-item-value! ...)
(toggle-item-value!                    ...)
```

</details>

---

This documentation is generated by the [docs-api](https://github.com/bithandshake/docs-api) engine

