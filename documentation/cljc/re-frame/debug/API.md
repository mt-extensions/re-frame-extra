
# re-frame.debug.api isomorphic namespace

##### [README](../../../../README.md) > [DOCUMENTATION](../../../COVER.md) > re-frame.debug.api

### Index

- [quit-debug-mode!](#quit-debug-mode)

- [set-debug-mode!](#set-debug-mode)

- [toggle-debug-mode!](#toggle-debug-mode)

### quit-debug-mode!

```
@description
Turns off the debug mode.
```

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
(ns my-namespace (:require [re-frame.debug.api :refer [quit-debug-mode!]]))

(re-frame.debug.api/quit-debug-mode!)
(quit-debug-mode!)
```

</details>

---

### set-debug-mode!

```
@description
Turns on the debug mode.
```

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
(ns my-namespace (:require [re-frame.debug.api :refer [set-debug-mode!]]))

(re-frame.debug.api/set-debug-mode!)
(set-debug-mode!)
```

</details>

---

### toggle-debug-mode!

```
@description
Toggles on/off the debug mode.
```

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
(ns my-namespace (:require [re-frame.debug.api :refer [toggle-debug-mode!]]))

(re-frame.debug.api/toggle-debug-mode!)
(toggle-debug-mode!)
```

</details>

---

This documentation is generated by the [docs-api](https://github.com/bithandshake/docs-api) engine
