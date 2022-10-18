
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns re-frame.types
    (:require [mid-fruits.string :as string]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn event-vector?
  ; @param (*) n
  ;
  ; @example
  ;  (r/event-vector? [:my-event ...])
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (and (-> n vector?)
       (-> n first keyword?)))

(defn subscription-vector?
  ; @param (*) n
  ;
  ; @example
  ;  (r/subscription-vector? [:my-namespace/get-something ...])
  ;  =>
  ;  true
  ;
  ; @example
  ;  (r/subscription-vector? [:my-namespace/something-happened? ...])
  ;  =>
  ;  true
  ;
  ; @example
  ;  (r/subscription-vector? [:div ...])
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [n]
  (and (-> n vector?)
       (and (-> n first keyword?)
            (or (-> n first name (string/starts-with? "get-"))
                (-> n first name (string/ends-with?   "?"))))))
