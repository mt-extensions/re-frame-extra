
(ns re-frame.types
    (:require [string.api :as string]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn event-vector?
  ; @param (*) n
  ;
  ; @example
  ; (event-vector? [:my-event ...])
  ; =>
  ; true
  ;
  ; @return (boolean)
  [n]
  (and (-> n vector?)
       (-> n first keyword?)))

(defn query-vector?
  ; @param (*) n
  ;
  ; @example
  ; (query-vector? [:my-namespace/get-something ...])
  ; =>
  ; true
  ;
  ; @example
  ; (query-vector? [:my-namespace/something-happened? ...])
  ; =>
  ; true
  ;
  ; @example
  ; (query-vector? [:div ...])
  ; =>
  ; false
  ;
  ; @return (boolean)
  [n]
  (and (-> n vector?)
       (and (-> n first keyword?)
            (or (-> n first name (string/starts-with? "get-"))
                (-> n first name (string/ends-with?   "?"))))))
