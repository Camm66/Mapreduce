package common.Actors

import akka.actor.Actor
import com.typesafe.config.ConfigFactory
import common.Plugins._
import common._

class ReduceActor[K2, V2](pluginType: Plugin) extends Actor {

  println("ReduceActor: ", self.path.toString)

  // Determine implementation type
  val PluginList = new PluginList[K2, V2, K2, V2]
  val pluginReduce : PluginReduce[K2, V2] = PluginList.GetReducePlugin(pluginType)

  Thread sleep 2000

  var remainingMappers = ConfigFactory.load.getInt("number-mappers")

  def receive = {
    case Reduce(key, value) =>
      // Reduce input (K2, List(V2)) -> (K2, V2)
      pluginReduce.reduce(key, value)
    case Flush =>
      remainingMappers -= 1
      if (remainingMappers == 0) {
        context.actorSelection("..") ! Result(pluginReduce.getResults)
        context.actorSelection("..") ! Done
        context.system.terminate
      }
  }
}
