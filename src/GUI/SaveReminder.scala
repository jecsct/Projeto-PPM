package GUI

import GUI.FxApp.gallery
import Package1.BitMap
import Package1.BitMap.createArray
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.stage.{DirectoryChooser, Stage}

import java.io.{File, PrintWriter}
import scala.annotation.tailrec

class SaveReminder {

  @FXML
  var saveChangesBtn: Button = _
  @FXML
  var dontSaveChangesBtn: Button = _

  def saveChanges(): Unit = {
    /** USAR IF(FILE.ISDIRECTORY) */
    val directoryChooser = new DirectoryChooser
    if (FxApp.galleryPath != "")
      directoryChooser.setInitialDirectory(new File(FxApp.galleryPath))
    val selectedDirectory = directoryChooser.showDialog(new Stage())
    if (selectedDirectory != null) {
      if (FxApp.galleryPath == selectedDirectory.toString) {
        clearDirectory(FxApp.galleryPath)
      }
      write(selectedDirectory.toString, System.getProperty("user.dir") + "\\src\\GUI\\GalleryPath.txt")
      FxApp.galleryPath = selectedDirectory.toString
      @tailrec
      def saveImages(lst: List[GUI.Image]): Unit = {
        lst match {
          case Nil => ;
          case x :: xs => {
            val bm = BitMap.makeBitMap(x.qt)
            val a = createArray(bm)
            val fileStr: String = selectedDirectory.toString + "\\" + x.info + ".png"
            ImageUtil.writeImage(a, fileStr, "png")
            saveImages(xs)
          }
        }
      }

      saveImages(gallery.lst)
      println("Images saved successfully")
      FxApp.saved = true
      clearDirectory(System.getProperty("user.dir") + "\\src\\GaleriaAux")
    }
    Platform.exit()
  }

  private def write(content: String, path: String): Unit = {
    val pw: PrintWriter = new PrintWriter(new File(path))
    pw.write(content)
    pw.close()
  }

  private def clearDirectory(dir: String): Unit = {
    val lista: List[String] = new File(dir).listFiles().map(_.getName()).toList.filter(x=>x.contains(".png"))

    @tailrec
    def aux(lst: List[String]): Unit = {
      lst match {
        case Nil =>
        case x :: Nil => new File(dir+ "\\" + x).delete()
        case x :: xs =>new File(dir + x).delete(); aux(xs)
      }
    }

    aux(lista)
  }


  def dontSaveChanges(): Unit = {
    clearDirectory(System.getProperty("user.dir") + "\\src\\GaleriaAux")
    val stage = dontSaveChangesBtn.getScene.getWindow.asInstanceOf[Stage]
    stage.close()
    Platform.exit()
  }
}
