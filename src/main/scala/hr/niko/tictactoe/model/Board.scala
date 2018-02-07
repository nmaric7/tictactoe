package hr.niko.tictactoe.model

import hr.niko.tictactoe.Grid

import scala.collection.mutable
import scala.io.Source
import scala.util.Random

/**
  * Created by Nikica on 10.2.2017..
  */

case class Board(grid: Grid) {

  override def toString: String =
        s"$numberOfMoves. \n" +
        s"-------\n" +
        s"|${grid(0)}|${grid(1)}|${grid(2)}|\n" +
        s"-------\n" +
        s"|${grid(3)}|${grid(4)}|${grid(5)}|\n" +
        s"-------\n" +
        s"|${grid(6)}|${grid(7)}|${grid(8)}|\n" +
        s"-------\n"

  def numberOfMoves: Int = grid.filter(_ != ' ').length

  def hasFreePosition: Boolean = grid.filter(_ == ' ').length > 0

  def freePositions: Array[Int] = {
    for (
      (x, idx) <- grid.zipWithIndex
      if x == ' '
    ) yield idx
  }

  def takeRandomFreePosition: Int = {
    if (freePositions.isEmpty) -1
    else {
      // get random position
      val r = new Random
      // return random value from free positions
      freePositions(r.nextInt(freePositions.length))
    }
  }

  def countMoves: (Int, Int) =
    (grid.filter(_ == 'X').length, grid.filter(_ == 'O').length)
}


object Board {

  def getBoards(name: String): List[Grid] =
    (for (
      line <- Source.fromResource(name).getLines()
    ) yield line.toArray).toList

  def getExamples() = {
    lazy val allRandomBoards: List[Grid] = getBoards("allBoards-RandomVsRandom.txt")
    lazy val allUnbeatableBoards: List[Grid] = getBoards("allBoards-UnbeatableVsUnbeatable.txt")
    lazy val allRandomVsUnbeatableBoards: List[Grid] = getBoards("allBoards-RandomVsUnbeatable.txt")
    val allBoards: List[Grid] = allRandomBoards ::: allUnbeatableBoards ::: allRandomVsUnbeatableBoards
    // val allBoards: List[Grid] = allUnbeatableBoards ::: allRandomVsUnbeatableBoards

    val trainingSet = mutable.MutableList[Grid]()
    val devSet = mutable.MutableList[Grid]()
    val testSet = mutable.MutableList[Grid]()

    Random.shuffle(allBoards).zipWithIndex.foreach{ case (b, i) => {
        val rest = i % 10
        if (rest < 6) trainingSet += b
        else if (rest >= 5 && rest < 8) devSet += b
        else testSet += b
      }
    }

    (trainingSet.toList, devSet.toList, testSet.toList)
  }


  def apply(): Board = new Board(Array.fill[Char](9)(' '))

  val winSets =
    Random.shuffle(
      List(
        List(0, 1, 2),
        List(3, 4, 5),
        List(6, 7, 8),
        List(0, 3, 6),
        List(1, 4, 7),
        List(2, 5, 8),
        List(0, 4, 8),
        List(2, 4, 6))
    )

  lazy val oppositeCornerPositions =
    Random.shuffle(
      List(
        (0, 8),
        (2, 6),
        (6, 2),
        (8, 0)
      )
    )

  lazy val cornerPositions =
    Random.shuffle(List(0, 2, 6, 8))

  lazy val sidePositions =
    Random.shuffle(List(1, 3, 5, 7))

  def wonBy(mark: Char, board: Grid): Boolean =
    Board.winSets.exists(winSet => winSet.forall(board(_) == mark))

  def hasWin(board: Grid): Boolean = wonBy('X', board) || wonBy('O', board)

  lazy val allBoards: List[Grid] = {
    println(s"Load all boards")
    (for (
      line <- Source.fromResource("allBoards-RandomVsUnbeatable.txt").getLines()
    ) yield line.toArray).toList
  }



}

