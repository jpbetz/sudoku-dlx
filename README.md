sudoku-dlx
==========

A simple sudoku solver.  

Solver maps a sudoku board to an [exact cover](http://en.wikipedia.org/wiki/Exact_cover) matrix and uses nieve implementations of [dancing links](http://cgi.cse.unsw.edu.au/~xche635/dlx_sodoku/) and [Knuth's Algorithm X](http://en.wikipedia.org/wiki/Knuth's_Algorithm_X), both written in scala.

Requires scala and sbt.

To setup:

Add this to your bash profile:

    export SUDOKU_HOME=<path-to-sudoku-dlx>
    export PATH=$PATH:$SUDOKU_HOME/cli-bin

and then run:

    $ sbt
    sbt> assembly
    sbt> exit


To build run:

    sudoku solve examples/puzzle1.txt