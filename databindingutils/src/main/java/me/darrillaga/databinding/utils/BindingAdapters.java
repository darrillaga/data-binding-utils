package me.darrillaga.databinding.utils;

import android.databinding.BindingAdapter;
import android.support.design.widget.TextInputLayout;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class BindingAdapters {

    @BindingAdapter(value = {"app:error", "app:errorMessageGenerator"}, requireAll = false)
    public static void setError(TextInputLayout textInputLayout, Throwable error, ErrorsMessageGenerator errorsMessageGenerator) {
        if (error == null) {
            textInputLayout.setError(null);
        } else {
            textInputLayout.setError(
                    errorsMessageGenerator.generateMessageError(textInputLayout.getContext(), error)
            );
        }
    }

    @BindingAdapter("clickableLinks")
    public static void showTextViewLinks(TextView textView, boolean show) {
        if (!show) {
            return;
        }

        // Force use of the link movement method to support an anchor tag different from the display text
        // @see http://stackoverflow.com/questions/2734270/how-do-i-make-links-in-a-textview-clickable
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
