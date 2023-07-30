package util;

import androidx.lifecycle.Observer;

public class EventObserver<T> implements Observer<SingleLiveEvent<T>> {
    private OnEventChanged onEventChanged;

    public EventObserver(OnEventChanged onEventChanged) {
        this.onEventChanged = onEventChanged;
    }

    @Override
    public void onChanged(SingleLiveEvent<T> tSingleLiveEvent) {
        if (tSingleLiveEvent != null && tSingleLiveEvent.getContentIfNotHandled() != null && onEventChanged != null)
            onEventChanged.onUnhandledContent(tSingleLiveEvent.getContentIfNotHandled());
    }

    interface OnEventChanged<T> {
        void onUnhandledContent(T data);
    }
}
