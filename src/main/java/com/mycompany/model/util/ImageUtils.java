package com.mycompany.model.util;

import com.mycompany.model.entity.Position;

import java.awt.*;

public class ImageUtils {
    private static final double RED_WEIGHT = 0.299d;
    private static final double GREEN_WEIGHT = 0.587d;
    private static final double BLUE_WEIGHT = 0.114d;

    public static double getBrightsForPixel(Color pixel) {
        return RED_WEIGHT * pixel.getRed() + GREEN_WEIGHT * pixel.getGreen() + BLUE_WEIGHT * pixel.getBlue();
    }

    public Position getPositionForSnakeWay(int blockNumber, int width, int heigh){
        Position position = getPositionForSnakeWay(blockNumber);
        return new Position(position.getX() * width, position.getY() * heigh);
    }

    @SuppressWarnings("Duplicates")
    public Position getPositionForSnakeWay(int blockNumber) {
        Position result;
        int sizeFullMatrix = (int) Math.floor(Math.sqrt(blockNumber));
        if (sizeFullMatrix % 2 == 0) {
            if (sizeFullMatrix * sizeFullMatrix == blockNumber) {
                return new Position(0, sizeFullMatrix - 1);
            }
            int remainingWay = (blockNumber - (sizeFullMatrix * sizeFullMatrix));
            int offsetX = remainingWay / (sizeFullMatrix + 2) == 0 ? remainingWay % (sizeFullMatrix + 2) : sizeFullMatrix + 1;
            int offsetY = sizeFullMatrix + 1 - (remainingWay / (sizeFullMatrix + 2)) * (remainingWay - offsetX);
            return new Position(offsetX - 1, offsetY - 1);
        } else {
            if (sizeFullMatrix * sizeFullMatrix == blockNumber) {
                return new Position(sizeFullMatrix - 1, 0);
            }
            int remainingWay = (blockNumber - (sizeFullMatrix * sizeFullMatrix));
            int offsetY = remainingWay / (sizeFullMatrix + 2) == 0 ? remainingWay % (sizeFullMatrix + 2) : sizeFullMatrix + 1;
            int offsetX = sizeFullMatrix + 1 - (remainingWay / (sizeFullMatrix + 2)) * (remainingWay - offsetY);
            return new Position(offsetX - 1, offsetY - 1);
        }
    }


}
