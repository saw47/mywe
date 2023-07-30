package util;

public class SingleLiveEvent<T> {
    private boolean hasBeenHandled = false;
    private T content;

    public SingleLiveEvent(T content) {
        this.content = content;
    }

    public T getContentIfNotHandled() {
        if (hasBeenHandled) {
            return null;
        } else {
            hasBeenHandled = true;
            return content;
        }
    }

    public boolean isHandled() {
        return hasBeenHandled;
    }
}