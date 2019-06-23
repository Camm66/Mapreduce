package common.Plugins.WordCount_Functions

import akka.actor.ActorRef
import common._

import scala.collection.mutable.HashMap
import scala.io.Source

class MapFunctions_WordCount[K1, V1, K2, V2] extends PluginMap [K1, V1, K2, V2]{

  val STOP_WORDS_LIST = List("a", "am", "an", "and", "are", "as", "at", "be",
    "do", "go", "if", "in", "is", "it", "of", "on", "the", "to", "  ")

  var aggregatedMap = new HashMap[Any, List[Any]]()

  def process[K1, V1](key: K1, value: V1) = {
    if(value.isInstanceOf[String]) {
      val url = value.asInstanceOf[String]
      val content = getContent(url)
      if (content != Nil) {
        val text = content.asInstanceOf[Iterator[String]]
        for (line <- text) {
          for (word <- line.toLowerCase.split("[\\p{Punct}\\s]+"))
            if ((!STOP_WORDS_LIST.contains(word))) {
              aggregate(word, 1)
            }
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
    else {
      aggregatedMap += (key -> (aggregatedMap(key) :+ value))
    }
  }

  def send(reduceRouter: ActorRef) = {
    for (item <- aggregatedMap) {
      reduceRouter ! Reduce(item._1, item._2)
    }
  }

  def getContent( url: String ): Object = {
    try {
      Source.fromURL(url).getLines
    } catch {
      case e: Exception => {
        println("No content found: " + url)
        Nil
      }
    }
  }
}