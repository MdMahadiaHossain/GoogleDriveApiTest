package services.dbService.inMemoryService


object InMemory {
  private var refreshTokenList: List[String] = List()
  private var codeList : List[String] = List()
  private var accessTokenList: List[String] = List()

  def addRefreshToken(refreshToken: String): Unit = {
    refreshTokenList = refreshTokenList.+:(refreshToken)
  }

  def addCode(code : String) : Unit ={
    codeList = codeList.+:(code)
  }

  def addAccessToken(accessToken: String): Unit = {
    accessTokenList = accessTokenList.+:(accessToken)
  }

  def getRefreshToken: String = {
    refreshTokenList head
  }

  def getCode : String ={
    codeList head
  }

  def getAccessToken: String = {
    accessTokenList head
  }


}
