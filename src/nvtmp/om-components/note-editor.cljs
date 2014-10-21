(ns nvtmp.om-components.note-editor
  (:require
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]
   [nvtmp.notes :as notes]))

(defn component [app owner]
  (reify
    om/IDidUpdate
    (did-update [_ _ _]
                (if (:editing app)
                  (-> (om/get-node owner "noteEditor")
                      (.focus))))
    om/IRender
    (render [_]
            (let [note (notes/selected (:notes app))]
              (if note
                (dom/div #js { :contentEditable true
                               :className "note-editor"
                               :ref "noteEditor" }
                         (:body note))
                (dom/div #js { :className "note-editor" }
                         (dom/em nil
                                 (dom/small nil "No note selected... press <down>/<up> to select an existing note from the list, or <enter> to create a new note with current search text as title."))))))))
