package com.bederr.utils;

import android.content.Context;
import android.graphics.Typeface;

public class Util_Fonts {
    public static Typeface setPNACursivaLight(Context paramContext) {
        return Typeface.createFromAsset(paramContext.getAssets(), "fonts/pna_cursiva_light.otf");
    }

    public static Typeface setPNALight(Context paramContext) {
        return Typeface.createFromAsset(paramContext.getAssets(), "fonts/pna_light.otf");
    }

    public static Typeface setPNASemiBold(Context paramContext) {
        return Typeface.createFromAsset(paramContext.getAssets(), "fonts/pna_semi_bold.otf");
    }
}
