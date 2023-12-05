
(ns re-frame.interceptors
    (:require [random.api       :as random]
              [re-frame.core    :as core]
              [re-frame.dev.api :as re-frame.dev]
              [vector.api       :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn event-vector<-id-f
  ; @ignore
  ;
  ; @description
  ; Puts a random generated keyword as the second parameter into the
  ; event vector only in case of the second parameter is not a keyword.
  ;
  ; By using this interceptor you can make sure that your event takes a keyword
  ; as its second parameter. (The first parameter is the ID of the event itself.)
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
  ;       (println "It's sure that the second item of the event vector is a keyword!")))
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
          ; (f0 [:my-event :my-id {...}])
          ; =>
          ; [:my-event :my-id {...}]
          ;
          ; @example
          ; (f0 [:my-event {...}])
          ; =>
          ; [:my-event :0ce14671-e916-43ab-b057-0939329d4c1b {...}]
          ;
          ; @return (vector)
          (f0 [event-vector]
              (if (-> event-vector second keyword?)
                  (-> event-vector)
                  (vec (concat [(first event-vector) (random/generate-keyword)] (rest event-vector)))))]
         (update-in context [:coeffects :event] f0)))

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
  (vector/conj-item interceptors re-frame.dev/log-event!))
