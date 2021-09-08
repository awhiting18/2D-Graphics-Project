package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Main {
	
	public static void main(String[] args) {
		
		double[][] weights = GaussianBlur.getInstance().generateWeightMatrix(150, Math.sqrt(150));
		GaussianBlur.getInstance().printWeightedMatrixToFile(weights);
		
		BufferedImage answer = null;
		try {
			BufferedImage source_image = ImageIO.read(new File("C:/Users/AJ/Desktop/School/2DGraphics_ImageProc/2DGraphics_Project/2D-Graphics-Project/bg_3.jpeg"));
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
