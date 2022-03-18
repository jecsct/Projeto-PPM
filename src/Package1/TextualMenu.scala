package Package1
import GUI.Effects
import Package1.BitMap._
import Package1.QTree._

import scala.annotation.tailrec
import scala.collection.SortedMap

object TextualMenu extends App{

  val imagePath:String=IO_Utils.prompt("Image Path")
  val t:Effects=new Effects()
  var qt:QTree[Coords] = makeQTree(create(imagePath))

  val options = SortedMap[Int, CommandLineOption](
    1 -> CommandLineOption("Rotate Right", t.rotateR()),
    2 -> CommandLineOption("Rotate Left", t.rotateL()),
    3 -> CommandLineOption("Horizontal Mirror", t.mirrorH()),
    4 -> CommandLineOption("Vertical Mirror", t.mirrorV()),
    5 -> CommandLineOption("Scale", t.auxScale()),
    6 -> CommandLineOption("Sepia", t.mapColourEffect(c => t.sepia(c))),
    7 -> CommandLineOption("Contrast", t.mapColourEffect(c => t.contrast(c))),
    8 -> CommandLineOption("Sepia", t.mapColourEffect(c => t.noise(c))),
    0 -> CommandLineOption("Exit", _ => sys.exit())
  )

  mainLoop(qt)

  @tailrec
  def mainLoop(qt:QTree[Coords]) {
    ImageUtil.writeImage(createArray(makeBitMap(qt)),imagePath,"png")
    IO_Utils.optionPrompt(options) match {
      case Some(opt) => val newQT = opt.exec(qt)
        mainLoop(newQT)
      case _ => println("Invalid option")
        mainLoop(qt)
    }
  }

}
