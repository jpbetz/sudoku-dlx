package junit

import org.scalatest.FunSuite

import com.github.jpbetz.AlgorithmX
import com.github.jpbetz.Matrix
import com.github.jpbetz.Sudoku
import com.github.jpbetz.SudokuWithCircularLinkedMatrix

class TestSudoku extends FunSuite {
  
  /*@Test def testGetBoxForRowColumn() = {
    
    val correct = Array(
      Array(0, 0, 1, 1),
      Array(0, 0, 1, 1),
      Array(2, 2, 3, 3),
      Array(2, 2, 3, 3)
    )
    
    val solver = new Sudoku(4)
    for(i <- 0 to 3) {
      for (j <- 0 to 3) {
        assertEquals(solver.getBoxForRowColumn(i,j), correct(i)(j))
      }
    }
    ()
  }*/
    
  test("testSudokuSolver_Large") {
    // easy
    val puzzle = Array(
          Array(4,3,0, 0,0,2, 0,8,0),
          Array(1,7,0, 8,3,0, 0,5,2),
          Array(0,0,6, 7,5,0, 0,0,0),
          
          Array(0,9,0, 0,2,0, 0,0,4),
          Array(2,0,3, 0,0,0, 8,0,7),
          Array(6,0,0, 0,7,0, 0,2,0),
          
          Array(0,0,0, 0,8,1, 2,0,0),
          Array(9,1,0, 0,4,3, 0,7,8),
          Array(0,8,0, 2,0,0, 0,6,3)
    )
    
    /* empty
       val puzzle = Array(
          Array(0,0,0, 0,0,0, 0,0,0),
          Array(0,0,0, 0,0,0, 0,0,0),
          Array(0,0,0, 0,0,0, 0,0,0),
          
          Array(0,0,0, 0,0,0, 0,0,0),
          Array(0,0,0, 0,0,0, 0,0,0),
          Array(0,0,0, 0,0,0, 0,0,0),
          
          Array(0,0,0, 0,0,0, 0,0,0),
          Array(0,0,0, 0,0,0, 0,0,0),
          Array(0,0,0, 0,0,0, 0,0,0)
    )
     */
    
    /* hardest ever
    val puzzle = Array(
          Array(1,0,0, 0,0,7, 0,9,0),
          Array(0,3,0, 0,2,0, 0,0,8),
          Array(0,0,9, 6,0,0, 5,0,0),
          
          Array(0,0,5, 3,0,0, 9,0,0),
          Array(0,1,0, 0,8,0, 0,0,2),
          Array(6,0,0, 0,0,4, 0,0,0),
          
          Array(3,0,0, 0,0,0, 0,1,0),
          Array(0,4,0, 0,0,0, 0,0,7),
          Array(0,0,7, 0,0,0, 3,0,0)
    )*/
    
    // http://www.extremesudoku.info/sudoku.html
    /*val puzzle = Array(
          Array(4,0,0, 0,0,0, 0,0,9),
          Array(0,0,5, 0,8,0, 3,0,0),
          Array(0,9,7, 0,5,0, 6,2,0),
          
          Array(0,0,0, 6,0,5, 0,0,0),
          Array(0,6,8, 0,0,0, 9,7,0),
          Array(0,0,0, 8,0,7, 0,0,0),
          
          Array(0,5,1, 0,3,0, 4,8,0),
          Array(0,0,3, 0,4,0, 1,0,0),
          Array(2,0,0, 0,0,0, 0,0,3)
    )*/
    
    val solver = new SudokuWithCircularLinkedMatrix(9)
    val solved = solver.solve(puzzle)
    prettyPrint(solved)
  }
  
  def prettyPrint(sudoku : Array[Array[Int]]) {
    
    println()
    printSplitter()
    for(i <- 0 to 2) {
      printLine(sudoku(i))
    }
    
    printSplitter()
    for(i <- 3 to 5) {
      printLine(sudoku(i))
    }
    
    printSplitter()
    for(i <- 6 to 8) {
      printLine(sudoku(i))
    }
    
    printSplitter()
    println()
  }
  
  def printSplitter() = {
    printf("+-------+-------+-------+\n")
  }
  
  def printLine(line : Array[Int]) = {
    printf("| %s %s %s | %s %s %s | %s %s %s |\n", line(0), line(1), line(2), line(3), line(4), line(5), line(6), line(7), line(8))
  }
  
  def toCell(value: Int) = {
    if(value == 0) {
      "_"
    } else {
      value.toString
    }
  }
  
  /*@Test def testSudokuSolver() = {
    val puzzle = Array(
            Array(1,2,  4,3),
            Array(0,4,  2,0),
            
            Array(4,0,  1,2),
            Array(2,1,  3,0)
    )
    
    val solver = new SudokuWithCircularLinkedMatrix(4)
    val solved = solver.solve(puzzle)
    println(solved.deep.mkString("\n"))

    val correct = Array(
      Array(1, 2, 4, 3),
      Array(3, 4, 2, 1),
      Array(4, 3, 1, 2),
      Array(2, 1, 3, 4)
    )
    
    for(i <- 0 to 3) {
      for(j <- 0 to 3) {
        assertEquals(solved(i)(j), correct(i)(j))
      }
    }
  }*/
}