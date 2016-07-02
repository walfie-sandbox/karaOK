package com.github.walfie.karaok.domain

import org.joda.time.DateTime

case class KaraokeModel(id: String, name: String)

object KaraokeModel {
  val PremierDAM = KaraokeModel("AB316238", "Premier DAM")
}

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

case class SearchResponse(
  searchResult: Seq[SearchResult],
  totalCount:   String,
  totalPage:    String
)

case class SearchResult(
  artistId:          String,
  artistName:        String,
  distEnd:           String,
  distStart:         String,
  firstBars:         String,
  funcAnimePicture:  String,
  funcPersonPicture: String,
  funcRecording:     String,
  funcScore:         String,
  indicationMonth:   String,
  myKey:             String,
  orgKey:            String,
  programTitle:      String,
  reqNo:             String,
  songName:          String,
  titleFirstKana:    String
)

case class Artist(id: String, name: String)

case class Song(
  id:        String,
  name:      String,
  artist:    Artist,
  firstBars: String
)

case class Page[T](
    items:        Seq[T],
    pageNumber:   Int,
    totalPages:   Int,
    totalResults: Int
) {
  def previousPage(): Option[Int] =
    if (pageNumber <= 1) None else Some(pageNumber - 1)

  def nextPage(): Option[Int] =
    if (pageNumber == totalPages) None else Some(pageNumber + 1)
}

