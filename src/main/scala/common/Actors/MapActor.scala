package common.Actors

import akka.actor.{Actor}
import akka.routing.{Broadcast, ConsistentHashingGroup}
import akka.routing.ConsistentHashingRouter.ConsistentHashMapping
import common._
import common.Plugins._

class MapActor[K1, V1, K2, V2](reduceActors: List[String], pluginType: Plugin) extends Actor {

  println("MapActor: ", self.path.toString)

  // Determine implementation type
  val PluginList = new PluginList[K1, V1, K2, V2]
  val pluginMap : PluginMap[K1, V1, K2, V2] = PluginList.GetMapPlugin(pluginType)

  Thread sleep 2000

  def hashMapping: ConsistentHashMapping = {
    case Reduce(key, value) => key
  }

  val reduceRouter = context.actorOf(ConsistentHashingGroup(reduceActors, hashMapping = hashMapping).props())

  def receive = {
    case Map(key, value) =>
        // Process input (K1, V1) -> List(K2, List(V2))
        pluginMap.process(key, value)
        // Send aggregated results (K2, List(V2)) to reducers
        pluginMap.send(reduceRouter)
    case Flush =>
        reduceRouter ! Broadcast(Flush)
  }
}

