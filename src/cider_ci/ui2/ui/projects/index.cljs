(ns cider-ci.ui2.ui.projects.index
  (:require-macros
    [reagent.ratom :as ratom :refer [reaction]]
    [cljs.core.async.macros :refer [go]]

    )
  (:require
    [cider-ci.ui2.ui.projects.orientation :as orientation]
    [cider-ci.ui2.ui.projects.table :as table]
    [cider-ci.ui2.constants :refer [REPOSITORY-CONTEXT CONTEXT]]
    [cider-ci.ui2.ui.projects.state :as state]

    [cider-ci.utils.url]

    [cljs-http.client :as http]
    [cljs.core.async :refer [<!]]
    [cljs.pprint :refer [pprint]]
    ))

(defn page []
  [:div.projects
   [:div.row.orientation
    [:div.col-md-6 [orientation/breadcrumbs {}]]]
   [:div
    [:ul.actions.list-inline.pull-right
     [:li
      [:a.btn.btn-primary
       {:href (str REPOSITORY-CONTEXT "/projects/new")}
       [:i.fa.fa-plus-circle] " Add a new project"]]]
    [:h2 "Projects"]
    [:p "This page is is mainly concerned with the "
     [:b " connection "] "of your projects to their " [:b "origin"] "."
     "See the " [:b [:a {:href "/cider-ci/ui/workspace"} " workspace " ]] "page to inspect "
     [:b "commits" ] " and " [:b " jobs."]]]
   [table/table]])

