package com.github.jpbetz

/**
 * Simple data type representing a DLX execution result.
 */
case class Result(val success: Boolean, val rowIds: List[Int])

/**
 * Dancing Links implementation of Knuth's algorithm X.
 * 
 * Suitable for solving some exact cover problems such as sudoku.
 */
object DLX {
  
  def solve(matrix: CircularLinkedMatrix): Result = {
    val result = solve(matrix, Nil)
    result
  }

  def solve(matrix: CircularLinkedMatrix, rowIds: List[Int]): Result = {
    val incompleteColumns = findUnfilledColumns(matrix)
    if (matrix.isEmpty) {
      new Result(true, rowIds) // success
    } else {
      for(header <- incompleteColumns) {
        val result = removeRows(header, matrix, rowIds)
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
      val headers = removeRow(node)
      val result = solve(matrix, node.rowId :: rowIds)
      if(result.success == true) {
        return result
      }
      backtrackRow(headers)
    }
    return new Result(false, Nil)
  }
  
  def removeRow(anchor: Node) = {
    var headers = List[Header](anchor.getHeader()) 
    var node = anchor
    while(node.right != anchor) {
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
    var header = matrix.root
    
    var results : List[Header] = Nil
    
    var minColHeader = None
    while(header.right != matrix.root) {
      header = header.right.asInstanceOf[Header]
      if(header.size() > 0) {
        results = header :: results
      }
    }
    results.sortBy{_.size()}
  }
}