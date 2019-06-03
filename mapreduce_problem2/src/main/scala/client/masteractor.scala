package client

import common._
import akka.actor.{Actor, AddressFromURIString, Deploy, Props}
import akka.remote.RemoteScope
import akka.remote.routing.RemoteRouterConfig
import akka.routing.{Broadcast, ConsistentHashingPool, RoundRobinPool}
import akka.routing.ConsistentHashingRouter.ConsistentHashMapping
import com.typesafe.config.ConfigFactory

class MasterActor extends Actor {

  val numberMappers  = ConfigFactory.load.getInt("number-mappers")
  val numberReducers  = ConfigFactory.load.getInt("number-reducers")
  var pending = numberReducers

  val addresses = Seq(
    AddressFromURIString("akka.tcp://MapReduce@127.0.0.1:2552"),
    AddressFromURIString("akka.tcp://MapReduce@127.0.0.1:2553"))

  var reduceActors = List[String]()
  for (i <- 0 until numberReducers)
    reduceActors = context.actorOf(Props[ReduceActor].withDeploy(Deploy(scope = RemoteScope(addresses(i % addresses.length))))).path.toString::reduceActors

  val mapRemote = context.system.actorOf(
    RemoteRouterConfig(RoundRobinPool(4), addresses).props(Props(classOf[MapActor], reduceActors)))

  def receive = {
    case Text(url) =>
      mapRemote ! Text(url)
    case Text2(title, url) =>
      mapRemote ! Text2(title, url)
    case Text2(link, url) =>
      mapRemote ! Text3(link, url)
    case Flush =>
      mapRemote ! Broadcast(Flush)
    case Result(source, content) =>
      println(source + " : " + content)
      println("*****************************************************************************")
    case Done =>
      println("Received Done from" + sender)
      pending -= 1
      if (pending == 0)
        context.system.terminate
  }
}