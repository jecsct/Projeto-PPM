package Package1
import Package1.QTree._

case class CommandLineOption(name : String, exec : QTree[Coords] => QTree[Coords])