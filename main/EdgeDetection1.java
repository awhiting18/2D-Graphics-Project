package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class EdgeDetection1 {

    public static final String HORIZONTAL_FILTER = "Horizontal Filter";
    public static final String VERTICAL_FILTER = "Vertical Filter";

    public static final String SOBEL_FILTER_VERTICAL = "Sobel Vertical Filter";
    public static final String SOBEL_FILTER_HORIZONTAL = "Sobel Horizontal Filter";

    public static final String SCHARR_FILTER_VETICAL = "Scharr Vertical Filter";
    public static final String SCHARR_FILTER_HORIZONTAL = "Scharr Horizontal Filter";

    public static final String GAUSSIAN_BLUR = "Gaussian Blur Filter";
    public static final String BOX_BLUR = "Box Blur Filter";

    public static final String SHARPEN_FILTER = "Sharpen Filter";

    public static final String OUTLINE_FILTER = "Outline Filter";

    public static final String EMBOSS_FILTER = "Emboss Filter";

    private static final double[][] FILTER_VERTICAL = { { 1, 0, -1 }, { 1, 0, -1 }, { 1, 0, -1 } };
    private static final double[][] FILTER_HORIZONTAL = { { 1, 1, 1 }, { 0, 0, 0 }, { -1, -1, -1 } };

    private static final double[][] FILTER_SOBEL_V = { { 1, 0, -1 }, { 2, 0, -2 }, { 1, 0, -1 } };
    private static final double[][] FILTER_SOBEL_H = { { 1, 2, 1 }, { 0, 0, 0 }, { -1, -2, -1 } };

    private static final double[][] FILTER_SCHARR_V = { { 3, 0, -3 }, { 10, 0, -10 }, { 3, 0, -3 } };
    private static final double[][] FILTER_SCHARR_H = { { 3, 10, 3 }, { 0, 0, 0 }, { -3, -10, -3 } };

    private static final double[][] FILTER_SHARPEN = { { 0, -1, 0 }, { -1, 5, -1 }, { 0, -1, 0 } };

    private static final double[][] FILTER_OUTLINE = { { -1, -1, -1 }, { -1, 8, -1 }, { -1, -1, -1 } };

    private static final double[][] FILTER_EMBOSS = { { -2, -1, 0 }, { -1, 1, 1 }, { 0, 1, 2 } };

    private static final float normValsBB = (1 / 9.0f);
    private static final double[][] FILTER_BOX = { { normValsBB, normValsBB, normValsBB },
            { normValsBB, normValsBB, normValsBB }, { normValsBB, normValsBB, normValsBB } };
    private static final double[][] FILTER_GAUSSIAN = { { 1 / 256.0f, 4 / 256.0f, 6 / 256.0f, 4 / 256.0f, 1 / 256.0f },
            { 4 / 256.0f, 16 / 256.0f, 24 / 256.0f, 16 / 256.0f, 4 / 256.0f },
            { 6 / 256.0f, 24 / 256.0f, 36 / 256.0f, 24 / 256.0f, 6 / 256.0f },
            { 4 / 256.0f, 16 / 256.0f, 24 / 256.0f, 16 / 256.0f, 4 / 256.0f },
            { 1 / 256.0f, 4 / 256.0f, 6 / 256.0f, 4 / 256.0f, 1 / 256.0f } };

    private final HashMap<String, double[][]> filterMap;

    public EdgeDetection1() {
        filterMap = buildFilterMap();

    }

    public File detectEdges(BufferedImage bufferedImage, String selectedFilter) throws IOException {
        double[][][] image = transformImageToArray(bufferedImage);
        double[][] filter = filterMap.get(selectedFilter);
        double[][][] convolvedPixels = applyConvolution(bufferedImage.getWidth(), bufferedImage.getHeight(), image,
                filter);
        return createImageFromConvolutionMatrix(bufferedImage, convolvedPixels);
    }

    private double[][][] transformImageToArray(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        double[][][] image = new double[3][height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color color = new Color(bufferedImage.getRGB(j, i));
                image[0][i][j] = color.getRed();
                image[1][i][j] = color.getGreen();
                image[2][i][j] = color.getBlue();
            }
        }
        return image;
    }

    private double[][][] applyConvolution(int width, int height, double[][][] image, double[][] filter) {
        Convolution convolution = new Convolution();
        double[][] redConv = convolution.convolutionType2(image[0], height, width, filter, 3, 3, 1);
        double[][] greenConv = convolution.convolutionType2(image[1], height, width, filter, 3, 3, 1);
        double[][] blueConv = convolution.convolutionType2(image[2], height, width, filter, 3, 3, 1);
        double[][][] finalConv = new double[3][redConv.length][redConv[0].length];
        /*
         * for (int i = 0; i < redConv.length; i++) { for (int j = 0; j <
         * redConv[i].length; j++) { finalConv[i][j] = redConv[i][j] + greenConv[i][j] +
         * blueConv[i][j]; } }
         */
        finalConv[0] = redConv;
        finalConv[1] = greenConv;
        finalConv[2] = blueConv;
        return finalConv;
    }

    private File createImageFromConvolutionMatrix(BufferedImage originalImage, double[][][] imageRGB)
            throws IOException {
        BufferedImage writeBackImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < imageRGB[0].length; i++) {
            for (int j = 0; j < imageRGB[0][i].length; j++) {
                Color color = new Color(fixOutOfRangeRGBValues(imageRGB[0][i][j]),
                        fixOutOfRangeRGBValues(imageRGB[1][i][j]), fixOutOfRangeRGBValues(imageRGB[2][i][j]));
                writeBackImage.setRGB(j, i, color.getRGB());
            }
        }
        File outputFile = new File("kernelPractice.png");
        ImageIO.write(writeBackImage, "png", outputFile);
        return outputFile;
    }

    private int fixOutOfRangeRGBValues(double value) {
        if (value < 0.0) {
            // value = -value;
            value = 0;
        }
        if (value > 255) {
            return 255;
        } else {
            return (int) value;
        }
    }

    private HashMap<String, double[][]> buildFilterMap() {
        HashMap<String, double[][]> filterMap;
        filterMap = new HashMap<>();
        filterMap.put(VERTICAL_FILTER, FILTER_VERTICAL);
        filterMap.put(HORIZONTAL_FILTER, FILTER_HORIZONTAL);

        filterMap.put(SOBEL_FILTER_VERTICAL, FILTER_SOBEL_V);
        filterMap.put(SOBEL_FILTER_HORIZONTAL, FILTER_SOBEL_H);

        filterMap.put(SCHARR_FILTER_VETICAL, FILTER_SCHARR_V);
        filterMap.put(SCHARR_FILTER_HORIZONTAL, FILTER_SCHARR_H);

        filterMap.put(GAUSSIAN_BLUR, FILTER_GAUSSIAN);
        filterMap.put(BOX_BLUR, FILTER_BOX);

        filterMap.put(SHARPEN_FILTER, FILTER_SHARPEN);

        filterMap.put(OUTLINE_FILTER, FILTER_OUTLINE);

        filterMap.put(EMBOSS_FILTER, FILTER_EMBOSS);

        return filterMap;
    }

}