package hr.niko.tictactoe

import hr.niko.tictactoe.model.Board

object TicTacToeGame extends App {
  // constants
  val INPUT_NEURONS = 18
  val HIDDEN_LAYER_NEURONS = 36
  val OUTPUT_NEURONS = 9

  // get examples from files
  val (trainingExamples, devExamples, testExamples) = Board.getExamples()
  // transform training examples for learning NN
  // val (trainingX, trainingY) = TransformUtils.getNNInputData(trainingExamples)
  // initialize NN
  // NeuralNetwork(List(INPUT_NEURONS, HIDDEN_LAYER_NEURONS, OUTPUT_NEURONS))






}
