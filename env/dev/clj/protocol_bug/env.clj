(ns protocol-bug.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [protocol-bug.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[protocol-bug started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[protocol-bug has shut down successfully]=-"))
   :middleware wrap-dev})
