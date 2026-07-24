package org.example;

import org.example.controller.Launcher;

public class Main {
    public static void main(String[] args) {
        try {
            Launcher.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}