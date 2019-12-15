package com.mycompany.model.method;

import com.mycompany.model.entity.Position;
import com.mycompany.model.util.ImageUtils;

import java.awt.image.BufferedImage;
import java.util.Random;

public class CutterMethodWithKey {
    protected static final double SIGNAL_ENERGY_DEFAULT = 0.09;
    protected static final int MIN_STEP_DEFAULT = 100;
    protected static final int MAX_STEP_DEFAULT = 400;
    protected static final int CROSS_SIZE = 2;
    protected int minStep;
    protected int maxStep;
    protected double signalEnergy;

    public CutterMethodWithKey() {
        signalEnergy = SIGNAL_ENERGY_DEFAULT;
        minStep = MIN_STEP_DEFAULT;
        maxStep = MAX_STEP_DEFAULT;
    }

    public CutterMethodWithKey(double signalEnergy) {
        this();
        if (signalEnergy <= 1 && signalEnergy >= 0) {
            this.signalEnergy = signalEnergy;
        }
    }

    public CutterMethodWithKey(double signalEnergy, int minStep, int maxStep) {
        this(signalEnergy);
        this.minStep = minStep;
        this.maxStep = maxStep;
    }

    public BufferedImage hideMessage(BufferedImage image, String binaryMessage, String key) {
        int numberPixel = 1;
        Random generatorStep = new Random(key.hashCode());
        for (char bite : binaryMessage.toCharArray()) {
            Position pixelPosition = ImageUtils.getPositionForSnakeWay(numberPixel, CROSS_SIZE);
            int newRGB = ImageUtils.calculateNewBlueComponent(image, pixelPosition, signalEnergy, bite);
            image.setRGB(pixelPosition.getX(), pixelPosition.getY(),newRGB);
            numberPixel += generatorStep.nextInt(maxStep - minStep + 1) + minStep;
        }
        return image;
    }

    public String retrieveMessage(BufferedImage image, String key) {
        return retrieveMessage(image, key, minStep, maxStep);
    }

    @SuppressWarnings("Duplicates")
    public String retrieveMessage(BufferedImage image, String key, int minStep, int maxStep){
        StringBuffer binaryMessage = new StringBuffer();
        Random generatorStep = new Random(key.hashCode());
        int numberPixel = 1;
        int quantityContainers = (int) Math.pow(Math.min(image.getHeight(), image.getWidth()) - (2 * CROSS_SIZE), 2);
        while (numberPixel <= quantityContainers) {
            Position pixelPosition = ImageUtils.getPositionForSnakeWay(numberPixel, CROSS_SIZE);;
            int blueComponent = ImageUtils.getBlueComponent(image, pixelPosition);
            int avgBlueComponent = ImageUtils.avgBlueComponetInCross(image, pixelPosition, CROSS_SIZE);

            if ((blueComponent - avgBlueComponent) > 0)
                binaryMessage.append('1');
            else
                binaryMessage.append('0');
            numberPixel += generatorStep.nextInt(maxStep - minStep + 1) + minStep;
        }
        return binaryMessage.toString();
    }

}
