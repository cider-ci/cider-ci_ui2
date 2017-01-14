(ns cider-ci.ui2.ui.debug
  (:require-macros
    [cljs.core.async.macros :as asyncm :refer (go go-loop)]
    [reagent.ratom :as ratom :refer [reaction]]
    )
  (:require
    [cider-ci.ui2.shared :refer [pre-component]]
    [cider-ci.ui2.constants :refer [CONTEXT]]
    [cider-ci.utils.core :refer [presence]]
    [cider-ci.ui2.ui.state :as state]
    [cider-ci.ui2.ui.state.projects :as state.projects]

    [secretary.core :as secretary :include-macros true]
    ))

(declare page)

(secretary/defroute (str CONTEXT "/debug") []
  (swap! state/page-state assoc :current-page
         {:component #'page}))

(defn toggle-debug []
  (swap! state/client-state
         (fn [cs]
           (assoc cs :debug (not (:debug cs))))))

(defn general-debug-section []
  (when (:debug @state/client-state)
    [:section.debug
     [:hr]
     [:h1 "Debug"]
     [:hr]
     [:h2 "General"]
     [:div.client-state
      [:h3 "Client State"]
      [pre-component @state/client-state]]
     [:div.client-state
      [:h3 "Server State"]
      [pre-component @state/client-state]]

     [:hr]
     [:h2 "Projects"]
     [:div.client-state
      [:h3 "Projects Client State"]
      [pre-component @state.projects/client-state]]
     [:div.client-state
      [:h3 "Projects Server State"]
      [pre-component @state.projects/server-state]]


     [:hr]
     [:div.page-state
      [:h3 "Page State"]
      [:pre (.stringify js/JSON (clj->js @state/page-state) nil 2)]]
     ]))

(defn page []
  [:div
   [:h1 "Debug"]
   [:p [:input {:style {:margin-left "1em"}
                :type "checkbox"
                :on-change toggle-debug
                :checked (:debug @state/client-state)
                }] " Debug state"]])
