package com.chinesequiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

/**
 * bananaJuice 11/2018
 */

public class Util {
    public static final int ACTIVITY_FINISHED = 1000;
    public static final int LOG_OUT = 1001;
    public static final int PAGE_SCROLLED = 1002;

    public static ProgressDialog progressDlg = null;
    public static Toast toast = null;

    /**
     * Set up the progress dialog and show it as a popup on the screen
     * @param titleStr Text displayed in the popup
     * @param context Context of where the popup should be displayed
     */
    public static void showProgressDialog(String titleStr, Context context)  {
        progressDlg = new ProgressDialog(context, R.style.AppDialogTheme);
        progressDlg.setIndeterminate(false);
        progressDlg.setCancelable(false);
        progressDlg.setMessage(titleStr);
        progressDlg.show();
    }

    /**
     * Take the progress popup off of the screen
     */
    public static void hideProgressDialog()  {
        if(progressDlg != null) {
            progressDlg.dismiss();
        }
    }

    /**
     * Setup and show a toast in the given context
     * @param toastStr Toast text
     * @param context Context of where the toast should be displayed
     */
    public static void showToast (String toastStr, Context context) {
        if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
            toast = Toast.makeText(context, toastStr, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

}
