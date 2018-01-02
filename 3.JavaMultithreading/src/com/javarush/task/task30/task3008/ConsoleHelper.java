package com.javarush.task.task30.task3008;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//ConsoleHelper - вспомогательный класс, для чтения или записи в консоль.
public class ConsoleHelper {
    private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    static public void writeMessage(String message) {
        System.out.println(message);
    }

    static public String readString() {
        boolean isNorm = true;
        String text2Return="";
        do {
            try {
                text2Return= bufferedReader.readLine();
                isNorm = true;
            } catch (IOException e) {
                writeMessage("Произошла ошибка при попытке ввода текста. Попробуйте еще раз.");
                isNorm = false;
            }}
            while (!isNorm) ;
        return text2Return;
        }
    static public int readInt() {
        boolean isNorm = true;
        int text2Return=0;
        do {
            try {
                text2Return= Integer.parseInt(readString());
                isNorm = true;
            }
            catch (NumberFormatException e){
                writeMessage("Произошла ошибка при попытке ввода числа. Попробуйте еще раз.");
                isNorm = false;
            }}
        while (!isNorm) ;
        return text2Return;
    }
    }

