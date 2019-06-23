package common.Plugins.ProperNames_Functions

import common.{PluginReduce, Result}

import scala.collection.mutable.HashMap

class ReduceFunctions_ProperNames[K2, V2] extends PluginReduce[K2, V2] {

  var reduceMap = new HashMap[String, List[String]]()

  def reduce[K2, V2](key: K2, value: List[V2]) {
    if (key.isInstanceOf[String] && value.isInstanceOf[List[String]]) {
      val word = key.asInstanceOf[String]
      val titles = value.asInstanceOf[List[String]]
      if (reduceMap.contains(word)) {
        for (title <- titles) {
          if (!reduceMap(word).contains(title))
            reduceMap += (word -> (title :: reduceMap(word)))
        }
      }
      else
        reduceMap += (word -> titles)
    }
    else {
      println("Invalid Key/Value types at Reduce Actor")
    }
  }

  def getResults(): Object ={
    reduceMap
  }
}

