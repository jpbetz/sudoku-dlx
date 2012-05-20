package com.github.jpbetz

object SudokuCLI {
  
  def main(args: Array[String]) {
    if(args.size != 1) {
      println("usage: sudoku [puzzle-file]")
      println("Example:") 
      val example : Array[Array[Int]] = Array.ofDim(9,9)
      example(0)(0) = 1
      example(0)(1) = 2
      example(2)(1) = 3
      SudokuWithCircularLinkedMatrix.prettyPrint(example)
    } else {
      val filename = args(0)
      val text = scala.io.Source.fromFile(filename).mkString
      val puzzle = SudokuWithCircularLinkedMatrix.read(text)
      val solver = new SudokuWithCircularLinkedMatrix(9)
      val solved = solver.solve(puzzle)
      SudokuWithCircularLinkedMatrix.prettyPrint(solved)
    }
  }
}