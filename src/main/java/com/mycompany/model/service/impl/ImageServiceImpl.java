package com.mycompany.model.service.impl;

import com.mycompany.model.entity.ImgFormat;
import com.mycompany.model.service.ImageService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageServiceImpl implements ImageService {

    public BufferedImage loadImage(File file) {
        BufferedImage img = null;
        try {
            System.out.println(file.getAbsoluteFile());
            img = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public boolean saveImage(BufferedImage image, String name, ImgFormat format, String path) {
        try {
            File outputfile = new File(path + name + format.getFormat());
            ImageIO.write(image, format.getFormat(), outputfile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
