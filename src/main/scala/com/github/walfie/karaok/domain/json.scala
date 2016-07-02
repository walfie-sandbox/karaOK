package com.github.walfie.karaok.domain

import play.api.libs.json._
import scala.language.implicitConversions

package object json {
  implicit def SearchResponseFormat: Format[SearchResponse] =
    Json.format[SearchResponse]

  implicit val SongFormat: Format[SearchResult] =
    Json.format[SearchResult]
}

