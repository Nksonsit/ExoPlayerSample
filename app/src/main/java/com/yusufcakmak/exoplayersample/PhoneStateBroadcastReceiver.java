package com.yusufcakmak.exoplayersample;

/**
 * Created by ishan on 02-11-2016.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class PhoneStateBroadcastReceiver extends BroadcastReceiver {

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new CustomPhoneStateListener(context), PhoneStateListener.LISTEN_CALL_STATE);

    }

    public class CustomPhoneStateListener extends PhoneStateListener {

        //private static final String TAG = "PhoneStateChanged";
        Context context; //Context to make Toast if required

        public CustomPhoneStateListener(Context context) {
            super();
            this.context = context;
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);

            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    //when Idle i.e no call
                    Intent i = new Intent(AppConstant.CALL);
                    i.putExtra("call", "off");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(i);
                    //Log.e("no call","no call");
                    //Toast.makeText(context, "Phone state Idle", Toast.LENGTH_LONG).show();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //when Off hook i.e in call
                    //Make intent and start your service here
//                    Toast.makeText(context, "Phone state Off hook", Toast.LENGTH_LONG).show();
                    Intent i1 = new Intent(AppConstant.CALL);
                    i1.putExtra("call", "on");
                    //Log.e("in call","in call");
//                    LocalBroadcastManager.getInstance(context).sendBroadcast(i1);
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    //when Ringing
//                    Toast.makeText(context, "Phone state Ringing", Toast.LENGTH_LONG).show();
                    Intent i2 = new Intent(AppConstant.CALL);
                    i2.putExtra("call", "on");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(i2);
                    //Log.e("ringing","ringing");
                    break;
                default:
                    break;
            }
        }
    }
}
