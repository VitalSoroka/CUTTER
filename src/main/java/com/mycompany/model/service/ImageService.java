package com.mycompany.model.service;

import com.mycompany.model.entity.ImgFormat;

import java.awt.image.BufferedImage;
import java.io.File;

public interface ImageService {
    BufferedImage loadImage(String path);

    boolean saveImage(BufferedImage image, String name, ImgFormat format, String path);
}
