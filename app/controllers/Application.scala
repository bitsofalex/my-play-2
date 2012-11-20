package controllers

import play.api.libs.json._
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.Routes
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import play.api.libs.json.JsBoolean

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Welcome to Fibi."))
  }

  def javascriptRoutes = Action {
    implicit request =>
      import routes.javascript._
      Ok(
        Routes.javascriptRouter("jsRoutes")(
          GooglePlacesController.suggestion
        )
      ).as("text/javascript")
  }
}