
(ns re-frame.interceptors
    (:require [noop.api       :refer [return]]
              [random.api     :as random]
              [re-frame.core  :as core]
              [re-frame.state :as state]
              [vector.api     :as vector]))

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

(defn log-event-f
  ; @ignore
  ;
  ; @description
  ; Prints the event vector to the console only in debug mode.
  ;
  ; @param (map) context
  ;
  ; @return (map)
  [context]
  (if @state/DEBUG-MODE? (-> context :coeffects :event println))
  (return context))

; @constant (?)
(def log-event! (core/->interceptor :id :re-frame/log-event! :before log-event-f))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn debug-f
  ; @ignore
  ;
  ; @description
  ; Prints the event vector to the console.
  ;
  ; @param (map) context
  ;
  ; @return (map)
  [context]
  (-> context :coeffects :event println)
  (return context))

; @constant (?)
(def debug! (core/->interceptor :id :re-frame/debug! :after debug-f))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn interceptors<-system-interceptors
  ; @ignore
  ;
  ; @param (vector) interceptors
  ;
  ; @return (vector)
  [interceptors]
  (vector/conj-item interceptors log-event!))
