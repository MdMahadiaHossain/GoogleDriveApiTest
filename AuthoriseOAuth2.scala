package controllers

import java.util

import com.google.api.client.googleapis.auth.oauth2.GoogleBrowserClientRequestUrl
import com.google.api.services.drive.model.File
import javax.inject.{Inject, Singleton}
import play.api.mvc._
import services.google._

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class AuthoriseOAuth2 @Inject()(cc: ControllerComponents)(implicit assetsFinder: AssetsFinder, executionContext: ExecutionContext) extends AbstractController(cc) {

  // 1st we redirect to the google with api "client id", "from-google-to-our-app-redirection path" and scope to be accessed
  def googleAuthorise: Action[AnyContent] = Action.apply {
    val toBeRedirected = "http://localhost:9000/getCode"
    val urlForRedirect = new GoogleBrowserClientRequestUrl("??????.apps.googleusercontent.com",
      toBeRedirected, util.Arrays.asList(
        "https://www.googleapis.com/auth/plus.login",
        "https://www.googleapis.com/auth/drive"
      )).set("access_type", "offline").set("response_type", "code").build()

    Redirect(urlForRedirect)
  }

  // 2nd google redirect to the "from-google-to-our-app-redirection path" with "code" value as query string
  def getCode: Action[AnyContent] = Action {
    request: Request[AnyContent] =>
      println("-------------------------> get code called")
      GoogleAuthMetaData.CODE = request.queryString("code").toList.head
      println(GoogleAuthMetaData.CODE)
      Redirect(routes.AuthoriseOAuth2.generateAccessToken())
  }

  // by the use of code value came as header we will get the accessToken
  def generateAccessToken: Action[AnyContent] = Action.async{
    println("-------------------------> get generateAccessToken called")
    Future{
      val responseContent: List[(String, String)] = GoogleAuthMetaData.getAccessAndIdToken(GoogleAuthMetaData.CODE)

      GoogleAuthMetaData.mutableAccessToken = responseContent(0)._2

      Redirect(routes.AuthoriseOAuth2.gotoDriveView())
    }
  }


  def gotoDriveView() = Action{
    val listOfFiles: util.List[File] = GoogleDrive.getDrive(GoogleAuthMetaData.mutableAccessToken).files().list().execute().getItems
    Ok(views.html.driveView(listOfFiles))
  }




}

