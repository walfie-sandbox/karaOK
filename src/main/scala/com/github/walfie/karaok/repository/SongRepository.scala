package com.github.walfie.karaok.repository

import com.github.walfie.karaok.domain._
import com.github.walfie.karaok.domain.json._
import com.github.walfie.karaok.util.http._
import okhttp3._
import play.api.libs.json._
import scala.concurrent.{ExecutionContext, Future}

trait SongRepository {
  def findByTitle(
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

  def findByTitle(
    query:    String,
    page:     Int,
    serialNo: Option[String]
  ): Future[SearchResponse] = {
    val json = Json.obj(
      "categoryCd" -> "020000",
      "page" -> page.toString,
      "songMatchType" -> "0",
      "songName" -> query,
      "deviceId" -> "",
      "serialNo" -> serialNo
    )

    val body = RequestBody.create(JsonMediaType, Json.stringify(json))

    val request = new Request.Builder().url(baseUrl).post(body).build()

    http.asyncJson[SearchResponse](request)
  }
}

