package GUI

import Package1.{BitMap, QTree}
import FxApp.{gallery, saved}
import GUI.GeneralPurposeFunctions.{clearDirectory, galleryCheck, readFile, saveImage, saveImage2}
import javafx.application.{Application, Platform}
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.{Modality, Stage, WindowEvent}

import java.io.File
import scala.annotation.tailrec

/** NAO CORRER A CLASS GUI RUNNER. CORRER O MAIN NO OBJECT FXAPP */
class GUIRunner extends Application {

  override def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("Projeto PPM")
    val fxmlLoader = new FXMLLoader(getClass.getResource("index.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    val scene = new Scene(mainViewRoot)
    scene.getStylesheets.add("./GUI/index.css")
    primaryStage.setScene(scene)
    primaryStage.show()
    primaryStage.setOnCloseRequest(event)
    galleryCheck(mainViewRoot)
  }

  @Override
  override def stop(): Unit = {
    clearDirectory(System.getProperty("user.dir") + "\\src\\GaleriaAux")
  }

  val event: javafx.event.EventHandler[javafx.stage.WindowEvent] =
    new EventHandler[WindowEvent]() {
      override def handle(event: WindowEvent): Unit = {
        if (!saved) {
          event.consume()
          val saveMenu: Stage = new Stage()
          saveMenu.setTitle("save")
          saveMenu.initModality(Modality.APPLICATION_MODAL)
          val fxmlLoader = new FXMLLoader(getClass.getResource("SaveReminder.fxml"))
          val mainViewRoot: Parent = fxmlLoader.load()
          val scene = new Scene(mainViewRoot)
          scene.getStylesheets.add("GUI/index.css")
          saveMenu.setScene(scene)
          saveMenu.show()
        }
        clearDirectory(System.getProperty("user.dir") + "\\src\\GaleriaAux")
      }
    }
}

object FxApp {

  var gallery: Gallery = new Gallery(List())
  var saved: Boolean = true
  var galleryPath: String = getGalleryPath()

  def main(args: Array[String]): Unit = {
    Application.launch(classOf[GUIRunner], args: _*)
  }

  private def getGalleryPath(): String = {
    val path: String = System.getProperty("user.dir") + "\\src\\GUI\\GalleryPath.txt"
    if (new File(path).exists()) {

      val line: String = readFile(path)
      println(line)
      println("line: " + line)
      if (line != "") {
        val list_1: List[String] = new File(line).listFiles().map(_.getName()).toList.filter(x=>x.contains(".png"))
        val list_2: List[String] = new File(line).listFiles().map(_.getName()).toList.filter(x=>x.contains(".jpg"))
        val list:List[String]= list_1.concat(list_2)
        println("lista: " + list)
        fillGallery(list, line)
        println("acabei line != null")
        line
      } else {
        println("fiz nada")
        ""
      }
    } else {
      new File(path).createNewFile()
      ""
    }
  }

  @tailrec
  private def fillGallery(lst: List[String], line: String): Unit = {
    lst match {
      case Nil => ;
      case x :: Nil => val i = new Image(QTree.makeQTree(BitMap.create(line + "\\" + x)), x.split(".png")(0))
        gallery = gallery.addImage(i)
        val name: String = i.info
        saveImage2(name, line)
      case x :: xs => val i = new Image(QTree.makeQTree(BitMap.create(line + "\\" + x)), x.split(".png")(0))
        gallery = gallery.addImage(i)
        val name: String = i.info
        println("name: "+name)
        saveImage2(name, line)
        fillGallery(xs, line)
    }
  }



}
