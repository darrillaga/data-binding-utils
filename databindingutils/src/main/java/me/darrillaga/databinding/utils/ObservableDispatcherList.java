package me.darrillaga.databinding.utils;

import android.databinding.ObservableList;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ObservableDispatcherList<T> implements ObservableList<T> {

    public static <T> ObservableDispatcherList<T> createMainThreadObservableList(ObservableList<T> list) {
        return new ObservableDispatcherList<>(list, new Handler(Looper.getMainLooper())::post);
    }

    private ObservableList<T> mList;
    private DispatcherWrapper mDispatcherWrapper;

    public ObservableDispatcherList(ObservableList<T> list, Dispatcher dispatcher) {
        mList = list;
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
        mDispatcherWrapper.execute(() -> mList.add(location, object));
    }

    @Override
    public boolean add(T object) {
        mDispatcherWrapper.execute(() -> mList.add(object));
        return true;
    }

    @Override
    public boolean addAll(int location, Collection<? extends T> collection) {
        mDispatcherWrapper.execute(() -> mList.addAll(location, collection));
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        mDispatcherWrapper.execute(() -> mList.addAll(collection));
        return true;
    }

    @Override
    public void clear() {
        mDispatcherWrapper.execute(() -> mList.clear());
    }

    @Override
    public boolean contains(Object object) {
        return mList.contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return mList.containsAll(collection);
    }

    @Override
    public T get(int location) {
        return mList.get(location);
    }

    @Override
    public int indexOf(Object object) {
        return mList.indexOf(object);
    }

    @Override
    public boolean isEmpty() {
        return mList.isEmpty();
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return mList.iterator();
    }

    @Override
    public int lastIndexOf(Object object) {
        return mList.lastIndexOf(object);
    }

    @Override
    public ListIterator<T> listIterator() {
        return mList.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator(int location) {
        return mList.listIterator(location);
    }

    @Override
    public T remove(int location) {
        T removed = get(location);
        mDispatcherWrapper.execute(() -> mList.remove(location));
        return removed;
    }

    @Override
    public boolean remove(Object object) {
        boolean exists = contains(object);
        mDispatcherWrapper.execute(() -> mList.remove(object));
        return exists;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        mDispatcherWrapper.execute(() -> mList.removeAll(collection));
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        mDispatcherWrapper.execute(() -> mList.retainAll(collection));
        return true;
    }

    @Override
    public T set(int location, T object) {
        mDispatcherWrapper.execute(() -> mList.set(location, object));
        return null;
    }

    @Override
    public int size() {
        return mList.size();
    }

    @NonNull
    @Override
    public List<T> subList(int start, int end) {
        return mList.subList(start, end);
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return mList.toArray();
    }

    @NonNull
    @Override
    public <T1> T1[] toArray(T1[] array) {
        return mList.toArray(array);
    }
}
