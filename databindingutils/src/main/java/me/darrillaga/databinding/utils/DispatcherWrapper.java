package me.darrillaga.databinding.utils;

public class DispatcherWrapper implements Dispatcher {

    private Dispatcher mDispatcher;

    public DispatcherWrapper() {
        this(null);
    }

    public DispatcherWrapper(Dispatcher dispatcher) {
        mDispatcher = dispatcher;
    }

    public void setDispatcher(Dispatcher dispatcher) {
        mDispatcher = dispatcher;
    }

    @Override
    public void execute(Runnable runnable) {
        if (mDispatcher == null) {
            runnable.run();
        } else {
            mDispatcher.execute(runnable);
        }
    }
}