(ns protocol-bug.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[protocol-bug started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[protocol-bug has shut down successfully]=-"))
   :middleware identity})
