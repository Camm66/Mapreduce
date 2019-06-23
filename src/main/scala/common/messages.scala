package common

case class Map[K1, V1](key: K1, value: V1)
case class Reduce[K2, V2](key: K2, value: List[V2])
case class Result(value: Any)
case object Flush
case object Done