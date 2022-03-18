package Package1

import scala.annotation.tailrec
import scala.collection.SortedMap
import scala.util.{Failure, Success, Try}
import scala.io.StdIn.readLine

object IO_Utils {

  def getUserInputInt(msg : String): Try[Int] = {
    print(msg + ": ")
    Try(readLine.trim.toUpperCase.toInt)
  }

  def getUserInputDouble(msg : String): Option[Double] = {
    print(msg + ": ")
    Try(readLine.toDouble) match {
      case Success(i) => Some(i)
      case Failure(_) => println("Invalid number!"); None;
    }
  }

  def prompt(msg : String):String = {
    print(msg + ": ")
    scala.io.StdIn.readLine()
  }

  @tailrec
  def optionPrompt(options : SortedMap[Int, CommandLineOption]): Option[CommandLineOption] =
  {
    println("-- Options --")
    options.toList map((option:(Int, CommandLineOption)) => println(option._1 + ") " + option._2.name))
    getUserInputInt("Select an option") match {
      case Success(i) => options.get(i)
      case Failure(_) => println("Invalid number!");optionPrompt(options)
    }
  }

}
