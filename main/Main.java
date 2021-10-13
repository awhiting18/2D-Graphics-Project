package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Main {

	public static void main(String[] args) throws IOException {
		// new Processor("giraffe.jpg").save("done.png");
		// new Processor("giraffe.jpg").gaussianBlur(20, 4).save("done.png");

		EdgeDetection1 k = new EdgeDetection1();
		BufferedImage bI = ImageIO.read(new File("giraffe.jpg"));
		k.detectEdges(bI, "Box Blur Filter");

		// new Processor("giraffe.jpg").histogram().save("histogram.png");
	}
}