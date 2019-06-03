package client

import common._
import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object client extends App {
  val system = ActorSystem("MapReduce", ConfigFactory.load.getConfig("client"))
  val master = system.actorOf(Props[MasterActor], name = "master")

  println("Instructions: \n" +
    "1 - Word Count \n" +
    "2 - Proper Names \n" +
    "3 - Incoming Hyperlinks")
  val job = scala.io.StdIn.readLine()

  if(job == "1") {
    master ! new Text("http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg580.txt")
    master ! new Text("http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg968.txt")
    master ! new Text("http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg1400.txt")
    master ! new Text("http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg699.txt")
    master ! Flush
  }
  else if(job == "2"){
    master ! new Text2("THE PICKWICK PAPERS", "http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg580.txt")
    master ! new Text2("LIFE AND ADVENTURES OF MARTIN CHUZZLEWIT", "http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg968.txt")
    master ! new Text2("GREAT EXPECTATIONS", "http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg1400.txt")
    master ! new Text2("A CHILD'S HISTORY OF ENGLAND", "http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg699.txt")
    master ! Flush
  }
  else if(job == "3"){
    master ! new Text3("", "")
    master ! new Text3("", "")
    master ! new Text3("", "")
    master ! new Text3("", "")
    master ! Flush
  }
  else
    println("Invalid input...")
}
