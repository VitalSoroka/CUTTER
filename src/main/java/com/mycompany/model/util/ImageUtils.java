package com.mycompany.model.util;

import com.mycompany.model.entity.Position;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Random;

public class ImageUtils {
    private static final double RED_WEIGHT = 0.299d;
    private static final double GREEN_WEIGHT = 0.587d;
    private static final double BLUE_WEIGHT = 0.114d;

    public static double getBrightsForPixel(Color pixel) {
        return RED_WEIGHT * pixel.getRed() + GREEN_WEIGHT * pixel.getGreen() + BLUE_WEIGHT * pixel.getBlue();
    }

    public static Position getPositionForSnakeWay(int blockNumber, int width, int height) {
        Position position = getPositionForSnakeWay(blockNumber, 0);
        return new Position(position.getX() * width,position.getY() * height);
    }

    public static void toBlue(BufferedImage image){
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                image.setRGB (i, j, new Color((int) ( new Color(image.getRGB(i, j)).getBlue()),
                        (int)( 225-new Color(image.getRGB(i, j)).getBlue()),
                        (int) ( 0*new Color(image.getRGB(i, j)).getBlue())).getRGB());
            }
        }
    }

    public static void clearLSB(BufferedImage image) {
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                image.setRGB (i, j, image.getRGB(i, j) & 0xFFFEFEFE);

            }
        }
    }

    public static void clearTwoLSB(BufferedImage image) {
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                image.setRGB( i, j, image.getRGB(i, j) & 0xFFFCFCFC);
            }
        }
    }

    public static int convertRGBToBlackAndWhite(int rgb){
        int avg = (((rgb & 0xFF0000) >> 16) + ((rgb & 0x00FF00) >> 8) + (rgb & 0x0000FF))/3;
        return (rgb & 0xFF000000) | (avg << 16) | (avg << 8) | avg;
    }

    public static void convertImgToBlackAndWhite(BufferedImage image) {
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                image.setRGB( i, j, convertRGBToBlackAndWhite(image.getRGB(i, j)));
            }
        }
    }
    public static String  convertImgToBites(BufferedImage image) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                char symbol = (char) (image.getRGB(i, j) & 0xff);
                result.append(TextUtils.getBitesForASCIISymbol(symbol));
            }
        }
        return result.toString();
    }

    public static void addNoise(BufferedImage image, float volume){
        int amount = Math.round(volume * 0xFF / 100);
        Random generator = new Random(new Date().getTime());
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color pixelColr = new Color(image.getRGB(i, j));
                int noiseForBlue = generator.nextInt(amount + 1);
                int noiseForGreen = generator.nextInt(amount + 1);
                int noiseForRed = generator.nextInt(amount + 1);
                Color pixelWithNoise = new Color(Math.min(0xFF, pixelColr.getRed() + noiseForRed),
                        Math.min(0xFF, pixelColr.getGreen() + noiseForGreen),
                        Math.min(0xFF, pixelColr.getBlue() + noiseForBlue));
                image.setRGB(i, j, pixelWithNoise.getRGB());
            }
        }
    }

    public static int calculateNewBlueComponent(BufferedImage image, Position position, double signalEnergy, char bite){
        Color colorPixel = new Color(image.getRGB(position.getX(), position.getY()));
        int blueComponent = getBlueComponent(image, position);

        int newBlueComponent = blueComponent + (int) (signalEnergy * ImageUtils.getBrightsForPixel(colorPixel) * (2 * (bite - 48) - 1) );
        newBlueComponent = Math.max(Math.min(0xFF, newBlueComponent), 0);

        return new Color(colorPixel.getRed(), colorPixel.getGreen(), newBlueComponent).getRGB();
    }

    public static int calculateNewComponent(BufferedImage image, Position position, double signalEnergy, char bite){
        Color colorPixel = new Color(image.getRGB(position.getX(), position.getY()));
        int redComponent = new Color(image.getRGB(position.getX(), position.getY())).getRed();
        int blueComponent = getBlueComponent(image, position);
        int greenComponent = new Color(image.getRGB(position.getX(), position.getY())).getGreen();
        int newRedComponent = redComponent + (int) (signalEnergy * ImageUtils.getBrightsForPixel(colorPixel) * (2 * (bite - 48) - 1) );
        newRedComponent = Math.max(Math.min(0xFF, newRedComponent), 0);
        int newBlueComponent = blueComponent + (int) (signalEnergy * ImageUtils.getBrightsForPixel(colorPixel) * (2 * (bite - 48) - 1) );
        newBlueComponent = Math.max(Math.min(0xFF, newBlueComponent), 0);
        int newGreenComponent = greenComponent + (int) (signalEnergy * ImageUtils.getBrightsForPixel(colorPixel) * (2 * (bite - 48) - 1) );
        newGreenComponent = Math.max(Math.min(0xFF, newGreenComponent), 0);

        return new Color(newRedComponent, newGreenComponent, newBlueComponent).getRGB();
    }

    public static int getBlueComponent(BufferedImage image, Position position){
        Color colorPixel = new Color(image.getRGB(position.getX(), position.getY()));
        return colorPixel.getBlue();
    }

    public static int avgBlueComponetInCross(BufferedImage image, Position position, int crossSize){
        int blueComponent = getBlueComponent(image, position);
        int sumOfBlueComponentInCross = 0;
        for (int i = -crossSize; i <= crossSize; i++) {
            sumOfBlueComponentInCross += new Color(image.getRGB(position.getX() + i, position.getY())).getBlue();
            sumOfBlueComponentInCross += new Color(image.getRGB(position.getX(), position.getY() + i)).getBlue();
        }
        return (sumOfBlueComponentInCross - 2 * blueComponent) / (crossSize * 4);
    }
    public static int avgComponetInCross(BufferedImage image, Position position, int crossSize){
        double component = getBrightsForPixel(new Color(image.getRGB(position.getX(), position.getY())));
        int sumOfBlueComponentInCross = 0;
        for (int i = -crossSize; i <= crossSize; i++) {
            sumOfBlueComponentInCross += getBrightsForPixel(new Color(image.getRGB(position.getX() + i, position.getY())));
            sumOfBlueComponentInCross += getBrightsForPixel(new Color(image.getRGB(position.getX(), position.getY() + i)));
        }
        return (int)(sumOfBlueComponentInCross - 2 * component) / (crossSize * 4);
    }

    @SuppressWarnings("Duplicates")
    public static Position getPositionForSnakeWay(int blockNumber, int borderOffset) {
        Position result;
        int sizeFullMatrix = (int) Math.floor(Math.sqrt(blockNumber));
        if (sizeFullMatrix % 2 == 0) {
            if (sizeFullMatrix * sizeFullMatrix == blockNumber) {
                return new Position(borderOffset, sizeFullMatrix - 1 + borderOffset);
            }
            int remainingWay = (blockNumber - (sizeFullMatrix * sizeFullMatrix));
            int offsetX = remainingWay / (sizeFullMatrix + 2) == 0 ? remainingWay % (sizeFullMatrix + 2) : sizeFullMatrix + 1;
            int offsetY = sizeFullMatrix + 1 - (remainingWay / (sizeFullMatrix + 2)) * (remainingWay - offsetX);
            return new Position(offsetX - 1 + borderOffset, offsetY - 1 + borderOffset);
        } else {
            if (sizeFullMatrix * sizeFullMatrix == blockNumber) {
                return new Position(sizeFullMatrix - 1 + borderOffset, borderOffset);
            }
            int remainingWay = (blockNumber - (sizeFullMatrix * sizeFullMatrix));
            int offsetY = remainingWay / (sizeFullMatrix + 2) == 0 ? remainingWay % (sizeFullMatrix + 2) : sizeFullMatrix + 1;
            int offsetX = sizeFullMatrix + 1 - (remainingWay / (sizeFullMatrix + 2)) * (remainingWay - offsetY);
            return new Position(offsetX - 1 + borderOffset, offsetY - 1 + borderOffset);
        }
    }


}
