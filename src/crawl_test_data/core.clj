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

(html/defsnippet link-li-snippet
  "templates/page.html"  
  [[:ul#menu] [:li (html/nth-of-type 1)]]
  [{:keys [text uri] :as link}]
  [:a]
  (if link
    (html/do->
     (html/set-attr :href uri)
     (html/content (str text)))
    (html/substitute nil)))

(html/deftemplate dummy-page-template
  "templates/page.html"
  [title menu-links body-links]
  [:title]
  (html/content (str title))
  [:ul#menu]
  (when (not-empty menu-links)
    (html/do->
     (html/html-content "")
     (html/append (map link-li-snippet menu-links))))
  [:ul#body-links]
  (when (not-empty body-links)
    (html/do->
     (html/html-content "")
     (html/append (map link-li-snippet body-links)))))

(defn make-link
  [v]
  {:text v
   :uri (str "/" v ".html")})

(defn write-page
  [directory filename html-seq]
  (spit (str directory filename) (apply str html-seq)))

(def alphabet "abcdefghijklmnopqrstuvwxyz")

(defn write-dummy-alphabet-pages!
  "Writes dummy pages for the alphabet to disk, using the provided
   base directory."
  [directory]
  (let [menu-links (map make-link alphabet)
        directory (if (= (last directory) \/)
                    directory
                    (str directory "/"))]
    (doseq [[letter start-n]
            (map-indexed (fn [i c] [c (* 10 i)]) alphabet)]
      (write-page directory
                  (str letter ".html")
                  (dummy-page-template letter
                                       menu-links
                                       (map make-link
                                            (range start-n (+ 10 start-n)))))
      (doseq [n (range start-n (+ 10 start-n))]
        (write-page directory
                    (str n ".html")
                    (dummy-page-template n
                                         menu-links
                                         []))))))