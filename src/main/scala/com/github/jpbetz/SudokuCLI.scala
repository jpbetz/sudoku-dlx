package com.github.jpbetz
import jpbetz.cli.CommandSet
import jpbetz.cli.Command
import jpbetz.cli.CommandContext
import java.io.File
import jpbetz.cli.Arg
import jpbetz.cli.CommandSummary
import jpbetz.cli.SubCommand

/**
 * Simple Command Line Interface for the sudoku solver.
 */
object SudokuCLI {
  
  def main(args: Array[String]) {
    val app = new CommandSet("sudoku")
    app.addSubCommand(classOf[Solve])
    app.addSubCommand(classOf[Empty])
    app.invoke(args)
  }
}

@SubCommand(name="solve", description="Solves a soduku puzzle.")
class Solve extends Command {
  
  @Arg(name="File containing sudoku puzzle")
  var file: File = null
  
  override def exec(commandLine: CommandContext) = {
    val text = scala.io.Source.fromFile(file.getAbsolutePath()).mkString
    val puzzle = SudokuWithCircularLinkedMatrix.read(text)
    val solver = new SudokuWithCircularLinkedMatrix(9)
    val solved = solver.solve(puzzle)
    SudokuWithCircularLinkedMatrix.prettyPrint(solved)
  }
}

@SubCommand(name="empty", description="Prints an empty sudoku puzzle.")
class Empty extends Command {
  
  override def exec(commandLine: CommandContext) = {
    val example : Array[Array[Int]] = Array.ofDim(9,9)
    SudokuWithCircularLinkedMatrix.prettyPrint(example)
  }
}