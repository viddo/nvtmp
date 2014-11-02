(ns nvtmp.om-components.filter-input
  (:require
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]
   [nvtmp.notes :as notes]))

(defn input-node [owner]
  (om/get-node owner "filterInput"))

(defn input-node-value [owner]
 (-> (input-node owner)
     (.-value)))

(defn filter! [app new-filter]
  (let [deselected-notes (map #(dissoc % :selected) (:notes @app))]
    (om/transact! app #(assoc %
                         :filter new-filter
                         :notes deselected-notes))))

(defn handle-filter-change [_ app owner]
  (filter! app (input-node-value owner)))


(def ENTER_KEY 13)

(defn str-or-default [s default]
 ({ false s, true default } (empty? s)))


(defn create-note! [app title]
  (om/update! app :notes (conj (:notes @app)
                               { :title title
                                 :body ""
                                 :selected true })))

(defn handle-key-down [e app owner]
  (condp == (.-keyCode e)
    ENTER_KEY (create-note! app (str-or-default
                                 (input-node-value owner)
                                 "Untitled"))
    nil))

(defn component [app owner]
  (reify
    om/IRender
    (render [_]
            (dom/input
             #js { :placeholder "Search or press <enter> to create a new untitled note"
                   :type "text"
                   :className "filter-input"
                   :onChange #(handle-filter-change % app owner)
                   :onKeyDown #(handle-key-down % app owner)
                   :ref "filterInput"
                   :value (:filter app)
                   }))))
