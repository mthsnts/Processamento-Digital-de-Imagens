package utils;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

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
	
	
	public static Image tiraRuido(Image image, int r, int g, int b){
		
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

	public void mediana(Pixel[] pixels, int c) {
		double[] v = new double[pixels.length];
		for (int i = 0; i < pixels.length; i++) {
			if (c == 'r' || c == 'R') {
				double mediana = pixels[i].r / 2;
			}
		}
	}

}
