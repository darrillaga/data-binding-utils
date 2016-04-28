package me.darrillaga.databinding.utils;

import android.content.res.Resources;
import android.util.TypedValue;

public class ResourceUtils {

    public static float getFloatFromResources(Resources resources, int id) {
        TypedValue outValue = new TypedValue();
        resources.getValue(id, outValue, true);
        return outValue.getFloat();
    }
}
