package controllers

import play.api.libs.json._
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import play.api.libs.json.JsBoolean
import play.api.libs.json.JsSuccess
import play.api.mvc.{Controller, Action}

/**
 * Created with IntelliJ IDEA.
 * User: AlexL
 * Date: 20/11/12
 * Time: 1:53 PM
 * To change this template use File | Settings | File Templates.
 */
object ScalaJsonController extends Controller {

  def show = Action {
    Ok(views.html.angularjs("AngularJS Example"))
  }

  case class Test(text: String, done: Boolean)

  implicit object TestFormat extends Format[Test] {
    def reads(json: JsValue): JsResult[Test] = JsSuccess(Test(
      (json \ "text").as[String],
      (json \ "done").as[Boolean]))

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
}
