package GUI

import GUI.FxApp.gallery
import Package1.BitMap.{convertToArray, makeBitMap}
import Package1.{BitMap, QTree}
import Package1.QTree.Coords
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Node, Parent}
import javafx.scene.control.{Button, Label, TextField}
import javafx.scene.image.ImageView
import javafx.stage.{FileChooser, Stage}

import java.io.{File, FileInputStream, FileNotFoundException, IOException}


class EffectsWindow {

  @FXML
  var mirrorVtcBtn: Button = _
  @FXML
  var mirrorHrztBtn: Button = _
  @FXML
  var rotateRightBtn: Button = _
  @FXML
  var rotateLeftBtn: Button = _
  @FXML
  var sepiaBtn: Button = _
  @FXML
  var noiseBtn: Button = _
  @FXML
  var contrastBtn: Button = _
  @FXML
  var returnBtn: Button = _
  @FXML
  var findImageTxtFld: TextField = _
  @FXML
  var ImageViewPn: ImageView = _
  @FXML
  var findImageBtn: Button = _
  @FXML
  var scaleBtn: Button = _
  @FXML
  var factorTxtFld: TextField = _

  val e: Effects = new Effects()

  def mirrorHrzt(): Unit = {

    if (findImageTxtFld.getText() != "") {
      val qt: QTree[Coords] = e.mirrorH()(generateQTree())
      applyEffect(qt)
    }

  }

  def mirrorVtc(): Unit = {
    if (findImageTxtFld.getText() != "") {
      val qt: QTree[Coords] = e.mirrorV()(generateQTree())
      applyEffect(qt)
    }
  }

  def rotateRight(): Unit = {
    if (findImageTxtFld.getText() != "") {
      val qt: QTree[Coords] = e.rotateR()(generateQTree())
      applyEffect(qt)
    }
  }

  def rotateLeft(): Unit = {
    if (findImageTxtFld.getText() != "") {
      val qt: QTree[Coords] = e.rotateL()(generateQTree())
      applyEffect(qt)
    }
  }

  def sepia(): Unit = {
    if (findImageTxtFld.getText() != "") {
      println(new File(findImageTxtFld.getText()).getName.split(".png")(0))
      val qt: QTree[Coords] = e.mapColourEffect(c=>e.sepia(c))(generateQTree())
      applyEffect(qt)
    }
  }

  def scale():Unit={
    if (findImageTxtFld.getText() != "") {
      println(new File(findImageTxtFld.getText()).getName.split(".png")(0))
      val qt: QTree[Coords] = e.scale(factorTxtFld.getText().toDouble)(generateQTree())
      applyEffect(qt)
    }
  }

  /** noise puro */
  /*def noise(): Unit = {
    if (findImageTxtFld.getText() != "") {
      println(new File(findImageTxtFld.getText()).getName.split(".png")(0))
      val qt: QTree[Coords] = e.mapColourEffect_1(c=>e.noise_1(c))(generateQTree())
      applyEffect(qt)
    }
  }*/

  /** noise impuro */
  def noise(): Unit = {
    if (findImageTxtFld.getText() != "") {
      println(new File(findImageTxtFld.getText()).getName.split(".png")(0))
      val qt: QTree[Coords] = e.mapColourEffect(c=>e.noise(c))(generateQTree())
      applyEffect(qt)
    }
  }

  def contrast(): Unit = {
    if (findImageTxtFld.getText() != "") {
      println(new File(findImageTxtFld.getText()).getName.split(".png")(0))
      val qt: QTree[Coords] = e.mapColourEffect(c=>e.contrast(c))(generateQTree())
      applyEffect(qt)
    }
  }

  private def applyEffect(qt:QTree[Coords]):Unit={
      val tempPath:String=System.getProperty("user.dir") + "\\src\\GalleryTemp\\temp.png"
      println(tempPath)
      ImageUtil.writeImage(convertToArray(makeBitMap(qt).lst).toArray, tempPath, "png")

      ImageViewPn.setImage(new javafx.scene.image.Image(new FileInputStream(tempPath)))
      findImageTxtFld.setText(tempPath)
    println("applied effect")
  }

  def returnFunction(): Unit = {
    val fxmlLoader = new FXMLLoader(getClass.getResource("index.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    returnBtn.getScene.setRoot(mainViewRoot)

    val txtNode: Node = mainViewRoot.getChildrenUnmodifiable.get(0).getScene.lookup("#pathTxtFld")
    val TxtFld: TextField = txtNode.asInstanceOf[TextField]
    TxtFld.setText(findImageTxtFld.getText())

    val impNode: Node = mainViewRoot.getChildrenUnmodifiable.get(0).getScene.lookup("#imageViewTile")
    val imageViewPane: ImageView = impNode.asInstanceOf[ImageView]
    imageViewPane.setImage(new javafx.scene.image.Image(new FileInputStream(findImageTxtFld.getText())))

    val effectsPage: Node = mainViewRoot.getChildrenUnmodifiable.get(0).getScene.lookup("#effectsBtn")
    val effectsBtn: Button = effectsPage.asInstanceOf[Button]
    effectsBtn.setDisable(false)
    effectsBtn.setOpacity(1)

    val labelNode: Node = mainViewRoot.getChildrenUnmodifiable.get(0).getScene.lookup("#nameLabel")
    val label: Label = labelNode.asInstanceOf[Label]
    label.setText(getName())

  }

  def getName():String={
    println(findImageTxtFld.getText().split("\\\\GaleriaAux\\\\")(1).split(".png")(0))
    findImageTxtFld.getText().split("\\\\GaleriaAux\\\\")(1).split(".png")(0)
  }

  def findImage(): Unit = {

    val filechooser: FileChooser = new FileChooser()
    val file: File = filechooser.showOpenDialog(new Stage())
    println("File to String: " + file.toString)
    try {
      val imagePath: String = file.getAbsolutePath
      println(imagePath)
      findImageTxtFld.setText(imagePath)
      ImageViewPn.setImage(new javafx.scene.image.Image(new FileInputStream(imagePath)))
    } catch {
      case e: FileNotFoundException => println("Couldn't find that file.")
      case e: IOException => println("Had an IOException trying to read that file")
    }
  }

  private def generateQTree():QTree[Coords]={
    QTree.makeQTree(BitMap.create(findImageTxtFld.getText()))
  }
}

