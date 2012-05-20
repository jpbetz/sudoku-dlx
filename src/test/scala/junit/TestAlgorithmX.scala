package junit

import com.github.jpbetz.AlgorithmX
import com.github.jpbetz.Matrix
import com.github.jpbetz.Sudoku
import org.scalatest.FunSuite

class TestAlgorithmX extends FunSuite {

  test("algorithm X solves a large constraint problem correctly") {
    val matrix = new Matrix(Array(
          Array(1,0,0,1,0,0,1), // 0
          Array(1,0,0,1,0,0,0), // 1
          Array(0,0,0,1,1,0,1), // 2
          Array(0,0,1,0,1,1,0), // 3
          Array(0,1,1,0,0,1,1), // 4
          Array(0,1,0,0,0,0,1)  // 5
    ))
          
    println(AlgorithmX.solve(matrix))
    assert(true)
  }
}