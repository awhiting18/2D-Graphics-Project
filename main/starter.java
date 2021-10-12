package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class starter {

    public static void main(String[] args) {
        new Processor("giraffe.jpg").rotatedNearestNeighbor(1).crop(10, 10, 300, 300).save("done.png");

        new Processor("horse.jpg").histogram().save("histogram.png");
    }
}
