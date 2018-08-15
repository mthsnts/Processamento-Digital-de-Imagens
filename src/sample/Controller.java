package sample;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
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
    
	private File openImage1(){
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
	public void openImage2(){
		
	}
	
	@FXML
	public void save(){
		
	}

}
