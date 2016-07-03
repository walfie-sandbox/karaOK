package com.github.walfie.karaok.domain

import org.joda.time.DateTime

case class KaraokeModel(id: String, name: String)

object KaraokeModel {
  val PremierDAM = KaraokeModel("AB316238", "Premier DAM")
}

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

