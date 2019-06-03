package server

import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem

object server2 extends App {
  val system = ActorSystem("MapReduce", ConfigFactory.load.getConfig("server2"))
  println("Server ready")
}
