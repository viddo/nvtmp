(ns nvtmp.app
  (:require
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]
   [nvtmp.om-components.filter-input :as filter-input]
   [nvtmp.om-components.filtered-notes :as filtered-notes]
   [nvtmp.om-components.note-editor :as note-editor]
   [nvtmp.notes :as notes]))

(enable-console-print!)

(def app-state (atom {:filter ""
                      :editing false
                      :notes [{ :title "1st" :body "First example" }
                              { :title "2nd" :body "Another note as example" }
                              { :title "bigger" :body "third\nwith some paragraphs\nof text should be searchable nevertheless" }
                              ]
                      }))

(defn main-view [app owner]
  (reify om/IRender
    (render [_]
            (dom/div #js { :className "container" }
                     (om/build filter-input/component app)
                     (om/build filtered-notes/component app)
                     (om/build note-editor/component app)
                     ))))

(om/root main-view app-state
  {:target (. js/document (getElementById "app"))})
