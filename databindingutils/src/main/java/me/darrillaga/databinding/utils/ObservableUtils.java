package me.darrillaga.databinding.utils;

import android.databinding.Observable;
import android.databinding.ObservableField;

import java.util.HashMap;
import java.util.Map;

import rx.subjects.BehaviorSubject;

public class ObservableUtils {

    /**
     * Implements the {@link Observable.OnPropertyChangedCallback} class redirecting calls
     * to an {@link OnPropertyChangedCallback}
     */
    public static Observable.OnPropertyChangedCallback
        functionalOnPropertyChangedCallback(OnPropertyChangedCallback onPropertyChangedCallback) {

        return new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                onPropertyChangedCallback.onPropertyChanged(sender, propertyId);
            }
        };
    }

    /**
     * Wraps an android databinding {@link ObservableField} into a generic {@link WrappedObject}
     * A wrapped object will be created using an android databinding observable field
     * @param field observable field to be wired with the observable
     * @param <T> any object observed by field
     * @return the observable associated with the field
     */
    public static <T> WrappedObject<T> wrap(ObservableField<T> field) {
        return new WrappedObject<T>() {

            private Map<OnPropertyChangedCallback, Observable.OnPropertyChangedCallback>
                    mBindingMap = new HashMap<>();

            @Override
            public void set(T vale) {
                field.set(vale);
            }

            @Override
            public T get() {
                return field.get();
            }

            @Override
            public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                removeOnPropertyChangedCallback(callback);

                Observable.OnPropertyChangedCallback observableOnPropertyChangedCallback =
                        functionalOnPropertyChangedCallback(callback);

                mBindingMap.put(callback, observableOnPropertyChangedCallback);

                field.addOnPropertyChangedCallback(observableOnPropertyChangedCallback);
            }

            @Override
            public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                Observable.OnPropertyChangedCallback onPropertyChangedCallback = mBindingMap.get(callback);

                if (onPropertyChangedCallback == null) {
                    return;
                }

                field.removeOnPropertyChangedCallback(onPropertyChangedCallback);
            }
        };
    };

    /**
     * Create a bridge between a WrappedObject and Rx Observable triggering onNext events
     * when the WrappedObject change listener callback fire changes.
     * Be aware that this method creates one callback per subscription and never completes
     * @param field wrapped object to be wired with the observable
     * @param <T> any object observed by field
     * @return the observable associated with the field
     */
    public static <T> rx.Observable.OnSubscribe<T> createOnSubscribe(WrappedObject<T> field) {
        return subscriber -> {
            field.addOnPropertyChangedCallback(
                    (sender, propertyId) -> subscriber.onNext(field.get())
            );

            subscriber.onNext(field.get());
        };
    }

    /**
     * Create a bridge between a WrappedObject and Rx Observable triggering onNext events,
     * when the WrappedObject change listener callback fire changes.
     * Be aware that completing via {@param stopObservable} is the unique way to finish the internal
     * listeners
     * @param field wrapped object to be wired with the observable
     * @param <T> any object observed by field
     * @param stopObservable and observable used to complete the sequence, when the observable triggers onNext
     *                       the source observable completes
     * @return the observable associated with the field
     */
    public static <T> rx.Observable<T> rxObservableField(WrappedObject<T> field, rx.Observable stopObservable) {
        BehaviorSubject<T> behaviorSubject = BehaviorSubject.create(field.get());

        OnPropertyChangedCallback onPropertyChangedCallback = (sender, propertyId) -> {
            try {
                behaviorSubject.onNext(field.get());
            } catch (Throwable e) {
                behaviorSubject.onError(e);
            }
        };

        field.addOnPropertyChangedCallback(onPropertyChangedCallback);

        rx.Observable<T> result = behaviorSubject;

        if (stopObservable != null) {
            result = result.takeUntil(stopObservable);
        }

        return result.doOnCompleted(() -> field.removeOnPropertyChangedCallback(onPropertyChangedCallback));
    }

    /**
     * @see #rxObservableField(WrappedObject, rx.Observable)
     * @param field wrapped object to be wired with the observable
     * @param <T> any object observed by field
     * @return the observable associated with the field
     */
    public static <T> rx.Observable<T> rxObservableField(WrappedObject<T> field) {
        return rxObservableField(field, null);
    }

    public interface OnPropertyChangedCallback {
        void onPropertyChanged(Observable sender, int propertyId);
    }

    public interface WrappedObject<T> {
        void set(T vale);
        T get();
        void addOnPropertyChangedCallback(OnPropertyChangedCallback callback);
        void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback);
    }

    public static <T> boolean isNotNull(T object) {
        return object != null;
    }
}
