(ns cider-ci.ui2.ui.projects.orientation
  (:require-macros
    [reagent.ratom :as ratom :refer [reaction]]
    [cljs.core.async.macros :refer [go]]
    )
  (:require
    [cider-ci.ui2.constants :refer [REPOSITORY-CONTEXT CONTEXT]]

    [cider-ci.ui2.ui.projects.request :as request]
    [cider-ci.ui2.ui.projects.state :as state]

    [cider-ci.utils.url]

    [accountant.core :as accountant]
    [cljs.pprint :refer [pprint]]
    [reagent.core :as r]))


(defn breadcrumbs [params]
  (fn [params]
    (let [{id :id issue-key :issue-key} params]
      [:ol.breadcrumb
       [:li [:a {:href "/cider-ci/ui/public"} "Home"]]
       [:li [:a {:href (str REPOSITORY-CONTEXT "/projects/")} "Projects"]]
       (when id
         [:li [:a {:href (str REPOSITORY-CONTEXT "/projects/" id)} "Project"]])
       (when (and id issue-key)
         [:li [:a {:href (str REPOSITORY-CONTEXT "/projects/" id "/issues/" issue-key)} "Issue"]])
       ])))

