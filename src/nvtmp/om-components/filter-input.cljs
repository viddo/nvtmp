(ns nvtmp.om-components.filter-input
  (:require
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]
   [nvtmp.notes :as notes]))

(defn input-node [owner]
  (om/get-node owner "filterInput"))

(defn filter! [app new-filter]
  (let [deselected-notes (map #(dissoc % :selected) (:notes @app))]
    (om/transact! app #(assoc %
                         :editing false
                         :filter new-filter
                         :notes deselected-notes))))

(defn handle-filter-input [_ app owner]
  (filter! app (-> (input-node owner)
                   (.-value))))

(defn component [app owner]
  (reify
    om/IDidUpdate
    (did-update [_ _ _]
                (if (not (:editing app))
                  (-> (input-node owner)
                      (.focus))))
    om/IRender
    (render [_]
            (dom/input
             #js { :placeholder "Search or press <enter> to create a new untitled note"
                   :type "text"
                   :className "filter-input"
                   :onChange #(handle-filter-input % app owner)
                   :ref "filterInput"
                   :value (:filter app)
                   }))))
