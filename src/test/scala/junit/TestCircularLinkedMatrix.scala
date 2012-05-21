package junit

import org.scalatest.FunSuite
import com.github.jpbetz.CircularLinkedMatrix
import com.github.jpbetz._

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
  
  test("all nodes in row") {
    println("all nodes in row: ")
    var node1 = new Node(1) 
    var node2 = new Node(2) 
    var node3 = new Node(3)
    
    node1.right = node2
    node2.right = node3
    node3.right = node1
    
    println(node1.allNodesInRow().toList.mkString(","))
  }
  
   test("testBuildMatrixFromMatrix") {
    val rows = Map(
          1 -> new Row(1, Array(1,0,0,1)),
          2 -> new Row(2, Array(1,1,0,1)),
          3 -> new Row(3, Array(0,1,0,1))
    )
    
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