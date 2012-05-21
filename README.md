sudoku-dlx
==========

A simple sudoku solver.  

Solver maps a sudoku board to an [exact cover](http://en.wikipedia.org/wiki/Exact_cover) matrix and uses nieve implementations of [dancing links](http://cgi.cse.unsw.edu.au/~xche635/dlx_sodoku/) and [Knuth's Algorithm X](http://en.wikipedia.org/wiki/Knuth's_Algorithm_X), both written in scala.

Requires scala and sbt.

To build and install:

Add this to your bash profile:

    export SUDOKU_HOME=<path-to-sudoku-dlx>
    export PATH=$PATH:$SUDOKU_HOME/cli-bin

..and then do:

    $ sbt
    sbt> assembly
    sbt> exit


To run:

Let's use the example at examples/puzzle.txt

    $ examples/puzzle.txt

    +-------+-------+-------+
    | _ 6 _ | 1 _ 4 | _ 5 _ |
    | _ _ 8 | 3 _ 5 | 6 _ _ |
    | 2 _ _ | _ _ _ | _ _ 1 |
    +-------+-------+-------+
    | 8 _ _ | 4 _ 7 | _ _ 6 |
    | _ _ 6 | _ _ _ | 3 _ _ |
    | 7 _ _ | 9 _ 1 | _ _ 4 |
    +-------+-------+-------+
    | 5 _ _ | _ _ _ | _ _ 2 |
    | _ _ 7 | 2 _ 6 | 9 _ _ |
    | _ 4 _ | 5 _ 8 | _ 7 _ |
    +-------+-------+-------+

to solve it, run:

    $ sudoku solve examples/puzzle1.txt    

    +-------+-------+-------+
    | 9 6 3 | 1 7 4 | 2 5 8 |
    | 1 7 8 | 3 2 5 | 6 4 9 |
    | 2 5 4 | 6 8 9 | 7 3 1 |
    +-------+-------+-------+
    | 8 2 1 | 4 3 7 | 5 9 6 |
    | 4 9 6 | 8 5 2 | 3 1 7 |
    | 7 3 5 | 9 6 1 | 8 2 4 |
    +-------+-------+-------+
    | 5 8 9 | 7 1 3 | 4 6 2 |
    | 3 1 7 | 2 4 6 | 9 8 5 |
    | 6 4 2 | 5 9 8 | 1 7 3 |
    +-------+-------+-------+

