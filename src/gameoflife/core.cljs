(ns gameoflife.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [gameoflife.game :refer [enumerate create-board count-live-neighbors
                                     next-board]]))

(def game-atom (atom {:paused true
                      :board (create-board 12 12)}))

(defn frankenstein! [[row col]]
  "because we're giving life. Badump-tshh."
  (reset! game-atom
   (update-in @game-atom [:board row col] (constantly true))))

(def blinker #{[1 1] [1 2] [1 3]})
(def glider #{[0 3] [1 3] [2 3] [2 2] [1 1]})
(defn set-pattern! [pattern]
  (reset! game-atom (assoc @game-atom :board (create-board 12 12 pattern))))

(defn set-glider! [] (set-pattern! glider))
(defn set-blinker! [] (set-pattern! blinker))

(defn clear-board! []
  (reset! game-atom (assoc @game-atom :board (create-board 12 12))))

(defn toggle-playstate! [e]
  (reset! game-atom
   (update-in @game-atom [:paused] not)))

(defn planck! []
  (when-not (:paused @game-atom)
      (reset! game-atom
            (update-in @game-atom [:board] next-board))))

(defn board-cell [[active rownum colnum]]
  (reify
    om/IRender
    (render [this]
      (dom/div #js {:className "col-md-1"}
               (dom/button
                #js {:onClick #(frankenstein! [rownum colnum])
                     :className "btn"}
                (str (if active "X" "")))))))

(defn board-row [[row-cursor rownum]]
  (reify
    om/IRender
    (render [this]
      (apply dom/div #js {:className "row board-row"}
        (om/build-all board-cell row-cursor
         {:fn (fn [cursor i] [cursor rownum i])})))))

(defn display-board [{:keys [board paused]}]
  (dom/div
   #js {:id "gameoflife-app" :className "container"}
   (dom/div
    #js {:className "row"}
    (dom/h1 nil "Game of Life"))
   (dom/div #js {:id "playpause" :className "row pprow"}
    (dom/div #js {:className "col-md-4 col-md-offset-4 text-center"}
     (let [playstate (if paused "play" "pause")]
       (dom/a #js {:className (str "btn btn-playstate btn-primary btn-" playstate)
                   :onClick toggle-playstate!
                   :title playstate}
        (dom/i #js {:className (str "fa fa-" playstate)})))
     (dom/a #js {:className (str "btn btn-playstate btn-danger")
                 :onClick clear-board!
                 :title "clear board"}
        (dom/i #js {:className "fa fa-stop"}))
     (dom/a #js {:className (str "btn btn-playstate btn-primary")
                 :onClick set-glider!
                 :title "glider"}
            "glider")
     (dom/a #js {:className (str "btn btn-playstate btn-primary")
                 :onClick set-blinker!
                 :title "blinker"}
            "blinker")))
   (dom/div #js {:id "tttboard"
                 :style #js {:width "75%"
                             :padding-bottom "75%"
                             :margin-left "auto" :margin-right "auto"
                             :position "relative"}}
            (apply dom/div #js {:style #js {:position "absolute"
                                            :top 0 :left 0 :bottom 0 :right 0}}
                    (om/build-all board-row board
                                  {:fn (fn [cursor i] [cursor i])})))))


(defn setup-ui! []
  (om/root
   (fn [app owner] (display-board app))
   game-atom
   {:target (.-body js/document)}))

(defn ^:export start []
  (setup-ui!) (js/setInterval planck! 500))
