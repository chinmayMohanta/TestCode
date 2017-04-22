package services.dataAccess
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Singleton

import scala.collection.mutable
import scala.collection.mutable.{HashSet, ListBuffer}
import scala.io.Source._

trait DataAccess {}  // can be used to reenforce the interface

@Singleton
class DataAccessService extends DataAccess {

    val countriesBuffer = fromFile("data/countries.csv")("UTF-8") //make sure correct byte encoding
    val airportsBuffer = fromFile("data/airports.csv")("UTF-8")
    val runwaysBuffer = fromFile("data/runways.csv")("UTF-8")

    val countries = new HashSet[Array[String]]
    val airports = new HashSet[Array[String]]
    val runways = new HashSet[Array[String]]

 // Load the data
  createTables()

  def createTables(): Unit = {
    val count1 = new AtomicInteger()
    count1.set(0)
    for (line <- countriesBuffer.getLines) {
      if (count1.get() ==0) count1.incrementAndGet() else {
        val cols = line.split(",").map(x => x.replaceAll("\"", "").trim)
        countries += cols
        count1.incrementAndGet()
      }
    }
    countriesBuffer.close
    //countries.foreach(x=> {println(x.mkString(","))})


    var count2 =new AtomicInteger() // helper counter to skip the header
    count2.set(0)
    for (line <- airportsBuffer.getLines) {
      if (count2.get() ==0) count2.incrementAndGet() else {
        val cols = line.split(",").map(x=> x.replaceAll("\"","").trim)  // Remove the double quote
        airports += cols
        count2.incrementAndGet()
      }
    }
    airportsBuffer.close
    //airports.foreach(x=> {println(x.mkString(","))})


    //val countries = new HashMap[String,Array[String]]
    var count3 =new AtomicInteger() // helper counter to skip the header
    count3.set(0)
    for (line <- runwaysBuffer.getLines) {
      if (count3.get() ==0) count3.incrementAndGet() else {
        val cols = line.split(",").map(x=> x.replaceAll("\"","").trim)
        runways += cols
        count3.incrementAndGet()
      }
    }
    runwaysBuffer.close
    //runways.foreach(x=> {println(x.mkString(","))})

  }


  def findAirportsByCountryCode(cc:String):HashSet[Map[String,String]] = {
    val res =airports.map(x => if (x(8).equalsIgnoreCase(cc)) x)
    val runDetails = new HashSet[String]
    for (airport <- res){
      airport match {
        case air:Array[String ]=> { val tmp =runways.filter(x => x(2).equalsIgnoreCase(air(1)))
          tmp.foreach(x=> runDetails += air(3) + ","+ x.mkString(","))}
        case _ => println("Empty")
      }
    }

    //runDetails.foreach(x => println(x))
    val rdetailsValues =runDetails.map(x => x.split(","))
    val rdetailsNames = List("name","id","airport_ref","airport_ident","length_ft","width_ft","surface","lighted","closed","le_ident","le_latitude_deg","le_longitude_deg","le_elevation_ft","le_heading_degT","le_displaced_threshold_ft","he_ident","he_latitude_deg","he_longitude_deg","he_elevation_ft","he_heading_degT","he_displaced_threshold_ft")
    rdetailsValues.map(x => rdetailsNames.zip(x) toMap)
  }

  def findAirportsByCountry(country:String):HashSet[Map[String,String]]  = {
    var cc = ""
    countries.filter(x => x(2).equalsIgnoreCase(country)).take(1).foreach(y => cc=y(1))
    findAirportsByCountryCode(cc.toString)
  }

  def countryWithHighestNumberOfAirports(): Map[String,Int] = {
    val counts = new HashSet[String]
    val res =airports.map(x => x(8).toString)
    val lbf = new ListBuffer[String]
    airports.foreach(x => lbf += x(8))
    lbf.groupBy(x => x).map(y => (y._1,y._2.length)).toSeq.sortWith(_._2 > _._2).take(10).toList.toMap
  }

  def countryWithLowestNumberOfAirports(): Map[String,Int] = {
    val lbf = new ListBuffer[String]
    airports.foreach(x => lbf += x(8))
    lbf.groupBy(x => x).map(y => (y._1,y._2.length)).toSeq.sortWith(_._2 < _._2).take(10).toMap
  }

  def findCountryAirportDetail(country:String):mutable.HashSet[String] = {
    var ccode = ""
    val cc = countries.filter(x => x(2).equalsIgnoreCase(country)).take(1).foreach(y => ccode=y(1))
    val res =airports.map(x => if (x(8).equalsIgnoreCase(ccode)) x)
    val runDetails = new HashSet[String]
    for (airport <- res){
      airport match {
        case air:Array[String ]=> { val tmp =runways.filter(x => x(2).equalsIgnoreCase(air(1)))
          tmp.foreach(x=> runDetails +=  x(5))}
        case _ => println("Empty")
      }
    }

   //runDetails.foreach(x => println(x))
    runDetails

  }

  def surfaceColumnPerCountry():Map[String,mutable.HashSet[String]] = {
    val cntries = countries.map(x => x(2))
    cntries.map(x => (x,findCountryAirportDetail(x))).toMap
  }

    def topLeIdent(): Map[String,Int]={
    val ledents = new ListBuffer[String]
    runways.foreach(x => if (x.length >=9) ledents += x(8))   // make sure that length is at least 8
    ledents.groupBy(x => x).map(y => (y._1,y._2.length)).toSeq.sortWith(_._2 > _._2).take(10).toMap

  }

}
