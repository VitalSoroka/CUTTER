package com.mycompany.model.method;

import com.mycompany.model.entity.Position;
import com.mycompany.model.util.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CutterMethod {
    protected static final double SIGNAL_ENERGY_DEFAULT = 0.06;
    protected static final int STEP_DEFAULT = 20;
    protected static final int CROSS_SIZE = 2;
    protected int step;
    protected double signalEnergy;

    public CutterMethod() {
        signalEnergy = SIGNAL_ENERGY_DEFAULT;
        step = STEP_DEFAULT;
    }

    public CutterMethod(double signalEnergy) {
        this();
        if (signalEnergy <= 1 && signalEnergy >= 0) {
            this.signalEnergy = signalEnergy;
        }
    }

    public CutterMethod(double signalEnergy, int step) {
        this(signalEnergy);
        this.step = step;
    }

    public BufferedImage hideMessage(BufferedImage image, String binaryMessage) {
        int numberPixel = 1;
        for (char bite : binaryMessage.toCharArray()) {

            Position pixelPosition = ImageUtils.getPositionForSnakeWay(numberPixel, CROSS_SIZE);
            int newRGB = ImageUtils.calculateNewBlueComponent(image, pixelPosition, signalEnergy, bite);
            image.setRGB(pixelPosition.getX(), pixelPosition.getY(),newRGB);
            numberPixel += step;
        }
        return image;
    }

    public String retrieveMessage(BufferedImage image) {
        return retrieveMessage(image, step);
    }

    @SuppressWarnings("Duplicates")
    public String retrieveMessage(BufferedImage image, int step){
        StringBuffer binaryMessage = new StringBuffer();
        int numberPixel = 1;
        int quantityContainers = (int) Math.pow(Math.min(image.getHeight(), image.getWidth()) - (2 * CROSS_SIZE), 2);
        while (numberPixel <= quantityContainers) {
            Position pixelPosition = ImageUtils.getPositionForSnakeWay(numberPixel, CROSS_SIZE);
            int blueComponent = ImageUtils.getBlueComponent(image, pixelPosition);
            int avgBlueComponent = ImageUtils.avgBlueComponetInCross(image, pixelPosition, CROSS_SIZE);

            if ((blueComponent - avgBlueComponent) > 0)
                binaryMessage.append('1');
            else
                binaryMessage.append('0');
            numberPixel += step;
        }
        return binaryMessage.toString();
    }


    @SuppressWarnings("Duplicates")
    public String attack(BufferedImage image, int step) {
        StringBuffer binaryMessage = new StringBuffer();
        int numberPixel = 1;
        int quantityContainers = (int) Math.pow(Math.min(image.getHeight(), image.getWidth()) - (2 * CROSS_SIZE), 2);
        while (numberPixel <= quantityContainers) {
            Position pixelPosition = ImageUtils.getPositionForSnakeWay(numberPixel, CROSS_SIZE);
            Color colorPixel = new Color(image.getRGB(pixelPosition.getX(), pixelPosition.getY()));
            int blueComponent = colorPixel.getBlue();
            int sumOfBlueComponentInCross = 0;
            for (int i = -CROSS_SIZE; i <= CROSS_SIZE; i++) {
                sumOfBlueComponentInCross += new Color(image.getRGB(pixelPosition.getX() + i, pixelPosition.getY())).getBlue();
                sumOfBlueComponentInCross += new Color(image.getRGB(pixelPosition.getX(), pixelPosition.getY() + i)).getBlue();
            }
            int avgBlueComponent = (sumOfBlueComponentInCross - 2 * blueComponent) / (CROSS_SIZE * 4);

            if ((blueComponent - avgBlueComponent) > (signalEnergy * 0.05 *  ImageUtils.getBrightsForPixel(colorPixel)))
                binaryMessage.append('1');
            if ((avgBlueComponent - blueComponent) > (signalEnergy * 0.05 *  ImageUtils.getBrightsForPixel(colorPixel)))
                binaryMessage.append('0');
            numberPixel += step;
        }
        return binaryMessage.toString();

    }

}
