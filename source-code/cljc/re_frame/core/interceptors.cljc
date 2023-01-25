
(ns re-frame.core.interceptors
    (:require [noop.api                    :refer [return]]
              [random.api                  :as random]
              [re-frame.core               :as core]
              [re-frame.debug.interceptors :as debug.interceptors]
              [vector.api                  :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn event-vector<-id-f
  ; @ignore
  ;
  ; @description
  ; Puts a random generated keyword type ID as the second parameter into the
  ; event vector, in case of the second parameter is not a keyword.
  ;
  ; By using this interceptor you can make sure that your event takes a keyword
  ; as its second parameter that you can use as an ID.
  ; (The first parameter is the ID of the event itself.)
  ;
  ; @param (map) context
  ;
  ; @usage
  ; (event-vector<-id-f {...})
  ;
  ; @usage
  ; (reg-event-fx
  ;   :my-effect-event
  ;   [event-vector<-id]
  ;   (fn [_ [_ my-id my-params]]
  ;       (println "The second item of the event vector is always a keyword!")))
  ;
  ; (dispatch [:my-effect-event        "My param"]) <- In this case it puts a random gen. keyword to the second place
  ; (dispatch [:my-effect-event :my-id "My param"])
  ;
  ; @return (map)
  [context]
  (letfn [
          ; @param (vector) event-vector
          ;
          ; @example
          ; (f [:my-event :my-id {...}])
          ; =>
          ; [:my-event :my-id {...}]
          ;
          ; @example
          ; (f [:my-event {...}])
          ; =>
          ; [:my-event :0ce14671-e916-43ab-b057-0939329d4c1b {...}]
          ;
          ; @return (vector)
          (f [event-vector]
             (if (->     event-vector second keyword?)
                 (return event-vector)
                 (vec (concat [(first event-vector) (random/generate-keyword)] (rest event-vector)))))]
         (update-in context [:coeffects :event] f)))

; @constant (?)
(def event-vector<-id (core/->interceptor :id :re-frame/event-vector<-id
                                          :before event-vector<-id-f))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn interceptors<-system-interceptors
  ; @ignore
  ;
  ; @param (vector) interceptors
  ;
  ; @return (vector)
  [interceptors]
  (vector/conj-item interceptors debug.interceptors/LOG-EVENT!))
