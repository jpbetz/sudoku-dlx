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