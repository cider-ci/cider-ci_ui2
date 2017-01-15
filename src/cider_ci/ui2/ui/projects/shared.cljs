(ns cider-ci.ui2.ui.projects.shared
  (:require-macros
    [reagent.ratom :as ratom :refer [reaction]]
    [cljs.core.async.macros :refer [go]]
    )
  (:require
    [cider-ci.ui2.ui.projects.request :as request]

    [cider-ci.ui2.ui.projects.state :as state]

    [cider-ci.utils.url]

    [accountant.core :as accountant]
    [cljs.pprint :refer [pprint]]
    [reagent.core :as r]))


(defn humanize-datetime [_ dt]
  (.to (js/moment) dt))


