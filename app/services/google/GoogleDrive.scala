package services.google

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.services.drive.Drive


object GoogleDrive{
  /**
    * Set Up Google App Credentials
    */
  def getDrive(accessToken: String): Drive = {

    //Build the Google credentials and make the Drive ready to interact
    val credential = new GoogleCredential.Builder()
      .setJsonFactory(GoogleAuthMetaData.jsonFactory)
      .setTransport(GoogleAuthMetaData.httpTransport)
      .setClientSecrets(GoogleAuthMetaData.CLIENT_ID, GoogleAuthMetaData.CLIENT_SECRET)
      .build()
    credential.setAccessToken(accessToken)
    //Create a new authorized API client
    new Drive.Builder(GoogleAuthMetaData.httpTransport, GoogleAuthMetaData.jsonFactory, credential).build()
  }

}