package com.example.testlogin.project_java2.service;

import net.sourceforge.tess4j.TesseractException;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;

public interface OcrService {


    String getImageString(File file) throws TesseractException;

    // Tiền xử lý ảnh
    BufferedImage preprocessImage(BufferedImage image);

    // Làm sáng ảnh
    BufferedImage brightenImage(BufferedImage image);

    // Đảo ảnh sang ảnh đen trắng
    BufferedImage convertToGrayscale(BufferedImage image);
}
