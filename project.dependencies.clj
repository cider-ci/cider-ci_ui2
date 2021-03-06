[
 [drtom/honeysql "1.3.0-beta.4"]
 [logbug "4.0.0"]

 [cheshire "5.5.0"]
 [clj-http "2.1.0"]
 [environ "1.1.0"]
 [me.raynes/fs "1.4.6"]
 [org.apache.commons/commons-io "1.3.2"]
 [org.clojure/core.incubator "0.1.3"]
 [org.clojure/tools.nrepl "0.2.12"]

 [cljs-http "0.1.41"]
 [cljsjs/bootstrap "3.3.6-1"]
 [cljsjs/jquery "2.2.4-0"]
 [cljsjs/moment "2.10.6-4"]
 [clojure-humanize "0.2.0"]
 [com.taoensso/sente "1.10.0"]
 [compojure "1.5.1"]
 [hiccup "1.0.5"]
 [org.clojure/clojurescript "1.9.93" :scope "provided"]  ; see guava below; also check `lein tree` and sync
 [prismatic/schema "1.1.3"]
 [reagent "0.6.0-rc"]
 [reagent-utils "0.1.9"]
 [ring "1.5.0"]
 [ring-middleware-accept "2.0.3"]
 [ring-middleware-accept "2.0.3"]
 [ring-server "0.4.0"]
 [ring/ring-defaults "0.2.1"]
 [secretary "1.2.3"]
 [timothypratley/patchin "0.3.5"]
 [venantius/accountant "0.1.7" :exclusions [org.clojure/tools.reader]]
 [yogthos/config "0.8"]


 ; explicit transient deps to force conflict resolution
 [com.google.guava/guava "19.0"]
 ]
