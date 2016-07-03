package com.github.walfie.karaok.dal

import com.github.walfie.karaok.domain.{Artist, KaraokeModel}
import okhttp3.OkHttpClient
import org.joda.time.DateTime
import org.scalatest._
import org.scalatest.concurrent._
import org.scalatest.time._
import scala.concurrent.ExecutionContext.Implicits.global

class KaraokeCatalogDaoSpec extends KaraokeCatalogDaoSpecHelpers
    with WordSpecLike with Matchers with ScalaFutures {

  override implicit val patienceConfig = PatienceConfig(
    timeout = Span(5, Seconds),
    interval = Span(50, Millis)
  )

  "findSongsByName" should {
    "return matching songs" in {
      val response = repo.findSongsByName("アイドル活動").futureValue

      response.items.map(_.name) should contain allOf (
        "アイドル活動!", "アイドル活動!(Ver.Rock)"
      )
    }

    "filter by karaoke machine model" in {
      // This song is available on the top tier machines
      val response1 = repo.findSongsByName("wake up my music").futureValue
      exactly(1, response1.items) should have(
        'name("Wake up my music"),
        'artist(Artist("96028", "りさ、えいみ")),
        'dateAdded(new DateTime(2013, 11, 2, 0, 0))
      )

      // PremierDAM doesn't have this song...
      val response2 = repo.findSongsByName(
        query = "wake up my music",
        serialNo = Some(KaraokeModel.PremierDAM.id)
      ).futureValue
      response2.items shouldBe empty
    }
  }

  "findSongsByArtist" should {
    "return matching songs" in {
      val response = repo.findSongsByArtist("107891").futureValue

      exactly(1, response.items) should have(
        'id("372915"),
        'name("Passion flower"),
        'artist(Artist("107891", "みほ・もな from AIKATSU☆STARS!"))
      )
    }
  }

  "findArtistsByName" should {
    "return matching artists" in {
      val response = repo.findArtistsByName("aikatsu").futureValue

      response.items.map(_.name) should contain("AIKATSU☆STARS!")
    }
  }
}

trait KaraokeCatalogDaoSpecHelpers {
  val repo = new OkHttpKaraokeCatalogDao(new OkHttpClient())
}

