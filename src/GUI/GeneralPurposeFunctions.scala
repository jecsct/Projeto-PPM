package GUI

import GUI.FxApp.gallery
import javafx.scene.{Node, Parent}
import javafx.scene.control.{Button, TextField}

import java.io.{File, FileInputStream, PrintWriter}
import scala.annotation.tailrec
import scala.io.Source

object GeneralPurposeFunctions {

  /** Limpa a diretoria indicada pelo parametro 'dir' */
 def clearDirectory(dir: String): Unit = {
    val lista: List[String] = new File(dir).listFiles().map(_.getName()).toList.filter(x=>x.contains(".png"))

    @tailrec
    def aux(lst: List[String]): Unit = {
      lst match {
        case Nil => ;
        case x :: Nil => new File(dir+ "\\" + x).delete()
        case x :: xs => new File(dir+"\\" + x).delete(); aux(xs)
      }
    }
    aux(lista)
  }

  def readFile(path: String): String = {
    var line: String = ""
    val bufferedSource = Source.fromFile(path)
    println(bufferedSource)
    for (x <- bufferedSource.getLines) {
      line = x
    }
    bufferedSource.close()
    line
  }

  def galleryCheck(mainViewRoot:Parent):Unit={
    if(gallery.lst.isEmpty) {
      val slideshowNode: Node = mainViewRoot.getChildrenUnmodifiable.get(0).getScene.lookup("#slideshowBtn")
      val slideshowBtn: Button = slideshowNode.asInstanceOf[Button]
      disableButton(slideshowBtn)
      val gridNode: Node = mainViewRoot.getChildrenUnmodifiable.get(0).getScene.lookup("#gridBtn")
      val gridBtn: Button = gridNode.asInstanceOf[Button]
      disableButton(gridBtn)
    }
  }

  /** Le a imagem a partir da variavel galPath + imageName e guarda-a na GaleriaAux */
  def saveImage(imageName: String, galPath: String): Unit = {
    val srcPath: String = System.getProperty("user.dir") + "\\src\\"
    println(srcPath + "GaleriaAux\\" + imageName + ".png")
    println(galPath)
    ImageUtil.writeImage(ImageUtil.readColorImage(galPath), srcPath + "GaleriaAux\\" + imageName + ".png", "png")
  }

  def saveImage2(imageName: String, galPath: String): Unit = {
    val srcPath: String = System.getProperty("user.dir") + "\\src\\"
    println(srcPath + "GaleriaAux\\" + imageName + ".png")
    println(galPath)
    ImageUtil.writeImage(ImageUtil.readColorImage(galPath+"\\" + imageName + ".png"), srcPath + "GaleriaAux\\" + imageName + ".png", "png")
  }

  def disableButton(btn:Button):Unit={
    btn.setDisable(true)
    btn.setOpacity(0.2)
  }

  def enableButton(btn:Button):Unit={
    btn.setDisable(false)
    btn.setOpacity(1)
  }

  def write(content: String, path: String): Unit = {
    val pw: PrintWriter = new PrintWriter(new File(path))
    pw.write(content)
    pw.close()
  }

  def createJavafxImage(imagePath:String):javafx.scene.image.Image ={
    new javafx.scene.image.Image(new FileInputStream(imagePath))
  }

  def setPathTxtFieldToGaleriaAux(pathTxtFld:TextField, imageName:String):Unit={
    pathTxtFld.setText(System.getProperty("user.dir") + "\\src\\GaleriaAux\\" + imageName +".png")
  }
}
