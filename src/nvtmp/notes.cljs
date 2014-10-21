(ns nvtmp.notes)

(defn selected [notes]
  (first (filter #(:selected %) notes)))

