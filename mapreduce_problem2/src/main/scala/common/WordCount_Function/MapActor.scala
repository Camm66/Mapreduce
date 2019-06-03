package common.WordCount_Function

import akka.actor.{Actor, ActorRef}
import akka.routing.ConsistentHashingGroup
import akka.routing.ConsistentHashingRouter.ConsistentHashMapping
import common.WordCount_Function.messages

class MapActor(reduceActors: List[String]) extends Actor {
  println("MapActor: ", self.path.toString)

  Thread sleep 5000

  def hashMapping: ConsistentHashMapping = {
    case Word(word, count) => word
  }
  val STOP_WORDS_LIST = List("a", "am", "an", "and", "are", "as", "at", "be",
    "do", "go", "if", "in", "is", "it", "of", "on", "the", "to")

  val numReducers = reduceActors.size

  val reduceRouter = context.actorOf(ConsistentHashingGroup(reduceActors, hashMapping = hashMapping).props())

  def receive = {
    case msg: String =>
      process(msg)
    case Flush =>
      for (i <- 0 until numReducers) {
        reduceActors(i) ! Flush
      }
  }

  def process(content: String) = {
    for (word <- content.toLowerCase.split("[\\p{Punct}\\s]+"))
      if ((!STOP_WORDS_LIST.contains(word))) {
        var index = Math.abs((word.hashCode())%numReducers)
        reduceActors(index) ! Word(word, 1)
      }
  }
}