package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Main {

	public static void main(String[] args) {

		double[][] weights = GaussianBlur.getInstance().generateWeightMatrix(150, 1.5);
		GaussianBlur.getInstance().printWeightedMatrixToFile(weights);

		BufferedImage answer = null;
		try {
			String filePath = "./giraffe.jpeg";
			BufferedImage source_image = ImageIO.read(new File(filePath));
			answer = GaussianBlur.getInstance().createGaussianedImage(source_image, weights, 20);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			ImageIO.write(answer, "PNG", new File("answer.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Done!");
	}

}
