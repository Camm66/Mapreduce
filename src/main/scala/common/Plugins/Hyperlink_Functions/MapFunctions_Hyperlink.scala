package common.Plugins.Hyperlink_Functions

import akka.actor.ActorRef
import common._
import scala.collection.mutable.HashMap


class MapFunctions_Hyperlink[K1, V1, K2, V2] extends PluginMap[K1, V1, K2, V2] {

  var aggregatedMap = new HashMap[Any, List[Any]]()

  def process[K1, V1](key: K1, value: V1): Unit = {
    if (key.isInstanceOf[String] && value.isInstanceOf[String]) {
      val content = value.asInstanceOf[String]
      if (content != "") {
        val regex = "href=[\\'\"]?([^\\'\" >]+)".r
        for (link <- regex.findAllIn(content)) {
          aggregate(key, 1)
        }
      }
    }
    else {
      println("Invalid Key/Value types at Map Actor")
    }
  }

  def aggregate[K2, V2](key: K2, value: V2): Unit = {
    if (!aggregatedMap.contains(key)) {
      aggregatedMap += (key -> List(value))
    }
    else{
      aggregatedMap += (key -> (aggregatedMap(key) :+ value))
    }
  }

  def send(reduceRouter: ActorRef) = {
    for (item <- aggregatedMap) {
      reduceRouter ! Reduce(item._1, item._2)
    }
  }
}
