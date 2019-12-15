package com.mycompany.model.service;

import com.mycompany.model.entity.ImgFormat;

import java.awt.image.BufferedImage;
import java.io.File;

public interface ImageService {
    BufferedImage loadImage(File file);

    boolean saveImage(BufferedImage image, String name, ImgFormat format, String path);
}
