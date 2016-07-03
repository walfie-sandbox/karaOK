package com.github.walfie.karaok.domain

import com.github.walfie.karaok.util.json.JsPathOps
import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._

/* Sample JSON response:
   ```
   {
     "searchResult": [
     {
       "artistId": "91801",
       "artistName": "わか、ふうり、すなお from STAR☆ANIS",
       "distEnd": "99999999",
       "distStart": "20130330",
       "firstBars": "",
       "funcAnimePicture": "",
       "funcPersonPicture": "",
       "funcRecording": "",
       "funcScore": "",
       "indicationMonth": "",
       "myKey": "0",
       "orgKey": "0",
       "programTitle": "",
       "reqNo": "360715",
       "songName": "アイドル活動!",
       "titleFirstKana": ""
     }
     ],
     "totalCount": "2",
     "totalPage": "1"
   }
   ```
*/

package object json {
  /**
    * API dates are `yyyyMMdd`, though end dates (which we don't care about)
    * are often "99999999". For songs, start date always seems to be valid,
    * but adding a default just in case.
    */
  private implicit val DateFormat: Format[DateTime] = Format(
    Reads.jodaDateReads("yyyyMMdd").orElse(Reads.pure(new DateTime(0))),
    Writes.jodaDateWrites("yyyyMMdd")
  )

  implicit val ArtistFormat: Format[Artist] = (
    (__ \ 'artistId).format[String] ~
    (__ \ 'artistName).format[String]
  )(Artist.apply, unlift(Artist.unapply))

  implicit val SongFormat: Format[Song] = (
    (__ \ 'reqNo).format[String] ~
    (__ \ 'songName).format[String] ~
    __.format[Artist] ~
    (__ \ 'firstBars).format[String] ~
    (__ \ 'distStart).format[DateTime]
  )(Song.apply, unlift(Song.unapply))

  def pageFormat[T: Format](pageNumber: Int): Format[Page[T]] = (
    (__ \ 'searchResult).format[Seq[T]] ~
    (__ \ 'page).formatWithDefault[Int](pageNumber) ~
    (__ \ 'totalCount).formatStringAsInt(0) ~
    (__ \ 'totalPage).formatStringAsInt(1)
  )(Page.apply, unlift(Page.unapply))
}

