package com.yzy.lib_common.util;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;

import java.util.HashMap;

/**
 * Created by yzy on 2020/12/17.
 */
public class FontCache {
    private static HashMap<String, Typeface> fontCache = new HashMap<>();
    public static Typeface getTypeface(String fontname, Context context) {
        Typeface typeface = fontCache.get(fontname);
        if (typeface == null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O&&fontname.contains("Medium")) {
                    typeface = new Typeface.Builder(context.getAssets(), fontname).setWeight(600).build();
                }else {
                    typeface =Typeface.createFromAsset(context.getAssets(), fontname);
                }
            } catch (Exception e) {
                return null;
            }
            fontCache.put(fontname, typeface);
        }
        return typeface;
    }
}
