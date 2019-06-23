package common

import akka.actor.{ActorRef}

abstract class PluginMap[K1, V1, K2, V2]() {
  def process[K1, V1](key: K1, value: V1)
  def aggregate[K2, V2](key: K2, value: V2)
  def send(reduceRouter: ActorRef)
}
