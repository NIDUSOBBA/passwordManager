package org.example.utile;

import org.example.Main;

import java.io.File;
import java.net.URISyntaxException;

public class PathDeterminant {

    // Определяем местоположение на диске
    public static String locationDisk(){
        // Определяем местоположение текущего класса на диске
        try {
            File jarDir = new File(Main.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI());

            // Если это JAR-файл, берем его родительскую папку.
            String appPath = jarDir.isDirectory() ? jarDir.getPath() : jarDir.getParent();

            return appPath + File.separator;
        } catch (URISyntaxException e) {
            System.err.println("Path Determinant exception: " + e.getMessage());
        }
        return "";
    }
}
