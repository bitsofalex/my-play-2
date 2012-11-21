package controllers

import play.api.mvc.{Action, Controller}
import dispatch._
import java.net.URLEncoder._
import util.Configuration
import play.api.libs.json.Json

/**
 * Created with IntelliJ IDEA.
 * User: AlexL
 * Date: 21/11/12
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */
object GeolocationController extends Controller {

  def show = Action {
    Ok(views.html.geolocation("Welcome"))
  }

  def lookupCityWithCoordinates = Action {
    request =>
      val svc = url("http://maps.googleapis.com/maps/api/geocode/json?sensor=false")
        .addQueryParameter("latlng", request.queryString("latlng").mkString)
      val response = Http(svc OK as.String)()
      Ok(Json.toJson(Json.parse(response)))
  }
}
