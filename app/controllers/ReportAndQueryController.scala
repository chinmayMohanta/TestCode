package controllers


import javax.inject._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future, Promise}
import play.api.libs.json.{JsValue, Json}




/**
  * @param exec We need an `ExecutionContext` to execute our
  * asynchronous code.
  */
@Singleton
class ReportAndQueryController @Inject() (das: services.dataAccess.DataAccessService)(implicit exec: ExecutionContext) extends Controller {

// List of Actions
// Detail by country code
  def airDetailbyCC(cc: String) = Action {
   val jsonArray = das.findAirportsByCountryCode(cc)
   Ok(Json.arr(jsonArray))
  }
//Detail by country name
  def airDetailbyCN(cn: String) = Action {
    val jsonArray = das.findAirportsByCountry(cn)
    Ok(Json.arr(jsonArray))
  }
// Country with highest number of Airports
  def highestNumAirport() = Action {

    val jsonArray = das.countryWithHighestNumberOfAirports()
    Ok(Json.arr(jsonArray))

  }
  // Country with lowest number of Airports
  def lowestNumAirport() = Action {

    val jsonArray = das.countryWithLowestNumberOfAirports()
    Ok(Json.arr(jsonArray))

  }
// Runway surfaces per country
  def surfacePerCountry() = Action {

    val jsonArray = das.surfaceColumnPerCountry()
    Ok(Json.arr(jsonArray))

  }
// Top leIdents
  def topLeIdent() = Action {

    val jsonArray = das.topLeIdent()
    Ok(Json.arr(jsonArray))

  }


}

