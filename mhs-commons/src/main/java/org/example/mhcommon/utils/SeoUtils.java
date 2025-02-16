package org.example.mhcommon.utils;

public class SeoUtils {
    public static String generatedSeoId(String title,String id){
        if(title != null) {
            String seoId = title.length() > 200 ? title.substring(0,200) : title;
            seoId = StringUtil.textToUrl(title);
            return seoId + "-" +id;
        }else{
            return "application -"+id;
        }
    }
}
