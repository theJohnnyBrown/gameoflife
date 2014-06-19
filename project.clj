(defproject gameoflife "0.1.0-SNAPSHOT"
  :description "Conway's Game of Life implemented in clojurescript,
                with a react.js powered UI"
  :license "Copyright © 2014 Jonathan Brown"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2202"]
                 [om "0.6.2"]]
  :plugins [[lein-cljsbuild "1.0.3"]
            [lein-npm "0.3.2"]]
  :node-dependencies [[react "0.9.0"]]

  :cljsbuild
  {:builds [{:id "node"
             :source-paths ["src"]
             :compiler
             {:language-in :ecmascript5
              :target :nodejs
              :output-to "gameoflife.js"
              :optimizations :simple
              :preamble ["gameoflife/react_preamble.js"]
              :pretty-print true}}
            {:id "browser-dev"
             :source-paths ["src"]
             :compiler
             {:language-in :ecmascript5
              :output-to "resources/public/js/app.js"
              :optimizations :simple
              :pretty-print true
              :preamble ["react/react.js"]
              :externs ["react/externs/react.js"]}}]})
