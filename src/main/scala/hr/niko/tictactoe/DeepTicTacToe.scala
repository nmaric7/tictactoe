package hr.niko.tictactoe

import java.util.Random

import breeze.linalg.{*, DenseMatrix, DenseVector, sum}
import breeze.numerics.{exp, log, sigmoid, tanh}
import hr.niko.tictactoe.model.{Board, NeuralPlayer, RandomPlayer, UnbeatablePlayer}

/**
  * Created by Nikica on 12.2.2017..
  */
object DeepTicTacToe extends App {

  val xPlayer = new RandomPlayer('X')
  val oPlayer = new NeuralPlayer('O')
  val oRandomPlayer = new RandomPlayer('O')

  val idealXPlayer = new UnbeatablePlayer('X')
  val idealOPlayer = new UnbeatablePlayer('O')

  val epsilonInit = 0.12
  val learningRate = 0.8

  val INPUT_NEURONS = 18
  val HIDDEN_LAYER_NEURONS = 36
  val OUTPUT_NEURONS = 9

  // get examples from files
  val (trainingExamples, devExamples, testExamples) = Board.getExamples()
  // filter only X moves
  val trainingXExamples = TransformUtils.filterExamplesByPlayer(trainingExamples, 'X')
  // transform training examples for learning NN
  val inputData = TransformUtils.getNNInputData(trainingExamples, idealXPlayer)

  val X = DenseMatrix.tabulate(18, inputData.length) { case (i, j) => inputData(j)._1(i)}
  val Y = DenseMatrix.tabulate(9, inputData.length) { case (i, j) => if (inputData(j)._2 == i) 1.0 else 0.0 }

  val m = X.cols.toDouble

  // initialize weights (w1, b1, w2, b2)
  var W1: DenseMatrix[Double] = (DenseMatrix.rand(HIDDEN_LAYER_NEURONS, INPUT_NEURONS) *:* (2 * epsilonInit)) -:- epsilonInit
  var b1: DenseVector[Double] = (DenseVector.zeros(HIDDEN_LAYER_NEURONS))
  var W2: DenseMatrix[Double] = (DenseMatrix.rand(OUTPUT_NEURONS, HIDDEN_LAYER_NEURONS) *:* (2 * epsilonInit)) -:- epsilonInit
  var b2: DenseVector[Double] = (DenseVector.zeros(OUTPUT_NEURONS))

  def calcTanh(value: DenseMatrix[Double]): DenseMatrix[Double]  = tanh(value)

  def shape(m: DenseMatrix[Double], name: String) = println(s"$name = (${m.rows}, ${m.cols})")
  def shapeVector(m: DenseVector[Double], name: String) = println(s"$name = (${m.size})")


  def train(i: Int) = {
    // forward propagation
    val Z1Temp = W1 * X
    val Z1 = Z1Temp(::, *) + b1
    val A1 = tanh(Z1)
    val Z2Temp = W2 * A1
    val Z2 = Z2Temp(::, *) + b2
    val A2 = sigmoid(Z2)

    // calculate cost function
    val costOnes = DenseMatrix.ones[Double](OUTPUT_NEURONS, m.toInt)
    val cost = -((log(A2) *:* Y) +:+ (log(costOnes -:- A2) *:* (costOnes -:- Y)))
    // val cost = -(log(A2) *:* Y)
    val costSum = sum(cost) / m
    if (i % 100 == 0) println(s"${i}. costSum = $costSum")

    // back propagation
    val dZ2 = A2 -:- Y
    val dW2 = (dZ2 * A1.t) /:/ m
    val db2 = sum(dZ2(*, ::)) /:/ m
    val ones = DenseMatrix.ones[Double](HIDDEN_LAYER_NEURONS, m.toInt)
    val dZ1 = (W2.t * dZ2) *:* (ones -:- (A1 *:* A1))
    val dW1 = (dZ1 * X.t) /:/ m
    val db1 = sum(dZ1(*, ::)) /:/ m

    W1 = W1 -:- (dW1 *:* learningRate)
    W2 = W2 -:- (dW2 *:* learningRate)

    b1 = b1 -:- (db1 *:* learningRate)
    b2 = b2 -:- (db2 *:* learningRate)

  }

  var countCalculatedPositions, countRandomPositions, movesAsUnbeatable = 0

  def softmax(Z: DenseMatrix[Double]) = {
    val T = exp(Z)
    val sumT = sum(T)
    T /:/ sumT
  }

  def calculateMove(board: Board) = {
    countCalculatedPositions = countCalculatedPositions + 1
    val grid = board.grid
    val transformedInput = TransformUtils.transformInput(grid)
    val X = DenseMatrix.tabulate(18, 1) { case (i, _) => transformedInput(i)}

    // forward propagation
    // shape(W1, "W1")
    // shape(b1, "b1")
    val Z1Temp = W1 * X
    val Z1 = Z1Temp(::, *) + b1
    // shape(t, "t")
    // val Z1 = t(::, *) + b1
    val A1 = tanh(Z1)
    // shape(A1, "A1")
    // shape(W2, "W2")
    // shape(b2, "b2")
    val Z2Temp = W2 * A1
    val Z2 = Z2Temp(::, *) + b2
    // shape(u, "u")
    // val Z2 = u + b2
    // val A2 = sigmoid(Z2)
    val A2 = softmax(Z2)
    // shape(A2, "A2")
    // println(A2)
//    var nextMove = 0
//    var maxValue = A2(0,0)
//    var counter = 0

    val sortedResult = A2.toArray.zipWithIndex.sortBy(_._1).reverse
    val result = sortedResult.find(r => grid(r._2) == ' ')

    val nextMove = result.getOrElse((1.0, 0))._2
    if (nextMove == idealXPlayer.chooseNextMove(board)) movesAsUnbeatable = movesAsUnbeatable + 1

    nextMove
  }

