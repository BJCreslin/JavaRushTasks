package com.javarush.task.task23.task2305;

/* 
Inner
*/
public class Solution {
    public InnerClass[] innerClasses = new InnerClass[2];

    public static class InnerClass {
    }

    public static Solution[] getTwoSolutions() {
        Solution [] solutionArray= new Solution[2];
        for (int i = 0; i <solutionArray.length ; i++) {
            solutionArray[i]=new Solution();
            for (int j = 0; j <solutionArray[i].innerClasses.length ; j++) {
                solutionArray[i].innerClasses[j]=new InnerClass();
            }

        }
        return solutionArray;
    }

    public static void main(String[] args) {

    }
}
