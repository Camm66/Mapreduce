package common

import scala.collection.mutable.HashMap

case class Word(word: String, title: String)

case class Text(url: String)
case class Text2(title: String, url: String)
case class Text3(link: String, url: String)

case class Result(source: String, content: Object)
case object Flush
case object Done
