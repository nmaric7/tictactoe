package hr.niko.draw

import scala.collection.mutable.ListBuffer

case class Draw(groups: ListBuffer[Group])

object Draw {
  // def apply(a: Group, b: Group, c: Group, d: Group, e: Group, f: Group, g: Group, h: Group): Draw = new Draw(a, b, c, d, e, f, g, h)

  def DEFAULT_DRAW: Draw = new Draw(ListBuffer(
    Group("A"), Group("B"), Group("C"), Group("D"),
    Group("E"), Group("F"), Group("G"), Group("H")))

  lazy val allCountries = List(
    Country(1, "Rusija", Federation.UEFA, 1),
    Country(2, "Njemačka", Federation.UEFA, 1),
    Country(3, "Brazil", Federation.CONMEBOL, 1),
    Country(4, "Portugal", Federation.UEFA, 1),
    Country(5, "Argentina", Federation.CONMEBOL, 1),
    Country(6, "Belgija", Federation.UEFA, 1),
    Country(7, "Poljska", Federation.UEFA, 1),
    Country(8, "Francuska", Federation.UEFA, 1),
    Country(9, "Španjolska", Federation.UEFA, 2),
    Country(10, "Peru", Federation.CONMEBOL, 2),
    Country(11, "Švicarska", Federation.UEFA, 2),
    Country(12, "Engleska", Federation.UEFA, 2),
    Country(13, "Kolumbija", Federation.CONMEBOL, 2),
    Country(14, "Meksiko", Federation.CONCACAF, 2),
    Country(15, "Urugvaj", Federation.CONMEBOL, 2),
    Country(16, "HRVATSKA", Federation.UEFA, 2),
    Country(17, "Danska", Federation.UEFA, 3),
    Country(18, "Island", Federation.UEFA, 3),
    Country(19, "Kostarika", Federation.CONCACAF, 3),
    Country(20, "Švedska", Federation.UEFA, 3),
    Country(21, "Tunis", Federation.CAF, 3),
    Country(22, "Egipat", Federation.CAF, 3),
    Country(23, "Senegal", Federation.CAF, 3),
    Country(24, "Iran", Federation.AFC, 3),
    Country(25, "Srbija", Federation.UEFA, 4),
    Country(26, "Nigerija", Federation.CAF, 4),
    Country(27, "Australija", Federation.AFC, 4),
    Country(28, "Japan", Federation.AFC, 4),
    Country(29, "Maroko", Federation.CAF, 4),
    Country(30, "Panama", Federation.CONCACAF, 4),
    Country(31, "Južna Koreja", Federation.AFC, 4),
    Country(32, "Saudijska Arabija", Federation.AFC, 4)
  )


}
