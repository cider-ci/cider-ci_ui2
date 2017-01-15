(ns cider-ci.ui2.ui.projects
  (:require-macros
    [reagent.ratom :as ratom :refer [reaction]]
    [cljs.core.async.macros :refer [go]]
    )
  (:require
    [secretary.core :as secretary :include-macros true]
    ))
