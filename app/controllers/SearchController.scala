package controllers

import play.api.mvc.Action
import dispatch._
import java.net.URLEncoder._
import play.api.libs.json.Json
import play.api.Play._
import play.api.mvc.Controller
import util.Configuration

object SearchController extends Controller {

  def suggestion(keyword: String) = Action {
    request =>
      val svc = url("https://maps.googleapis.com/maps/api/place/autocomplete/json?types=establishment&sensor=false")
        .addQueryParameter("location", "-37.7833,144.9667")
        .addQueryParameter("input", encode(request.queryString("keyword").mkString, "UTF-8"))
        .addQueryParameter("key", Configuration.googlePlacesKey)
        .addQueryParameter("radius", "500")
      val response = Http(svc OK as.String)()
      val suggestions = Json.parse(response) \ "predictions" \\ "description"
      Ok(Json.toJson(Map("options" -> suggestions)))
  }
}