package com.github.walfie.karaok.util

import play.api.libs.functional.syntax._
import play.api.libs.json._
import scala.util.Try

package object json {
  implicit class JsPathOps(val path: JsPath) extends AnyVal {
    /** If a value isn't present in reading JSON, set it to `default`. */
    def formatWithDefault[T: Format](default: T): OFormat[T] =
      OFormat(
        path.read[T].orElse(Reads.pure(default)),
        path.write[T]
      )

    /**
      * Read a `JsString` as an `Int` (with fallback value on failure),
      * and write an `Int` as a `JsString`
      */
    def formatStringAsInt(onFailure: => Int): OFormat[Int] =
      path.format[String].inmap(
        (s: String) => Try(s.toInt).getOrElse(onFailure),
        (i: Int) => i.toString
      )
  }
}

