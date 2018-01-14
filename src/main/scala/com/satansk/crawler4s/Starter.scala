package com.satansk.crawler4s

import akka.actor.{ActorRef, ActorSystem}

/**
  * Author:  satansk
  * Email:   satansk@hotmail.com
  * Date:    18/1/14
  */
object Starter extends App {

  /**
    * 创建 ActorSystem，它是 Actor 运行的容器
    */
  val system: ActorSystem = ActorSystem("crawler")

  // 创建需要运行的 Actor 实例
  val crawler: ActorRef = system.actorOf(CrawlerActor.props("https://akka.io"), "Crawler")

  // 发送消息给 Actor 实例
  crawler ! ""

}
