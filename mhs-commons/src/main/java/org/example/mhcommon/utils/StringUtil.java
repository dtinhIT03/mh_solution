package org.example.mhcommon.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtil {
    public static String removeAccent(String s){
        if(org.apache.commons.lang3.StringUtils.isEmpty(s)) return "";
        String temp = Normalizer.normalize(s,Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("")
                .replaceAll("đ", "d")
                .replaceAll("Đ", "D")
                .replaceAll("`", "")
                .replaceAll("´", "")
                .replaceAll("\\^", "");
    }
    public static String textToUrl(String text){
        text = removeAccent(text);
        text = text.toLowerCase();
        text = text.replaceAll("[^a-zA-Z0-9 ]","");
        text = text.replaceAll(" ","-");
        return text;
    }
}
