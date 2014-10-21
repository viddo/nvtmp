(ns nvmyalt.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)

(def app-state (atom {:notes [{:body "First example" :created (js/Date.)}
                              {:body "Another note as example" :created (js/Date.)}
                              ]

                      }
                     )
  )

(defn note-preview [note owner]
  (reify
    om/IRender
    (render [this]
            (dom/tr nil
                    (dom/td nil (:body note))
                    (dom/td nil (:created note))
                    ))))

(defn filtered-notes [app owner]
  (reify
    om/IRender
    (render [this]
            (dom/table nil
                     (apply dom/div nil
                            (om/build-all note-preview (:notes app)))))))

(defn main-view [app owner]
  (reify om/IRender
    (render [_]
      (dom/div nil
               (dom/input #js { :placeholder "Search or press enter to create a new untitled note" })
               (apply dom/div nil
                        (map (fn [{:keys [body]}] (dom/div nil body)) (:notes app)))
               (dom/div nil (.toString (js/Date.)))
       ))))


(om/root main-view app-state
  {:target (. js/document (getElementById "app"))})
