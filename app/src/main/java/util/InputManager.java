package util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class InputManager {

    static String TAG = "MW-IM";

    public static void hideKeyboard(Context context) {
        try {
            ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            if ((((Activity) context).getCurrentFocus() != null) && (((Activity) context).getCurrentFocus().getWindowToken() != null)) {
                Log.d(TAG, "IM if OK hideKeyboard");
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            Log.d(TAG, "IM if Exception hideKeyboard");
            e.printStackTrace();
        }
    }

    public static void showKeyboard(Context context, EditText edit) {
        Log.d(TAG, " showKeyboard");
        edit.requestFocus();
        ((InputMethodManager) (context).getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
