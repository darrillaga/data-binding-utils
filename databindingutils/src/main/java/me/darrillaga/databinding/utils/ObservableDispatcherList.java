package me.darrillaga.databinding.utils;

import android.databinding.ObservableList;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ObservableDispatcherList<T> implements ObservableList<T> {

    public static <T> ObservableDispatcherList<T> createMainThreadObservableList(ObservableList<T> list) {
        return new ObservableDispatcherList<>(list, new Handler(Looper.getMainLooper())::post);
    }

    private ObservableList<T> mList;
    private List<T> mSynchronizedList;
    private DispatcherWrapper mDispatcherWrapper;

    public ObservableDispatcherList(ObservableList<T> list, Dispatcher dispatcher) {
        mList = list;
        mSynchronizedList = Collections.synchronizedList(mList);
        mDispatcherWrapper = new DispatcherWrapper(dispatcher);
    }

    @Override
    public void addOnListChangedCallback(OnListChangedCallback<? extends ObservableList<T>> onListChangedCallback) {
        mList.addOnListChangedCallback(onListChangedCallback);
    }

    @Override
    public void removeOnListChangedCallback(OnListChangedCallback<? extends ObservableList<T>> onListChangedCallback) {
        mList.addOnListChangedCallback(onListChangedCallback);
    }

    @Override
    public void add(int location, T object) {
        mDispatcherWrapper.execute(() -> mSynchronizedList.add(location, object));
    }

    @Override
    public boolean add(T object) {
        mDispatcherWrapper.execute(() -> mSynchronizedList.add(object));
        return true;
    }

    @Override
    public boolean addAll(int location, Collection<? extends T> collection) {
        mDispatcherWrapper.execute(() -> mSynchronizedList.addAll(location, collection));
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        mDispatcherWrapper.execute(() -> mSynchronizedList.addAll(collection));
        return true;
    }

    @Override
    public void clear() {
        mDispatcherWrapper.execute(() -> mSynchronizedList.clear());
    }

    @Override
    public boolean contains(Object object) {
        return mSynchronizedList.contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return mSynchronizedList.containsAll(collection);
    }

    @Override
    public T get(int location) {
        return mSynchronizedList.get(location);
    }

    @Override
    public int indexOf(Object object) {
        return mSynchronizedList.indexOf(object);
    }

    @Override
    public boolean isEmpty() {
        return mSynchronizedList.isEmpty();
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return mSynchronizedList.iterator();
    }

    @Override
    public int lastIndexOf(Object object) {
        return mSynchronizedList.lastIndexOf(object);
    }

    @Override
    public ListIterator<T> listIterator() {
        return mSynchronizedList.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator(int location) {
        return mSynchronizedList.listIterator(location);
    }

    @Override
    public T remove(int location) {
        T removed = get(location);
        mDispatcherWrapper.execute(() -> mSynchronizedList.remove(location));
        return removed;
    }

    @Override
    public boolean remove(Object object) {
        boolean exists = contains(object);
        mDispatcherWrapper.execute(() -> mSynchronizedList.remove(object));
        return exists;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        mDispatcherWrapper.execute(() -> mSynchronizedList.removeAll(collection));
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        mDispatcherWrapper.execute(() -> mSynchronizedList.retainAll(collection));
        return true;
    }

    @Override
    public T set(int location, T object) {
        mDispatcherWrapper.execute(() -> mSynchronizedList.set(location, object));
        return null;
    }

    @Override
    public int size() {
        return mSynchronizedList.size();
    }

    @NonNull
    @Override
    public List<T> subList(int start, int end) {
        return mSynchronizedList.subList(start, end);
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return mSynchronizedList.toArray();
    }

    @NonNull
    @Override
    public <T1> T1[] toArray(T1[] array) {
        return mSynchronizedList.toArray(array);
    }
}
