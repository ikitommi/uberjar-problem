(ns protocol-bug.middleware
  (:require [protocol-bug.env :refer [defaults]]
            [cheshire.generate :as cheshire]
            [cognitect.transit :as transit]
            [clojure.tools.logging :as log]
            [protocol-bug.layout :refer [error-page]]
            [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
            [ring.middleware.webjars :refer [wrap-webjars]]
            [protocol-bug.middleware.formats :as formats]
            [muuntaja.middleware :refer [wrap-format wrap-params]]
            [protocol-bug.config :refer [env]]
            [ring.middleware.flash :refer [wrap-flash]]
            [immutant.web.middleware :refer [wrap-session]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [muuntaja.format.core :as cf]
            [muuntaja.core :as m]
            [muuntaja.middleware :as mw])
  (:import  [org.joda.time ReadableInstant])
  (:gen-class))

(defn wrap-internal-error [handler]
  (fn [req]
    (try
      (handler req)
      (catch Throwable t
        (log/error t (.getMessage t))
        (error-page {:status 500
                     :title "Something very bad has happened!"
                     :message "We've dispatched a team of highly trained gnomes to take care of the problem."})))))

(defn wrap-csrf [handler]
  (wrap-anti-forgery
    handler
    {:error-response
     (error-page
       {:status 403
        :title "Invalid anti-forgery token"})}))


(defn wrap-formats [handler]
  (let [wrapped (-> handler wrap-params (wrap-format formats/instance))]
    (fn [request]
      ;; disable wrap-formats for websockets
      ;; since they're not compatible with this middleware
      ((if (:websocket? request) handler wrapped) request))))

(defn wrap-base [handler]
  (-> ((:middleware defaults) handler)
      wrap-webjars
      wrap-flash
      (wrap-session {:cookie-attrs {:http-only true}})
      (wrap-defaults
        (-> site-defaults
            (assoc-in [:security :anti-forgery] false)
            (dissoc :session)))
      wrap-internal-error))

(defn encoder [_]
  (reify
    cf/EncodeToBytes
    (encode-to-bytes [_ data charset]
      nil)
    cf/EncodeToOutputStream
    (encode-to-output-stream [_ data charset]
      nil)))


(defn decoder [options]
  (reify cf/Decode
    (decode [this data charset]
      nil)))


(def options
  (m/create (-> m/default-options
                (assoc-in [:formats "application/xml"]
                          {
                           :encoder [encoder]
                           :decoder [decoder]
                           })
               )))