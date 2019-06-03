package common.Hyperlink_Functions

import akka.actor.Actor
import akka.routing.ConsistentHashingRouter.ConsistentHashMapping
import akka.routing.{Broadcast, ConsistentHashingGroup}
import scala.collection.mutable.HashSet
import scala.io.Source


class MapActor(reduceActors: List[String]) extends Actor {

  println("MapActor: ", self.path.toString)

  Thread sleep 5000

  def hashMapping: ConsistentHashMapping = {
    case Word(word, title) => word
  }

  val reduceRouter = context.actorOf(ConsistentHashingGroup(reduceActors, hashMapping = hashMapping).props())


  def receive = {
    case Text(title, url) =>
      processText(title, url)
    case Flush =>
        reduceRouter ! Broadcast(Flush)
  }

  def processText(title: String, url: String) : Any = {
    val content = getContent(url)
    if(content != "") {
      val text = content.asInstanceOf[Iterator[String]]
      val regex = "href=[\\'\"]?([^\\'\" >]+)".r
      for (line <- text) {
        for (link <- regex.findAllIn(line)) {
              reduceRouter ! Word(url, link.size)
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
