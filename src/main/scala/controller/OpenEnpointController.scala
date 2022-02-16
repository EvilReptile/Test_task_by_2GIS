package controller

import akka.http.scaladsl.model.StatusCodes.{InternalServerError, OK}
import akka.http.scaladsl.server.{Directives, Route}
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport._
import service.OpenEndpointService

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class OpenEnpointController(openEndpointService: OpenEndpointService)
                           (implicit ec: ExecutionContext)
    extends Directives {
    
    private val endpoint = path("getTitlesByUrl") {
        (post & entity(as[Seq[String]])) { urls =>
            onComplete(
                openEndpointService
                    .listUrlByTitle(urls)
            ) {
                case Success(value) => complete(OK, value)
                case Failure(_) => complete(InternalServerError, "Ошибка при обработке списка ссылок")
            }
        }
    }
    
    val routes: Route = endpoint
}
