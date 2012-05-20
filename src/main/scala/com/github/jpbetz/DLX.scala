package com.github.jpbetz

case class Result(val success: Boolean, val rowIds: List[Int])

// array is row,column (x,y)
object DLX {
  
  def solve(matrix: CircularLinkedMatrix): Result = {
    //println("> solving")
    //println(matrix)
    val result = solve(matrix, Nil)
    //println("< done solving")
    result
  }

  def solve(matrix: CircularLinkedMatrix, rowIds: List[Int]): Result = {
    val solution = List[Row]()
    val incompleteColumns = findUnfilledColumns(matrix)
    //println("  eligible columns: " + incompleteColumns)
    if (matrix.isEmpty) {
      new Result(true, rowIds) // success
    } else {
      for(header <- incompleteColumns) {
        //println("> removing rows for column: " + header.id + " rowIds: " + rowIds)
        val result = removeRows(header, matrix, rowIds)
        //println("< removing rows for column: " + header.id + " rowIds: " + rowIds)
        //println(matrix + " is Empty: " + matrix.isEmpty)
        if(result.success) {
          return result
        }
      }
      return new Result(false, Nil)
    }
  }
  
  def removeRows(header: Header, matrix: CircularLinkedMatrix, rowIds: List[Int]) : Result = {
    var node : Node = header
    while(node.down != header) {
      node = node.down
      //println("  removing row: " + node.rowId + " rowIds: " + rowIds)
      val headers = removeRow(node)
      //println(matrix)
      val result = solve(matrix, node.rowId :: rowIds)
      if(result.success == true) {
        //println("  found solution!" + " rowIds: " + result.rowIds)
        return result
      }
      //println("  no solutions found, backtracking")
      backtrackRow(headers)
    }
    return new Result(false, Nil)
  }
  
  def removeRow(anchor: Node) = {
    var headers = List[Header](anchor.getHeader()) 
    var node = anchor
    while(node.right != anchor) {
      //println("removing column " + node.getHeader().id + " as part of " + anchor.rowId)
      headers = node.right.getHeader() :: headers
      node = node.right
    }
    for(header <- headers) {
      header.coverColumn()
    }
    headers
  }
  
  def backtrackRow(headers : List[Header]) {
    for(header <- headers) {
      header.uncoverColumn()
    }
  }
  
  def findUnfilledColumns(matrix: CircularLinkedMatrix) : List[Header] = {
    //println("finding column")
    var header = matrix.root
    
    var results : List[Header] = Nil
    
    var minColHeader = None
    while(header.right != matrix.root) {
      header = header.right.asInstanceOf[Header]
      //println("  header (id,rowId): (" + header.id + "," + header.rowId + ") up: " + header.up.rowId + " up-up: " + header.up.up.rowId)
      //if(header == header.up || header != header.up.up) {
      if(header.size() > 0) {
        //println("  found column: header (id,rowId): (" + header.id + "," + header.rowId + ") up: " + header.up.rowId + " up-up: " + header.up.up.rowId)
        // empty column or column with more than one cell
        //if(minColHeader.isEmpty || minColHeader.get.)
        results = header :: results
      }
    }
    //println("  no unsolved columns remaining.")
    results.sortBy{_.size()}
  }
}