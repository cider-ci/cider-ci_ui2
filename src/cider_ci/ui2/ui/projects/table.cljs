(ns cider-ci.ui2.ui.projects.table
  (:require-macros
    [reagent.ratom :as ratom :refer [reaction]]
    )
  (:require
    [cider-ci.ui2.constants :refer [REPOSITORY-CONTEXT CONTEXT]]
    [cider-ci.ui2.ui.projects.branches :as branches]
    [cider-ci.ui2.ui.projects.fetch-and-update :as fetch-and-update]
    [cider-ci.ui2.ui.projects.push-hooks :as push-hooks]
    [cider-ci.ui2.ui.projects.push-notifications :as push-notifications]
    [cider-ci.ui2.ui.projects.shared :refer [humanize-datetime]]
    [cider-ci.ui2.ui.projects.state :as state]
    [cider-ci.ui2.ui.projects.status-pushes :as status-pushes]

    [cider-ci.ui2.ui.state]

    [accountant.core :as accountant]
    [clojure.contrib.humanize]
    [clojure.contrib.inflect :refer [pluralize-noun]]
    [reagent.core :as r]
    ))


;### sorting ##################################################################

(def order (reaction (or (-> @cider-ci.ui2.ui.state/page-state :current-page :query-params :order)
                         "name")))

(defn most-recent-first-sort-fn [o]
  (- (.unix (cond
              (string? o) (js/moment o)
              :else (js/moment)))))

(def sort-fn
  (reaction
    (case @order
      "name" (fn [project] (:name project))
      "fetched" (fn [project] (most-recent-first-sort-fn (:last_fetched_at project)))
      "last_committed_at" (fn [project] (most-recent-first-sort-fn (:last_commited_at project)))
      (fn [project] (:name project)))))


;##############################################################################

(defn name-color-class [project]
  (let [cell-classes (map (fn [fun] (apply fun [project]))
                          [branches/color-class
                           fetch-and-update/color-class
                           push-notifications/color-class
                           push-hooks/color-class
                           status-pushes/color-class
                           ])]
    (cond
      (some #{"executing" "active"} cell-classes) "executing"
      (some #{"danger"} cell-classes) "danger"
      (every? #{"success" "default"} cell-classes) "success"
      :else "warning")))

(defn row [id project]
  (fn [id project]
    [:tr.text-center
     [:td.name {:class (name-color-class project)}
      [:a {:href (str REPOSITORY-CONTEXT "/projects/" (:id project))}
       (:name project)]]
     [branches/td project]
     [fetch-and-update/td project]
     [push-notifications/td project]
     [push-hooks/td project]
     [status-pushes/td project]]))


;##############################################################################

(defn th-name []
  [:th.text-center {:key :name }
   [:div {:class "form-inline"}
    " Name " ]])


;##############################################################################

(defn table []
  [:div
      [:table.table.table-striped.table-projects
    [:thead
     [:tr.text-center {:key :thr}
      [th-name]
      [branches/th]
      [fetch-and-update/th]
      [push-notifications/th]
      [push-hooks/th]
      [status-pushes/th]]]
    [:tbody.table-bordered
     (for [[id project] (->> @state/db
                             :repositories
                             (sort-by (fn [[k v]] (apply @sort-fn [v]))))]
       ^{:key (str "project_row_" id)} [row id project])]]])
