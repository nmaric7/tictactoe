package hr.niko.tictactoe

import breeze.linalg.{DenseMatrix, sum}
import breeze.numerics.{sigmoid, tanh}
import hr.niko.tictactoe.model.{Board, NeuralPlayer, RandomPlayer, UnbeatablePlayer}

/**
  * Created by Nikica on 12.2.2017..
  */
object NeuralTicTacToe extends App {

  val xPlayer = new RandomPlayer('X')
  val oPlayer = new NeuralPlayer('O')

  val idealXPlayer = new UnbeatablePlayer('X')
  val idealOPlayer = new UnbeatablePlayer('O')

  val epsilonInit = 0.1
  val learningRate = 0.80

  val INPUT_NEURONS = 18
  val HIDDEN_LAYER_NEURONS = 36
  val OUTPUT_NEURONS = 9

  // initialize weights
  // 19 input features x 36 hidden layer features
  var theta1: DenseMatrix[Double] = (DenseMatrix.rand(INPUT_NEURONS + 1, HIDDEN_LAYER_NEURONS) *:* (2 * epsilonInit)) -:- epsilonInit
  // 37 input features x 9 output layer features
  var theta2: DenseMatrix[Double] = (DenseMatrix.rand(HIDDEN_LAYER_NEURONS + 1, OUTPUT_NEURONS) *:* (2 * epsilonInit)) -:- epsilonInit

  var theta1Grad = DenseMatrix.tabulate[Double](theta1.rows, theta1.cols) { case (_, _) => 0.0}
  var theta2Grad = DenseMatrix.tabulate[Double](theta2.rows, theta2.cols) { case (_, _) => 0.0}

  println(s"Learning....")

  val inputs = Board.allBoards.map { input =>
    val board = new Board(input)
    val (xMoves, oMoves) = board.countMoves
    val (nextXMove, nextOMove) =
      if (xMoves == oMoves) (idealXPlayer.chooseNextMove(board), idealOPlayer.chooseNextMove(board))
      else if (xMoves < oMoves) (idealXPlayer.chooseNextMove(board), -1)
      else (-1, idealOPlayer.chooseNextMove(board))
    (input, nextXMove, nextOMove)
  }

  // transform input board case into NN input
  // First 9 elements presents X moves (X = 1, other 0
  // Next 9 elements presents O moves (O = 1, other 0)
  // O  X  XOX  -> 000100101100000010
  def transformInput(grid: Grid) = transformInputByMark(grid, 'X') ++ transformInputByMark(grid, 'O')

  def transformInputByMark(grid: Grid, mark:Char): Array[Double] = grid.map( x => if (x == mark) 1.0 else 0.0 )

  def calcTanh(value: DenseMatrix[Double]): DenseMatrix[Double]  = tanh(value)

  def calcTanhInverse(value: DenseMatrix[Double]): DenseMatrix[Double] = {
    val s = tanh(value)
    s *:* (DenseMatrix.ones[Double](value.rows, value.cols) -:- s)

  }

  def calcInverse(value: DenseMatrix[Double]): DenseMatrix[Double] = {
    val tmp = tanh(value)
    DenseMatrix.ones[Double](value.rows, value.cols) -:- (tmp *:* tmp)
  }

//  private def tanh(u: Double) = {
//    val a = Math.exp(u)
//    val b = Math.exp(-u)
//    (a - b) / (a + b)
//  }

  def getIndexOfMaxValue(matrix: DenseMatrix[Double]) = {
    var offset = 0
    var maxValue = Double.MinValue
    var maxIndex = -1
    while (offset < matrix.activeSize) {
      if (matrix.valueAt(offset) > maxValue) {
        maxValue = matrix.valueAt(offset)
        maxIndex = matrix.indexAt(offset)
      }
      offset += 1
    }
    maxIndex
  }

  def sigmoidGradient(z: DenseMatrix[Double]): DenseMatrix[Double]= {
    val s = sigmoid(z)
    s *:* (DenseMatrix.ones[Double](z.rows, z.cols) -:- s)
  }

  def forwardPropagate(nnInput: Array[Double], nextMove: Int): DenseMatrix[Double] = {
    // println(s"nnInput = ${nnInput.mkString(" ")}")
    // calculate forward propagation
    val a1bias: DenseMatrix[Double] = DenseMatrix.tabulate(1, nnInput.length + 1) { case (_, j) => if (j == 0) 1.0 else nnInput(j - 1) }
    val z2: DenseMatrix[Double] = theta1.t * a1bias.t
    val a2: DenseMatrix[Double] = sigmoid(z2)
    // val a2: DenseMatrix[Double] = calcTanh(z2)

    val a2bias: DenseMatrix[Double] = DenseMatrix.vertcat(DenseMatrix.ones[Double](1, 1), a2)
    val z3: DenseMatrix[Double] = theta2.t * a2bias
    sigmoid(z3)
  }

