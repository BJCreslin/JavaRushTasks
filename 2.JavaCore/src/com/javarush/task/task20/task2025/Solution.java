package com.javarush.task.task20.task2025;


import java.util.ArrayList;

/*
Алгоритмы-числа
*/
public class Solution {
    public static long[] getNumbers(long N) {
        ArrayList<Long> arrayOut=new ArrayList<>();
        int M=defineLenghtLong(N);
        for (long i = 0L; i < N; i++) {
        char []numbersPart=getNumbersPart(i);
        long number2=0l;
            for (int j = 0; j <numbersPart.length ; j++) {
                number2+=Math.pow((int)numbersPart[j],numbersPart.length);
            }
        if (number2==i){arrayOut.add(number2);}
        }

        Object[] result = arrayOut.toArray();
        return result;
    }

    private static char[] getNumbersPart(long i) {
       return  Long.toString(i).toCharArray();

    }

    private static int defineLenghtLong(long n) {
       return Long.toString(n).length();
    }

    public static void main(String[] args) {

    }
}
