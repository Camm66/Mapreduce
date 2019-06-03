package common.Hyperlink_Functions

import akka.actor.Actor
import com.typesafe.config.ConfigFactory

import scala.collection.mutable.HashMap

class ReduceActor extends Actor {

  println("ReduceActor: ", self.path.toString)

  Thread sleep 5000

  var remainingMappers = ConfigFactory.load.getInt("number-mappers")
  var reduceMap = HashMap[String, List[String]]()
    
  def receive = {
    case Word(word, title) =>
      if (reduceMap.contains(word)) {
        if (!reduceMap(word).contains(title))
          reduceMap += (word -> (title :: reduceMap(word)))
      }
      else
        reduceMap += (word -> List(title))
    case Flush =>
      remainingMappers -= 1
      if (remainingMappers == 0) {
        context.actorSelection("..") ! Result(self.path.toStringWithoutAddress, reduceMap)
        context.actorSelection("..") ! Done
      }
  }
}
