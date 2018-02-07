package hr.niko.neutralnetwork

import breeze.numerics.sqrt

/**
  * Created by Nikica on 18.2.2017..
  */

object NeuralNetwork {

  //List of weights under given neuron
  type NeuronWeight = Double
  type NeuronWeights = List[NeuronWeight]
  type WeightMatrix = List[NeuronWeights]
  type Weights = List[Layer]

  abstract class Layer(var layer: List[NeuronWeights], val bias: Boolean) {
    var inputs: List[Double] = null
    // deltas are calculated during back propagation
    // they should be the same length as neuron weights
    var deltas: List[Double] = null
    // old weight updates for backpropagation momentum
    var previousWeightUpdates: List[NeuronWeights] =
      for (neuron <- layer) yield {
        for (_ <- neuron) yield 0.0
      }

    def activationFunction(x: Double): Double

    def calculate(inps: List[Double]) = {

      if (bias) inputs = -1.0 :: inps else inputs = inps

      for (weights <- layer) yield {
        if (weights.length != inputs.length)
          throw new Exception("weights and inputs not of equal length" + weights.length.toString + inputs.length.toString)
        activationFunction(psp(weights, inputs))
      }
    }

    def psp(weights: NeuronWeights, inputs: List[Double]) = {
      (for {
        (x, y) <- weights zip inputs
      } yield x * y).sum
    }

    def replace_weights1(which: Int, withWhat: NeuronWeights, weights: List[NeuronWeights], acu: List[NeuronWeights]): List[NeuronWeights] = {
      val current :: nextNeurons = weights
      if (which == 0)
        acu.reverse ++ (withWhat :: nextNeurons)
      else
        replace_weights1(which - 1, withWhat, nextNeurons, current :: acu)
    }

    def replaceWeights(which: Int, withWhat: NeuronWeights) = {
      replace_weights1(which, withWhat, layer, List())
    }

    def learn(trainingSet: List[List[Double]]): List[NeuronWeights] = null

  }

  case class LinearLayer(layerx: List[NeuronWeights], biasx: Boolean = true) extends Layer(layerx, biasx) {
    override def activationFunction(x: Double): Double = x
  }

  case class SigmoidLayer(layerx: List[NeuronWeights], biasx: Boolean = true) extends Layer(layerx, biasx) {
    override def activationFunction(x: Double): Double = 1.0 / (1.0 + math.exp(-x))
  }

  case class ThresholdLayer(layerx: List[NeuronWeights], biasx: Boolean = true) extends Layer(layerx, biasx) {
    override def activationFunction(x: Double): Double = if (x > 0) 1.0 else 0.0
  }

  case class KohonenLayer(layerx: List[NeuronWeights]) extends Layer(layerx, false) {

    def this(inputs: Int, outputs: Int) =
      this(
        (for {
          _ <- 0 to outputs - 1
        } yield Array.fill(inputs)(0.0).toList).toList)


    var learningRate: Double = 0.03
    var conscience: Double = 1.0
    var neighShape: Int = 1
    var neighDist: Int = 0
    var winningCount = Array.fill(layer.length)(0)

    override def activationFunction(x: NeuronWeight): NeuronWeight = x

    override def psp(weights: NeuronWeights, inputs: List[NeuronWeight]): NeuronWeight = {
      (for {
        (x, y) <- weights zip inputs
      } yield math.pow((x - y), 2.0)).sum
    }

    def outputDistances(inputs: List[NeuronWeight]): List[NeuronWeight] = {
      for (weights <- layer) yield {
        if (weights.length != inputs.length)
          throw new Exception("weights and inputs not of equal length" + weights.length.toString + inputs.length.toString)
        psp(weights, inputs)
      }
    }

    def markWinner(outputs: List[Double]): List[NeuronWeight] = {
      val min = outputs.min
      val result = Array.fill(outputs.length)(0.0)
      result(outputs.indexOf(min)) = 1.0
      result.toList
    }

    override def calculate(inputs: List[Double]): List[NeuronWeight] = {
      markWinner(outputDistances(inputs))
    }

    def adjusted_distance(inputs: List[Double]): List[Double] = {
      val len = layer.length
      val result = markWinner(
        for {
          (output, winFreq) <- outputDistances(inputs) zip winningCount
        } yield output + conscience * (winFreq / len - 1)
      )
      winningCount(result.indexOf(1.0)) += 1
      result
    }

    def getNeighbourhood(i: Int): List[(Int, Int)] = {

      neighShape match {
        case 1 =>
          val rowSize = layer.length
          (for {
            x <- -neighDist to neighDist
            if x + i >= 0 && x + i < rowSize
          } yield (math.abs(x - i), x + i)).toList

        case 2 =>
          val rowSize = sqrt(layer.length) toInt

          (for {
            rowz <- -neighDist to neighDist; colz <- -neighDist to neighDist
            if (rowz >= 0 && rowz < rowSize && colz >= 0 && colz < rowSize && math.abs(rowz) + math.abs(colz) < neighDist)
          } yield (math.abs(rowz) + math.abs(colz), rowz * rowSize + colz)).toList

      }
    }

    override def learn(trainingSet: List[List[Double]]): List[NeuronWeights] = {
      for {
        trainingExample <- trainingSet
      } yield {
        val result = adjusted_distance(trainingExample)
        val winner = result.indexOf(1.0)
        val neuronsToTeach = getNeighbourhood(winner)
        train(neuronsToTeach, trainingExample)
      }
      layer
    }

    def train(neurons: List[(Int, Int)], input: List[Double]) {
      for {(dist, neuron) <- neurons} yield
        layer = replaceWeights(neuron,
          for {
            (weight, inp) <- layer(neuron) zip input
          } yield weight + (learningRate / (dist + 1)) * (inp - weight))
    }
  }

  def calculate(input: List[Double]) = ???

  def backPropagate(networkOutput: Nothing, targetOutput: List[Double], learningRate: Double, momentum: Double) = ???


}