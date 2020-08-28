package com.bulahej.tazweeg.utilties;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.bulahej.tazweeg.R;

//import com.technation.pediaclub.R;
//import com.technation.pediaclub.activity.LoginActivity;
//import com.technation.pediaclub.constant.Key;

public class AppUtility {

    public static AlertDialog createPleaseWaitDialog(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle(R.string.working_text);
        builder.setMessage(R.string.please_wait_text);
        builder.setCancelable(false);
        return builder.create();

    }


}
