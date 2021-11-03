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
		float test = 1 / 273.0f + 4 / 273.0f + 7 / 273.0f + 4 / 273.0f + 1 / 273.0f + 4 / 273.0f + 16 / 273.0f
				+ 26 / 273.0f + 16 / 273.0f + 4 / 273.0f + 7 / 273.0f + 26 / 273.0f + 41 / 273.0f + 26 / 273.0f
				+ 7 / 273.0f + 4 / 273.0f + 16 / 273.0f + 26 / 273.0f + 16 / 273.0f + 4 / 273.0f + 1 / 273.0f
				+ 4 / 273.0f + 7 / 273.0f + 4 / 273.0f + 1 / 273.0f;

		System.out.print(test);
		// new Processor("giraffe.jpg").applyConvolutionKernelFilter("Gaussian Blur
		// Filter").save("DoneGaussian.png");
		// new Processor("brickWall.jpeg").applyConvolutionKernelFilter("Emboss
		// Filter").save("DoneEmboss.png");
		// new Processor("giraffe.jpg").histogram().save("histogram.png");
	}
}