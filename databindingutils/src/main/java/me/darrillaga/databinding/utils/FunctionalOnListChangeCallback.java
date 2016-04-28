package me.darrillaga.databinding.utils;

import android.databinding.ObservableList;

public class FunctionalOnListChangeCallback<T extends ObservableList> extends ObservableList.OnListChangedCallback<T> {

    private DispatcherWrapper mDispatcherWrapper;

    private OnChangedListener<T> mOnChangedListener = sender -> {};
    private OnItemRangeChangedListener<T> mOnItemRangeChangedListener = (sender, positionStart, itemCount) -> {};
    private OnItemRangeInsertedListener<T> mOnItemRangeInsertedListener = (sender, positionStart, itemCount) -> {};
    private OnItemRangeMovedListener<T> mOnItemRangeMovedListener = (sender, fromPosition, toPosition, itemCount) -> {};
    private OnItemRangeRemovedListener<T> mOnItemRangeRemovedListener = (sender, positionStart, itemCount) -> {};

    public FunctionalOnListChangeCallback() {
        mDispatcherWrapper = new DispatcherWrapper();
    }

    @Override
    public void onChanged(T sender) {
        mDispatcherWrapper.execute(() -> mOnChangedListener.onChanged(sender));
    }

    @Override
    public void onItemRangeChanged(T sender, int positionStart, int itemCount) {
        mDispatcherWrapper.execute(() -> mOnItemRangeChangedListener.onItemRangeChanged(sender, positionStart, itemCount));
    }

    @Override
    public void onItemRangeInserted(T sender, int positionStart, int itemCount) {
        mDispatcherWrapper.execute(() -> mOnItemRangeInsertedListener.onItemRangeInserted(sender, positionStart, itemCount));
    }

    @Override
    public void onItemRangeMoved(T sender, int fromPosition, int toPosition, int itemCount) {
        mDispatcherWrapper.execute(() -> mOnItemRangeMovedListener.onItemRangeMoved(sender, fromPosition, toPosition, itemCount));
    }

    @Override
    public void onItemRangeRemoved(T sender, int positionStart, int itemCount) {
        mDispatcherWrapper.execute(() -> mOnItemRangeRemovedListener.onItemRangeRemoved(sender, positionStart, itemCount));
    }

    public FunctionalOnListChangeCallback<T> withDispatcher(Dispatcher dispatcher) {
        mDispatcherWrapper.setDispatcher(dispatcher);

        return this;
    }

    public FunctionalOnListChangeCallback<T> whenChanged(OnChangedListener<T> onChangedListener) {
        if (onChangedListener == null) {
            onChangedListener = sender -> {};
        }

        mOnChangedListener = onChangedListener;

        return this;
    }

    public FunctionalOnListChangeCallback<T> whenItemRangeChanged(OnItemRangeChangedListener<T> onItemRangeChangedListener) {
        if (onItemRangeChangedListener == null) {
            onItemRangeChangedListener = (sender, positionStart, itemCount) -> {};
        }

        mOnItemRangeChangedListener = onItemRangeChangedListener;

        return this;
    }

    public FunctionalOnListChangeCallback<T> whenItemRangeInserted(OnItemRangeInsertedListener<T> onItemRangeInsertedListener) {
        if (onItemRangeInsertedListener == null) {
            onItemRangeInsertedListener = (sender, positionStart, itemCount) -> {};
        }

        mOnItemRangeInsertedListener = onItemRangeInsertedListener;

        return this;
    }

    public FunctionalOnListChangeCallback<T> whenItemRangeMoved(OnItemRangeMovedListener<T> onItemRangeMovedListener) {
        if (onItemRangeMovedListener == null) {
            onItemRangeMovedListener = (sender, fromPosition, toPosition, itemCount) -> {};
        }

        mOnItemRangeMovedListener = onItemRangeMovedListener;

        return this;
    }

    public FunctionalOnListChangeCallback<T> whenItemRangeRemoved(OnItemRangeRemovedListener<T> onItemRangeRemovedListener) {
        if (onItemRangeRemovedListener == null) {
            onItemRangeRemovedListener = (sender, positionStart, itemCount) -> {};
        }

        mOnItemRangeRemovedListener = onItemRangeRemovedListener;

        return this;
    }

    public interface OnChangedListener<T> {
        void onChanged(T sender);
    }

    public interface OnItemRangeChangedListener<T> {
        void onItemRangeChanged(T sender, int positionStart, int itemCount);
    }

    public interface OnItemRangeInsertedListener<T> {
        void onItemRangeInserted(T sender, int positionStart, int itemCount);
    }

    public interface OnItemRangeMovedListener<T> {
        void onItemRangeMoved(T sender, int fromPosition, int toPosition, int itemCount);
    }

    public interface OnItemRangeRemovedListener<T> {
        void onItemRangeRemoved(T sender, int positionStart, int itemCount);
    }
}
