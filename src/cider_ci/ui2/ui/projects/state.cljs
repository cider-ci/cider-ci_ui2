(ns cider-ci.ui2.ui.projects.state
  (:refer-clojure :exclude [str keyword])

  (:require-macros
    [cljs.core.async.macros :as asyncm :refer (go go-loop)]
    [reagent.ratom :as ratom :refer [reaction]]
    )

  (:require
    [cider-ci.utils.core :refer [keyword str presence]]
    [cider-ci.utils.digest :refer [digest]]
    [cljsjs.moment]
    [reagent.core :as r]
    [timothypratley.patchin :refer [patch]]
    [goog.dom :as dom]
    [goog.dom.dataset :as dataset]
    [taoensso.sente  :as sente :refer (cb-success?)]
    ))


(defonce server-state (r/atom {}))

(defonce client-state (r/atom {}))

(def db (reaction (merge @server-state @client-state)))

(let [{:keys [chsk ch-recv send-fn state]}
      (sente/make-channel-socket! (str "/cider-ci/repositories" "/ws/chsk")
                                  {:type :auto})]
  ;(swap! client-state assoc :ws-connection state)
  (def chsk chsk)
  (def ch-chsk ch-recv)
  (def chsk-send! send-fn)
  (def chsk-state state))

(defn update-server-state [data]
  (let [new-state (if-let [full (:full data)]
                    (reset! server-state full)
                    (if-let [p (:patch data)]
                      (swap! server-state (fn [db p] (patch db p)) p)
                      (throw (ex-info "Either :full or :patch must be supplied" {}))))
        _ (swap! client-state assoc :server_state_updated_at (js/moment))
        new-state-digest (digest new-state)
        supposed-digest (:digest data)]
    (if (= new-state-digest supposed-digest)
      (swap! client-state assoc :server-state-is-in-sync true)
      (do (swap! client-state assoc-in
                 :server-state-is-in-sync false
                 :last-bad-server-state-sync-at (js/moment)
                 :last-bad-server-state-sync-data new-state)))))

(defn event-msg-handler [{:as message :keys [id ?data event]}]
  ;(js/console.log (clj->js {:id id :message message}))
  (case id
    :chsk/recv (let [[event-id data] ?data]
                 (when (and event-id data)
                   ;(js/console.log (clj->js {:event-id event-id :data data}))
                   (case event-id
                     :cider-ci.repository/db (update-server-state data))
                   ))
    :chsk/state (let [[_ new-state] ?data]
                  (swap! client-state assoc :connection
                         (assoc new-state :updated_at (js/moment))
                         ))

    nil))


(sente/start-chsk-router! ch-chsk event-msg-handler)