  def playAgainstUnbeatable(numberOfGames: Int) = {
    var xWon, oWon, draw = 0
    for (i <- 1 to numberOfGames) {
      val board = Board()

      val r = new Random()
      var playerOnMove = if (r.nextInt(2) == 0) 'X' else 'O'

      def isActiveGame(board: Board): Boolean = board.hasFreePosition && !Board.hasWin(board.grid)

      while (isActiveGame(board)) {
        if ('X' == playerOnMove) {
          //        println(s"random player ${xPlayer.chooseNextMove(board)}")
          //        chooseNextMove(board)
          //        board.grid(xPlayer.chooseNextMove(board)) = 'X'
          board.grid(calculateMove(board)) = 'X'
          playerOnMove = 'O'
        } else {
          board.grid(idealOPlayer.chooseNextMove(board)) = 'O'
          // board.grid(oRandomPlayer.chooseNextMove(board)) = 'O'
          playerOnMove = 'X'
        }
      }

      if (Board.wonBy('X', board.grid)) xWon = xWon + 1
      else if (Board.wonBy('O', board.grid)) oWon = oWon + 1
      else draw = draw + 1
    }
    // println(board.toString)
    // println(board.grid.mkString)
    println(s"UNBEATABLE ::: Total calculated positions = $countCalculatedPositions, Moves as Unbeatble = $movesAsUnbeatable")
    println(s"UNBEATABLE ::: xWon = $xWon, oWon = $oWon, draw = $draw")
  }

  def playAgainstRandom(numberOfGames: Int) = {
    var xWon, oWon, draw = 0
    for (i <- 1 to numberOfGames) {
      val board = Board()

      val r = new Random()
      var playerOnMove = if (r.nextInt(2) == 0) 'X' else 'O'

      def isActiveGame(board: Board): Boolean = board.hasFreePosition && !Board.hasWin(board.grid)

      while (isActiveGame(board)) {
        if ('X' == playerOnMove) {
          //        println(s"random player ${xPlayer.chooseNextMove(board)}")
          //        chooseNextMove(board)
          //        board.grid(xPlayer.chooseNextMove(board)) = 'X'
          board.grid(calculateMove(board)) = 'X'
          playerOnMove = 'O'
        } else {
          // board.grid(idealOPlayer.chooseNextMove(board)) = 'O'
          board.grid(oRandomPlayer.chooseNextMove(board)) = 'O'
          playerOnMove = 'X'
        }
      }

      if (Board.wonBy('X', board.grid)) xWon = xWon + 1
      else if (Board.wonBy('O', board.grid)) oWon = oWon + 1
      else draw = draw + 1
    }
    // println(board.toString)
    // println(board.grid.mkString)
    println(s"RANDOM ::: Total calculated positions = $countCalculatedPositions, Moves as Unbeatble = $movesAsUnbeatable")
    println(s"RANDOM ::: xWon = $xWon, oWon = $oWon, draw = $draw")
  }

  def calculateAccuracy(examples: List[Grid], name: String) = {
    val acc = TransformUtils.getNNInputData(examples, idealXPlayer)
    var goodMoves = 0
    examples.zipWithIndex.foreach{case (x, i) => {
      val b = new Board(x)
      val nextMove = calculateMove(b)
      if (nextMove == acc(i)._2) goodMoves = goodMoves + 1
    }}
    val accuracy = goodMoves.toFloat / acc.length
    println(s"$name: $accuracy")
  }

  def trainAndPlay(numberOfIteration: Int, numberOfTrainings: Int, numberOfGames: Int) = {
    countCalculatedPositions = 0
    movesAsUnbeatable = 0
    for (i <- 1 to numberOfTrainings)
      train(numberOfIteration * numberOfTrainings + i)

    calculateAccuracy(trainingXExamples, "Training")
    calculateAccuracy(TransformUtils.filterExamplesByPlayer(devExamples, 'X'), "Dev")
    calculateAccuracy(TransformUtils.filterExamplesByPlayer(testExamples, 'X'), "Test")

    val numberOfGames = 200
    playAgainstUnbeatable(numberOfGames)
    playAgainstRandom(numberOfGames)

  }


  try {
    val numberOfIterations = 500
    for (i <- 0 to numberOfIterations) {
      val numberOfTrainings = 100
      val numberOfGames = 200
      if (trainAndPlay(i, numberOfTrainings, numberOfGames) == numberOfGames) throw new Exception
    }
  } catch {
    case _: Throwable => println("Learned!!!")
  }


}
