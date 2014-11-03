(ns nvtmp.om-components.filtered-notes
  (:require-macros
   [cljs.core.async.macros :refer [go]])
  (:require
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]
   [cljs.core.async :refer [put! chan <!]]))


(defn str-contains? [needle haystack]
  (-> (.search haystack (.toLowerCase needle))
      (>= 0)))

(defn note-contains-str? [s note]
  (some (partial str-contains? s) (map #(note %) [:title :body])))

(defn filter-notes-by-str [notes s]
   (if (empty? s)
     notes
     (filter (partial note-contains-str? s) notes)))


(defn preview-body [note]
  (let [body (:body note)]
    (if-not (empty? body)
      (str (subs body 0 25) "…"))))

(defn select-note! [note app]
  (om/transact! app :notes
                (fn [xs] (map #(condp = %
                                 note (assoc % :selected true)
                                 (dissoc % :selected))
                              xs))))

(defn row [note owner]
  (reify
    om/IRenderState
     (render-state [_ {:keys [select-chan]}]
      (dom/tr #js { :className ({true "selected"} (:selected note))
                    :onClick #(put! select-chan note) }
              (dom/td nil
                      (:title note)
                      (dom/span #js { :className "title-preview-separator" } " — ")
                      (dom/span #js { :className "preview" } (preview-body note)))))))


(defn component [app owner]
  (reify
    om/IInitState
    (init-state [_]
                {:select-chan (chan)})
    om/IWillMount
    (will-mount [_]
                (let [select-chan (om/get-state owner :select-chan)]
                  (go (loop []
                        (let [note (<! select-chan)]
                          (select-note! note app)
                          (recur))))))
    om/IRenderState
    (render-state [_ {:keys [select-chan]}]
            (dom/div #js { :className "filtered-notes" }
                     (dom/table nil
                                (dom/thead nil
                                           (dom/tr nil
                                                   (dom/th nil "Note")))
                                (apply dom/tbody nil
                                       (om/build-all row (filter-notes-by-str (:notes app) (:filter app))
                                                     {:init-state {:select-chan select-chan}})))))))
