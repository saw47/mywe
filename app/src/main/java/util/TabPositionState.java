package util;

public enum TabPositionState {
    ACTIVE (0),
    OLD (1),
    UNDEF(-1);

    private int tabIndex;

    TabPositionState(int tabIndex) {
    }

    public int getTabIndex() {
        return tabIndex;
    }
}
