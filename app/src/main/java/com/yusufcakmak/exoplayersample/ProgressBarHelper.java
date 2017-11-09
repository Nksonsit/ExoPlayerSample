package com.yusufcakmak.exoplayersample;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by anish on 22-02-2017.
 */

public class ProgressBarHelper implements ProgressListener {

    ProgressDialog dialog;

    public ProgressBarHelper(Context context, boolean isCancelable) {
        dialog = new ProgressDialog(context);
        dialog.setCancelable(isCancelable);
        dialog.setCanceledOnTouchOutside(isCancelable);
        dialog.setMessage("Please wait...");
    }

    @Override
    public void showProgressDialog() {
        if (dialog != null) {
            dialog.show();
        }
    }

    @Override
    public void showProgressDialog(String message) {
        if (dialog != null) {
            dialog.setMessage(message);
            dialog.show();
        }
    }

    @Override
    public void hideProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
