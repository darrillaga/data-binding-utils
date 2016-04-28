package me.darrillaga.databinding.utils;

import android.databinding.ObservableList;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;

import java8.util.stream.RefStreams;
import rx.functions.Action1;

public class ObservableListAdapterObservers {

    private ObservableListAdapterObservers() {
        throw new AssertionError();
    }

    public static <T extends ObservableList> FunctionalOnListChangeCallback<T> createObservableListAdapterBridgeWithMainLooperThreadDispatch(
            T observableList, RecyclerView.Adapter... adapters) {

        Handler handler = new Handler(Looper.getMainLooper());

        return createObservableListAdapterBridge(observableList, handler::post, adapters);
    }

    public static <T extends ObservableList> FunctionalOnListChangeCallback<T> createObservableListAdapterBridge(
            T observableList, Dispatcher dispatcher, RecyclerView.Adapter... adapters) {

        FunctionalOnListChangeCallback<T> observer = new FunctionalOnListChangeCallback<T>()
                .withDispatcher(dispatcher)
                .whenChanged(
                        sender -> RefStreams.of(adapters).forEach(RecyclerView.Adapter::notifyDataSetChanged)
                )
                .whenItemRangeChanged(
                        (sender, positionStart, itemCount) ->
                                RefStreams.of(adapters).forEach(adapter -> adapter.notifyItemRangeChanged(positionStart, itemCount))
                )
                .whenItemRangeInserted(
                        (sender, positionStart, itemCount) ->
                                RefStreams.of(adapters).forEach(adapter -> adapter.notifyItemRangeInserted(positionStart, itemCount))
                )
                .whenItemRangeMoved(
                        (sender, fromPosition, toPosition, itemCount) ->
                                RefStreams.of(adapters).forEach(adapter -> adapter.notifyItemMoved(fromPosition, toPosition))
                )
                .whenItemRangeRemoved(
                        (sender, positionStart, itemCount) ->
                                RefStreams.of(adapters).forEach(adapter -> adapter.notifyItemRangeRemoved(positionStart, itemCount))
                );

        observableList.addOnListChangedCallback(observer);

        return observer;
    }

    /**
     * Register a callback to be executed when the list is modified in any way
     *
     * @param observableList
     * @param action1
     * @param <T>
     * @return
     */
    public static <T extends ObservableList> FunctionalOnListChangeCallback<T> createObservableListChangeListener(
            T observableList, Action1 action1) {

        FunctionalOnListChangeCallback<T> observer = new FunctionalOnListChangeCallback<T>().
                whenChanged(
                        sender -> action1.call(sender)
                ).
                whenItemRangeChanged(
                        (sender, positionStart, itemCount) ->
                                action1.call(sender)
                ).
                whenItemRangeInserted(
                        (sender, positionStart, itemCount) ->
                                action1.call(sender)
                ).
                whenItemRangeMoved(
                        (sender, fromPosition, toPosition, itemCount) ->
                                action1.call(sender)
                ).
                whenItemRangeRemoved(
                        (sender, positionStart, itemCount) ->
                                action1.call(sender)
                );

        observableList.addOnListChangedCallback(observer);

        return observer;
    }
}
