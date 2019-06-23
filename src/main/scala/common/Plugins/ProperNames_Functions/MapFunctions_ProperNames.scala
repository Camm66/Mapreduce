package common.Plugins.ProperNames_Functions

import common._
import akka.actor.{ActorRef}
import scala.collection.mutable.HashMap
import scala.io.Source

class MapFunctions_ProperNames[K1, V1, K2, V2] extends PluginMap[K1, V1, K2, V2] {

  val STOP_WORDS_LIST = List("a", "am", "an", "and", "are", "as", "at", "be", "was", "nor", "my", "here", "by", "y",
    "do", "go", "if", "in", "is", "it", "of", "on", "the", "then", "from", "our", "to", "i", "for", "them", "ah", "god",
    "but", "yet", "no", "so", "when", "not", "help", "which", "o", "had", "tell", "again", "permit", "one", "your",
    "how", "dear", "send", "what", "leaving", "before", "oh", "heaven", "ask", "two", "after", "said", "sun", "great",
    "why", "accordingly", "new", "still", "until", "s", "although", "old", "now", "thought", "between", "perhaps")

  var aggregatedMap = new HashMap[Any, List[Any]]()

  def process[K1, V1](key: K1, value: V1) : Unit = {
    if(key.isInstanceOf[String] && value.isInstanceOf[String]) {
      val title = key.asInstanceOf[String]
      val url = value.asInstanceOf[String]
      val content = getContent(url)
      if (content != Nil) {
        val text = content.asInstanceOf[Iterator[String]]
        val regex = "([A-Z][a-z]*)[\\s-]([A-Z][a-z]*)".r
        for (line <- text) {
          for (word <- regex.findAllIn(line)) {
            val content = word.toLowerCase.split("[\\p{Punct}\\s]+")
            if (!STOP_WORDS_LIST.contains(content(0)) && !STOP_WORDS_LIST.contains(content(1)) && word.length() > 5) {
                aggregate(word, title)
              }
            }
          }
        }
      }
    else {
      println("Invalid Key/Value types at Map Actor")
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

  def aggregate[K2, V2](key: K2, value: V2): Unit = {
    aggregatedMap += (key -> List(value))

  }

  def send(reduceRouter: ActorRef) = {
    for (item <- aggregatedMap) {
      reduceRouter ! Reduce(item._1, item._2)
    }
  }
}
