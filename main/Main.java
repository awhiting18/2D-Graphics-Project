package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Main {

	public static void main(String[] args) throws IOException {
		// Processor test = new Processor("giraffe.jpg");//
		// .applyConvolutionKernelFilter("Outline
		// Filter").save("done.png");

		// for (int x = 0; x < 50; x++) {
		// test.applyConvolutionKernelFilter("Box Blur Filter");
		// }

		// test.save("DoneBox.png");
		// new Processor("brickWall.jpeg").applyConvolutionKernelFilter("Outline
		// Filter").save("DoneOutline.png");

		// new Processor("giraffe.jpg").applyConvolutionKernelFilter("Gaussian Blur
		// Filter").save("DoneGaussian.png");
		// new Processor("brickWall.jpeg").applyConvolutionKernelFilter("Emboss
		// Filter").save("DoneEmboss.png");
		// new Processor("giraffe.jpg").histogram().save("histogram.png");
		// new IPImage("./in/horse.jpg").RBGscale(5).save("./out/blue.png");
		try {
			// new
			// IPImage("./in/blueCar.jpeg").focusOnColor(2).save("./out/selectedColorBlue.png");
			new IPImage("./in/sunset1.jpg").mixImages("./in/sunset2.jpg").save("./out/sunsetMixed.png");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}