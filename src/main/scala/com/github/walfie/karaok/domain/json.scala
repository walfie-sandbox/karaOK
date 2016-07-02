package com.github.walfie.karaok.domain

import com.github.walfie.karaok.util.json.JsPathOps
import play.api.libs.functional.syntax._
import play.api.libs.json._
import scala.language.implicitConversions

package object json {
  implicit def SearchResponseFormat: Format[SearchResponse] =
    Json.format[SearchResponse]

  implicit val SearchResultFormat: Format[SearchResult] =
    Json.format[SearchResult]

  implicit val ArtistFormat: Format[Artist] = (
    (__ \ 'artistId).format[String] ~
    (__ \ 'artistName).format[String]
  )(Artist.apply, unlift(Artist.unapply))

  implicit val SongFormat: Format[Song] = (
    (__ \ 'reqNo).format[String] ~
    (__ \ 'songName).format[String] ~
    __.format[Artist] ~
    (__ \ 'firstBars).format[String]
  )(Song.apply, unlift(Song.unapply))

  def pageFormat[T: Format](pageNumber: Int): Format[Page[T]] = (
    (__ \ 'searchResult).format[Seq[T]] ~
    (__ \ 'page).formatWithDefault[Int](pageNumber) ~
    (__ \ 'totalCount).formatStringAsInt(0) ~
    (__ \ 'totalPage).formatStringAsInt(1)
  )(Page.apply, unlift(Page.unapply))
}

