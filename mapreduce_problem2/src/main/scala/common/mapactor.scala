package common

import scala.collection.mutable.HashSet
import akka.actor.{Actor, ActorRef}
import com.typesafe.config.ConfigFactory
import akka.routing.{Broadcast, ConsistentHashingGroup}
import akka.routing.ConsistentHashingRouter.ConsistentHashMapping

import scala.io.Source

class MapActor(reduceActors: List[String]) extends Actor {

  println("MapActor: ", self.path.toString)

  Thread sleep 5000

  def hashMapping: ConsistentHashMapping = {
    case Word(word, title) => word
  }

  val STOP_WORDS_LIST = List("a", "am", "an", "and", "are", "as", "at", "be", "was", "nor", "my", "here", "by", "y",
    "do", "go", "if", "in", "is", "it", "of", "on", "the", "then", "from", "our", "to", "i", "for", "them", "ah", "god",
    "but", "yet", "no", "so", "when", "not", "help", "which", "o", "had", "tell", "again", "permit", "one", "your",
    "how", "dear", "send", "what", "leaving", "before", "oh", "heaven", "ask", "two", "after", "said", "sun", "great",
    "why", "accordingly", "new", "still", "until", "s", "although", "old", "now", "thought", "between", "perhaps")

  val reduceRouter = context.actorOf(ConsistentHashingGroup(reduceActors, hashMapping = hashMapping).props())


  def receive = {
    case Text2(title, url) =>
      processText(title, url)
    case Flush =>
        reduceRouter ! Broadcast(Flush)
  }

  def processText(title: String, url: String) : Any = {
    val content = getContent(url)
    var namesFound = HashSet[String]()
    if(content != "") {
      val text = content.asInstanceOf[Iterator[String]]
      val regex = "([A-Z][a-z]*)[\\s-]([A-Z][a-z]*)".r
      for (line <- text) {
        for (word <- regex.findAllIn(line)) {
          val content = word.toLowerCase.split("[\\p{Punct}\\s]+")
          if (!STOP_WORDS_LIST.contains(content(0)) && !STOP_WORDS_LIST.contains(content(1)) && word.length() > 5) {
            if (!namesFound.contains(word)) {
              reduceRouter ! Word(word, title)
              namesFound.add(word)
            }
          }
        }
      }
    }
  }

  def getContent( url: String ): Object = {
    try {
      Source.fromURL(url).getLines
    } catch {
      case e: Exception => ""
    }
  }

}
