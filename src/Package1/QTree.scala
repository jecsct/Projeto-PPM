package Package1
import java.awt.Color
import scala.annotation.tailrec

trait QTree[+A]

case class QNode[A](value: A, one: QTree[A], two: QTree[A], three: QTree[A], four: QTree[A]) extends QTree[A]
case class QLeaf[A, B](value: B) extends QTree[A]
case object QEmpty extends QTree[Nothing]
case class Example[A](myField: QTree[A])

object QTree {
  type Point = (Int, Int)
  type Coords = (Point, Point)
  type Section = (Coords, Color)

  def makeQTree(lista: BitMap): QTree[Coords] = {
    def aux(l: List[List[Int]], x: Int, y: Int, sizeX: Int, sizeY: Int): QTree[Coords] = {
      if (l.isEmpty) QEmpty
      else
        if (checkColors(l)) {
          val color = new Color(ImageUtil.decodeRgb(l.head.head).toList.head, ImageUtil.decodeRgb(l.head.head).toList(1), ImageUtil.decodeRgb(l.head.head).toList(2))
          QLeaf((((x, y), (x + sizeX, y + sizeY)), color))
        } else {
          if ((sizeX % 2 == 0) && (sizeY % 2 == 0)) { // x e y são par
            QNode(((x, y), (x + sizeX, y + sizeY)),
              aux(listPrep(l, 1), x, y, sizeX / 2, sizeY / 2),
              aux(listPrep(l, 2), x + sizeX / 2, y, sizeX / 2, sizeY / 2),
              aux(listPrep(l, 3), x, y + sizeY / 2, sizeX / 2, sizeY / 2),
              aux(listPrep(l, 4), x + sizeX / 2, y + sizeY / 2, sizeX / 2, sizeY / 2)
            )
          } else if ((sizeX % 2 != 0) && (sizeY % 2 == 0)) { // x é impar
            QNode(((x, y), (x + sizeX, y + sizeY)),
              aux(listPrep(l, 1), x, y, sizeX / 2, sizeY / 2),
              aux(listPrep(l, 2), x + sizeX / 2, y, sizeX / 2 + 1, sizeY / 2),
              aux(listPrep(l, 3), x, y + sizeY / 2, sizeX / 2, sizeY / 2),
              aux(listPrep(l, 4), x + sizeX / 2, y + sizeY / 2, sizeX / 2 + 1, sizeY / 2)
            )
          } else if ((sizeX % 2 == 0) && (sizeY % 2 != 0)) { // y é impar
            QNode(((x, y), (x + sizeX, y + sizeY)),
              aux(listPrep(l, 1), x, y, sizeX / 2, sizeY / 2),
              aux(listPrep(l, 2), x + sizeX / 2, y, sizeX / 2, sizeY / 2),
              aux(listPrep(l, 3), x, y + sizeY / 2, sizeX / 2, sizeY / 2 + 1),
              aux(listPrep(l, 4), x + sizeX / 2, y + sizeY / 2, sizeX / 2, sizeY / 2 + 1)
            )
          } else { // x e y são impar
            QNode(((x, y), (x + sizeX, y + sizeY)),
              aux(listPrep(l, 1), x, y, sizeX / 2, sizeY / 2),
              aux(listPrep(l, 2), x + sizeX / 2, y, sizeX / 2 + 1, sizeY / 2),
              aux(listPrep(l, 3), x, y + sizeY / 2, sizeX / 2, sizeY / 2 + 1),
              aux(listPrep(l, 4), x + sizeX / 2, y + sizeY / 2, sizeX / 2 + 1, sizeY / 2 + 1)
            )
          }
        }
    }
    aux(lista.lst, 0, 0, lista.lst.head.size, lista.lst.size)
  }

  def listPrep(lista: List[List[Int]], q: Int): List[List[Int]] = {
    def prepX(lista: List[List[Int]], quad: Int): List[List[Int]] = {
      lista match {
        case Nil => Nil
        case x :: xs => quad match {
          case 1 => x.splitAt(x.size / 2)._1 :: prepX(xs, quad)
          case 2 => x.splitAt(x.size / 2)._2 :: prepX(xs, quad)
        }
      }
    }
    q match {
      case 1 => prepX(lista.splitAt(lista.size / 2)._1, 1)
      case 2 => prepX(lista.splitAt(lista.size / 2)._1, 2)
      case 3 => prepX(lista.splitAt(lista.size / 2)._2, 1)
      case 4 => prepX(lista.splitAt(lista.size / 2)._2, 2)
    }
  }

  @tailrec
  def checkColors(lista: List[List[Int]]): Boolean = {
    val firstColor: Int = lista.head.head
    def aux(list: List[Int]): Boolean = {
      if((list filter (x => !x.equals(firstColor))).nonEmpty)
        false
      else
        true
    }
    lista match {
      case Nil => true
      case x :: Nil => aux(x)
      case x :: xs => if (aux(x)) checkColors(xs) else false
    }
  }

}
