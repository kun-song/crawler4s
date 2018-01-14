package com.satansk.crawler4s

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.ByteString

/**
  * Author:  satansk
  * Email:   satansk@hotmail.com
  * Date:    18/1/14
  */
class CrawlerActor(destUri: String) extends Actor with ActorLogging {

  import akka.pattern.pipe
  import context.dispatcher

  final implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(context.system))

  val http = Http(context.system)

  override def preStart: Unit =
    http.singleRequest(HttpRequest(uri = destUri)).pipeTo(self)

  override def receive: Receive = {
    case HttpResponse(StatusCodes.OK, _, entity, _) ⇒
      entity.dataBytes.runFold(ByteString(""))(_ ++ _).foreach {
        body ⇒ log.info(s"Response body: ${body.utf8String}")
      }
    case resp @ HttpResponse(code, _, _, _) ⇒
      log.info(s"Request failed, response code: $code")
      resp.discardEntityBytes()
  }

}

object CrawlerActor {
  def props(destUri: String): Props = Props(new CrawlerActor(destUri))
}