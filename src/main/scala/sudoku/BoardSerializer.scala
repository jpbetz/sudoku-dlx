package com.github.jpbetz.sudoku

object BoardSerializer {
  
  def read(text : String) : Array[Array[Int]] = {
    val result = for(line <- text.lines if line.startsWith("|")) yield {
        line.trim().split("""[\| ]+""").filter(_ != "").map{n => if(n == "_") {"0"} else {n}}.map{_.toInt}
    }
    result.toArray[Array[Int]]
  }
  
  def write(sudoku : Array[Array[Int]]) {
    
    printSplitter()
    for(i <- 0 to 2) {
      printLine(sudoku(i))
    }
    
    printSplitter()
    for(i <- 3 to 5) {
      printLine(sudoku(i))
    }
    
    printSplitter()
    for(i <- 6 to 8) {
      printLine(sudoku(i))
    }
    
    printSplitter()
  }
  
  def printSplitter() = {
    printf("+-------+-------+-------+\n")
  }
  
  def printLine(line : Array[Int]) = {
    printf("| %s %s %s | %s %s %s | %s %s %s |\n", 
           toCell(line(0)), toCell(line(1)), toCell(line(2)), 
           toCell(line(3)), toCell(line(4)), toCell(line(5)),
           toCell(line(6)), toCell(line(7)), toCell(line(8)))
  }
  
  def toCell(value: Int) = {
    if(value == 0) {
      "_"
    } else {
      value.toString
    }
  }
}