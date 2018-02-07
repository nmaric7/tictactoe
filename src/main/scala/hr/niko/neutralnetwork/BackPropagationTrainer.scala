package hr.niko.neutralnetwork

/**
  * Created by Nikica on 18.2.2017..
  */
class BackPropagationTrainer(val learningRate: Double, val momentum: Double, val iterations: Int) {

//  def train(neuralNetwork: NeuralNetwork, examples: List[(List[Double], List[Double])]) = {
//
//    for (i <- 1 to iterations) {
//      for {
//        (input, targetOutput) <- examples
//      } yield  {
//        val networkOutput = neuralNetwork.calculate(input)
//        neuralNetwork.backPropagate(networkOutput, targetOutput, learningRate, momentum)
//      }
//    }
//
//  }
//
//  def calculateError(neuralNetwork: NeuralNetwork, examples : List[(List[Double], List[Double])]) = {
//
//    (for {
//      (input, output) <- examples
//    } yield {
//      math.sqrt(math.pow(
//        (for {
//          (o1, o2) <- neuralNetwork.calculate(input) zip output
//        } yield {
//          math.pow(o1 - o2, 2.0)
//        }).sum, 2.0) / output.length)
//    }).sum
//  }

}
