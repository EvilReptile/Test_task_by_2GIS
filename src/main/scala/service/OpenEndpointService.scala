package service

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, Uri}
import akka.http.scaladsl.unmarshalling.Unmarshal
import const.Constant
import model.ResultDTO
import org.jsoup.Jsoup

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

class OpenEndpointService()
                         (implicit system: ActorSystem,
                          execContext: ExecutionContext) {
    
    def listUrlByTitle(urls: Seq[String]): Future[Seq[ResultDTO]] = {
        Future.sequence(
            urls.flatMap { url =>
                Try(getHtmlHeaderTag(url).map(ResultDTO(url, _))).toOption match {
                    case None =>
                        println(s"При обработке $url произошла ошибка")
                        None
                    case value => value
                }
            }
        )
    }
    
    private def getHtmlHeaderTag(url: String,
                                 tag: String = Constant.defaultSearchTag): Future[String] = {
        for {
            resp <- Http().singleRequest(
                HttpRequest(
                    method = HttpMethods.GET,
                    uri = Uri(url)
                )
            )
            html <- Unmarshal(resp).to[String]
            body = Jsoup.parse(html)
        } yield {
            body.head()
                .getElementsByTag(tag)
                .first()
                .html()
        }
        
    }
}
