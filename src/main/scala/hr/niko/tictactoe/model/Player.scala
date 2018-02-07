package hr.niko.tictactoe.model

import hr.niko.tictactoe.Grid

/**
  * Created by Nikica on 9.2.2017.
  */

sealed trait Player {

  // players mark 'X' or 'O'
  def mark: Char
  // choose next move
  def chooseNextMove(board: Board): Int

  def otherMark: Char = if (mark == 'X') 'O' else 'X'
}

case class RandomPlayer(markChar: Char) extends Player {
  // sign
  override def mark: Char = markChar

  // choose next move
  override def chooseNextMove(board: Board): Int = board.takeRandomFreePosition

}

case class UnbeatablePlayer(markChar: Char) extends Player {
  // players mark 'X' or 'O'
  override def mark: Char = markChar

//  // miss last move
//  def playerWinInMove(board: Board): Unit = {
//
//    if (board.numberOfMoves > 3) {
//      // remove win combination that contains other mark
//      val withoutOtherMark = Board.winSets.filter(winSet => winSet.forall(board.grid(_) != otherMark))
//      // find win combination with two marks
//      val winCombinations = withoutOtherMark.filter(winSet => winSet.filter(board.grid(_) == mark).length == 2)
//
//      winCombinations match {
//        case x :: _ =>
//          val idx = x.filter(board.grid(_) == ' ').head
//          println(s"Player '$mark' win in move at position $idx")
//          board.grid(idx) = mark
//        case _ =>
//      }
//    }
//  }

  def checkForWin(idx: Int, board: Board): Boolean = {
    val temp: Grid = board.grid.zipWithIndex.map { case (x, i) =>
        if (i == idx) mark
        else x
    }
    Board.wonBy(mark, temp)
  }

  def playerWinInMove(board: Board): Int = {
    board.freePositions.find(checkForWin(_, board)).getOrElse(-1)
  }

//  def blockOpponentWinInMove(board: Board): Unit = {
//
//    if (board.numberOfMoves > 3) {
//      // remove win combination that contains players mark
//      val withoutOtherMark = Board.winSets.filter(winSet => winSet.forall(board.grid(_) != mark))
//      // find win combination with two opponent marks
//      val winCombinations = withoutOtherMark.filter(winSet => winSet.filter(board.grid(_) == otherMark).length == 2)
//
//      winCombinations match {
//        case x :: _ =>
//          val idx = x.filter(board.grid(_) == ' ').head
//          println(s"Block opponent '$otherMark' win in move at position $idx")
//          board.grid(idx) = mark
//        case _ =>
//      }
//    }
//  }

  def checkForBlock(idx: Int, board: Board): Boolean = {
    val temp: Grid = board.grid.zipWithIndex.map { case (x, i) =>
      if (i == idx) otherMark
      else x
    }
    Board.wonBy(otherMark, temp)
  }

  def blockOpponentWinInMove(board: Board): Int = {
    board.freePositions.find(checkForBlock(_, board)).getOrElse(-1)

  }

  // there is no other mark in combination
  def potentialWinningCombinationForMark(grid: Grid, row: List[Int], m: Char): Boolean = row.forall(grid(_) != m)

  // there is exactly two marks in combination, and third is free
  def winningPositionForMark(grid: Grid, row: List[Int], m: Char): Boolean = row.filter(grid(_) == m).length == 2

  def checkForFork(idx: Int, board: Board): Boolean = {
    val temp: Grid = board.grid.zipWithIndex.map { case (x, i) =>
      if (i == idx) mark
      else x
    }

    Board.winSets
      .filter(potentialWinningCombinationForMark(temp, _, otherMark))
      .filter(winningPositionForMark(temp, _, mark))
      .length > 1
  }


  def playerFork(board: Board): Int = {
    board.freePositions.find(checkForFork(_, board)).getOrElse(-1)
  }

  def checkForBlockFork(idx: Int, board: Board): Boolean = {
    val temp: Grid = board.grid.zipWithIndex.map { case (x, i) =>
      if (i == idx) otherMark
      else x
    }

    Board.winSets
      .filter(potentialWinningCombinationForMark(temp, _, mark))
      .filter(winningPositionForMark(temp, _, otherMark))
      .length > 1

  }

  def findTwoInRow(idx: Int, board: Board): Boolean = {
    val temp: Grid = board.grid.zipWithIndex.map { case (x, i) =>
      if (i == idx) mark
      else x
    }

    Board.winSets
      .filter(potentialWinningCombinationForMark(temp, _, otherMark))
      .filter(winningPositionForMark(temp, _, mark))
      .length > 0

  }

  def blockOpponentFork(board: Board): Int = {

    val blockForkPositions = board.freePositions.filter(checkForBlockFork(_, board))
    if (blockForkPositions.isEmpty) -1
    else {
      // player has center, take side position
      if (blockForkPositions.length == 2) {
        // make two in row
        Board.sidePositions.find(findTwoInRow(_, board)).getOrElse(-1)
      }
      // opponent has center, take corner position
      else if (blockForkPositions.length == 4) {
        // make two in row
        Board.cornerPositions.find(findTwoInRow(_, board)).getOrElse(-1)
      }
      // only one position to fork, block it
      else {
        blockForkPositions.head
      }
    }
  }

  def takeCenterIfAvailable(board: Board): Int = if (board.grid(4) == ' ') 4 else -1

  def checkOppositeCorner(corners: (Int, Int), board: Board): Boolean =
    board.grid(corners._1) == otherMark && board.grid(corners._2) == ' '


  def takeOppositeCornersIfAvalable(board: Board): Int = {
    Board.oppositeCornerPositions.find(checkOppositeCorner(_, board)).getOrElse((-1, -1))._2
  }

  def takeAvailableCorner(board: Board): Int = {
    Board.cornerPositions.find(board.grid(_) == ' ').getOrElse(-1)
  }

  def takeAvailableSide(board: Board): Int = {
    Board.sidePositions.find(board.grid(_) == ' ')getOrElse(-1)
  }

  // choose next move
  override def chooseNextMove(board: Board): Int = {

    var nextMove = -1
    // player win in one move
    if (nextMove == -1) nextMove = playerWinInMove(board)
    // block opponent win in one move
    if (nextMove == -1) nextMove = blockOpponentWinInMove(board)
    // try to fork
    if (nextMove == -1) nextMove = playerFork(board)
    // block opponent fork
    if (nextMove == -1) nextMove = blockOpponentFork(board)
    // Take the center if available.
    if (nextMove == -1) nextMove = takeCenterIfAvailable(board)
    // Take the opposite corner from the opponent, if available.
    if (nextMove == -1) nextMove = takeOppositeCornersIfAvalable(board)
    // Take any available corner.
    if (nextMove == -1) nextMove = takeAvailableCorner(board)
    // Take any available side.
    if (nextMove == -1) nextMove = takeAvailableSide(board)
    // Never should be happen, but just in case take random
    if (nextMove == -1) nextMove = board.takeRandomFreePosition

    nextMove
  }
}

case class NeuralPlayer(markChar: Char) extends Player {

  def trainAllBoardsOnePass() = {
    val allBoards = Board.allBoards
    println(s"Train all boards in one pass")
    // allBoards.foreach(l => println(l.mkString))
  }

  // sign
  override def mark: Char = markChar
  // choose next move
  override def chooseNextMove(board: Board): Int = {
    1
  }
}