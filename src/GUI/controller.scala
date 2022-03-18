package GUI

import Package1.BitMap.createArray
import Package1.{BitMap, QTree}
import FxApp.{gallery, saved}
import GUI.GeneralPurposeFunctions.{clearDirectory, createJavafxImage, disableButton, enableButton, saveImage, setPathTxtFieldToGaleriaAux, write}

import scala.annotation.tailrec
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.{Button, Label, TextField}
import javafx.scene.image.ImageView
import javafx.scene.Parent
import javafx.stage.{DirectoryChooser, FileChooser, Stage}
import javafx.scene.Node

import java.io.{File, FileInputStream}

class controller {
  @FXML
  var addImageButton: Button = _
  @FXML
  var removeImageButton: Button = _
  @FXML
  var findImageButton: Button = _
  @FXML
  var userInputTxtFld: TextField=_
  @FXML
  var pathTxtFld: TextField = _
  @FXML
  var imageViewTile: ImageView = _
  @FXML
  var effectsBtn: Button = _
  @FXML
  var searchImageButton: Button = _
  @FXML
  var warningLabel: Label = _
  @FXML
  var nameLabel:Label=_
  @FXML
  var saveBtn: Button = _
  //@FXML
  //var showContentBtn: Button = _
  @FXML
  var slideshowBtn: Button = _
  @FXML
  var gridBtn: Button = _
  @FXML
  var reverseBtn: Button = _
  @FXML
  var switchImagesBtn: Button = _
  @FXML
  var switchImageTxtFld2: TextField = _
  @FXML
  var switchImageTxtFld1: TextField = _
  @FXML
  var updateBtn: Button = _
  @FXML
  var savedTxtFld:Label=_

  /** Check */
  def searchImage(): Unit = {
    val fileChooser: FileChooser = new FileChooser()
    //fileChooser.setInitialDirectory(new File("Quick access"))
    val file: File = fileChooser.showOpenDialog(new Stage())
    if(file.toString!=null){
      val imagePath: String = file.getAbsolutePath
      pathTxtFld.setText(imagePath)
      imageViewTile.setImage(createJavafxImage(imagePath))
      nameLabel.setText("")
    }
  }

  /** Check */
  def addImage(): Unit = {
    if (userInputTxtFld.getText() != "" && pathTxtFld.getText() != "") {
      val image: GUI.Image = new GUI.Image(QTree.makeQTree(BitMap.create(pathTxtFld.getText())), userInputTxtFld.getText())
      gallery = gallery.addImage(image)
      saveImage(image.info, pathTxtFld.getText())
      setPathTxtFieldToGaleriaAux(pathTxtFld,userInputTxtFld.getText)
      nameLabel.setText(userInputTxtFld.getText())
      userInputTxtFld.clear()
      setSaved(false)
      visualFormsCheck()
    } else {
      warningLabel.setText("Introduza os campos necessários")
    }
    visualFormsCheck()
    enableButton(effectsBtn)
  }


/** Check */
  def removeImage(): Unit = {
    if (userInputTxtFld.getText() != "") {
      gallery = gallery.removeImage(userInputTxtFld.getText())
      val file: File = new File(System.getProperty("user.dir") + "\\src\\GaleriaAux\\" + userInputTxtFld.getText + ".png")
      if(file.toString.equals(pathTxtFld.getText())){
        imageViewTile.setImage(null)
        pathTxtFld.clear()
        nameLabel.setText("")
      }
      file.delete()
      println("O file '"+file+"' existe? -> "+file.exists())
      setSaved(false)
      visualFormsCheck()
      userInputTxtFld.clear()
    }else
      warningLabel.setText("Please insert an image name to delete")
    checkExistence()
  }

/** CHECK */
  def updateInfo(): Unit = {
    if(userInputTxtFld.getText()!=""){
      gallery = gallery.updateInfo(nameLabel.getText,userInputTxtFld.getText())
      updateSaveImage(userInputTxtFld.getText,pathTxtFld.getText)
      deleteImage(pathTxtFld.getText)
      nameLabel.setText(userInputTxtFld.getText)
      setPathTxtFieldToGaleriaAux(pathTxtFld,userInputTxtFld.getText)
      imageViewTile.setImage(createJavafxImage(pathTxtFld.getText))
      setSaved(false)
      userInputTxtFld.clear()
    }else
      warningLabel.setText("Please insert a name for update")
  }

/** OK */
  private def updateSaveImage(imageName: String, imagePath: String): Unit = {
  println("no save imagePath-> " + imagePath)
  ImageUtil.writeImage(ImageUtil.readColorImage(imagePath), System.getProperty("user.dir")+ "\\src\\GaleriaAux\\" + imageName + ".png", "png")
  }

  /** o file nao da delete nao percemos porque */
  def deleteImage(imagePath: String): Unit = {
    new File(imagePath).delete()
  }

