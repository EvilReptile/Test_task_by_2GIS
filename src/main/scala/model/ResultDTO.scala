package model

import play.api.libs.json.{Format, Json}

case class ResultDTO(url: String,
                     title: String)

object ResultDTO {
    implicit val format: Format[ResultDTO] = Json.format[ResultDTO]
}
