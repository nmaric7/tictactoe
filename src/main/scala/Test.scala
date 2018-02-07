import breeze.linalg.DenseMatrix
import breeze.numerics.sigmoid

/**
  * Created by Nikica on 16.2.2017..
  */
object Test extends App {

  val d = 2.0

  val s = sigmoid(d)

  val g = s * (1 - s)

  val a1 = DenseMatrix((1.0,2.0),
    (4.0,5.0))

  val a2 = DenseMatrix((-1.0,2.0),
    (4.0,-5.0))

  val p1 = a1 * a2

  println(s"$d -> $s -> $g")
  println(s"$p1")

}
