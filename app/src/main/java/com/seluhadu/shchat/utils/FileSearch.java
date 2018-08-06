package com.seluhadu.shchat.utils;

import java.io.File;
import java.util.ArrayList;

public class FileSearch {
    public ArrayList<String> getDirectries(String directory) {
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listFile = file.listFiles();
        for (File aListFile : listFile) {
            if (aListFile.isDirectory()) {
                pathArray.add(aListFile.getAbsolutePath());
            }
        }
        return pathArray;
    }

    public ArrayList<String> getFilePath(String directory) {
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listFile = file.listFiles();
        for (File aListFile : listFile) {
            if (aListFile.isFile()) {
                pathArray.add(aListFile.getAbsolutePath());
            }
        }
        return pathArray;
    }
}
