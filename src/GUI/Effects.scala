package GUI
import Package1.{IO_Utils, QEmpty, QLeaf, QNode, QTree, RandomWithState}
import Package1.QTree._

import java.awt.Color
import scala.util._

class Effects {

  def sizeX(qt:QTree[Coords]):Int = {
    qt match{
      case QEmpty => 0
      case QLeaf((((x1:Int, _), (x2:Int, _)), _)) => x2-x1
      case QNode(((x1:Int, _), (x2:Int, _)), _, _, _, _) => x2-x1
    }
  }

  def sizeY(qt:QTree[Coords]):Int = {
    qt match{
      case QEmpty => 0
      case QLeaf((((_, y1:Int), (_, y2:Int)), _)) => y2-y1
      case QNode(((_, y1:Int), (_, y2:Int)), _, _, _, _) => y2-y1
    }
  }

  def scale(s:Double)(qt: QTree[Coords]): QTree[Coords] = {
    qt match {
      case QEmpty => QEmpty
      case QLeaf ((((x1: Int, y1: Int), (x2: Int, y2: Int) ), color) ) =>
        QLeaf (((((s * x1).toInt, (s * y1).toInt), ((s * x2).toInt, (s * y2).toInt) ), color) )
      case QNode (((x1: Int, y1: Int), (x2: Int, y2: Int) ), l1, l2, l3, l4) =>
        QNode ((((s * x1).toInt, (s * y1).toInt), ((s * x2).toInt, (s * y2).toInt) ), scale (s) (l1), scale (s) (l2), scale (s) (l3), scale (s) (l4) )
    }
  }

  def auxScale()(qt:QTree[Coords]):QTree[Coords] = {
    val n: Option[Double] = IO_Utils.getUserInputDouble("Scale Factor")
    n match {
      case None => throw new Exception ("Invalid value")
      case Some(f) => scale(f)(qt)
    }
  }

  def mirrorV()(qt: QTree[Coords]): QTree[Coords] = {
    def aux(qt1:QTree[Coords], y:Int):QTree[Coords]= {
      qt1 match {
        case QEmpty => QEmpty
        case QLeaf((((x1:Int, y1:Int), (x2:Int, y2:Int)), color)) => QLeaf((((x1, y - y2), (x2, y - y1)), color))
        case QNode(((x1:Int, y1:Int), (x2:Int, y2:Int)), l1, l2, l3, l4) =>
          QNode(((x1, y - y2), (x2, y - y1)), aux(l2, y), aux(l1, y), aux(l4, y), aux(l3, y))
      }
    }
    aux(qt, sizeY(qt))
  }


  def mirrorH()(qt: QTree[Coords]):QTree[Coords]={
    def aux(qt1:QTree[Coords],x:Int):QTree[Coords]= {
      qt1 match {
        case QEmpty => QEmpty
        case QLeaf((((x1:Int, y1:Int), (x2:Int, y2:Int)), color)) => QLeaf((((x - x2, y1), (x - x1, y2)), color))
        case QNode(((x1:Int, y1:Int), (x2:Int, y2:Int)), l1, l2, l3, l4) =>
          QNode(((x - x2, y1), (x - x1, y2)), aux(l3,x), aux(l4,x), aux(l1,x), aux(l2,x))
      }
    }
    aux(qt, sizeX(qt))
  }

  def rotateL()(qt: QTree[Coords]): QTree[Coords] = { //rodar para a esquerda
    def aux(qt1: QTree[Coords], x: Int): QTree[Coords] = {
      qt1 match {
        case QEmpty => QEmpty
        case QLeaf((((x1: Int, y1: Int), (x2: Int, y2: Int)), color)) => QLeaf((((y1, x - x2), (y2, x - x1)), color))
        case QNode(((x1: Int, y1: Int), (x2: Int, y2: Int)), l1, l2, l3, l4) =>
          QNode(((y1, x-x2), (y2, x-x1)), aux(l2,x), aux(l4,x), aux(l1,x), aux(l3,x))
      }
    }
    aux(qt,sizeX(qt))
  }

