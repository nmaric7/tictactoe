package hr.niko.draw

import hr.niko.draw
import hr.niko.draw.Federation.Federation

import scala.collection.mutable.ListBuffer

object Federation extends Enumeration {
  type Federation = Value
  val AFC, CAF, CONCACAF, CONMEBOL, UEFA, EMPTY = Value
}

case class Country(id: Int, name: String, federation: Federation, pot: Int)

object Country {
  // def apply(id: Int, name: String, federation: Federation, pot: Int): Country = new Country(id, name, federation, pot)
  def DEFAULT_COUNTRY = new Country(-1, "-", Federation.EMPTY, 0)
}

case class Group(name: String, countries: ListBuffer[Country]) {
  def freeSpots: Int = countries.count(c => c.id == -1)
  def federationsMap: Map[Federation, Int] = countries.groupBy(c => c.federation).mapValues(_.size)
  def availableFederations: draw.Federation.ValueSet = Federation.values.filter(x => isAvailableFederation(x))

  def isAvailableFederation(x: draw.Federation.Value): Boolean = x match {
    case Federation.EMPTY =>
      false
    case _ =>
      val noOfCountries = federationsMap.getOrElse(x, 0)
      if (freeSpots == 1 && Federation.UEFA != x && federationsMap.getOrElse(Federation.UEFA, 0) == 0) false
      else if (Federation.UEFA != x && noOfCountries == 1) false
      else if (Federation.UEFA == x && noOfCountries == 2) false
      else true
  }
}

object Group {
  def apply(name: String): Group = new Group(name,
    ListBuffer(Country.DEFAULT_COUNTRY, Country.DEFAULT_COUNTRY, Country.DEFAULT_COUNTRY, Country.DEFAULT_COUNTRY))
}
