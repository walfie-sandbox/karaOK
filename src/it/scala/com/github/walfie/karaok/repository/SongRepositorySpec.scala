package com.github.walfie.karaok.repository

import com.github.walfie.karaok.domain.KaraokeModel
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

  "findBySongName" should {
    "return matching songs" in {
      val response = repo.findBySongName("アイドル活動").futureValue

      response.searchResult.map(_.songName) should contain allOf(
        "アイドル活動!", "アイドル活動!(Ver.Rock)"
      )
    }

    "filter by karaoke machine model" in {
      // This song is available on the top tier machines
      val response1 = repo.findBySongName("wake up my music").futureValue
      exactly(1, response1.searchResult) should have(
        'songName ("Wake up my music"),
        'artistName ("りさ、えいみ")
      )

      // PremierDAM doesn't have this song...
      val response2 = repo.findBySongName(
        query = "wake up my music",
        serialNo = Some(KaraokeModel.PremierDAM.id)
      ).futureValue
      response2.searchResult shouldBe empty
    }
  }
}

trait SongRepositorySpecHelpers {
  val repo = new SongRepositoryHttp(new OkHttpClient())
}

