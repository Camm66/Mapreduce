package client

import common._
import common.Plugins.Hyperlink_Functions._
import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object client extends App {
  val system = ActorSystem("MapReduce", ConfigFactory.load.getConfig("client"))

  println("\nInstructions: \n" +
    "1 - Word Count \n" +
    "2 - Proper Names \n" +
    "3 - Incoming Hyperlinks")
  val job = scala.io.StdIn.readLine()

  if(job == "1") {
    // Word count
    val master = system.actorOf(Props(classOf[MasterActor[String, String, String, Int]], WORD_COUNT), name = "master")
    master ! new Map("THE PICKWICK PAPERS", "http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg580.txt")
    master ! new Map("LIFE AND ADVENTURES OF MARTIN CHUZZLEWIT", "http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg968.txt")
    master ! new Map("GREAT EXPECTATIONS", "http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg1400.txt")
    master ! new Map("A CHILD'S HISTORY OF ENGLAND", "http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg699.txt")
    master ! Flush
  }
  else if(job == "2"){
    // Proper Names
    val master = system.actorOf(Props(classOf[MasterActor[String, String, String, String]], PROPER_NAMES), name = "master")
    master ! new Map("THE PICKWICK PAPERS", "http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg580.txt")
    master ! new Map("LIFE AND ADVENTURES OF MARTIN CHUZZLEWIT", "http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg968.txt")
    master ! new Map("GREAT EXPECTATIONS", "http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg1400.txt")
    master ! new Map("A CHILD'S HISTORY OF ENGLAND", "http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg699.txt")
    master ! Flush
  }
  else if(job == "3"){
    // Hyperlinks
    val master = system.actorOf(Props(classOf[MasterActor[String, String, String, Int]], HYPERLINK), name = "master")
    master ! new Map("http://www.example1.com", testExamples.example1)
    master ! new Map("http://www.example2.com", testExamples.example2)
    master ! new Map("http://www.example3.com", testExamples.example3)
    master ! new Map("http://www.example4.com", testExamples.example4)
    master ! new Map("http://www.example5.com", testExamples.example5)
    master ! new Map("http://www.example6.com", testExamples.example6)
    master ! new Map("http://www.example7.com", testExamples.example7)
    master ! new Map("http://www.example8.com", testExamples.example8)
    master ! Flush
  }
  else {
    println("Invalid input...")
    system.terminate
  }
}
