package com.github.jpbetz

class Row(indexIn: Int, cellsIn: Array[Int]) {
  val idx = indexIn
  val cells = cellsIn

  def apply(idx: Int) {
    cells(idx)
  }

  override def toString() = {
    "(" + idx + " -> " + cells.mkString(", ") + ")"
  }

  def size = cells.length
}

class Matrix(rowsIn: Map[Int, Row]) {
  val rows = rowsIn

  def this(raw: Array[Array[Int]]) = {
    this(raw.view.zipWithIndex.map { case (el, idx) => (idx, new Row(idx, el)) }.toMap)
  }

  override def toString() = {
    rows.toList.sortBy{_._1}.mkString("\n")
  }

  def apply(i: Int) = row(i)
  def apply(i: Int, j: Int) = row(i).cells(j)

  def row(i: Int) = {
    rows(i)
  }

  def column(i: Int): Iterable[Int] = {
    rows.map { case (key, value) => value.cells(i) }
  }

  def rowCount = rows.size

  def columnCount = {
    val it = rows.values.iterator
    if (it.hasNext) it.next.size else 0
  }
  
  
}

// array is row,column (x,y)
object AlgorithmX {

  def solve(matrix: Matrix): Matrix = {
    //println(matrix)
    val reduced = reduce(matrix)
    //println(reduced)
    reduced match {
      case (true, rows) => {
        println(rows)
        new Matrix(rows.map(row => (row.idx, matrix.row(row.idx))).toMap)
      }
      case (false, _) => {
        throw new RuntimeException("No solutions for given matrix")
      }
    }
  }

  private def reduce(matrix: Matrix): (Boolean, List[Row]) = {
    if (matrix.rowCount == 0) {
      // successful
      (true, Nil)
    } else if (matrix.rowCount == 1 && !matrix.rows.forall { case (key, value) => value.cells.forall(_ == 1) }) {
      // unsuccessful, TODO: unsure about this terminal case, this might be incorrect
      (false, Nil)
    } else {
      val chosenColumnIdx = chooseColumnDeterministically(matrix)
      val eligibleRows = matrix.rows.values.filter(_.cells(chosenColumnIdx) == 1).toSeq
      nonDeterministicallyReduceRows(eligibleRows, matrix)
    }
  }

  private def chooseColumnDeterministically(matrix: Matrix) = {
    var columnWithMinOnes = 0
    var minOnes = Int.MaxValue
    for (i <- 0 to matrix.columnCount - 1) {
      val sum = matrix.column(i).foldLeft(0)(_ + _)
      if (sum < minOnes) {
        minOnes = sum
        columnWithMinOnes = i
      }
    }
    columnWithMinOnes
  }

  private def nonDeterministicallyReduceRows(eligibleRows: Seq[Row], matrix: Matrix): (Boolean, List[Row]) = {
    val results = eligibleRows.map(reduceRow(_, matrix))
    val successfulResults = results.filter { case (success, _) => success == true }
    if (successfulResults.isEmpty) (false, Nil)
    else successfulResults(0) // if more than one successful result, just return the first one
  }

  private def reduceRow(row: Row, matrix: Matrix): (Boolean, List[Row]) = {
    // find the columns where maxtix[rowIdx][colIdx] == 1
    val colIdxs = (0 to matrix.columnCount - 1).filter(colIdx => matrix(row.idx, colIdx) == 1).toSet
    // find the rows where matrix[rowIdx][colIdx] == 1
    val rowIdxs = colIdxs.flatMap(colIdx => matrix.rows.values.filter(_.cells(colIdx) == 1)).map(_.idx).toSet
    
    // now we know what needs to be removed, just need to jump through some hoops to build a submatrix with
    // the rows and columns removed
    val rowsRemoved = matrix.rows.filterNot{case (_, value) => rowIdxs.contains(value.idx)}
    val submatrix = new Matrix(
      rowsRemoved.mapValues {
        value =>
          {
            val reducedCellsForRow = value.cells.view.zipWithIndex.filterNot {
              case (cell, idx) => colIdxs.contains(idx)
            }.map {
              case (cell, idx) => cell
            }.toArray // debugger was paused here
            new Row(value.idx, reducedCellsForRow)
          }
      })

    // recurse
    println("rows remaining: " + submatrix.rowCount)
    val result = reduce(submatrix)
    
    // propagate result
    result match {
      case (true, rows) => {
        (true, row :: rows)
      }
      case (false, _) => {
        result
      }
    }
  }
}