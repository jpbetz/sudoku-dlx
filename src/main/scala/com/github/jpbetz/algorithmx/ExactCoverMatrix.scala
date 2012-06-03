package com.github.jpbetz.algorithmx

/**
 * Matrix interface required by Algorithm X.
 * 
 */
trait ExactCoverMatrix {
  
  /**
   * Does the matrix have any cells?
   */
  def isEmpty : Boolean;
  
  /**
   * Iterator over the columns in the matrix.
   */
  def columns() : Iterator[Column];
}

/**
 * A column in an ExactCoverMatrix.  
 */
trait Column {
  
  /**
   * Next column in the matrix, or, if this is the last column, return the first column.
   */
  def next() : Column;
  
  /**
   * Previous column in the matrix, or, if this is the first column, return the last column.
   */
  def previous() : Column;
  
  /**
   * Count of nodes in the column.
   */
  def size() : Integer;
  
  /**
   * Iterator of the nodes in the column.
   */
  def nodes() : Iterator[Node];
  
  /**
   * Hide the column and all the nodes in it from the matrix.   Operation must be reversible by calling uncoverColumn later.
   */
  def coverColumn();
  
  /**
   * Unhides the column, this must exactly reverse a previous call to coverColumn
   */
  def uncoverColumn();
}

/**
 * 
 */
trait Node {
  
  /**
   * Gets an identifier of the current row.
   */
  def getRowId() : Integer;
  
  /**
   * Iterator over all the nodes in this row.
   */
  def allNodesInRow() : Iterator[Node];
  
  /**
   * Gets the column this row is in.
   */
  def getColumn() : Column;
}