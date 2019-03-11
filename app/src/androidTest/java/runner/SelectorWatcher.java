package runner;

import android.app.UiAutomation;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiWatcher;

/**
 * Created by wangfuwen on 2017/5/24.
 */

public class SelectorWatcher implements UiWatcher {

    @Override
    public boolean checkForCondition() {
        return false;
    }
}
