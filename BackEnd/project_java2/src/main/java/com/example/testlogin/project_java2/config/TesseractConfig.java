package com.example.testlogin.project_java2.config;


import com.example.testlogin.project_java2.ProjectJava2Application;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class TesseractConfig {

    //Hằng số cho OCR Engine Mode (OEM)
    public static final int OEM_TESSERACT_ONLY = 1;

    //Hằng số cho OCR Page Segmentation Mode (PSM)
    public static final int PSM_AUTO = 3;
    @Value("${app.tesseract_language}")
    private  String TESSERACT_LANGUAGE;

    @Bean
    public Tesseract tesseract() {

        Tesseract tesseract = new Tesseract();

        tesseract.setLanguage(this.TESSERACT_LANGUAGE);
        tesseract.setDatapath(ProjectJava2Application.getTessDataPath());
        tesseract.setOcrEngineMode(OEM_TESSERACT_ONLY);
        tesseract.setPageSegMode(PSM_AUTO);
        // Đặt độ phân giải cho Tesseract
        tesseract.setTessVariable("user_defined_dpi", "300");

        return tesseract;
    }
}
