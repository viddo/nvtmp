(ns nvtmp.om-components.filtered-notes
  (:require
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]))


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

(defn row [note owner]
  (reify
    om/IRender
     (render [_]
      (dom/tr #js { :className ({true "selected"} (:selected note)) }
              (dom/td nil
                      (:title note)
                      (dom/span #js { :className "title-preview-separator" } " — ")
                      (dom/span #js { :className "preview" } (preview-body note)))
              ))))


(defn component [app owner]
  (reify
    om/IRender
    (render [_]
            (dom/div #js { :className "filtered-notes" }
                     (dom/table nil
                                (dom/thead nil
                                           (dom/tr nil
                                                   (dom/th nil "Note")
                                                   ))
                                (apply dom/tbody nil
                                       (om/build-all row (filter-notes-by-str (:notes app) (:filter app)))))))))
