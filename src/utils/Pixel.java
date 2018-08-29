package utils;

public class Pixel {
	public Double r,g,b;
	public int x,y;
	
	Pixel[] nghbrX = new Pixel[4];
	Pixel[] nghbrc = new Pixel[4];
	Pixel[] nghbr3 = new Pixel[4];

	public Pixel(Double r, Double g, Double b, int x, int y) {
		super();
		this.r = r;
		this.g = g;
		this.b = b;
		this.x = x;
		this.y = y;
	}
}
