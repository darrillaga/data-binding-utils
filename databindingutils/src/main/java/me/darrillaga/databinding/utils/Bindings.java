package me.darrillaga.databinding.utils;

public class Bindings {

    public static <T, R> T attachChanges(R fieldToListen, T field) {
        return field;
    }

    public static <R> boolean attachChangesBoolean(R fieldToListen, boolean field) {
        return field;
    }
}
