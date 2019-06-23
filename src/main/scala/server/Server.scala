package server

import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import common._

object Server extends App {
  val system = ActorSystem("MapReduce", ConfigFactory.load.getConfig("server"))
  println("Server ready")
}
