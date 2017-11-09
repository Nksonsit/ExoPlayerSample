package com.yusufcakmak.exoplayersample;

/**
 * Created by anish on 22-02-2017.
 */

public interface ProgressListener {

    void showProgressDialog();

    void showProgressDialog(String message);

    void hideProgressDialog();

}
