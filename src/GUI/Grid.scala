package GUI

import GUI.FxApp.gallery
import GUI.GeneralPurposeFunctions.createJavafxImage
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.Parent
import javafx.scene.control.{Button, ScrollPane}
import javafx.scene.image.ImageView
import javafx.scene.layout.TilePane

class Grid {

  @FXML
  var tilePaneTP: TilePane = _
  @FXML
  var scrollPane:ScrollPane=_
  @FXML
  var returnHomeBtn: Button = _
  @FXML
  var gridBtn: Button = _

  var stateShown: Boolean = false

  def showGrid():Unit= {

    if(stateShown){}else {
      for (x <- gallery.lst) {
        println(System.getProperty("user.dir") + "\\src\\GaleriaAux\\" + x.info + ".png")
        val imageView: javafx.scene.image.ImageView = new ImageView(createJavafxImage(System.getProperty("user.dir") + "\\src\\GaleriaAux\\" + x.info + ".png"))
        imageView.setPreserveRatio(true)
        imageView.setFitWidth(250)
        imageView.setFitHeight(250)
        tilePaneTP.getChildren.add(imageView)
        println("adicionei imagem")
      }
      stateShown=true
    }
  }

  def returnHome():Unit={
    val fxmlLoader = new FXMLLoader(getClass.getResource("index.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    returnHomeBtn.getScene.setRoot(mainViewRoot)
  }

}
