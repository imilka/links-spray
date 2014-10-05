package api

import akka.actor.ActorRef
import akka.util.Timeout
import core.LinksActor.{GetLinksCountInfo, LinksCountResponse, EmptyRequest}
import spray.http._
import spray.routing.Directives

import akka.pattern.ask
import scala.concurrent.duration._

import scala.concurrent.ExecutionContext

class LinksService(links: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives with DefaultJsonFormats {

  implicit val timeout = Timeout(2.seconds)

  implicit val getLinksCountRequestFormat = jsonFormat1(GetLinksCountInfo)
  implicit val linksCountResponseFormat = jsonFormat1(LinksCountResponse)
  implicit val emptyRequestFormat = jsonObjectFormat[EmptyRequest.type]

  implicit object EitherErrorSelector extends ErrorSelector[EmptyRequest.type] {
    def apply(v: EmptyRequest.type): StatusCode = StatusCodes.BadRequest
  }

  val route =
    path("links" / "count") {
      get {
//        handleWith { gl: GetLinksCountInfo => (links ? gl).mapTo[Either[EmptyRequest.type, LinksCountResponse]] }
        parameter('query) { (query) =>
          complete((links ? GetLinksCountInfo(query)).mapTo[Either[EmptyRequest.type, LinksCountResponse]])
        }
      }
    }
}
