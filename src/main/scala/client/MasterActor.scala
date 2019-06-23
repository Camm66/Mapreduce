package client

import common._
import common.Actors._
import akka.actor.{Actor, AddressFromURIString, Deploy, Props}
import akka.remote.RemoteScope
import akka.remote.routing.RemoteRouterConfig
import akka.routing.{Broadcast, RoundRobinPool}
import com.typesafe.config.ConfigFactory
import scala.collection.mutable.HashMap


class MasterActor[K1, V1, K2, V2](pluginType: Plugin) extends Actor {

  val numberMappers  = ConfigFactory.load.getInt("number-mappers")
  val numberReducers  = ConfigFactory.load.getInt("number-reducers")
  var pending = numberReducers

  val addresses = Seq(
    AddressFromURIString("akka.tcp://MapReduce@127.0.0.1:2552"),
    AddressFromURIString("akka.tcp://MapReduce@127.0.0.1:2553"))

  var reduceActors = List[String]()
  for (i <- 0 until numberReducers)
    reduceActors = context.actorOf(Props(classOf[ReduceActor[K2, List[V2]]], pluginType).withDeploy(
      Deploy(scope = RemoteScope(addresses(i % addresses.length))))).path.toString::reduceActors

  val mapRemote = context.system.actorOf(
    RemoteRouterConfig(RoundRobinPool(4), addresses).props(Props(classOf[MapActor[K1, V1, K2, V2]], reduceActors, pluginType)))

  def receive = {
    case Map(key, value) =>
      mapRemote ! Map(key, value)
    case Flush =>
      mapRemote ! Broadcast(Flush)
    case Result(content) =>
      println(sender + "\n" + content)
      println("*****************************************************************************\n")
    case Done =>
      println("Received Done from" + sender)
      pending -= 1
      if (pending == 0)
        context.system.terminate
  }
}