  def calculateZ2(nnInput: Array[Double], nextMove: Int): DenseMatrix[Double] = {
    val a1bias: DenseMatrix[Double] = DenseMatrix.tabulate(1, nnInput.length + 1) { case (_, j) => if (j == 0) 1.0 else nnInput(j - 1) }
    val z2: DenseMatrix[Double] = theta1.t * a1bias.t
    sigmoid(z2)
  }

  def trainOneMove(nnInput: Array[Double], nextMove: Int) = {

    // println(s"nnInput = ${nnInput.mkString(" ")}")
    // calculate forward propagation
    val a1bias: DenseMatrix[Double] = DenseMatrix.tabulate(1, nnInput.length + 1) { case (_, j) => if (j == 0) 1.0 else nnInput(j - 1) }
    val z2: DenseMatrix[Double] = theta1.t * a1bias.t
    val a2: DenseMatrix[Double] = sigmoid(z2)
    // val a2: DenseMatrix[Double] = calcTanh(z2)

    val a2bias: DenseMatrix[Double] = DenseMatrix.vertcat(DenseMatrix.ones[Double](1, 1), a2)
    val z3: DenseMatrix[Double] = theta2.t * a2bias
    val a3 = sigmoid(z3)

    val output: DenseMatrix[Double] = DenseMatrix.tabulate(1, OUTPUT_NEURONS) { case (_, j) => if (j == nextMove) 1.0 else 0.0 }
    // d3
    val d3: DenseMatrix[Double] = a3.t - output

    // bias z2
    val z2bias = DenseMatrix.vertcat(DenseMatrix.ones[Double](1, 1), z2)
    // calculate d2 (theta2 * d3) .* sigmoidGradient(z2)
    val d2biasTemp = theta2 * d3.t

    val g2: DenseMatrix[Double] = sigmoidGradient(z2bias)
    // val g2: DenseMatrix[Double] = calcTanhInverse(z2bias)
    val d2bias: DenseMatrix[Double] = d2biasTemp *:* g2
    // remove bias
    val d2: DenseMatrix[Double] = DenseMatrix.tabulate[Double](d2bias.rows - 1, d2bias.cols) { case (i, j) => d2bias(i + 1, j) }

    // calculate Theta2Grad, Theta1Grad
    theta2Grad = (d3.t * a2bias.t).t
    theta1Grad = (d2 * a1bias).t

    theta2 = theta2 -:- (theta2Grad *:* learningRate)
    theta1 = theta1 -:- (theta1Grad *:* learningRate)

  }

  var trainPass = 1
  var trainFail = 1
  var step = 1

  def minimize(nnInput: Array[Double], nextMove: Int) = {
    val limit = 0.3
    var mean = Double.MaxValue
    val result: DenseMatrix[Double] = DenseMatrix.tabulate(1, OUTPUT_NEURONS) { case (_, j) => if (j == nextMove) 1.0 else 0.0 }
    var trainedValue: Int = -1

    while (mean > limit && trainedValue != nextMove) {
      trainOneMove(nnInput, nextMove)
      val t1 = forwardPropagate(nnInput, nextMove)
      trainedValue = getIndexOfMaxValue(t1)
      val d3: DenseMatrix[Double] = t1.t - result
      mean = sum(d3) / (d3.cols * d3.rows)
    }

    trainedValue = getIndexOfMaxValue(forwardPropagate(nnInput, nextMove))
    // println(s"Trained=$trainedValue  NextMove=$nextMove")
    // update counters
    if (trainedValue == nextMove) trainPass = trainPass + 1 else trainFail = trainFail + 1

  }
  var minFail = Int.MaxValue

  while (step < 100000 && trainFail > 0) {

    trainPass = 0
    trainFail = 0


    // println(s"${theta1(0,0)} - ${theta2(0,0)}")
    inputs.foreach { case (input, nextXMove, nextOMove) =>
      // println(s"input = ${input.mkString}")
      val nnInput = transformInput(input)
      if (nextXMove > -1) minimize(nnInput, nextXMove)
      if (nextOMove > -1) minimize(nnInput, nextOMove)
    }

    if (trainFail < minFail) {
      minFail = trainFail
      println(s"Step $step: Train pass = $trainPass, Train fail = $trainFail")
    }

    if (step % 100 == 0) println(s"Step $step: Train pass = $trainPass, Train fail = $trainFail")
    step = step + 1

/*
    theta1 = theta1Grad
    theta2 = theta2Grad
*/
  }

  if (trainFail == 0) {
    println(s"Step $step: Training finished")
    println(s"Theta1: $theta1")
    println(s"Theta2: $theta2")
  }

}
