package common


abstract class PluginReduce[K2, V2] () {
  def reduce[K2, V2](key: K2, value: List[V2])
  def getResults : Object
}
