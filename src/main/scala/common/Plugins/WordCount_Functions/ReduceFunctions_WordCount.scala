package common.Plugins.WordCount_Functions

import common._
import scala.collection.mutable.HashMap

class ReduceFunctions_WordCount[K2, V2] extends PluginReduce[K2, V2] {

  var reduceMap = new HashMap[String, Int]()

  def reduce[K2, V2](key: K2, value: List[V2]) {
    if (key.isInstanceOf[String] && value.isInstanceOf[List[Int]]) {
      var word = key.asInstanceOf[String]
      var list = value.asInstanceOf[List[Int]]
      var count = 0
      for(item <- list)
        count += item
      if (reduceMap.contains(word)) {
        reduceMap += (word -> (reduceMap(word) + count))
      }
      else
        reduceMap += (word -> count)
    }
    else {
      println("Invalid Key/Value types at Map Actor")
    }
  }

  def getResults(): Object ={
    reduceMap
  }

}