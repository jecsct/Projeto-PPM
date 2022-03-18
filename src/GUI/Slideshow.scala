package GUI

import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Node, Parent}
import javafx.scene.control.{Button, Label, TextField}
import javafx.scene.image.ImageView

import java.io.FileInputStream

class Slideshow {

  @FXML
  var nextBtn: Button = _
  @FXML
  var previousBtn: Button = _
  @FXML
  var imageViewPn: ImageView = _
  @FXML
  var returnBtn: Button = _
  @FXML
  var supportTxtFld: TextField = _
  @FXML
  var nameLabel:Label=_

  def nextImage(): Unit = {
    slide(FxApp.gallery.lst, System.getProperty("user.dir") + "\\src\\GaleriaAux\\",false)
  }

  def previousImage(): Unit = {
    slide(FxApp.gallery.lst.reverse, System.getProperty("user.dir") + "\\src\\GaleriaAux\\",true)
  }

  private def slide(lst: List[Image], path: String,reverse:Boolean): Unit = {
    lst match {
      case x :: Nil =>
        val newPath:String=getImagePath(path,reverse)
        imageViewPn.setImage(new javafx.scene.image.Image(new FileInputStream(newPath)))
        supportTxtFld.setText(newPath)
      case x :: y :: xs => val curPath: String = path + x.info + ".png"
        val newPath: String = path + y.info + ".png"
        if (curPath.equals(supportTxtFld.getText())) {
          imageViewPn.setImage(new javafx.scene.image.Image(new FileInputStream(newPath)))
          supportTxtFld.setText(newPath)
          nameLabel.setText(getName())
        } else
          slide(y :: xs, path,reverse)
    }
  }

  private def getImagePath(path:String,b:Boolean):String={
    if(b) {
      val x :String = path + FxApp.gallery.lst.last.info + ".png"
      nameLabel.setText(FxApp.gallery.lst.last.info)
      x
    } else {
      val x :String = path + FxApp.gallery.lst.head.info + ".png"
      nameLabel.setText(FxApp.gallery.lst.head.info)
      x
    }
  }

  def returnFunc(): Unit = {
    val fxmlLoader = new FXMLLoader(getClass.getResource("index.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    returnBtn.getScene().setRoot(mainViewRoot)

    val imageNode: Node = mainViewRoot.getChildrenUnmodifiable.get(0).getScene.lookup("#imageViewTile")
    val imageView: ImageView = imageNode.asInstanceOf[ImageView]
    imageView.setImage(new javafx.scene.image.Image(new FileInputStream(supportTxtFld.getText())))

    val txtNode: Node = mainViewRoot.getChildrenUnmodifiable.get(0).getScene.lookup("#pathTxtFld")
    val txtFld: TextField = txtNode.asInstanceOf[TextField]
    txtFld.setText(supportTxtFld.getText())

    val effectsPage: Node = mainViewRoot.getChildrenUnmodifiable.get(0).getScene.lookup("#effectsBtn")
    val effectsBtn: Button = effectsPage.asInstanceOf[Button]
    effectsBtn.setDisable(false)
    effectsBtn.setOpacity(1)

    val labelNode: Node = mainViewRoot.getChildrenUnmodifiable.get(0).getScene.lookup("#nameLabel")
    val label: Label = labelNode.asInstanceOf[Label]
    label.setText(getName())
  }

  def getName():String={
    supportTxtFld.getText().split("\\\\GaleriaAux\\\\")(1).split(".png")(0)
  }
}
