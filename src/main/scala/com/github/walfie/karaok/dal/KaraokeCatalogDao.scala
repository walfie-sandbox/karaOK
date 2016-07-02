package com.github.walfie.karaok.dal

import com.github.walfie.karaok.domain._
import com.github.walfie.karaok.domain.json._
import com.github.walfie.karaok.util.http._
import okhttp3._
import play.api.libs.json._
import scala.concurrent.{ExecutionContext, Future}

trait KaraokeCatalogDao {
  import KaraokeCatalogDao.MatchType

  def findSongsByName(
    query:     String,
    matchType: MatchType,
    page:      Int,
    serialNo:  Option[String]
  ): Future[Page[Song]]
}

object KaraokeCatalogDao {
  sealed abstract class MatchType(val value: String)
  object MatchType {
    case object StartsWith extends MatchType("0")
    case object Partial extends MatchType("1")
  }
}

class OkHttpKaraokeCatalogDao(
    http: OkHttpClient
)(implicit ec: ExecutionContext) extends KaraokeCatalogDao {
  import KaraokeCatalogDao.MatchType

  // TODO: Don't hardcode this in here
  val baseUrl = "https://denmoku.clubdam.com/dkdenmoku/DkDamSearchServlet"

  def findSongsByName(
    query:     String,
    matchType: MatchType      = MatchType.StartsWith,
    page:      Int            = 1,
    serialNo:  Option[String] = None
  ): Future[Page[Song]] = {
    // TODO: Figure out what some of these params mean
    val json = Json.obj(
      "appVer" -> "2.1.0",
      "categoryCd" -> "020000",
      "deviceId" -> "",
      "page" -> page.toString,
      "serialNo" -> serialNo,
      "songMatchType" -> matchType.value,
      "songName" -> query
    )

    val body = RequestBody.create(JsonMediaType, Json.stringify(json))

    val request = new Request.Builder().url(baseUrl).post(body).build()

    implicit val format: Format[Page[Song]] = pageFormat(page)
    http.asyncJson[Page[Song]](request)
  }
}

