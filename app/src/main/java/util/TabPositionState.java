package util;

public enum TabPositionState {
    ACTIVE (0),
    OLD (1),
    UNDEF(-1);

    private final int tabIndex;
    TabPositionState(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public int getTabIndex() {
        return tabIndex;
    }
}
