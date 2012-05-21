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

  /**
   * Part of the recursive solver.
   * 
   * For the given matrix, finds a column containing the fewest rows and searches for a solution involving those rows. 
   */
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
  
  /**
   * Part of the recursive solver.
   * 
   * For the given column, finds all rows that have an entry for the column.  For each of these rows,
   * attempts to remove the row, and recursively solves the matrix with that row removed, searching for a solution.
   * If any row removal results in a solved matrix, returns the solution, else returns a false result.
   */
  def removeRows(header: Header, matrix: CircularLinkedMatrix, rowIds: List[Int]) : Result = {
    for(node <- header.nodes()) {
      val headers = removeRow(node)
      val result = solve(matrix, node.rowId :: rowIds)
      if(result.success == true) {
        return result
      }
      backtrackRow(headers)
    }
    return new Result(false, Nil)
  }
  
  /**
   * Hides an entire row, transitively removing all columns with an entry for that row, and also all other rows
   * where those columns also have a entry.
   */
  def removeRow(anchor: Node) = {
    val headers = anchor.allNodesInRow().map(_.getHeader()).toList
    headers.foreach(_.coverColumn())
    headers
  }
  
  /**
   * Reverses a removeRow operation
   */
  def backtrackRow(headers : List[Header]) {
    headers.foreach(_.uncoverColumn())
  }
  
  /**
   * Finds all columns with entries, sorted by columns with the least entries first.
   */
  def findUnfilledColumns(matrix: CircularLinkedMatrix) : List[Header] = {
    matrix.headers().filter(_.size() > 0).toList.sortBy(_.size())
  }
}