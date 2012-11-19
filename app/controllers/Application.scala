package controllers

import play.api.libs.json.Format
import play.api.libs.json.JsBoolean
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.Controller
import dispatch._
import play.api.data._
import play.api.data.Forms._
import java.net.URLEncoder._
import play.api.Play._
import play.api.Routes
import util.Configuration

object Application extends Controller {


  val searchForm = Form("search" -> text)

  def index = Action {
    Ok(views.html.index("Welcome to Fibi.", searchForm))
  }

  case class Test(text: String, done: Boolean)

  implicit object TestFormat extends Format[Test] {
    def reads(json: JsValue): Test = Test(
      (json \ "text").as[String],
      (json \ "done").as[Boolean])

    def writes(t: Test): JsValue = JsObject(Seq(
      "text" -> JsString(t.text),
      "done" -> JsBoolean(t.done)))
  }

  def bodyAsText = Action {
    request =>
      val test = request.body.asJson.map(_.as[Test])
      Ok(Json.toJson(Seq(test, test)))
  }

  def bodyAsJson = Action(parse.json) {
    request =>
      val test = request.body.as[Test]
      val textAttributeInTest = (request.body \ "text")
      Ok(Json.toJson(Seq(test, test, test)))
  }

  def search = Action {
    implicit request =>
      searchForm.bindFromRequest.fold(
        errors => BadRequest(""),
        search => {
          val svc = url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?types=doctor&sensor=false")
            .addQueryParameter("location", "-37.7833,144.9667")
            .addQueryParameter("keyword", encode(search, "UTF-8"))
            .addQueryParameter("key", Configuration.googlePlacesKey)
            .addQueryParameter("radius", "500")
          val response = Http(svc OK as.String)()
          val results = Json.parse(response)
          Ok("")
        })
  }

  def testThis(input: String): String = input match {
    case "1" => "yes"
    case "0" => "no"
    case _ => "error"
  }

  def javascriptRoutes =  Action { implicit request =>
    import routes.javascript._
    Ok (
      Routes.javascriptRouter("jsRoutes")(
        SearchController.suggestion
      )
    ).as("text/javascript")
  }
}