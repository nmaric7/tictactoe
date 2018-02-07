package hr.niko.tictactoe

import hr.niko.tictactoe.model.{Board, Player, UnbeatablePlayer}

import scala.collection.mutable

object TransformUtils {

  // transform input board case into NN input
  // First 9 elements presents X moves (X = 1, other 0)
  // Next 9 elements presents O moves (O = 1, other 0)
  // O  X  XOX  -> 000100101100000010
  def transformInput(grid: Grid): Array[Double] = transformInputByMark(grid, 'X') ++ transformInputByMark(grid, 'O')

  def transformInputByMark(grid: Grid, mark:Char): Array[Double] = grid.map( x => if (x == mark) 1.0 else 0.0 )

  def getNNInputData(examples: List[Grid], player: Player): List[(Array[Double], Int)] = {
    examples.map(input => {
      val board = new Board(input)
      (transformInput(input), player.chooseNextMove(board))
    })
  }

  def filterExamplesByPlayer(examples: List[Grid], mark: Char) = {
    examples.filter(e => {
      val b = new Board(e)
      val (countXMoves, countOMoves) = b.countMoves
      if (mark == 'X) countXMoves <= countOMoves
      else countXMoves >= countOMoves
    })
  }

}
