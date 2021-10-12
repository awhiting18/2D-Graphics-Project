package main;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.awt.Color;

public class Processor {

  BufferedImage image;

  public Processor(String filename) {
    try {
      BufferedImage bufferedImage = ImageIO.read(new File(filename));
      this.image = bufferedImage;

    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public Processor save(String filename) {
    try {
      ImageIO.write(this.image, "PNG", new File(filename));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return this;
  }

  public Processor rotatedNearestNeighbor(double angle) {
    BufferedImage toReturn = new BufferedImage(this.image.getWidth(), this.image.getHeight(),
        BufferedImage.TYPE_INT_ARGB);

    int width = this.image.getWidth();
    int height = this.image.getHeight();
    double centerX = width / 2;
    double centerY = height / 2;

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {

        // Get the distance and angle from the origin
        double distanceX = x - centerX;
        double distanceY = y - centerY;
        double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
        double postAngle = Math.atan2(distanceY, distanceX);
        double preAngle = postAngle - angle;
        int originalX = (int) ((distance * Math.cos(preAngle) + .5) + centerX);
        int originalY = (int) ((distance * Math.sin(preAngle) + .5) + centerY);
        if (originalX < 0 || originalX >= width || originalY < 0 || originalY >= height)
          continue;

        int pixel = this.image.getRGB(originalX, originalY);
        toReturn.setRGB(x, y, pixel);
      }
    }

    this.image = toReturn;
    return this;
  }

  public Processor scaleNearestNeighbor(double scaleX, double scaleY) {
    int newWidth = (int) (this.image.getWidth() * scaleX);
    int newHeight = (int) (this.image.getHeight() * scaleY);
    BufferedImage toReturn = new BufferedImage(this.image.getWidth(), this.image.getHeight(),
        BufferedImage.TYPE_INT_ARGB);

    for (int y = 0; y < newHeight; y++) {
      for (int x = 0; x < newWidth; x++) {
        if (x >= this.image.getWidth() || y >= this.image.getHeight())
          continue;
        // Find the source pixel
        int originalX = (int) (x / scaleX);
        int originalY = (int) (y / scaleY);
        int pixel = this.image.getRGB(originalX, originalY);
        toReturn.setRGB(x, y, pixel);
      }
    }

    this.image = toReturn;
    return this;
  }

  public Processor translateNearestNeighbor(double i, double j) {
    BufferedImage toReturn = new BufferedImage(this.image.getWidth(), this.image.getHeight(),
        BufferedImage.TYPE_INT_ARGB);
    for (var y = 0; y < toReturn.getHeight(); y++) {
      for (var x = 0; x < toReturn.getWidth(); x++) {
        int originalX = (int) (x - i + .5);
        int originalY = (int) (y - j + .5);

        if (originalX < 0 || originalX >= this.image.getWidth() || originalY < 0 || originalY >= this.image.getHeight())
          continue;

        var pixelInt = this.image.getRGB(originalX, originalY);
        toReturn.setRGB(x, y, pixelInt);
      }
    }

    this.image = toReturn;
    return this;
  }

  public Processor translateLinear(double i, double j, boolean b) {
    BufferedImage toReturn = new BufferedImage(this.image.getWidth(), this.image.getHeight(),
        BufferedImage.TYPE_INT_ARGB);
    for (var y = 0; y < toReturn.getHeight(); y++) {
      for (var x = 0; x < toReturn.getWidth() - 1; x++) {
        int originalX = (int) (x - i + .5);
        int leftPixel = (originalX);
        int rightPixel = (originalX + 1);
        int originalY = (int) (y - j + .5);

        if (originalX < 0 || originalX >= this.image.getWidth() - 1 || originalY < 0
            || originalY >= this.image.getHeight() - 1)
          continue;

        Color pixelLeft = new Color(this.image.getRGB(leftPixel, originalY));
        Color pixelRight = new Color(this.image.getRGB(rightPixel, originalY));

        double percent = i - (int) i;
        Color leftPixelContibution = new Color((int) (pixelLeft.getRed() * (1 - percent)),
            (int) (pixelLeft.getGreen() * (1 - percent)), (int) (pixelLeft.getBlue() * (1 - percent)));
        Color rightPixelContibution = new Color((int) (pixelRight.getRed() * (percent)),
            (int) (pixelRight.getGreen() * (percent)), (int) (pixelRight.getBlue() * (percent)));

        int finalRed = leftPixelContibution.getRed() + rightPixelContibution.getRed();
        int finalGreen = leftPixelContibution.getGreen() + rightPixelContibution.getGreen();
        int finalBlue = leftPixelContibution.getBlue() + rightPixelContibution.getBlue();
        Color finalColor = new Color(finalRed, finalGreen, finalBlue);

        toReturn.setRGB(x, y, finalColor.getRGB());
      }
    }

    this.image = toReturn;
    return this;
  }

  public Processor crop(int ulx, int uly, int lrx, int lry) {
    var width = lrx - ulx;
    var height = lry - uly;
    var toReturn = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for (var h = 0; h < height; h++) {
      for (var w = 0; w < width; w++) {
        var pixelInt = this.image.getRGB(w + ulx, h + uly);
        var pixelColor = new Color(pixelInt);
        toReturn.setRGB(w, h, pixelColor.getRGB());
      }
    }

    this.image = toReturn;
    return this;

  }

  public Processor histogram() {
    int height = 100;
    var toReturn = new BufferedImage(256, height, BufferedImage.TYPE_4BYTE_ABGR);

    // Generate the histogram info
    int[] counts = new int[256];
    for (var h = 0; h < this.image.getHeight(); h++) {
      for (var w = 0; w < this.image.getWidth(); w++) {
        Color pixel = new Color(this.image.getRGB(w, h));
        int red = pixel.getRed();
        int green = pixel.getGreen();
        int blue = pixel.getBlue();
        int value = (int) ((red + green + blue) / 3.0);
        counts[value]++;

      }
    }

    // Render the histogram
    Graphics2D g = (Graphics2D) toReturn.getGraphics();
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, counts.length, height);

    var maxValue = Arrays.stream(counts).max().orElse(0);
    for (var i = 0; i < counts.length; i++) {
      int percent = (int) (counts[i] / (double) maxValue * height);
      g.setColor(Color.WHITE);
      g.fillRect(i, height - percent, i + 1, percent);
    }

    this.image = toReturn;
    return this;

  }

  public Processor gaussianBlur(int radius, int variance) {
    System.out.print("working...");

    double[][] weights = generateWeightMatrix(radius, variance);
    BufferedImage source_image = this.image;

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

        answer.setRGB(x, y, new Color(getWeightedColorValue(distributedColorRed),
            getWeightedColorValue(distributedColorGreen), getWeightedColorValue(distributedColorBlue)).getRGB());
      }
    }
    this.image = answer;
    return this;
  }

  private double[][] generateWeightMatrix(int radius, double variance) {
    double[][] weights = new double[radius][radius];
    double summation = 0;

    // gets the gaussian value for each of the matrix elements
    for (int i = 0; i < weights.length; i++) {
      for (int j = 0; j < weights[i].length; j++) {
        weights[i][j] = gaussianModel(i - radius / 2, j - radius / 2, variance);
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

  private int getWeightedColorValue(double[][] weightedColor) {
    double summation = 0;

    for (int i = 0; i < weightedColor.length; i++) {
      for (int j = 0; j < weightedColor[i].length; j++) {
        summation += weightedColor[i][j];
      }
    }
    return (int) summation;
  }

  private double gaussianModel(double x, double y, double variance) {
    return (1 / (2 * Math.PI * Math.pow(variance, 2))
        * Math.exp(-(Math.pow(x, 2) + Math.pow(y, 2)) / (2 * Math.pow(variance, 2))));
  }

  public Processor boxBlur(int radius) {
    System.out.print("working...");
    double[][] weights = generateBoxMatrix(radius);
    BufferedImage source_image = this.image;

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

        answer.setRGB(x, y, new Color(getWeightedColorValue(distributedColorRed),
            getWeightedColorValue(distributedColorGreen), getWeightedColorValue(distributedColorBlue)).getRGB());
      }
    }
    this.image = answer;
    return this;
  }

  private double[][] generateBoxMatrix(int radius) {
    double[][] weights = new double[radius][radius];
    double summation = 0;
    for (int i = 0; i < weights.length; i++) {
      for (int j = 0; j < weights[i].length; j++) {
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
}
