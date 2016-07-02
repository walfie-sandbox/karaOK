package com.github.walfie.karaok.util

import java.io.IOException
import okhttp3._
import scala.concurrent.{ExecutionContext, Future, Promise}
import play.api.libs.json._

package object http {
  val JsonMediaType = MediaType.parse("application/json; charset=utf-8");

  implicit class OkHttpClientOps(val http: OkHttpClient) extends AnyVal {
    def async(request: Request): Future[Response] = {
      val promise = Promise[Response]

      http.newCall(request).enqueue(new Callback() {
        override def onResponse(call: Call, response: Response): Unit = {
          if (response.isSuccessful)
            promise.success(response)
          else // TODO: better error
            promise.failure(new IOException("Unexpected response " + response))
        }

        override def onFailure(call: Call, e: IOException): Unit = promise.failure(e)
      })

      promise.future
    }

    def asyncJson[T: Reads](request: Request)(implicit ec: ExecutionContext): Future[T] = {
      async(request).flatMap { response =>
        Json.parse(response.body.string).validate[T] match {
          case JsSuccess(value, _) => Future.successful(value)
          case JsError(errors) => Future.failed(JsResultException(errors))
        }
      }
    }
  }
}

