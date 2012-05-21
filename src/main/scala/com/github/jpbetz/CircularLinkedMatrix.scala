package com.github.jpbetz

/**
 * Node in a CircularLinkedMatrix.
 */
class Node(val rowId: Int) {
  
  var up: Node = this
  var down: Node = this
  var left: Node = this
  var right: Node = this
  
  def searchDown(searchRowId: Int) : Option[Node] = {

    var next = this.down
    while(next != this) {
      if(next.rowId == searchRowId) return Some(next)
      next = next.down
    }
    return None

  }
  
  def getHeader() = {
    if(this.isInstanceOf[Header]) {
      this.asInstanceOf[Header]
    } else {
      var node = this.up
      while(!node.isInstanceOf[Header]) {
        node = node.up
      }
      node.asInstanceOf[Header]
    }
  }
  
  def coverHoriz() = {
    right.left = left
    left.right = right
  }
  
  def uncoverHoriz() = {
    right.left = this
    left.right = this
  }
  
  def coverVert() = {
    up.down = down
    down.up = up
  }
  
  def uncoverVert() = {
    down.up = this
    up.down = this
  }
  
  def coverRow(anchor: Node) = {
    var node = anchor
    while(node.right != anchor) {
      node.right.coverVert()
      node = node.right
    }
    
  }
  
  def uncoverRow(anchor: Node) = {
    var node = anchor
    while(node.right != anchor) {
      node.right.uncoverVert()
      node = node.right
    }
  }

  def addRight(node: Node) = {
    node.right = right
    node.left = this
    
    right.left = node
    right = node
    
  }
  
  def addDown(node: Node) = {
    node.down = down
    node.up = this
    
    down.up = node
    down = node
  }
  
  override def toString() = {
    "Node(" + rowId + ")"
  }
}

/**
 * Header in a CircularLinkedMatrix.
 */
class Header(val id: Int) extends Node(-1) {
  def searchRight(searchHeaderId: Int) : Option[Node] = {

    var next : Header = this.right.asInstanceOf[Header]
    while(next != this) {
      if(next.id == searchHeaderId) return Some(next)
      next = next.right.asInstanceOf[Header]
    }
    return None
  }
  
  def size() = {
    var i = 0
    var node = this.down
    while(node != this) {
      node = node.down
      i += 1
    }
    
    i
  }
    
  def coverColumn() = {
    var node = this.down
    while(node != this) {
      coverRow(node)
      node = node.down
    }
    this.coverHoriz()
  }
  
    
  def uncoverColumn() = {
    var node = this.down
    while(node != this) {
      uncoverRow(node)
      node = node.down
    }
    this.uncoverHoriz()
  }
  
  override def toString() = {
    "Header(" + id + ")"
  }
}

/**
 * A sparse matrix represented by a circular linked list of headers each containing a circular list of nodes.
 * 
 * Think of it as a torus, with the headers as a circle on the top of the torus and the nodes wrapping around.
 */
class CircularLinkedMatrix {
  
  val root: Header = new Header(-1)
  
  def this(rowsIn: Map[Int, Row]) = {
    this()
    
    val rows = rowsIn.toList.sortBy{_._1}
    
    val rowCount = rows.size-1
    val iter = rows.iterator
    val colCount = iter.next()._2.size-1
    
    var prevHeader = root
    for(c <- 0 to colCount) {
      val header = new Header(c)
      prevHeader.addRight(header)
      prevHeader = header
    }
    
    
    for(row <- rows) {
      var header = root
      val rawRow = row._2
      var prev :Option[Node] = None
      for(c <- 0 to colCount) {
        header = header.right.asInstanceOf[Header]
        if(rawRow.cells(c) == 1) {
          val node = new Node(rawRow.idx)
          header.up.addDown(node)
          if(prev.isDefined) {
            prev.get.addRight(node)
          }
          
          prev = Option(node)
        }
      }
    }
  }
  
  def this(matrix: Array[Array[Int]]) = {
    this()
    
    val rowCount = matrix.size-1
    val colCount = matrix(0).size-1
    
    var prevHeader = root
    for(c <- 0 to colCount) {
      val header = new Header(c)
      prevHeader.addRight(header)
      prevHeader = header
    }
    
    
    for(r <- 0 to rowCount) {
      var header = root
      var prev :Option[Node] = None
      for(c <- 0 to colCount) {
        header = header.right.asInstanceOf[Header]
        if(matrix(r)(c) == 1) {
          val node = new Node(r)
          header.up.addDown(node)
          if(prev.isDefined) {
            prev.get.addRight(node)
          }
          
          prev = Option(node)
        }
      }
    }
  } 
  
  def isEmpty() : Boolean = {
    root == root.right
  }
  
  override def toString() : String = {
    var header = root
    var result: String = "";
    
    result += "matrix: [\n"
    
    while(header.right != root) {
      header = header.right.asInstanceOf[Header]
      
      result += header.id + ": ["
      
      var node: Node = header
      while(node.down != header) {
        node = node.down
        result += node.rowId 
        if(node.down != header) result += ", "
      }
      result += "]\n"
    }
      
    result += "]"
    
    result
  }
}