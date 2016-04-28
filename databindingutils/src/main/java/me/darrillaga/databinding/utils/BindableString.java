package me.darrillaga.databinding.utils;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import me.darrillaga.databindingutils.R;
import rx.Observable;

public class BindableString extends BaseBindableField<String> {

    public BindableString(String value) {
        super(value);
    }

    public BindableString() {
        this("");
    }

    public static Observable<String> createObservableFromDataBinding(BindableString field) {
        return Observable.create(
                ObservableUtils.createOnSubscribe(field)
        );
    }

    @BindingConversion
    public static String convertBindableToString(BindableString bindableString) {
        return bindableString.get();
    }

    @BindingAdapter({"app:binding"})
    public static void bindEditText(EditText view, BindableString value) {
        if (view.getTag(R.id.binded) == null) {
            view.setTag(R.id.binded, true);

            view.addTextChangedListener(
                    new FunctionalTextWatcher().onChanged(
                            (charSequence, start, before, count) -> value.set(charSequence.toString())
                    )
            );
        }

        String newValue = value.get();

        if (!view.getText().toString().equals(newValue)) {
            view.setText(newValue);
        }
    }

    public static void clearString(BindableString bindableString) {
        bindableString.set(null);
    }

    @BindingAdapter({"app:binding"})
    public static void bindSpinner(Spinner view, BindableString value) {
        if (view.getTag(R.id.binded) == null) {
            view.setTag(R.id.binded, true);

            view.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            value.set(adapterView.getItemAtPosition(i).toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            value.set(adapterView.getItemAtPosition(0).toString());
                        }
                    }
            );
        }
    }
}