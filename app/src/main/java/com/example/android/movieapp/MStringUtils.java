package com.example.android.movieapp;

/**
 * Created by archit.m on 28/11/15.
 */
public class MStringUtils {
    public static boolean isValid(String content){
        if(content==null || content.equals("null") || content.equals("")){
            return false;
        }
        return true;
    }
}
