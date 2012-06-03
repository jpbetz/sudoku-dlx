package com.github.jpbetz.sudoku.junit

import org.scalatest.FunSuite

import com.github.jpbetz.algorithmx._
import com.github.jpbetz.algorithmx.dancinglinks._

class TestDLX extends FunSuite {
  test("testDLX") {
    val array = Array(
          Array(1,0,0,1,0,0,1), // 0
          Array(1,0,0,1,0,0,0), // 1
          Array(0,0,0,1,1,0,1), // 2
          Array(0,0,1,0,1,1,0),
          Array(0,1,1,0,0,1,1), // 4
          Array(0,1,0,0,0,0,1) // 5
    )
    
    val matrix = new CircularLinkedMatrix(array)
    
   // (1,(1 -> 1, 0, 0, 1, 0, 0, 0))
   // (3,(3 -> 0, 0, 1, 0, 1, 1, 0))
   // (5,(5 -> 0, 1, 0, 0, 0, 0, 1))
          
    val solution = AlgorithmX.solve(matrix)
    println(solution.success)
    println(solution.rowIds)
    for(rowId <- solution.rowIds) {
      println(array(rowId).mkString(","))
    }
  }
}