package com.javarush.task.task22.task2209;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

/*
Составить цепочку слов
*/
public class Solution {
    public static void main(String[] args) throws IOException {
        //...
//
        BufferedReader bufferedReader2=new BufferedReader(new InputStreamReader(System.in));
//        String fileName = scanner.nextLine();
        String fileName = bufferedReader2.readLine();
        String strFromFile;
        String[] strArray=null;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))
      ) {
            strFromFile = bufferedReader.readLine();
            if (strFromFile!=null){
                strArray=strFromFile.split(" ");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder result = getLine(strArray);
        System.out.println(result.toString());
    }

    public static StringBuilder getLine(String... words) {
        if (words==null) return new StringBuilder();
        if(words.length<1)return new StringBuilder();
        List<String> listWord = Arrays.asList(words);
        listWord.sort((x1,x2)->x1.compareToIgnoreCase(x2));
        StringBuilder result = new StringBuilder(listWord.get(0));
        listWord.remove(0);
        boolean toBe = true;

        while (toBe) {
            String lastSymbol = result.substring(result.length() - 1).toLowerCase();
            for (String string : listWord) {
                if (string.substring(0, 1).equals(lastSymbol)) {
                    result.append(" ");
                    result.append(string);
                    listWord.remove(string);
                    break;
                }
            }
            toBe = false;
        }
        for (String string:listWord){
            result.append(" ");
            result.append(string);
        }

        return result;
    }
}
