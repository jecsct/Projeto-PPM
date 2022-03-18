
package GUI
import Package1.{QEmpty, QTree}
import Package1.QTree._

import scala.annotation.tailrec

case class Image(qt:QTree[Coords], info:String)

case class Gallery(lst:List[Image]) {
  def addImage(img:Image): Gallery =Gallery.addImage(img)(this)
  def removeImage(info:String):Gallery=Gallery.removeImage(info)(this)
  def findImage(info:String):Image=Gallery.findImage(info)(this)
  def reverse():Gallery = Gallery.reverse()(this)
  def updateInfo(curName:String,newName:String):Gallery=Gallery.updateInfo(curName,newName)(this)
  def switchOrder(s1:String,s2:String): Gallery = Gallery.switchOrder(s1,s2)(this)
}

object Image{}

object Gallery{

  def addImage(img:Image)(g:Gallery):Gallery={
    new Gallery(g.lst:+img)
  }

  def removeImage(info:String)(g:Gallery):Gallery = {
    def aux(l:List[Image]):List[Image] = {
      l match {
        case Nil => Nil
        case x::xs => if(x.info.equals(info)) aux(xs) else x::aux(xs)
      }
    }
    new Gallery(aux(g.lst))
  }

  def findImage(info:String)(g:Gallery):Image = {
    @tailrec
    def aux(l:List[Image]):Image = {
      l match {
        case Nil => new Image(QEmpty,"No Image Found")
        case x::xs => if(x.info.equals(info)) x else aux(xs)
      }
    }
    aux(g.lst)
  }

  def reverse()(g:Gallery):Gallery = {
    new Gallery(g.lst.reverse)
  }

  def updateInfo(curName:String, newName:String)(g:Gallery):Gallery = {
    def aux(l:List[Image]):List[Image] = {
      l match{
        case Nil => Nil
        case x::xs => if(x.info==curName) new Image(x.qt,newName)::xs else x::aux(xs)
      }
    }
    new Gallery(aux(g.lst))
  }

  def switchOrder(s1:String,s2:String)(g:Gallery):Gallery = {
    def aux(l:List[Image]):List[Image] = {
      l match {
        case Nil => Nil
        case x :: xs => if (x.info.equals(s1)) findImage(s2)(g) :: aux(xs) else
          if (x.info.equals(s2)) findImage(s1)(g) :: aux(xs) else x::aux(xs)
      }
    }
    new Gallery(aux(g.lst))
  }

}
