package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Main {

	public static void main(String[] args) {

		int blurType = 1;
		switch (blurType) {
			case 0: {
				double[][] weights = GaussianBlur.getInstance().generateWeightMatrix(20, 10);

				BufferedImage gaussianBlurredImage = null;
				try {
					String filePath = "./giraffe.jpg";
					BufferedImage source_image = ImageIO.read(new File(filePath));
					gaussianBlurredImage = GaussianBlur.getInstance().createGaussianedImage(source_image, weights, 20);
				} catch (IOException e) {
					e.printStackTrace();
				}

				try {
					ImageIO.write(gaussianBlurredImage, "PNG", new File("gaussianBlur.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}

				System.out.println("Done!");
			}
				break;
			case 1: {
				// creating a boxMatrix for the box blur
				double[][] boxMatrix = BoxBlur.getInstance().generateBoxMatrix(100);
				BufferedImage boxBlurredImage = null;
				try {
					String filePath = "./giraffe.jpg";
					BufferedImage source_image = ImageIO.read(new File(filePath));
					boxBlurredImage = BoxBlur.getInstance().createBoxBlurredImage(source_image, boxMatrix, 100);
				} catch (IOException e) {
					e.printStackTrace();
				}

				try {
					ImageIO.write(boxBlurredImage, "PNG", new File("boxBlur.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}

				System.out.println("Done!");

			}
				break;
		}
	}

}
