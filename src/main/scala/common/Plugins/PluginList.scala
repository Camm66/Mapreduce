package common

import common.Plugins.Hyperlink_Functions._
import common.Plugins.ProperNames_Functions._
import common.Plugins.WordCount_Functions._

sealed trait Plugin
case object WORD_COUNT extends Plugin
case object PROPER_NAMES extends Plugin
case object HYPERLINK extends Plugin

class PluginList[K1,V1,K2,V2] {
    def GetMapPlugin(plugin: Plugin) : PluginMap[K1, V1, K2, V2] = {
      plugin match {
        case WORD_COUNT =>
           new MapFunctions_WordCount[K1, V1, K2, V2]
        case PROPER_NAMES =>
           new MapFunctions_ProperNames[K1, V1, K2, V2]
        case HYPERLINK =>
           new MapFunctions_Hyperlink[K1, V1, K2, V2]
      }
    }

    def GetReducePlugin(plugin: Plugin) : PluginReduce[K2, V2] = {
      plugin match {
        case WORD_COUNT =>
          return new ReduceFunctions_WordCount[K2, V2]
        case PROPER_NAMES =>
          return new ReduceFunctions_ProperNames[K2, V2]
        case HYPERLINK =>
          return new ReduceFunctions_Hyperlink[K2, V2]
      }
    }
}
