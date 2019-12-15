package com.mycompany.model.method;

import com.mycompany.model.entity.Position;
import com.mycompany.model.util.ImageUtils;

import java.awt.image.BufferedImage;

public class CutterMethodWithBlocks extends CutterMethod{
    protected static final int WIDTH_BLOCK_DEFAULT = 5;
    protected static final int HEIGHT_BLOCK_DEFAULT = 5;
    protected int widthBlock = WIDTH_BLOCK_DEFAULT;
    protected int heightBlock = HEIGHT_BLOCK_DEFAULT;

    public CutterMethodWithBlocks(){
        super();
    }

    public CutterMethodWithBlocks(double signalEnergy) {
        super(signalEnergy);
    }

    public CutterMethodWithBlocks(double signalEnergy, int widthBlock, int heightBlock) {
        super(signalEnergy);
        this.widthBlock = widthBlock;
        this.heightBlock = heightBlock;
    }

    public CutterMethodWithBlocks(double signalEnergy, int widthBlock, int heightBlock, int step){
        super(signalEnergy, step);
        this.widthBlock = widthBlock;
        this.heightBlock = heightBlock;
    }

    public BufferedImage hideMessage(BufferedImage image, String binaryMessage) {
        int numberBlock = 1;
        for (char bite : binaryMessage.toCharArray()) {

            Position pixelPosition = ImageUtils.getPositionForSnakeWay(numberBlock, widthBlock, heightBlock);
            pixelPosition.setX(pixelPosition.getX() + (widthBlock / 2 + 1));
            pixelPosition.setY(pixelPosition.getY() + (heightBlock/ 2 + 1));
            int newRGB = ImageUtils.calculateNewBlueComponent(image, pixelPosition, signalEnergy, bite);
            image.setRGB(pixelPosition.getX(), pixelPosition.getY(),newRGB);
            numberBlock += step;
        }
        return image;
    }

    public String retrieveMessage(BufferedImage image) {
        return retrieveMessage(image, step);
    }

    @SuppressWarnings("Duplicates")
    public String retrieveMessage(BufferedImage image, int step){
        StringBuffer binaryMessage = new StringBuffer();
        int numberBlock = 1;
        int quantityContainers = (int) Math.pow(Math.min(Math.floor(image.getHeight()/heightBlock), Math.floor(image.getWidth()/widthBlock)), 2);
        while (numberBlock <= quantityContainers) {
            Position pixelPosition = ImageUtils.getPositionForSnakeWay(numberBlock, widthBlock, heightBlock);
            pixelPosition.setX(pixelPosition.getX() + (widthBlock / 2 + 1));
            pixelPosition.setY(pixelPosition.getY() + (heightBlock/ 2 + 1));
            int blueComponent = ImageUtils.getBlueComponent(image, pixelPosition);
            int avgBlueComponent = ImageUtils.avgBlueComponetInCross(image, pixelPosition, CROSS_SIZE);


            if (blueComponent > avgBlueComponent)
                binaryMessage.append('1');
            else
                binaryMessage.append('0');
            numberBlock += step;
        }
        return binaryMessage.toString();
    }


}
