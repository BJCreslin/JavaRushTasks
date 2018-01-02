package com.javarush.task.task22.task2208;

import javax.swing.text.html.parser.Entity;
import java.util.HashMap;
import java.util.Map;

/* 
Формируем WHERE
*/
public class Solution {
    public static void main(String[] args) {
        Map<String,String>map=new HashMap<>();
        map.put("name", "Ivanov");
        map.put("country", "Ukraine");
        map.put("city", "Kiev");
        map.put("age", null);
        System.out.println(getQuery(map));

    }

    public static String getQuery(Map<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean isNotFirst = false;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getValue() != null) {
                if (isNotFirst){ stringBuilder.append(" and ");}
                stringBuilder.append(entry.getKey());
                stringBuilder.append(" = '");
                stringBuilder.append(entry.getValue());
                stringBuilder.append("'");
                isNotFirst = true;}
            }
            return stringBuilder.toString();
        }

    }

