package util

import play.api.Play._

/**
 * Created with IntelliJ IDEA.
 * User: AlexL
 * Date: 19/11/12
 * Time: 5:28 PM
 * To change this template use File | Settings | File Templates.
 */
object Configuration {

  val googlePlacesKey = current.configuration.getString("google.places.key").mkString

}
