package utils;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javafx.event.ActionEvent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Pdi {

	public void ordena(double v[]) {
		for (int i = v.length; i >= 1; i--) {
			for (int j = 1; j < i; j++) {
				if (v[j - 1] > v[j]) {
					double aux = v[j];
					v[j] = v[j - 1];
					v[j - 1] = aux;
				}
			}
		}
	}
//	public void gerarHistograma(ActionEvent event){
//		  try{
//			  Stage stage = new Stage();
//			  FXMLLoader loader = new FXMLLoader(getClass().
//					  getResource("HistogramaModal.fxml"));
//			  Parent root = loader.load();
//			  stage.setScene(new Scene(root));
//			  stage.setTitle("Histograma");
//			  //stage.initModality(Modality.WINDOW_MODAL);
//			  stage.initOwner(((Node)event.getSource()).
//					  getScene().getWindow() );
//			  stage.show();
//			  
//			  HistogramaModalController controller = 
//					  (HistogramaModalController)loader.getController();
//			  
//			  if(imagem1!=null)
//				  Pdi.getGrafico(imagem1, controller.hist1);
//			  if(imagem2!=null)
//				  Pdi.getGrafico(imagem2, controller.hist2);
//			  if(imagem3!=null)
//				  Pdi.getGrafico(imagem3, controller.hist3);
//			  
//			  
//		  }catch (Exception e) {
//			e.printStackTrace();
//		}
//	  }
	
	public static int getValorHist(Image img, int pos){
		 int[] hist = new int[255];
			int w = (int) img.getWidth();
			int h = (int) img.getHeight();
			PixelReader pr = img.getPixelReader();
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					if(i < 254)
						hist[i] = pr.getArgb(i, j) * -1;
					}
				}
			
		 return hist[pos];
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void getGrafico(Image img,BarChart<String, Number> grafico){
		CategoryAxis eixoX = new CategoryAxis();
		NumberAxis eixoY = new NumberAxis();
	    eixoX.setLabel("Canal");       
	    eixoY.setLabel("valor");
	    XYChart.Series vlr = new XYChart.Series();
	    vlr.setName("Intensidade");
	    int[] hist = new int[255];
	    for(int i =0;i < 255; i++){
		   hist[i] = getValorHist(img, i);
	    }
	    
	    img = greyScale(img, 0, 0, 0);
//	    int [] hist = new int[] {3,5,3,0,0,2,1,1};
	    for (int i=0; i<hist.length; i++) {
	    	vlr.getData().add(new XYChart.Data(i+"", getValorHist(img, i)));
		}
	    grafico.getData().addAll(vlr);
	}
	

	public static Image tiraRuido(Image image, String tipo) {
		if (tipo == "x") {

		} else if (tipo == "cruz") {

		} else if (tipo == "3x3") {

		}
		return image;
	}

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
					double median;
					if (r == 0 && g == 0 && b == 0) {
						median = (prvsColor.getRed() + prvsColor.getGreen() + prvsColor.getBlue()) / 3;
						
					} else {
						median = ((prvsColor.getRed() * r) / 100) + ((prvsColor.getGreen() * g) / 100)
								+ ((prvsColor.getBlue() * b) / 100) / 100;
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

	public static Image limiar(Image image, double value) {
		greyScale(image, 0, 0, 0);
		try {

			int w = (int) image.getWidth();
			int h = (int) image.getHeight();

			PixelReader pr = image.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();

			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					Color prvsColor = pr.getColor(i, j);
					Color newColor;
					if (prvsColor.getRed() >= value) {
						newColor = new Color(1, 1, 1, prvsColor.getOpacity());
					} else {
						newColor = new Color(0, 0, 0, prvsColor.getOpacity());
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

	public static Image negativa(Image image) {
		int w = (int) image.getWidth();
		int h = (int) image.getHeight();

		PixelReader pr = image.getPixelReader();
		WritableImage wi = new WritableImage(w, h);
		PixelWriter pw = wi.getPixelWriter();

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Color prvsColor = pr.getColor(i, j);
				Color newColor;
				newColor = new Color(1 - prvsColor.getRed(), 1 - prvsColor.getGreen(), 1 - prvsColor.getBlue(),
						prvsColor.getOpacity());
				pw.setColor(i, j, newColor);
			}
		}
		return wi;
	}

	public static Image adicionaBorda(Image img, MouseEvent evt) {
		int x = (int) evt.getX();
		int y = (int) evt.getY();
		int x2 = (int) evt.getX();
		int y2 = (int) evt.getY();


		PixelReader pr = img.getPixelReader();
		WritableImage wi = new WritableImage(x, y);
		PixelWriter pw = wi.getPixelWriter();

		for (int i = x; i < x2; i++) {
				Color newColor;
				newColor = new Color(1,1,1,1);
				pw.setColor(x, x2, newColor);
		}
			return wi;
	}

	public static Image giraImgaem(Image img) {
		int w = (int) img.getWidth();
		int h = (int) img.getHeight();

		PixelReader pr = img.getPixelReader();
		WritableImage wi = new WritableImage(w, h);
		PixelWriter pw = wi.getPixelWriter();
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {

			}
		}
		return wi;
	}

	// w-1, h-1
	public static Image tiraRuido(Image img) {
		int w = (int) img.getWidth();
		int h = (int) img.getHeight();
		for (int i = 0; i < w - 1; i++) {
			for (int j = 0; j < h - 1; j++) {

			}
		}
		return null;
	}

	public static Image adicao(Image img1, Image img2, double pctImg1, double pctImg2) {

		int w1 = (int) img1.getWidth();
		int h1 = (int) img1.getHeight();
		int w2 = (int) img2.getWidth();
		int h2 = (int) img2.getHeight();

		int h = Math.min(h1, h2);
		int w = Math.min(w1, w2);

		PixelReader p1 = img1.getPixelReader();
		PixelReader p2 = img2.getPixelReader();

		WritableImage wi = new WritableImage(w, h);
		PixelWriter pw = wi.getPixelWriter();

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {

				Color c1 = p1.getColor(i, j);
				Color c2 = p2.getColor(i, j);

				Color novaCor = new Color((c1.getRed() * (pctImg1 / 100)) + (c2.getRed() * (pctImg2 / 100)),
						(c1.getGreen() * (pctImg1 / 100)) + (c2.getGreen() * (pctImg2 / 100)),
						(c1.getBlue() * (pctImg1 / 100)) + (c2.getBlue() * (pctImg2 / 100)),
						((c1.getOpacity() * (pctImg1 / 100)) + (c2.getOpacity() * (pctImg2 / 100))));

				pw.setColor(i, j, novaCor);
			}
		}
		return wi;
	}

	public static Image subtrai(Image img1, Image img2, int pctImg1, int pctImg2) {
		int w1 = (int) img1.getWidth();
		int h1 = (int) img1.getHeight();
		int w2 = (int) img2.getWidth();
		int h2 = (int) img2.getHeight();

		int h = getMinHeight(h1, h2);
		int w = getMinWidth(w1, w2);
		PixelReader p1 = img1.getPixelReader();
		PixelReader p2 = img2.getPixelReader();
		WritableImage wi = new WritableImage(w, h);
		PixelWriter pr = wi.getPixelWriter();

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Color c1 = p1.getColor(i, j);
				Color c2 = p2.getColor(i, j);

				Color novaCor = new Color(c1.getRed() - c2.getRed(), c1.getGreen() - c2.getGreen(),
						c1.getBlue() - c2.getBlue(), c1.getOpacity() - c2.getOpacity());

				pr.setColor(i, j, novaCor);
			}
		}
		return wi;
	}

	public static Pixel[] getNghbrX(Image image, Pixel p) {
		Pixel[] vizX = new Pixel[5];
		PixelReader pr = image.getPixelReader();
		Color cor = pr.getColor(p.x - 1, p.y - 1);
		vizX[0] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x, p.y);
		cor = pr.getColor(p.x - 1, p.y - 1);
		vizX[1] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x, p.y);
		cor = pr.getColor(p.x + 1, p.y - 1);
		vizX[2] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x, p.y);
		cor = pr.getColor(p.x - 1, p.y + 1);
		vizX[3] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x, p.y);
		vizX[4] = p;
		return vizX;
	}

	public static Pixel[] getNghbrC(Image image, Pixel p) {
		Pixel[] vizX = new Pixel[5];
		PixelReader pr = image.getPixelReader();
		Color cor = pr.getColor(p.x, p.y - 1);
		vizX[0] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x, p.y);
		cor = pr.getColor(p.x, p.y + 1);
		vizX[1] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x, p.y);
		cor = pr.getColor(p.x + 1, p.y);
		vizX[2] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x, p.y);
		cor = pr.getColor(p.x - 1, p.y);
		vizX[3] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x, p.y);
		vizX[4] = p;
		return vizX;
	}

	public static Pixel[] getNghbr3(Image image, Pixel p) {
		Pixel[] vizX = new Pixel[9];
		PixelReader pr = image.getPixelReader();
		Color cor = pr.getColor(p.x - 1, p.y - 1);
		vizX[0] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x, p.y);
		cor = pr.getColor(p.x - 1, p.y - 1);
		vizX[1] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x, p.y);
		cor = pr.getColor(p.x + 1, p.y - 1);
		vizX[2] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x, p.y);
		cor = pr.getColor(p.x - 1, p.y + 1);
		vizX[3] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x, p.y);
		cor = pr.getColor(p.x - 1, p.y - 1);
		vizX[4] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x, p.y);
		cor = pr.getColor(p.x - 1, p.y - 1);
		vizX[5] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x, p.y);
		cor = pr.getColor(p.x + 1, p.y - 1);
		vizX[6] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x, p.y);
		cor = pr.getColor(p.x - 1, p.y + 1);
		vizX[7] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x, p.y);
		vizX[8] = p;
		return vizX;
	}

	public static int getMinHeight(int h1, int h2) {
		System.out.println(h1 > h2 ? h2 : h1);
		return h1 > h2 ? h2 : h1;
	}

	public static int getMinWidth(int w1, int w2) {
		System.out.println(w1 > w2 ? w2 : w1);
		return w1 > w2 ? w2 : w1;

	}

}
