;; src/crawl_test_data/core.clj: generates dummy HTML pages
;;
;; Copyright 2013, F.M. de Waard & Vixu.com <fmw@vixu.com>.
;;
;; Licensed under the Apache License, Version 2.0 (the "License");
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at
;; 
;; http://www.apache.org/licenses/LICENSE-2.0
;; 
;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.

(ns crawl-test-data.core
  (:require [net.cgrand.enlive-html :as html]))

(html/defsnippet menu-item-snippet
  "templates/page.html"  
  [[:ul#menu] [:li (html/nth-of-type 1)]]
  [{:keys [text uri] :as link}]
  [:a]
  (if link
    (html/do->
     (html/set-attr :href uri)
     (html/content text))
    (html/substitute nil)))

(html/deftemplate dummy-page-template
  "templates/page.html"
  [title links]
  [:title]
  (html/content title)
  [:ul#menu]
  (when (not-empty links)
    (html/do->
     (html/html-content "")
     (html/append (map menu-item-snippet links)))))