package com.github.jpbetz

class Sudoku(sizeIn: Int) {
  val size = sizeIn
  val parts : Int = Math.sqrt(size).toInt
  
  def solve(puzzle : Array[Array[Int]]) = {
    val matrix = convertPuzzleToConstraintMatrix(puzzle)
    val solvedMatrix = AlgorithmX.solve(matrix)
    convertSolvedMatrixToPuzzle(solvedMatrix)
  }
  
  // constrained array column structure: (9*9*4 columns total)
  // 
  // 1. Row-Column constraints R1C1 ... R1C9, ...., R9C1 ... R9C9 (9*9 columns)
  // 2. Row-number R1#1, R1#2 ... R1#9, ... , R9#1, ... R9#9 (9*9 columns)
  // 3. Column-number C1#1, C1#2 ... C1#9, ... , C9#1, ... C9#9 (9*9 columns)
  // 4. Box-number B1#1, B1#2 ... B1#9, ... , B9#1, ... B9#9 (9*9 columns)
  
  // rows:
  // R1C1#1, R1C1#2 ..., R1C1#9, R1C2#1, ... R1C2#9, ...., R1C9#1, ... R9C9#9
  // (9*9*9 columns total)
  
  def convertPuzzleToConstraintMatrix(puzzle : Array[Array[Int]]) = {
    if(size != puzzle.size) throw new RuntimeException("puzzle size does not match solver configured size")
    
    var rows = List[Row]()
    //val a = Array.ofDim[Int](size*size*size, size*size*4)
    
    //println("done adding row-column conditions")
    //println("adding conditions for all cells")
    for(r <- 0 to size-1) {
      for (c <- 0 to size-1) {
        val b = getBoxForRowColumn(r,c)
        val constrainedNumber = puzzle(r)(c)
        //println("adding conditions for " + r + ":" + c)
        if(constrainedNumber > 0) {
          // add constraints for number
          val constraintRow = createConstraintRow(r,c,constrainedNumber)
          // put row number constraint
          addNumberConstraint(constraintRow, r,c,b,constrainedNumber)
          addRowColumnCondition(constraintRow, r,c,constrainedNumber)
          
          rows = constraintRow :: rows
        } else {
          // add all possible number values for square
          for(possibleNumber <- 1 to size) {
            val constraintRow = createConstraintRow(r,c,possibleNumber)
            addNumberConstraint(constraintRow, r,c,b,possibleNumber)
            addRowColumnCondition(constraintRow, r,c,possibleNumber)
            
            rows = constraintRow :: rows
          }
        }
      }
    }
    
    new Matrix(rows.map{ row => (row.idx, row)}.toMap)
  }
  
  def getBoxForRowColumn(r: Int, c: Int) = {
    //(((r+1) / parts)-1)*parts + (((c+1) / parts)-1)
    //println("r: " + r + " c: " + c + " size: " + size + " parts: " + parts)
    val row = (r / parts)
    val col = (c / parts)
    //println("row: " + row + " col: " + col)
    row*parts + col
  }
  
  def createConstraintRow(puzzleRow: Int, puzzleColumn: Int, number: Int) = {
    val rowId = puzzleRow*size*size+puzzleColumn*size+(number-1)
    val constraintRow = Array.ofDim[Int](size*size*4)
    val row = new Row(rowId, constraintRow)
    addColumnNumberConstraint(row, puzzleColumn, number)
    
    row
  }
  
  def addNumberConstraint(constraintRow: Row, puzzleRow: Int, puzzleColumn: Int, puzzleBox: Int, number: Int) = {
    addRowNumberConstraint(constraintRow, puzzleRow, number)
    addColumnNumberConstraint(constraintRow, puzzleColumn, number)
    addBoxNumberConstraint(constraintRow, puzzleBox, number)
  }
  
  def addRowColumnCondition(constraintRow: Row, puzzleRow: Int, puzzleColumn: Int, number: Int) = {
    constraintRow.cells(0*size*size + size*puzzleRow + puzzleColumn) = 1
  }
  
  def addRowNumberConstraint(constraintRow: Row, puzzleRow: Int, number: Int) = {
    constraintRow.cells(1*size*size + size*puzzleRow + (number-1)) = 1
  }
  
  def addColumnNumberConstraint(constraintRow: Row, puzzleColumn: Int, number: Int) = {
    //println("column size: " + constraintRow.cells.size)
    constraintRow.cells(2*size*size + size*puzzleColumn + (number-1)) = 1
  }
  
  def addBoxNumberConstraint(constraintRow: Row, puzzleBox: Int, number: Int) = {
    constraintRow.cells(3*size*size + size*puzzleBox + (number-1)) = 1
  }
  
  def convertSolvedMatrixToPuzzle(matrix: Matrix) = {
    val a = Array.ofDim[Int](size,size)
    for(row <- matrix.rows) {
      val id = row._1
      val rowArray = row._2
      
      val r = id / (size*size)
      val c = (id % (size*size)) / size 
      val n = (id % (size*size)) % size
      
      a(r)(c) = n+1
    }
    
    a
  }
}