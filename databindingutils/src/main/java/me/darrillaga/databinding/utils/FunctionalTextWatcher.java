package me.darrillaga.databinding.utils;

import android.text.Editable;
import android.text.TextWatcher;

public class FunctionalTextWatcher implements TextWatcher {

    private OnTextChangedListener mOnTextChangedListener = (s, start, before, count) -> {};
    private OnBeforeTextChangedListener mOnBeforeTextChangedListener = (s, start, count, after) -> {};
    private OnAfterTextChangedListener mOnAfterTextChangedListener = s -> {};

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        mOnBeforeTextChangedListener.beforeTextChanged(s, start, count, after);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mOnTextChangedListener.onTextChanged(s, start, before, count);
    }

    @Override
    public void afterTextChanged(Editable s) {
        mOnAfterTextChangedListener.afterTextChanged(s);
    }

    public FunctionalTextWatcher onBeforeChanged(OnBeforeTextChangedListener onBeforeTextChangedListener) {
        mOnBeforeTextChangedListener = onBeforeTextChangedListener;

        return this;
    }

    public FunctionalTextWatcher onChanged(OnTextChangedListener onTextChangedListener) {
        mOnTextChangedListener = onTextChangedListener;

        return this;
    }

    public FunctionalTextWatcher onAfterChanged(OnAfterTextChangedListener onAfterTextChangedListener) {
        mOnAfterTextChangedListener = onAfterTextChangedListener;

        return this;
    }

    public interface OnTextChangedListener {
        void onTextChanged(CharSequence s, int start, int before, int count);
    }

    public interface OnAfterTextChangedListener {
        void afterTextChanged(Editable s);
    }

    public interface OnBeforeTextChangedListener {
        void beforeTextChanged(CharSequence s, int start, int count, int after);
    }
}