  def rotateR()(qt: QTree[Coords]): QTree[Coords] = {
    def aux(qt1: QTree[Coords], y: Int): QTree[Coords] = {
      qt1 match {
        case QEmpty => QEmpty
        case QLeaf((((x1: Int, y1: Int), (x2: Int, y2: Int)), color)) => QLeaf((((y - y2, x1), (y - y1, x2)), color))
        case QNode(((x1: Int, y1: Int), (x2: Int, y2: Int)), l1, l2, l3, l4) =>
          QNode(((y - y2, x1), (y - y1, x2)), aux(l3,y), aux(l1,y), aux(l4,y), aux(l2,y))
      }
    }
    aux(qt,sizeY(qt))
  }


  def mapColourEffect(f: Color => Color)(qt: QTree[Coords]): QTree[Coords] = {
    qt match {
      case QEmpty => QEmpty
      case QLeaf( (coords, c: Color)) => QLeaf( coords, f(c))
      case QNode( coords, l1, l2, l3, l4) => QNode( coords, mapColourEffect(f)(l1), mapColourEffect(f)(l2),
        mapColourEffect(f)(l3), mapColourEffect(f)(l4))
    }
  }

  //Para o Noise puro
  def mapColourEffect_1(f: (Color,RandomWithState) => (Color,RandomWithState), r: RandomWithState)(qt: QTree[Coords]): (QTree[Coords],RandomWithState) = {
    qt match {
      case QEmpty => (QEmpty,r)
      case QLeaf((coords, c: Color)) => (QLeaf( coords, f(c,r)._1),f(c,r)._2)
      case QNode( coords, l1, l2, l3, l4) => (QNode(coords, mapColourEffect_1(f,r.nextInt._2)(l1)._1, mapColourEffect_1(f,r.nextInt._2)(l2)._1,
        mapColourEffect_1(f,r.nextInt._2)(l3)._1, mapColourEffect_1(f,r.nextInt._2)(l4)._1),r)
    }
  }

  def noise(colour: Color): Color = {
    val avg: Int = (colour.getRed + colour.getGreen + colour.getBlue) / 3
    if (Random.nextBoolean()) {
      new Color(avg, avg, avg)
    } else
      colour
  }

  //Noise Puro
  def noise_1(colour: Color, r:RandomWithState): (Color,RandomWithState) = {
    val avg: Int = (colour.getRed + colour.getGreen + colour.getBlue) / 3
    if ((r.nextInt._1 % 2)!=0) {
      (new Color(avg, avg, avg),r.nextInt._2)
    } else {
      (colour,r.nextInt._2)
    }
  }

  def sepia(colour: Color): Color = {
    new Color(betweenBorders((0.393 * colour.getRed + 0.769 * colour.getGreen + 0.189 * colour.getBlue).toInt),
      betweenBorders((0.349 * colour.getRed + 0.686 * colour.getGreen + 0.168 * colour.getBlue).toInt),
      betweenBorders((0.272 * colour.getRed + 0.534 * colour.getGreen + 0.131 * colour.getBlue).toInt))
  }

  def contrast(colour: Color): Color = {
    val luminance: Int = Math.round(colour.getRed * .21 + colour.getGreen * .71 + colour.getBlue * .08).toInt
    val contrast: Int = 50
    if (luminance < 128)
      new Color(betweenBorders(colour.getRed - contrast),
        betweenBorders(colour.getGreen - contrast), betweenBorders(colour.getBlue - contrast))
    else
      new Color(betweenBorders(colour.getRed + contrast),
        betweenBorders(colour.getGreen + contrast), betweenBorders(colour.getBlue + contrast))
  }

  def betweenBorders(value: Int): Int = {
    if (value > 255)
      255
    else if (value < 0) 0
    else value
  }

}
