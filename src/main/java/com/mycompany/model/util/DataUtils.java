package com.mycompany.model.util;

import com.mycompany.model.service.impl.ImageServiceImpl;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DataUtils {

    @Deprecated
    static class ImageProperties {
        public int[] red = null;
        public int[] green = null;
        public int[] blue = null;
    }

    public static ImageProperties getAndSaveImgData(BufferedImage image) {
        ImageProperties properties = new ImageProperties();
        properties.red = new int[256];
        properties.green = new int[256];
        properties.blue = new int[256];

        for (int i = 0; i < 300/*image.getWidth()*/; i++) {
            for (int j = 0; j < 300/*image.getHeight()*/; j++) {
                Color color = new Color(image.getRGB(i, j));
                properties.red[color.getRed()] += 1;
                properties.green[color.getGreen()] += 1;
                properties.blue[color.getBlue()] += 1;
            }
        }

        return properties;
    }

    public static void savePropertiesToCSV(File file, ImageProperties properties) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("");
            for (int i = 0; i < properties.red.length; i++) {
                writer.append(i + ";" + properties.red[i] + ";" + properties.green[i] + ";" + properties.blue[i]);
                writer.append((char) 0x0A);
            }
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static File[] getFilesInFolder(String pathToFolder) {
        File folder = new File(pathToFolder);
        return folder.listFiles();
    }

    public static void calculateDataForImage(String pathToImages, String pathToData){
        File[] imageFiles = getFilesInFolder(pathToImages);
        for (File imageFile : imageFiles) {
            String name = imageFile.getName().split("\\.")[0];
            File dataFile = new File(pathToData + name + ".csv");
            BufferedImage image = new ImageServiceImpl().loadImage(pathToImages + imageFile.getName());
            savePropertiesToCSV(dataFile, getAndSaveImgData(image));
        }
    }


}
