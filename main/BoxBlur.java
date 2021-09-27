package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BoxBlur {
    private BoxBlur() {
    }

    private static BoxBlur blur;

    public static BoxBlur getInstance() {
        if (blur == null) {
            blur = new BoxBlur();
        }
        return blur;
    }

    public double[][] generateBoxMatrix(int radius) {
        double[][] weights = new double[radius][radius];
        double summation = 0;

        // gets the gaussian value for each of the matrix elements
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                // weights[i][j] = gaussianModel(i - radius / 2, j - radius / 2, variance);
                weights[i][j] = 1;
                summation += weights[i][j];
            }
        }

        // normalizes the matrix elements
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] /= summation;
            }
        }

        return weights;
    }

    public BufferedImage createBoxBlurredImage(BufferedImage source_image, double weights[][], int radius) {
        System.out.print("working...");

        BufferedImage answer = new BufferedImage(source_image.getWidth(), source_image.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < source_image.getWidth(); x++) {
            for (int y = 0; y < source_image.getHeight(); y++) {
                double[][] distributedColorRed = new double[radius][radius];
                double[][] distributedColorGreen = new double[radius][radius];
                double[][] distributedColorBlue = new double[radius][radius];

                for (int weightX = 0; weightX < weights.length; weightX++) {
                    for (int weightY = 0; weightY < weights[weightX].length; weightY++) {

                        int sampleX = x + weightX - (weights.length / 2);
                        int sampleY = y + weightY - (weights.length / 2);

                        if (sampleX > source_image.getWidth() - 1) {
                            int error_offset = sampleX - (source_image.getWidth() - 1);
                            sampleX = (source_image.getWidth() - 1) - error_offset;
                        }
                        if (sampleY > source_image.getHeight() - 1) {
                            int error_offset = sampleY - (source_image.getHeight() - 1);
                            sampleY = (source_image.getHeight() - 1) - error_offset;
                        }

                        if (sampleX < 0) {
                            sampleX = Math.abs(sampleX);
                        }
                        if (sampleY < 0) {
                            sampleY = Math.abs(sampleY);
                        }

                        double currentWeight = weights[weightX][weightY];

                        Color sampledColor = new Color(source_image.getRGB(sampleX, sampleY));

                        distributedColorRed[weightX][weightY] = currentWeight * sampledColor.getRed();
                        distributedColorGreen[weightX][weightY] = currentWeight * sampledColor.getGreen();
                        distributedColorBlue[weightX][weightY] = currentWeight * sampledColor.getBlue();

                    }
                }

                answer.setRGB(x, y,
                        new Color(getWeightedColorValue(distributedColorRed),
                                getWeightedColorValue(distributedColorGreen),
                                getWeightedColorValue(distributedColorBlue)).getRGB());
            }
        }
        return answer;
    }

    private int getWeightedColorValue(double[][] weightedColor) {
        double summation = 0;

        for (int i = 0; i < weightedColor.length; i++) {
            for (int j = 0; j < weightedColor[i].length; j++) {
                summation += weightedColor[i][j];
            }
        }
        return (int) summation;
    }
}