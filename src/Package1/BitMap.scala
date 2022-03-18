package Package1
import Package1.QTree._
import java.awt.Color
import scala.annotation.tailrec

case class BitMap(lst:List[List[Int]]) {
  def makeBitMap(qt:QTree[Coords]):BitMap=BitMap.makeBitMap(qt)
  def createArray():Array[Array[Int]]=BitMap.createArray(this)
  def create(imagePath:String):BitMap=BitMap.create(imagePath)
}

object BitMap {

  def convertToList(lst:List[Array[Int]]):List[List[Int]] = {
    lst match {
      case Nil => Nil
      case x::xs => x.toList::convertToList(xs)
    }
  }

  def create(imagePath: String):BitMap = {
    new BitMap( convertToList(ImageUtil.readColorImage(imagePath).toList))
  }

  def convertToArray(lst:List[List[Int]]):List[Array[Int]] = {
    lst match {
      case Nil => Nil
      case x::xs => x.toArray::convertToArray(xs)
    }
  }

  def createArray(bitMap: BitMap):Array[Array[Int]] = {
    convertToArray(bitMap.lst).toArray
  }

  def makeBitMap(qt:QTree[Coords]):BitMap = {
    def aux(qt: QTree[Coords]): List[List[Int]] = {
      qt match {
        case QEmpty => Nil
        case QLeaf((((x1: Int, y1: Int), (x2: Int, y2: Int)), color: Color)) => makeList(x2 - x1, y2 - y1, color)
        case QNode(((_, _), (_, _)), l1, l2, l3, l4) =>joinListsY(joinListsX(aux(l1),aux(l2)),joinListsX(aux(l3),aux(l4)))
      }
    }
    new BitMap(aux(qt))
  }

  def joinListsX(l1:List[List[Int]], l2:List[List[Int]]):List[List[Int]] = {
    (l1,l2) match {
      case (Nil,Nil) => Nil
      case(Nil,y) => y
      case(x,Nil) => x
      case(x::xs,y::ys) => concat(x,y)::joinListsX(xs,ys)
    }
  }

  def joinListsY(l1:List[List[Int]], l2:List[List[Int]]):List[List[Int]] = (l1 foldRight l2)((x,xs)=>x::xs)

  def concat(lst1:List[Int], lst2:List[Int]):List[Int] = (lst1  foldRight lst2) (_::_)

  def makeList(sizeX:Int, sizeY:Int, color:Color):List[List[Int]] = {
    @tailrec
    def aux(sizeX:Int, color:Color,lst:List[Int]):List[Int] = {
      if(sizeX != 0)
        aux(sizeX-1,color,color.getRGB::lst)
      else
        lst
    }
    if(sizeY != 0)
      aux(sizeX,color,List())::makeList(sizeX,sizeY-1,color)
    else
      Nil
  }

}
