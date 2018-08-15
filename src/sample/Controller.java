package sample;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

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
	public void save(){
		
	}
	
	@FXML
	public void clear(){
		
	}
	
	@FXML
	public void rasterImg(MouseEvent evt){
		ImageView iv = (ImageView) evt.getTarget();
		if(iv.getImage() != null){
			getRgb(iv.getImage(), (int) evt.getX(), (int) evt.getSceneY());
		}
	}
	
	private void getRgb(Image img, int x, int y){
		try{
		Color cor = img.getPixelReader().getColor(x-1, y-1);
		label_R.setText("R: " + (int) (cor.getRed()*255));
		label_G.setText("G: " + (int) (cor.getRed()*255));
		label_B.setText("B: " + (int) (cor.getRed()*255));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
