package hr.niko.tictactoe.model

import breeze.linalg.DenseMatrix
import breeze.numerics.{sigmoid, tanh}

/**
  * Created by Nikica on 18.2.2017.
  */
trait ActivationFunction {
  def forwardPropagation(z: DenseMatrix[Double]): DenseMatrix[Double];
  def backPropagation(z: DenseMatrix[Double]): DenseMatrix[Double];
}

object Sigmoid extends ActivationFunction {
  override def forwardPropagation(z: DenseMatrix[Double]): DenseMatrix[Double] =  sigmoid(z)
  override def backPropagation(z: DenseMatrix[Double]): DenseMatrix[Double] = {
    val s = sigmoid(z)
    s *:* (DenseMatrix.ones[Double](z.rows, z.cols) -:- s)
  }

}

object Tanh extends ActivationFunction {
  override def forwardPropagation(s: DenseMatrix[Double]): DenseMatrix[Double] = tanh(s)
  override def backPropagation(z: DenseMatrix[Double]) = ???
}