  /** Check */
  def findImage(): Unit = {
    val auxTxt: String = userInputTxtFld.getText()
    if (auxTxt != "") {
      val auxPath : String = System.getProperty("user.dir") + "\\src\\GaleriaAux\\" + auxTxt + ".png"
      if (new File(auxPath).exists()) {
        imageViewTile.setImage(createJavafxImage(auxPath))
        pathTxtFld.setText(auxPath)
        nameLabel.setText(userInputTxtFld.getText())
        userInputTxtFld.clear()
      }
    } else {
      warningLabel.setText("Please insert a name for search")
    }
    checkExistence()
  }

/** CHECK */
  def effectsWindow(): Unit = {
    val fxmlLoader = new FXMLLoader(getClass.getResource("EffectsWindow.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    effectsBtn.getScene.setRoot(mainViewRoot)

    val txtNode: Node = mainViewRoot.getChildrenUnmodifiable.get(0).getScene.lookup("#findImageTxtFld")
    val TxtFld: TextField = txtNode.asInstanceOf[TextField]
    TxtFld.setText(pathTxtFld.getText())

    val impNode: Node = mainViewRoot.getChildrenUnmodifiable.get(0).getScene.lookup("#ImageViewPn")
    val imageViewPane: ImageView = impNode.asInstanceOf[ImageView]
    imageViewPane.setImage(new javafx.scene.image.Image(new FileInputStream(pathTxtFld.getText())))
  }

/** CHECK */
  def slideshow(): Unit = {
    val fxmlLoader = new FXMLLoader(getClass.getResource("Slideshow.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    slideshowBtn.getScene.setRoot(mainViewRoot)

    val txtNode: Node = mainViewRoot.getChildrenUnmodifiable.get(0).getScene.lookup("#supportTxtFld")
    val TxtFld: TextField = txtNode.asInstanceOf[TextField]
    TxtFld.setText(System.getProperty("user.dir") + "\\src\\GaleriaAux\\" + FxApp.gallery.lst.head.info + ".png")

    val imageNode: Node = mainViewRoot.getChildrenUnmodifiable.get(0).getScene.lookup("#imageViewPn")
    val imageView: ImageView = imageNode.asInstanceOf[ImageView]
    imageView.setImage(new javafx.scene.image.Image(new FileInputStream(TxtFld.getText())))

    val labelNode: Node = mainViewRoot.getChildrenUnmodifiable.get(0).getScene.lookup("#nameLabel")
    val label: Label = labelNode.asInstanceOf[Label]
    label.setText(FxApp.gallery.lst.head.info)
  }

/** CHECK */
  def reverse(): Unit = {
    gallery = gallery.reverse()
  }

  def grid(): Unit = {
    val fxmlLoader = new FXMLLoader(getClass.getResource("Grid.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()

    gridBtn.getScene.setRoot(mainViewRoot)
  }

/** CHECK */
  def switchImages():Unit = {
    if(switchImageTxtFld2.getText()!= "" && switchImageTxtFld1.getText() != ""){
      println({switchImageTxtFld1.getText();switchImageTxtFld2.getText()})
      gallery = gallery.switchOrder(switchImageTxtFld1.getText(),switchImageTxtFld2.getText())
      switchImageTxtFld1.clear()
      switchImageTxtFld2.clear()
      setSaved(false)
    }else
      warningLabel.setText("Fill both camps")
  }

  def save(): Unit = {
    val directoryChooser = new DirectoryChooser
    if (FxApp.galleryPath != "")
      directoryChooser.setInitialDirectory(new File(FxApp.galleryPath))
    val selectedDirectory = directoryChooser.showDialog(new Stage())
    if (selectedDirectory != null) {
      if (FxApp.galleryPath == selectedDirectory.toString)
        clearDirectory(FxApp.galleryPath)
      write(selectedDirectory.toString, System.getProperty("user.dir") + "\\src\\GUI\\GalleryPath.txt")
      FxApp.galleryPath = selectedDirectory.toString
      @tailrec
      def saveImages(lst: List[GUI.Image]): Unit = {
        lst match {
          case Nil =>
          case x :: xs =>
            val bm = BitMap.makeBitMap(x.qt)
            val a = createArray(bm)
            val fileStr: String = selectedDirectory.toString + "\\" + x.info + ".png"
            ImageUtil.writeImage(a, fileStr, "png")
            saveImages(xs)
        }
      }
      saveImages(gallery.lst)
      println("Images saved successfully")
      savedTxtFld.setDisable(false)
      setSaved(true)
    }
  }

  /**def showContent(): Unit = {
    println(gallery.lst.map(x=>x.info))
  }
  */

  /** OK */
  private def setSaved(value: Boolean): Unit = {
    saved = value
  }


  def checkExistence():Unit={
    if(new File(pathTxtFld.getText()).exists()) {
      enableButton(effectsBtn)
    } else{
      disableButton(effectsBtn)
    }
  }


  def visualFormsCheck():Unit={
    if(FxApp.gallery.lst.nonEmpty){
      enableButton(slideshowBtn)
      enableButton(gridBtn)
    }else{
      disableButton(slideshowBtn)
      disableButton(gridBtn)
    }
  }
}