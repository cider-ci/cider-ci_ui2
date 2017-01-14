(ns cider-ci.ui2.ui.commits
  (:require-macros
    [cljs.core.async.macros :as asyncm :refer (go go-loop)]
    [reagent.ratom :as ratom :refer [reaction]]
    )
  (:require
    [cider-ci.ui2.constants :refer [CONTEXT]]
    [cider-ci.utils.core :refer [presence]]
    [cider-ci.ui2.ui.state :as state]
    [secretary.core :as secretary :include-macros true]
    ))

(declare page)

(secretary/defroute path (str CONTEXT "/commits/") []
  (swap! state/page-state assoc :current-page
         {:component #'page}))

(def data (reaction (-> @state/client-state :commits)))

;;; form ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def form-data (reaction (-> @data :form-data)))

(defn update-form-data [fun]
  (swap! state/client-state
         (fn [cs]
           (assoc-in cs [:commits :form-data]
                     (fun (-> cs :commits :form-data))))))

(defn update-form-data-value [k v]
  (update-form-data (fn [fd] (assoc fd k v))))

;;; branch-name
(defn branch-name [tabindex]
  [:div
   [:label.sr-only {:for :branch-name}]
   [:input.form-control.branch-name
    {:type :text
     :placeholder "Branch name"
     :value (:branch-name @form-data)
     :on-change #(update-form-data-value
                   :branch-name (-> % .-target .-value presence))
     :tabindex tabindex}]])

;;; heads only

(defn toggle-heads-only []
  (update-form-data
    (fn [fd]
      (assoc fd :heads-only
             (-> fd :heads-only boolean not)))))

(defn heads-only [tabindex]
  [:div
   [:input#heads-only.heads-only
    {:type :checkbox
     :on-change toggle-heads-only
     :checked (:heads-only @form-data)
     :value true
     :tabindex tabindex}]
   [:span " Branch heads only"]])

;;; git-ref
(defn git-ref [tabindex]
  [:div
   [:label.sr-only {:for :git-ref}]
   [:input#git-ref.form-control.git-ref
    {:type :text
     :placeholder "Git ref (commit-іd or tree-id)"
     :value (:git-ref @form-data)
     :on-change #(update-form-data-value
                   :git-ref (-> % .-target .-value presence))
     :tabindex 3 }]])

;;; project-name
(defn project-name [tabindex]
  [:div
   [:label.sr-only {:for :project-name}]
   [:input.form-control.project-name
    {:type :text
     :placeholder "Project name"
     :value (:project-name @form-data)
     :on-change #(update-form-data-value
                   :project-name (-> % .-target .-value presence))
     :tabindex tabindex}]])

;;; my commits

(defn toogle-my-commits []
  (update-form-data
    (fn [fd]
      (assoc fd :my-commits
             (-> fd :my-commits boolean not)))))

(defn my-commits [tabindex]
  [:div
   [:input#my-commits.my-commits
    {:type :checkbox
     :on-change toogle-my-commits
     :checked (:my-commits @form-data)
     :value true
     :tabindex tabindex}]
   [:span " My commits "]])


;;; main form

(defn form []
  [:div.well.form
   [:div.fieldset.row
    [:div.form-group.col-sm-4
     [project-name 1]]
    [:div.form-group.col-sm-4
     [branch-name 2]]
    [:div.form-group.col-sm-4
     [git-ref 3]]]
   [:div.fieldset.row
    [:div.col-sm-2 [my-commits 4]]
    [:div.form-group.col-sm-3 [heads-only 5]]
    [:div.form-group.col-sm-5]
    [:div.form-group.col-sm-2
     [:div.pull-right
      [:a.btn.btn-primary
       [:i.fa.fa-filter]
       " Filter "]]]]])


;;; page ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn page []
  [:div
   [:h1 "Commits"]
   [form]
   ])
