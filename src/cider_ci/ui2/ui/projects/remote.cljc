; Copyright © 2013 - 2016 Dr. Thomas Schank <Thomas.Schank@AlgoCon.ch>

(ns cider-ci.ui2.ui.projects.remote
  (:require
    [cider-ci.utils.url :as url]
    [cider-ci.utils.core :refer [presence]]
    ))

(defn api-type [project]
  (or (:remote_api_type project)
      (cond
        (= "github.com" (-> project :git_url cider-ci.utils.url/dissect :host)) "github"
        (= "gitlab.com" (-> project :git_url cider-ci.utils.url/dissect :host)) "gitlab"
        :else (-> project :remote_api_type presence))))

(defn github-api-endpoint [repository]
  (let [dissected-url (-> repository :git_url url/dissect)
        protocol (:protocol dissected-url)]
    (cond (= "github.com" (:host dissected-url)) "https://api.github.com"
          (re-matches #"(?i)^http.*" protocol) (str protocol "://" "api." (:host_port dissected-url))
          :else nil)))

(defn gitlab-api-endpoint [repository]
  (let [dissected-url (-> repository :git_url url/dissect)
        protocol (:protocol dissected-url)]
    (cond (= "gitlab.com" (:host dissected-url)) "https://gitlab.com"
          (and (presence protocol)
               (re-matches #"(?i)^http.*" protocol)) (str protocol "://" (:host_port dissected-url))
          :else nil)))

(defn api-endpoint [repository]
  (presence (or (:remote_api_endpoint repository)
                (case (api-type repository)
                  "github" (github-api-endpoint repository)
                  "gitlab" (gitlab-api-endpoint repository)
                  nil))))

(defn api-endpoint! [repository]
  (or (api-endpoint repository)
      (throw (ex-info "The required api-endpoint could not be inferred"
                      {:repository repository}))))

(defn api-namespace [repository]
  (presence (or (:remote_api_namespace repository)
                (-> repository :git_url url/dissect :project_namespace))))

(defn api-namespace! [repository]
  (or (api-namespace repository)
      (throw (ex-info "The required api-namespace could not be inferred."
                      {:repository repository}))))

(defn api-name [repository]
  (presence (or (:remote_api_name repository)
                (-> repository :git_url url/dissect :project_name))))

(defn api-name! [repository]
  (or (api-name repository)
      (throw (ex-info "The required api-name could not be inferred."
                      {:repository repository}))))


(defn api-token! [repository]
  (or (-> repository :remote_api_token presence)
      (throw (ex-info "The required api-token could not be inferred."
                      {:repository repository}))))

(defn gitlab-api-access? [project]
  (boolean (and
             (or (= "gitlab" (:remote_api_type project))
                 (= "gitlab.com" (-> project :git_url
                                     cider-ci.utils.url/dissect :host)))
             (presence (:remote_api_token project))
             (api-endpoint project)
             (api-namespace project)
             (api-name project))))

(defn github-api-access? [project]
  (boolean (and
             (or (= "github" (:remote_api_type project))
                 (= "github.com" (-> project :git_url
                                     cider-ci.utils.url/dissect :host)))
             (presence (:remote_api_token project))
             (api-endpoint project)
             (api-namespace project)
             (api-name project))))

(defn api-access? [project]
  (case (api-type project)
    "github" (github-api-access? project)
    "gitlab" (gitlab-api-access? project)
    false))

