package com.example.testlogin.project_java2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class ProjectJava2Application {



    public static void unzip(InputStream inS, Path targetDir) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(inS)) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                Path newPath = targetDir.resolve(zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    Files.createDirectories(newPath);
                } else {
                    Files.createDirectories(newPath.getParent());
                    Files.copy(zis, newPath);
                }
            }
        }
    }


    public static String getTessDataPath() {
        URL res = ProjectJava2Application.class.getClassLoader().getResource("tesseract-ocr/tessdata");
        if (res != null) {
            try {
                URI uri = res.toURI();
                if (uri.getScheme().equals("file")) {
                    return Paths.get(uri).toString();
                } else {
                    Path tempDir = Files.createTempDirectory("tessdata");
                    try (InputStream is = res.openStream()) {
                        unzip(is, tempDir);
                    }
                    return tempDir.toString();
                }
            } catch (URISyntaxException | IOException e) {
                throw new RuntimeException("Không thể chuyển đổi URL thành URI", e);
            }
        } else {
            throw new RuntimeException("Không tìm thấy thư mục tesseract-ocr/tessdata");
        }
    }


    public static void main(String[] args) {


        SpringApplication.run(ProjectJava2Application.class, args);
        System.out.println("Running...");
        System.out.println(getTessDataPath());

    }



}
