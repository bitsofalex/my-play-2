package controllers

import util.Configuration
import dispatch._
import play.api.libs.json._
import java.net.URLEncoder._
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._

/**
 * Created with IntelliJ IDEA.
 * User: AlexL
 * Date: 20/11/12
 * Time: 1:26 PM
 * To change this template use File | Settings | File Templates.
 */
object GooglePlacesController extends Controller {

  val searchForm = Form("search" -> text)

  def show = Action {
    Ok(views.html.search("Search", searchForm))
  }

  case class ServiceProvider(id: String, name: String, reference: String, vicinity: String)

  implicit object ServiceProviderFormat extends Format[ServiceProvider] {
    def reads(json: JsValue): JsResult[ServiceProvider] = JsSuccess(ServiceProvider(
      (json \ "id").as[String],
      (json \ "name").as[String],
      (json \ "reference").as[String],
      (json \ "vicinity").as[String]))

    def writes(s: ServiceProvider): JsValue = JsObject(Seq(
      "id" -> JsString(s.id),
      "name" -> JsString(s.name),
      "reference" -> JsString(s.reference),
      "vicinity" -> JsString(s.vicinity)))
  }

  def search = Action {
    implicit request =>
      searchForm.bindFromRequest.fold(
        errors => BadRequest(""),
        search => {
          val svc = url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?sensor=false")
            .addQueryParameter("location", "-37.8210,145.1250")
            .addQueryParameter("keyword", encode(sanitize(search), "UTF-8"))
            .addQueryParameter("key", Configuration.googlePlacesKey)
            .addQueryParameter("radius", "5000")
          val response = Http(svc OK as.String)()
          val results = (Json.parse(response) \ "results").asOpt[List[ServiceProvider]]
          Ok(Json.toJson(results))
        })
  }

  def suggestion(keyword: String) = Action {
    request =>
      val svc = url("https://maps.googleapis.com/maps/api/place/autocomplete/json?types=establishment&sensor=false")
        .addQueryParameter("location", "-37.8210,145.1250")
        .addQueryParameter("input", encode(request.queryString("keyword").mkString, "UTF-8"))
        .addQueryParameter("key", Configuration.googlePlacesKey)
        .addQueryParameter("radius", "500")
      val response = Http(svc OK as.String)()
      val suggestions = Json.parse(response) \ "predictions" \\ "description"
      Ok(Json.toJson(Map("options" -> suggestions)))
  }

  def sanitize(keyword: String) = keyword.map(_.toLower match {
    case c if (('a' <= c && c <= 'z') || ('0' <= c && c <= '9') || (c == '"')) => c
    case _ => ' '
  }).mkString.trim
}
