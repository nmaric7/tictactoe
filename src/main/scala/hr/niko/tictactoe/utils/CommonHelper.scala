package hr.niko.tictactoe.utils

import breeze.linalg.DenseMatrix
import breeze.numerics.rint

/**
  * Created by Nikica on 15.2.2017..
  */
object CommonHelper {

  def roundMatrixElementsToNDecimals(matrix: DenseMatrix[Double], n: Int): DenseMatrix[Double] =
    rint(matrix *:* Math.pow(10, n)) /:/ Math.pow(10, n)


}
