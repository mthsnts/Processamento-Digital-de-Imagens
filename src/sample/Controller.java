package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import utils.OpenCVUtils;
import utils.Pdi;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static oracle.jrockit.jfr.events.Bits.intValue;

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
    private Slider sobelSlider;
    @FXML
    private Slider slider;

    @FXML
    private TextField pctImg1;
    @FXML
    private TextField pctImg2;

    @FXML
    private TextField krnlsz;

    @FXML
    private TextField txtAlpha;

    @FXML
    private TextField txtBeta;

    @FXML
    private TextField sobelKernel;


    @FXML
    private TextField threshold;


    @FXML
    private TextField tp;
    private static final Size BLUR_SIZE = new Size(3, 3);
    private static final int MAX_LOW_THRESHOLD = 100;
    private static final int RATIO = 3;
    private static final int KERNEL_SIZE = 3;
    private File f;
    private Image img_1, img_2, img_3;
    private OpenCVUtils converter;
    private Mat matImgSrc;
    int ddepth = CvType.CV_16S;
    int scale = 1;
    int delta = 0;
    private Random rng = new Random(12345);

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

    @FXML
    public void erosaoDilatacao() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat matImgDst = new Mat();
        int kernelSize = Integer.parseInt(krnlsz.getText());
        String tipo = tp.getText();
        matImgSrc = OpenCVUtils.imageToMat(img_1);
        Mat element = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT,
                new Size(2 * kernelSize + 1, 2 * kernelSize + 1), new Point(9, 9));
        if (tipo.equals("erosão")) {
            Imgproc.erode(matImgSrc, matImgDst, element);
        } else {
            Imgproc.dilate(matImgSrc, matImgDst, element);
        }

        img_3 = OpenCVUtils.matrixToImage(matImgDst);
        updateImage3();
    }

    @FXML
    public void canny() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat src = OpenCVUtils.imageToMat(img_1);
        Mat srcBlur = new Mat();
        Mat detectedEdges = new Mat();
        Mat dst = new Mat();

        Imgproc.blur(src, srcBlur, BLUR_SIZE);
        Imgproc.Canny(srcBlur, detectedEdges, Integer.parseInt(threshold.getText()), Integer.parseInt(threshold.getText()) * RATIO, KERNEL_SIZE, false);
        dst = new Mat(src.size(), CvType.CV_8UC3, Scalar.all(0));
        src.copyTo(dst, detectedEdges);
        img_3 = OpenCVUtils.matrixToImage(dst);
        updateImage3();
    }

    @FXML
    public void sobel() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat grad = new Mat();
        Mat src_gray = new Mat();

        if (img_1 != null) {
            Mat src = OpenCVUtils.imageToMat(img_1);
            Imgproc.GaussianBlur(src, src, new Size(3, 3), 0, 0, Core.BORDER_DEFAULT);
            Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_RGB2GRAY);
            Mat grad_x = new Mat(), grad_y = new Mat();
            Mat abs_grad_x = new Mat(), abs_grad_y = new Mat();
            if (intValue(sobelSlider.getValue()) % 2 != 0) {
                Imgproc.Sobel(src_gray, grad_x, ddepth, 1, 0, intValue(sobelSlider.getValue()), scale, delta, Core.BORDER_DEFAULT);
                Imgproc.Sobel(src_gray, grad_y, ddepth, 0, 1, intValue(sobelSlider.getValue()), scale, delta, Core.BORDER_DEFAULT);
                Core.convertScaleAbs(grad_x, abs_grad_x);
                Core.convertScaleAbs(grad_y, abs_grad_y);
                Core.addWeighted(abs_grad_x, 0.5, abs_grad_y, 0.5, 0, grad);
                img_3 = Pdi.negativa(OpenCVUtils.matrixToImage(grad));
                updateImage3();
            }
        }
    }

    private byte saturate(double val) {
        int iVal = (int) Math.round(val);
        iVal = iVal > 255 ? 255 : (iVal < 0 ? 0 : iVal);
        return (byte) iVal;
    }


    @FXML
    public void contraste() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat image = OpenCVUtils.imageToMat(img_1);
        Mat newImage = Mat.zeros(image.size(), image.type());
        double alpha = Double.parseDouble(txtAlpha.getText()); /*< Simple contrast control */
        int beta = Integer.parseInt(txtBeta.getText());       /*< Simple brightness control */
        byte[] imageData = new byte[(int) (image.total() * image.channels())];
        image.get(0, 0, imageData);
        byte[] newImageData = new byte[(int) (newImage.total() * newImage.channels())];
        for (int y = 0; y < image.rows(); y++) {
            for (int x = 0; x < image.cols(); x++) {
                for (int c = 0; c < image.channels(); c++) {
                    double pixelValue = imageData[(y * image.cols() + x) * image.channels() + c];
                    pixelValue = pixelValue < 0 ? pixelValue + 256 : pixelValue;
                    newImageData[(y * image.cols() + x) * image.channels() + c]
                            = saturate(alpha * pixelValue + beta);
                }
            }
        }
        newImage.put(0, 0, newImageData);
        img_3 = OpenCVUtils.matrixToImage(newImage);
        updateImage3();

    }

    @FXML
    public void equalizar() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat src = OpenCVUtils.imageToMat(img_1);
        Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY);
        Mat dst = new Mat();
        Imgproc.equalizeHist(src, dst);

        img_3 = OpenCVUtils.mat2Image(dst);
        updateImage3();

    }

    @FXML
    public void negativa() {
        img_3 = Pdi.negativa(img_1);
        updateImage3();
    }


    @FXML
    public void limiar() {
        double value = 128.0;//slider.getValue();
        value = value / 255;
        img_3 = Pdi.limiar(img_1, value);
        updateImage3();
        System.out.print(value);
    }

    @FXML
    public void Adiciona() {
        img_3 = Pdi.adicao(img_1, img_2, Double.parseDouble(pctImg1.getText()), Double.parseDouble(pctImg2.getText()));
        updateImage3();
    }

    @FXML
    public void subtrai() {
        img_3 = Pdi.subtrai(img_1, img_2, Double.parseDouble(pctImg1.getText()), Double.parseDouble(pctImg2.getText()));
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
    private void updateImage2() {
        image_view_2.setImage(img_2);
        image_view_2.setFitWidth(img_2.getWidth());
        image_view_2.setFitHeight(img_2.getHeight());
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

    @FXML
    public void salvar() {
        if (img_3 != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagem", "*.png"));
            fileChooser.setInitialDirectory(new File("C:/Users/mathe/Desktop"));
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                BufferedImage bImg = SwingFXUtils.fromFXImage(img_3, null);
                try {
                    ImageIO.write(bImg, "PNG", file);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @FXML
    public void contour() {

        Mat src = OpenCVUtils.image2Mat(Pdi.greyScale(img_1, 0, 0, 0));
        Mat cannyOutput = new Mat();
        Imgproc.Canny(src, cannyOutput, 5, 5 * 3, 3, false);
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(cannyOutput, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Mat drawing = Mat.zeros(cannyOutput.size(), CvType.CV_8UC3);
        for (int i = 0; i < contours.size(); i++) {
            Scalar color = new Scalar(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
            Imgproc.drawContours(drawing, contours, i, color, 2, Core.LINE_8, hierarchy, 0, new Point());
        }
        img_3 = OpenCVUtils.mat2Image(drawing);
        updateImage3();
    }


    @FXML
    public void diagnose() {
        int perimetro = 0;
        int controle = 0;
        int brancos = 0;
        int w = (int) img_1.getWidth();
        int h = (int) img_1.getHeight();
        PixelReader pr = img_1.getPixelReader();
        WritableImage wi = new WritableImage(w, h);
        PixelWriter pw = wi.getPixelWriter();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                Color cor = pr.getColor(i, j);
                System.out.println("teste = " + cor.getRed() + "/" + cor.getGreen());

                if (cor.getRed() == 1.0 && cor.getGreen() == 1.0 && cor.getBlue() == 1.0) {
                    brancos += 1;
                    if (pr.getColor(i, j - 1).getRed() != 1.0 || pr.getColor(i, j - 1).getGreen() == 1.0 || pr.getColor(i, j - 1).getBlue() == 1.0) {
                        controle += 1;
                    }
                    if (pr.getColor(i, j + 1).getRed() != 1.0 || pr.getColor(i, j + 1).getGreen() == 1.0 || pr.getColor(i, j + 1).getBlue() == 1.0) {
                        controle += 1;
                    }
                    if (pr.getColor(i - 1, j).getRed() != 1.0 || pr.getColor(i - 1, j).getGreen() == 1.0 || pr.getColor(i - 1, j).getBlue() == 1.0) {
                        controle += 1;
                    }
                    if (pr.getColor(i + 1, j).getRed() != 1.0 || pr.getColor(i + 1, j - 1).getGreen() == 1.0 || pr.getColor(i + 1, j - 1).getBlue() == 1.0) {
                        controle += 1;
                    }

                    if (controle > 0 && controle < 4) {
                        perimetro += 1;
                    }
                    controle = 0;
                }
            }
        }
        System.out.println("Razão = " + perimetro + "/" + brancos);
    }
}
