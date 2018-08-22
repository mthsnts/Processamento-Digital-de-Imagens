package sample;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import utils.Pdi;

public class Controller {

	@FXML private Button image1_button;
	@FXML private ImageView image_view_1;
	@FXML private Button image2_button;
	@FXML private ImageView image_view_2;
	@FXML private Button save_button;
	@FXML private ImageView image_view_3;
    @FXML private Label label_R;
    @FXML private Label label_G;
    @FXML private Label label_B;
    @FXML private TextField r_percentage;
    @FXML private TextField g_percentage;
    @FXML private TextField b_percentage;
    @FXML private Slider slider;
	
    private File f;
    private Image img_1;
    private Image img_2;
    private Image img_3;
       
	private File selectImage(){
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter
				("Imagens", "*.jpg", "*JPG", "**.png", "*PNG", "*.gif", "*.GIF", "*.bmp", "*.BMP"));
		File img = fileChooser.showOpenDialog(null);
		try{
			if(img != null){
				return img;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	@FXML
	public void limiar(){
		double value = slider.getValue();
		value = value / 255;
		img_3 = Pdi.limiar(img_1, value);
		updateImage3();
	}
	
	@FXML
    public void openImage1(){
		f = selectImage();	
		if( f != null){
			img_1 = new Image(f.toURI().toString());
			image_view_1.setImage(img_1);
			image_view_1.setFitWidth(img_1.getWidth());
			image_view_1.setFitHeight(img_1.getHeight());
		}
    }
	
	@FXML 
	public void openImage2(){
		f = selectImage();	
		if( f != null){
			img_2 = new Image(f.toURI().toString());
			image_view_2.setImage(img_2);
			image_view_2.setFitWidth(img_2.getWidth());
			image_view_2.setFitHeight(img_2.getHeight());
		}
	}
	
	
	@FXML
	private void updateImage3(){
		image_view_3.setImage(img_3);
		image_view_3.setFitWidth(img_3.getWidth());
		image_view_3.setFitHeight(img_3.getHeight());
	}
	
	
	@FXML
	public void greyScaleMedian(){
		img_3 = Pdi.greyScale(img_1, 0,0,0);
		updateImage3();
	}
	
	@FXML
	public void greyScaleP(){
		int rv = Integer.parseInt(r_percentage.getText());
		int gv = Integer.parseInt(g_percentage.getText());
		int bv = Integer.parseInt(b_percentage.getText());
		
		img_3 = Pdi.greyScale(img_1, rv, gv, bv);
		updateImage3();
	}
	
	
	public void save(){
		if(img_3 == null){
			FileChooser fc = new FileChooser();
			fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image", "*.png*"));
			fc.setInitialDirectory(new File("C:/Users/apoio.inftb/Desktop/Processamento-Digital-de-Imagens/src/utils/imgs"));
			File f = fc.showSaveDialog(null);
			if(f != null){
				BufferedImage bImg = SwingFXUtils.fromFXImage(img_3, null);
				try{
					ImageIO.write(bImg, "PNG", f);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	private void showMessage(String title, String header, String msg, AlertType type){
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(msg);
		alert.showAndWait();
	}
	
	
	@FXML
	public void clear(){
		
	}
	
	@FXML
	public void rasterImg(MouseEvent evt){
		ImageView iv = (ImageView) evt.getTarget();
		if(iv.getImage() != null){
			getRgb(iv.getImage(), (int) evt.getX(), (int) evt.getY());
		}
	}
	
	private void getRgb(Image img, int x, int y){
		try{
		Color cor = img.getPixelReader().getColor(x, y);
		label_R.setText("R: " + (int) (cor.getRed()*255));
		label_G.setText("G: " + (int) (cor.getGreen()*255));
		label_B.setText("B: " + (int) (cor.getBlue()*255));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
