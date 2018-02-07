package hr.niko.tictactoe

import java.io.PrintWriter
import java.util.Random

import hr.niko.tictactoe.model.{Board, NeuralPlayer, RandomPlayer, UnbeatablePlayer}

/**
  * Created by Nikica on 9.2.2017..
  */
object TicTacToe extends App {

  val xPlayer = new UnbeatablePlayer('X')
  val oPlayer = new UnbeatablePlayer('O')

  var xWon, oWon, draw = 0

  var allBoards = List[String]()
  for (i <- 1 to 1000) {
    // TicTacToe Grid, represents as array of 9 elements
    //  -------
    //  |0|1|2|
    //  -------
    //  |3|4|5|
    //  -------
    //  |6|7|8|
    //  -------
    val board = Board()

    // allBoards = board.grid.mkString :: allBoards
    val r = new Random()
    var playerOnMove = if (r.nextInt(2) == 0) 'X' else 'O'

    def isActiveGame(board: Board): Boolean = board.hasFreePosition && !Board.hasWin(board.grid)

    while (isActiveGame(board)) {
      allBoards = board.grid.mkString :: allBoards
      if ('X' == playerOnMove) {
        board.grid(xPlayer.chooseNextMove(board)) = 'X'
        playerOnMove = 'O'
      } else {
        board.grid(oPlayer.chooseNextMove(board)) = 'O'
        playerOnMove = 'X'
      }

    }

    if (Board.wonBy('X', board.grid)) xWon = xWon + 1
    else if (Board.wonBy('O', board.grid)) oWon = oWon + 1
    else draw = draw + 1

    if (i % 500 == 0) print(s".")
    if (i % 25000 == 0) print(s"\n")
    if (i % 100000 == 0) print(s"$i\n")

  }

  println(s"\nX player won $xWon games \n" +
    s"O player won $oWon games \n" +
    s"Draw $draw games")

  println(s"List size = ${allBoards.length}")
  println(s"Set size = ${allBoards.toSet.size}")

  new PrintWriter("allBoards-UnbeatableVsUnbeatable.txt") {
    allBoards.toSet[String].foreach(l => write(s"$l\n"))
    close
  }


}
