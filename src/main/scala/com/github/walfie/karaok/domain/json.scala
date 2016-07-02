package com.github.walfie.karaok.domain

import play.api.libs.json._

package object json {
  implicit val SongFormat = Json.format[Song]
}

