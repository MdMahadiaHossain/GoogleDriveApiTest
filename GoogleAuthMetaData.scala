package services.google

import java.io.{BufferedReader, DataOutputStream, InputStreamReader}
import java.net.URL

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import javax.net.ssl.HttpsURLConnection
import play.api.http.HeaderNames

object GoogleAuthMetaData  {

  val CLIENT_ID = "??????.apps.googleusercontent.com"
  val CLIENT_SECRET = "???????"
  val httpTransport = new NetHttpTransport
  val jsonFactory = new JacksonFactory
  var CODE=""
  var mutableAccessToken : String = ""

  def getAccessAndIdToken(code: String) : List[(String, String)] = {
    val googleAuthUrl: String = "https://accounts.google.com/o/oauth2/token"
    // we are preparing POST method to contact with the google OAuth2 for getting access token with in the request/response body
    val url = new URL(googleAuthUrl)
    val connection: HttpsURLConnection = url.openConnection().asInstanceOf[HttpsURLConnection]
    connection.setRequestMethod("POST")
    connection.setRequestProperty("User-Agent", HeaderNames.USER_AGENT)
    connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5")
    val urlParameters = s"code=$code&client_id=??????.apps.googleusercontent.com&client_secret=????????&redirect_uri=http://localhost:9000/getCode&grant_type=authorization_code&Content-Type=application/x-www-form-urlencoded"
    connection.setDoOutput(true)
    val write = new DataOutputStream(connection.getOutputStream)
    write.writeBytes(urlParameters)
    write.flush()
    write.close()
    val bufferedReader: BufferedReader = new BufferedReader(
      new InputStreamReader(
        connection.getInputStream
      )
    )
    val response: StringBuffer = new StringBuffer
    while (bufferedReader.readLine() != null){
      response.append(bufferedReader.readLine())
    }

    bufferedReader.close()

    // storing access token and id_token from the POST request response from the https://accounts.google.com/o/oauth2/token redirection
    val responseContent: List[(String, String)] = response.toString.split(",").toList.map{
      string =>
        val list = string.split(":")
        list(0) -> list(1)
    }

    responseContent
  }


  def getNewAccessToken(refreshToken: String):String = {
    val credentialBuilder = new GoogleCredential.Builder()
      .setTransport(httpTransport).setJsonFactory(jsonFactory)
      .setClientSecrets(CLIENT_ID, CLIENT_SECRET)

    val credential = credentialBuilder.build()
    credential.setRefreshToken(refreshToken)
    credential.refreshToken()
    credential.getAccessToken
  }
}
