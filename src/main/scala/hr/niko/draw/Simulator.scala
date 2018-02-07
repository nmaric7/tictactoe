package hr.niko.draw

import hr.niko.draw.Federation.Federation

import scala.collection.immutable.ListMap
import scala.collection.mutable.ListBuffer
import scala.util.Random

object Simulator extends App {

  implicit def reflector(ref: AnyRef) = new {
    def getV(name: String): Any = ref.getClass.getMethods.find(_.getName == name).get.invoke(ref)
    def setV(name: String, value: Any): Unit = ref.getClass.getMethods.find(_.getName == name + "_$eq").get.invoke(ref, value.asInstanceOf[AnyRef])
  }

  def getCountryById(countryId: Int) = Draw.allCountries.find(x => x.id == countryId).getOrElse(Country.DEFAULT_COUNTRY)

  def setCountryToGroup(c: Country, g: String, draw: Draw): Unit = {
    val groupIndex = draw.groups.indexWhere(group => group.name == g)
    draw.groups(groupIndex).countries(c.pot - 1) = c
  }

  def simulateDraw(): Unit = {
    val draw = Draw.DEFAULT_DRAW
    // set Host to A1
    setCountryToGroup(getCountryById(1), "A", draw)

    drawPot(draw, 1)
    drawPot(draw, 2)
    drawPot(draw, 3)
    drawPot(draw, 4)

    printDraw(draw)
  }

  for (i <- 1 to 10000) {
    println(s" Simulation $i")
    simulateDraw()
  }


  def filterReserved(group: Group, reservedFederations: List[Federation]) =
    if (reservedFederations.isEmpty) true
    else {
      println(s"Reserved federations = $reservedFederations, group = $group")
      reservedFederations.exists(f => !group.isAvailableFederation(f))
    }

  def filterReservedGroups1(tail: List[Country], groups: ListBuffer[Group]): Unit = {
    val countriesByFederation = tail.groupBy(c => c.federation).mapValues(_.size)
    println(s"filterReservedGroups1 - countriesByFederation : $countriesByFederation ")
    val federationCombinations = countriesByFederation.keys.toSet[Federation].subsets.map(_.toList).toList
    println(s"Federation combination - ${federationCombinations.mkString(" - ")}")
    val filteredFederationCombinations = federationCombinations.filter(f => {
      val groupCount = groups.count(g => f.forall(fed => g.isAvailableFederation(fed)))
      val federationSum = countriesByFederation.filter(t => f.contains(t._1)).values.sum
      println(s"$f - groupCount = $groupCount federationSum = $federationSum")
      groupCount == federationSum
    })

    println(s"Filtered federation combination - ${filteredFederationCombinations.mkString(" - ")}")

  }

  def filterReservedGroups(tail: List[Country], groups: ListBuffer[Group]): ListBuffer[Group] = {
    filterReservedGroups1(tail, groups)
    // number of available groups by federation (ex. UEFA -> 7, ACF -> 6 ...)
    val availableGroupsByFederation = groups.flatMap(g => g.availableFederations).groupBy(identity).mapValues(_.size)
    println(s"availableGroupsByFederation : $availableGroupsByFederation")
    // number of countries in pot by federation (ex. UEFA -> 2, ACF -> 1 ...)
    val countriesByFederation = tail.groupBy(c => c.federation).mapValues(_.size)
    // check is equal for some federation
    val reservedFederationsMap = countriesByFederation.filter(x => availableGroupsByFederation.getOrElse(x._1, 0) == x._2)

    val reservedFederations = ListMap(reservedFederationsMap.toSeq.sortBy(_._2):_*).keys.toList
    println(s"reservedFederations : $reservedFederations")
    if (reservedFederations.isEmpty) groups
    else filterReservedGroups(tail.filterNot(c => c.federation == reservedFederations.head), groups.filterNot(g => g.isAvailableFederation(reservedFederations.head)))
  }

  def drawSingleGroup(country: Country, tail: List[Country], groups: ListBuffer[Group]) = {
    // if country federation is not in reserved, remove those groups from available
    val filteredReservedGroups = filterReservedGroups(tail, groups)
    println(s"filterReservedGroups : $filteredReservedGroups")
    // check is group available for country
    val availableGroups = filteredReservedGroups.filter(g => g.isAvailableFederation(country.federation))
    println(s"availableGroups : $availableGroups")
    //    // check is there group where only fit selected country
    //    val singleGroup = availableGroups.filter(ag => tail.forall(c => !ag.isAvailableFederation(c.federation)))
    //    println(s"singleGroup : ${singleGroup}")

    availableGroups.head.name
    //    if (singleGroup.isEmpty)
    //    else singleGroup.head.name

  }

  def drawGroup(country: Country, tail: List[Country], groups: ListBuffer[Group]): String = {
    val federetionsInPot = (country :: tail).groupBy(c => c.federation).keys
    // check is there group with single possible federation
    val singleGroups = groups.filter(g => federetionsInPot.count(f => g.isAvailableFederation(f)) == 1)

    singleGroups.find(g => g.isAvailableFederation(country.federation)) match {
      case Some(gr) => gr.name
      case None => drawSingleGroup(country, tail, groups)
    }
  }

  def drawPot(draw: Draw, potId: Int) = {
    // remove host country from draw. it is already set
    val potCountries = Draw.allCountries.filter(x => x.pot == potId && x.id != 1)
    val potGroups = draw.groups.filter(x => x.freeSpots == 4 - potId + 1)

    def setPlaces(pot: List[Country], groups: ListBuffer[Group], draw: Draw): Unit = pot match {
      case (head :: tail) =>
        println(s"Country ${head.name}")
        println(s"Available groups ${groups.map(_.name).mkString(" - ")}")
        val group = drawGroup(head, tail, groups)
        println(s"to Group $group")
        setCountryToGroup(head, group, draw)
        setPlaces(Random.shuffle(tail), groups.filterNot(g => g.name == group), draw)
      case _ =>
    }
    setPlaces(Random.shuffle(potCountries), potGroups, draw)
  }


  def printGroup(g: Group) = println(s"${g.name} :: ${g.countries.map(_.name).mkString(" - ")}")

  def printDraw(draw: Draw) = {
    draw.groups.foreach(g => printGroup(g))
  }


}
