package com.javarush.task.task31.task3105;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/* 
Добавление файла в архив
*/
public class Solution {
    public static void main(String[] args) throws IOException {
        ArrayList<ZipEntry> arrayZip = new ArrayList<>();
        if (args.length > 1) {
            FileInputStream fileInputStream = new FileInputStream(args[1]);
            ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
            while (zipInputStream.available() > 0) {
                arrayZip.add(zipInputStream.getNextEntry());
            }
            FileInputStream fileInputStream1 = new FileInputStream(args[0]);
            ZipInputStream zipInputStream1 = new ZipInputStream(fileInputStream1);
            arrayZip.add(zipInputStream1.getNextEntry());

            FileOutputStream fileOutputStream = new FileOutputStream(args[1]);
            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

            for (ZipEntry zipEntry : arrayZip) {
                zipOutputStream.putNextEntry(zipEntry);

            }
            zipInputStream.close();
            zipInputStream1.close();
            zipOutputStream.flush();
            zipOutputStream.close();

        }
    }
}
