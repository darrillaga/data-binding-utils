package me.darrillaga.databinding.utils;

import android.content.Context;

public interface ErrorsMessageGenerator {

    String generateMessageError(Context context, Throwable throwable);
}
