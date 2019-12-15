package com.mycompany.model.method;

import com.mycompany.model.entity.Position;
import com.mycompany.model.util.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CutterMethod {
    private static final double SIGNAL_ENERGY_DEFAULT = 0.31;
    private static final int STEP_DEFAULT = 10;
    private static final int CROSS_SIZE = 2;
    private int step;
    private double signalEnergy;

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
            Color colorPixel = new Color(image.getRGB(pixelPosition.getX(), pixelPosition.getY()));
            int blueComponent = colorPixel.getBlue();

            int newBlueComponent = blueComponent + (int) (signalEnergy * ImageUtils.getBrightsForPixel(colorPixel) * (2 * (bite - 48) - 1) );

            colorPixel = new Color(colorPixel.getRed(), colorPixel.getGreen(), newBlueComponent);
            image.setRGB(pixelPosition.getX(), pixelPosition.getY(), colorPixel.getRGB());
            numberPixel += step;
        }
        return image;
    }

    public String retrieveMessage(BufferedImage image) {
        return retrieveMessage(image, 1);
    }

    public String retrieveMessage(BufferedImage image, int step) {
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

            if ((blueComponent - avgBlueComponent) > (signalEnergy * 0.6 *  ImageUtils.getBrightsForPixel(colorPixel)))
                binaryMessage.append('1');
            if ((avgBlueComponent - blueComponent) > (signalEnergy * 0.6 *  ImageUtils.getBrightsForPixel(colorPixel)))
                binaryMessage.append('0');
            numberPixel += step;
        }
        return binaryMessage.toString();

    }

}
