package com.example.test.jvm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MintQ on 2018/6/28.
 */
public class regexTest {



    public static void main(String[] args) {

        String regex = "^\\d{11}"; // "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
        boolean matches = Pattern.matches(regex, "12334578997");
        System.out.println(matches);

    }

}
