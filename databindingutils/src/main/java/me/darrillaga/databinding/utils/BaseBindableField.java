package me.darrillaga.databinding.utils;

import android.databinding.BaseObservable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BaseBindableField<T> extends BaseObservable
        implements Serializable, ObservableUtils.WrappedObject<T> {

    static final long serialVersionUID = 1L;
    private T mValue;

    private Map<ObservableUtils.OnPropertyChangedCallback, OnPropertyChangedCallback>
            mBindingMap = new HashMap<>();

    public BaseBindableField(T value) {
        mValue = value;
    }

    public BaseBindableField() {
    }

    public T get() {
        return mValue;
    }

    @Override
    public String toString() {
        return super.toString() + " value=" + mValue;
    }

    @Override
    public void addOnPropertyChangedCallback(ObservableUtils.OnPropertyChangedCallback callback) {
        removeOnPropertyChangedCallback(callback);

        OnPropertyChangedCallback observableOnPropertyChangedCallback =
                ObservableUtils.functionalOnPropertyChangedCallback(callback);

        mBindingMap.put(callback, observableOnPropertyChangedCallback);

        addOnPropertyChangedCallback(observableOnPropertyChangedCallback);
    }

    @Override
    public void removeOnPropertyChangedCallback(ObservableUtils.OnPropertyChangedCallback callback) {
        OnPropertyChangedCallback onPropertyChangedCallback = mBindingMap.get(callback);

        if (onPropertyChangedCallback == null) {
            return;
        }

        removeOnPropertyChangedCallback(onPropertyChangedCallback);
    }

    public void set(T value) {
        if (value == null && mValue == null) {
            return;
        }

        if (value == null || !value.equals(mValue)) {
            mValue = value;
            notifyChange();
        }
    }
}