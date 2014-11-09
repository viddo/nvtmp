(ns nvtmp.om-components.note-editor
  (:require
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]
   [nvtmp.notes :as notes]))

(defn input-node [owner]
  (om/get-node owner "noteEditor"))

(defn input-node-value [owner]
 (-> (input-node owner)
     (.-textContent)))

(defn handle-key-down [e note app owner]
  (let [body (input-node-value owner)]
    (om/transact! app :notes
                  (fn [xs] (map #(condp = %
                                   note (assoc % :body body)
                                   %)
                                xs)))))

(defn component [app owner]
  (reify
    om/IRender
    (render [_]
            (let [note (first (filter #(:selected %) (:notes app)))]
              (if note
                (dom/div #js { :contentEditable true
                               :className "note-editor"
                               :ref "noteEditor"
                               :onKeyUp #(handle-key-down % note app owner) }
                         (:body note))
                (dom/div #js { :className "note-editor" }
                         (dom/em nil
                                 (dom/small nil "No note selected... press <down>/<up> to select an existing note from the list, or <enter> to create a new note with current search text as title."))))))))
