(ns gameoflife.game
  (:require [clojure.string :as str]))

(defn enumerate [seq] (map-indexed vector seq))

(defn create-board [width height & [_alive]]
  "alive is a set of coordinate pairs (vectors) which are alive"
  (let [alive (or _alive #{})]
   (vec (for [rownum (range height)]
          (vec (for [colnum (range width)]
                 (if (alive [rownum colnum] true) false)))))))

;; see http://programmablelife.blogspot.com/2012/08/conways-game-of-life-in-clojure.html
(defn count-live-neighbors [[rownum colnum] & [board]]
  (apply +
   (apply concat
          (for [row-offset [-1 0 1]]
            (for [col-offset [-1 0 1]]
              (if (and (not= 0 row-offset col-offset)
                       (get-in board [(+ rownum row-offset)
                                      (+ colnum col-offset)]))
                1 0))))))

(defn next-board [board]
  (vec (for [[rownum row] (enumerate board)]
         (vec (for [[colnum cell] (enumerate row)]
                (let [neighbor-count
                      (count-live-neighbors [rownum colnum] board)]
                  (cond
                   (and cell (> 2 neighbor-count)) false
                   (and cell (#{2 3} neighbor-count)) cell
                   (and cell (< 3 neighbor-count)) false
                   ;; neighbor-count # Z, so if we're here cell is false
                   (= 3 neighbor-count) true
                   :else false)))))))

(defn bool->int [b] (if b 1 0))

(defn serialize-board [board]
  (str/join "\n" (map #(str/join " " (map bool->int %)) board)))

(defn run-cli []
  (enable-console-print!)
  (let [node-require (fn [s] (js/require s))
        input-filename (-> (.-argv js/process) js->clj (nth 2))
        fs (node-require "fs")]
    (.readFile fs
     input-filename #js {:encoding "utf8"}
     (fn [err data]
       (if err (throw err)
           (let [lines (-> data str/split-lines vec)
                 board (vec (for [line lines]
                              (vec
                               (for [cell (str/split line #"\s+")]
                                 (= cell "1")))))]
             (println (str (-> board next-board serialize-board)))))))))

(set! *main-cli-fn* run-cli)
