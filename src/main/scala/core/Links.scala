package core

import java.util.UUID

import akka.actor.{Actor, Props}
import core.LinksActor.{LinksCountResponse, EmptyRequest, GetLinksCountInfo}

object LinksActor {

  case class GetLinksCountInfo(query: String)
  case class LinksCountResponse(data: String)
  case object EmptyRequest

}

class LinksActor extends Actor {

  def receive: Receive = {
    case GetLinksCountInfo(query) if query.isEmpty => sender ! Left(EmptyRequest)
    case GetLinksCountInfo(query)                  => sender ! Right(LinksCountResponse(query))
  }
}
