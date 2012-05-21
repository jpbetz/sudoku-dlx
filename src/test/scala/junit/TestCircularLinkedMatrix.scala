package junit

import org.scalatest.FunSuite

import com.github.jpbetz.Matrix
import com.github.jpbetz.CircularLinkedMatrix
import com.github.jpbetz.Header

class TestCircularLinkedMatrix extends FunSuite {
  
  test("testBuildMatrix") {
    val arrays = Array(
          Array(1,0,0,1),
          Array(1,0,0,1),
          Array(0,0,0,1)
    )
          
    var matrix = new CircularLinkedMatrix(arrays)
    println(matrix.toString())
  }
  
   test("testBuildMatrixFromMatrix") {
    val m = new Matrix(Array(
          Array(1,0,0,1),
          Array(1,1,0,1),
          Array(0,1,0,1))
    )
    
    var rows = m.rows
          
    var matrix = new CircularLinkedMatrix(rows)
    println(matrix.toString())
    
    val header = matrix.root.right.asInstanceOf[Header]
    val header2 = matrix.root.right.right.right.right.asInstanceOf[Header]
    assert(header == header.down.down.getHeader())
    
    header.coverColumn()
    
    println(matrix.toString())
    
    header2.coverColumn()
    
    println(matrix.toString())
    
    header2.uncoverColumn()
    
    println(matrix.toString())
    
    header.uncoverColumn()
    
    println(matrix.toString())
  }
}