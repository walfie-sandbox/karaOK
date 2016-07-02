package com.github.walfie.karaok.repository

import com.github.walfie.karaok.domain._
import com.github.walfie.karaok.domain.json._
import com.github.walfie.karaok.util.http._
import okhttp3._
import play.api.libs.json._
import scala.concurrent.{ExecutionContext, Future}

trait SongRepository {
  def findBySongName(
    query:    String,
    page:     Int,
    serialNo: Option[String]
  ): Future[SearchResponse]
}

class SongRepositoryHttp(
    http: OkHttpClient
)(implicit ec: ExecutionContext) extends SongRepository {
  // TODO: Don't hardcode this in here
  val baseUrl = "https://denmoku.clubdam.com/dkdenmoku/DkDamSearchServlet"

  def findBySongName(
    query:    String,
    page:     Int            = 1,
    serialNo: Option[String] = None
  ): Future[SearchResponse] = {
    // TODO: Figure out what some of these params mean
    val json = Json.obj(
      "appVer" -> "2.1.0",
      "categoryCd" -> "020000",
      "deviceId" -> "",
      "page" -> page.toString,
      "serialNo" -> serialNo,
      "songMatchType" -> "0",
      "songName" -> query
    )

    val body = RequestBody.create(JsonMediaType, Json.stringify(json))

    val request = new Request.Builder().url(baseUrl).post(body).build()

    http.asyncJson[SearchResponse](request)
  }
}

