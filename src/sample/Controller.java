package sample;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.Pdi;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import utils.OpenCVUtils;

public class Controller {

	@FXML
	private ImageView image_view_1;
	@FXML
	private ImageView image_view_2;
	@FXML
	private ImageView image_view_3;

	@FXML
	private Label label_R;
	@FXML
	private Label label_G;
	@FXML
	private Label label_B;

	@FXML
	private TextField r_percentage;
	@FXML
	private TextField g_percentage;
	@FXML
	private TextField b_percentage;

	@FXML
	private RadioButton rX;
	@FXML
	private RadioButton rC;
	@FXML
	private RadioButton r3;

	@FXML
	private Slider slider;

	@FXML
	private TextField pctImg1;
	@FXML
	private TextField pctImg2;

	@FXML
	private TextField krnlsz;

	@FXML
	private TextField tp;
    private static final Size BLUR_SIZE = new Size(3,3);
    private static final int MAX_LOW_THRESHOLD = 100;
    private static final int RATIO = 3;
    private static final int KERNEL_SIZE = 3;
	private File f;
	private Image img_1, img_2, img_3;
	private OpenCVUtils converter;
	private Mat matImgSrc;

	private File selectImage() {
		FileChooser fileChooser = new FileChooser();

		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagens", "*.jpg", "*JPG", "**.png",
				"*PNG", "*.gif", "*.GIF", "*.bmp", "*.BMP"));
		File img = fileChooser.showOpenDialog(null);
		try {
			if (img != null) {
				return img;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// @FXML
	// public void detectaFaces(){
	// f = selectImage();
	// String path = f.getAbsolutePath();
	//
	// System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	//
	// CascadeClassifier faceDetector = new
	// CascadeClassifier("haarcascade_frontalface_alt.xml");
	// Mat image = Imgcodecs.imread("rostos.jpg");
	//
	// MatOfRect faceDetections = new MatOfRect();
	// faceDetector.detectMultiScale(image, faceDetections);
	//
	// System.out.println(String.format("Detected %s faces",
	// faceDetections.toArray().length));
	//
	// for (Rect rect : faceDetections.toArray()) {
	// Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x +
	// rect.width, rect.y + rect.height),
	// new Scalar(0, 255, 0));
	// }
	//
	// String filename = "ouput.png";
	// System.out.println(String.format("Writing %s", filename));
	// Imgcodecs.imwrite(filename, image);
	// }

	@FXML
	public void erosaoDilatacao() {
    	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat matImgDst = new Mat();
		int kernelSize = Integer.parseInt(krnlsz.getText());
		String tipo = tp.getText();
		matImgSrc = OpenCVUtils.imageToMat(img_1);
		Mat element = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT,
				new Size(2 * kernelSize + 1, 2 * kernelSize + 1), new Point(kernelSize, kernelSize));
		if (tipo.equals("erosão")) {
			Imgproc.erode(matImgSrc, matImgDst, element);
		} else {
			Imgproc.dilate(matImgSrc, matImgDst, element);
		}
		Image imgTest = OpenCVUtils.matrixToImage(matImgDst);
		img_3 = imgTest;
		updateImage3();
	}
	
	@FXML
	public void canny(){
    	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat src = OpenCVUtils.imageToMat(img_1);
	    Mat srcBlur = new Mat();
	    Mat detectedEdges = new Mat();
	    Mat dst = new Mat();
	    int lowThresh = 0;

		   Imgproc.blur(src, srcBlur, BLUR_SIZE);
	        Imgproc.Canny(srcBlur, detectedEdges, lowThresh, lowThresh * RATIO, KERNEL_SIZE, false);
	        dst = new Mat(src.size(), CvType.CV_8UC3, Scalar.all(0));
	        src.copyTo(dst, detectedEdges);
	        Image imgTest = OpenCVUtils.matrixToImage(dst);
			img_3 = imgTest;
			updateImage3();
	}

	@FXML
	public void negativa() {
		img_3 = Pdi.negativa(img_1);
		updateImage3();
	}

	@FXML
	public void limiar() {
		double value = slider.getValue();
		value = value / 255;
		img_3 = Pdi.limiar(img_1, value);
		updateImage3();
	}

	@FXML
	public void Adiciona() {
		img_3 = Pdi.adicao(img_1, img_2, Double.parseDouble(pctImg1.getText()), Double.parseDouble(pctImg2.getText()));
		updateImage3();
	}

	@FXML
	public void subtrai() {
		img_3 = Pdi.subtrai(img_1, img_2, Integer.parseInt(pctImg1.getText()), Integer.parseInt(pctImg2.getText()));
		updateImage3();
	}

	@FXML
	public void openImage1() {
		f = selectImage();
		if (f != null) {
			img_1 = new Image(f.toURI().toString());
			image_view_1.setImage(img_1);
			image_view_1.setFitWidth(img_1.getWidth());
			image_view_1.setFitHeight(img_1.getHeight());
		}
	}

	@FXML
	public void openImage2() {
		f = selectImage();
		if (f != null) {
			img_2 = new Image(f.toURI().toString());
			image_view_2.setImage(img_2);
			image_view_2.setFitWidth(img_2.getWidth());
			image_view_2.setFitHeight(img_2.getHeight());
		}
	}

	@FXML
	private void updateImage3() {
		image_view_3.setImage(img_3);
		image_view_3.setFitWidth(img_3.getWidth());
		image_view_3.setFitHeight(img_3.getHeight());
	}

	@FXML
	public void greyScaleMedian() {
		img_3 = Pdi.greyScale(img_1, 0, 0, 0);
		updateImage3();
	}

	@FXML
	public void greyScaleP() {
		int rv = Integer.parseInt(r_percentage.getText());
		int gv = Integer.parseInt(g_percentage.getText());
		int bv = Integer.parseInt(b_percentage.getText());

		img_3 = Pdi.greyScale(img_1, rv, gv, bv);
		updateImage3();
	}

	public void save() {
		if (img_3 != null) {
			FileChooser fc = new FileChooser();
			fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image", "*.png*"));
			fc.setInitialDirectory(new File("C://users/mathe/Desktop/Processamento-Digital-de-Imagens/src/utils/imgs"));
			File f = fc.showSaveDialog(null);
			if (f != null) {
				BufferedImage bImg = SwingFXUtils.fromFXImage(img_3, null);
				try {
					ImageIO.write(bImg, "PNG", f);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@FXML
	public void abreHistograma(ActionEvent event) {
		try {
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Histograma.fxml"));
			Parent root = loader.load();
			stage.setScene(new Scene(root));
			stage.setTitle("Histograma");
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(((Node) event.getSource()).getScene().getWindow());
			stage.show();

			HistogramaController cntrllr = (HistogramaController) loader.getController();

			if (img_1 != null)
				Pdi.getGrafico(img_1, cntrllr.graph1);
			img_3 = Pdi.equaliza(img_1);
			updateImage3();
			Pdi.getGrafico(img_3, cntrllr.graph3);
			// if(img_2!=null)
			// Pdi.getGrafico(img_2, cntrllr.graph2);
			// if(img_3!=null)
			// Pdi.getGrafico(img_3, cntrllr.graph3);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showMessage(String title, String header, String msg, AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(msg);
		alert.showAndWait();
	}

	@FXML
	public void clear() {

	}

	@FXML
	public void rasterImg(MouseEvent evt) {
		ImageView iv = (ImageView) evt.getTarget();
		if (iv.getImage() != null) {
			getRgb(iv.getImage(), (int) evt.getX(), (int) evt.getY());
		}
	}

	public void getRgb(Image img, int x, int y) {
		try {
			Color cor = img.getPixelReader().getColor(x, y);
			label_R.setText("R: " + (int) (cor.getRed() * 255));
			label_G.setText("G: " + (int) (cor.getGreen() * 255));
			label_B.setText("B: " + (int) (cor.getBlue() * 255));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
