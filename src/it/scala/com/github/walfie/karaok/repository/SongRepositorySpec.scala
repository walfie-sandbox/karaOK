package com.github.walfie.karaok.repository

import okhttp3.OkHttpClient
import org.scalatest._
import org.scalatest.concurrent._
import org.scalatest.time._
import scala.concurrent.ExecutionContext.Implicits.global

class SongRepositorySpec extends SongRepositorySpecHelpers
    with WordSpecLike with Matchers with ScalaFutures {

  override implicit val patienceConfig = PatienceConfig(
    timeout = Span(5, Seconds),
    interval = Span(50, Millis)
  )

  "findByTitle" should {
    "return matching songs" in {
      val response = repo.findByTitle("アイドル活動", 0, None).futureValue

      response.searchResult.map(_.songName) should contain allOf(
        "アイドル活動!", "アイドル活動!(Ver.Rock)"
      )
    }
  }
}

trait SongRepositorySpecHelpers {
  val repo = new SongRepositoryHttp(new OkHttpClient())
}

