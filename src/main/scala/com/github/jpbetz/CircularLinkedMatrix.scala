package com.github.jpbetz

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
      //val header = node.right.getHeader()
      //if(header.size() == 0) header.coverHoriz()
      node = node.right
    }
    
  }
  
  def uncoverRow(anchor: Node) = {
    var node = anchor
    while(node.right != anchor) {
      node.right.uncoverVert()
      //val header = node.right.getHeader()
      //header.uncoverHoriz()
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
      //println("  covering row: " + node.rowId)
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

class CircularLinkedMatrix {
  
  val root: Header = new Header(-1)
  
  def this(rowsIn: Map[Int, Row]) = {
    this()
    
    val rows = rowsIn.toList.sortBy{_._1}
    
    val rowCount = rows.size-1
    val iter = rows.iterator
    val colCount = iter.next()._2.size-1
    
    //println("rowCount: " + rowCount + ", colCount: " + colCount)
    var prevHeader = root
    for(c <- 0 to colCount) {
      //println("h: " + c)
      val header = new Header(c)
      prevHeader.addRight(header)
      prevHeader = header
    }
    
    
    for(row <- rows) {
      var header = root
      val rawRow = row._2
      //println("r: " + rawRow.idx)
      var prev :Option[Node] = None
      for(c <- 0 to colCount) {
        //println("c: " + c)
        header = header.right.asInstanceOf[Header]
        if(rawRow.cells(c) == 1) {
          val node = new Node(rawRow.idx)
          //println("adding " + node + " below " + header.up)
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
    
    //println("rowCount: " + rowCount + ", colCount: " + colCount)
    var prevHeader = root
    for(c <- 0 to colCount) {
      //println("h: " + c)
      val header = new Header(c)
      prevHeader.addRight(header)
      prevHeader = header
    }
    
    
    for(r <- 0 to rowCount) {
      var header = root
      //println("r: " + r)
      var prev :Option[Node] = None
      for(c <- 0 to colCount) {
        //println("c: " + c)
        header = header.right.asInstanceOf[Header]
        if(matrix(r)(c) == 1) {
          val node = new Node(r)
          //println("adding " + node + " below " + header.up)
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
    /*var header: Node = root
    while(header.right != root) {
      header = header.right
      if(header.down != header) return false
    }
    return true*/
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