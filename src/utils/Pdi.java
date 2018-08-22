package utils;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Pdi {

	public static Image greyScale(Image image, int r, int g, int b) {
		try {

			int w = (int) image.getWidth();
			int h = (int) image.getHeight();

			PixelReader pr = image.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();

			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					Color prvsColor = pr.getColor(i, j);
					double median = 0;
					if(r == 0 && g == 0 && b == 0){
					 median = (prvsColor.getRed() + prvsColor.getGreen() + prvsColor.getBlue()) / 3;
					}else{
						 median = ((prvsColor.getRed() * r)/100) + ( (prvsColor.getGreen() * g)/100 ) + ( (prvsColor.getBlue()*b)/100)
								 / 100;
					}
					Color newColor = new Color(median, median, median, prvsColor.getOpacity());
					pw.setColor(i, j, newColor);
					}
				}
			return wi;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static Image limiar(Image image, double value){
		greyScale(image, 0,0,0);
		try {

			int w = (int) image.getWidth();
			int h = (int) image.getHeight();

			PixelReader pr = image.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();

			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					Color prvsColor = pr.getColor(i, j);
					double median = 0;
					Color newColor;
					if(prvsColor.getRed() >= value){
						newColor = new Color(1,1,1, prvsColor.getOpacity());
					}else{
						newColor = new Color(0,0,0, prvsColor.getOpacity());
					}
					pw.setColor(i, j, newColor);
					}
				}
			return wi;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	
}
