# gameoflife

Conway's Game of Life implemented in clojurescript, with a react.js powered UI.

## Dependencies

This application manages dependencies through [leiningen](http://leiningen.org/). It also needs [npm](https://www.npmjs.org/) for javascript dependencies.

## Usage

To view Game of Life as a web app

    lein cljsbuild once
    cd resource/public && python -m SimpleHTTPServer # or any other way of serving static files
    firefox http://localhost:8000/game.html
    
It also works as a command line application. Make sure you have npm installed, then

    lein deps
    lein cljsbuild once
    node gameoflife.js inputfile

Where `inputfile` looks something like

    0 1 0 0 0
    1 0 0 1 1
    1 1 0 0 1
    0 1 0 0 0
    1 0 0 0 1
    
which in the example above would output 

    0 0 0 0 0
    1 0 1 1 1
    1 1 1 1 1
    0 1 0 0 0
    0 0 0 0 0

## License

Copyright © 2014 Jonathan Brown
