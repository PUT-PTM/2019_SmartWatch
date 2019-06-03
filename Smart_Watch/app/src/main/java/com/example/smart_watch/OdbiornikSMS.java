package com.example.smart_watch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.*;
import android.util.Log;
import android.widget.Toast;


final public class OdbiornikSMS extends BroadcastReceiver {
    private static final String SMS_RECEIVED="android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SmsBroadcastReceiver";
    String msg, phoneNo ="";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG , "IntentReceived: "+ intent.getAction());
        if(intent.getAction()==SMS_RECEIVED)
        {
            Bundle dataBunndle = intent.getExtras();
            if(dataBunndle!=null)
            {
                Object[] mypdu = (Object[])dataBunndle.get("pdus");
                final SmsMessage[] message = new SmsMessage[mypdu.length];
                for (int i = 0; i <mypdu.length ; i++) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        String format = dataBunndle.getString("format");
                        message[i]=SmsMessage.createFromPdu((byte[])mypdu[i],format);
                    }
                    else
                    {
                        message[i]= SmsMessage.createFromPdu((byte[])mypdu[i]);
                    }
                    msg = message[i].getMessageBody();
                    phoneNo = message[i].getOriginatingAddress();
                    
                }

                Toast.makeText(context, "Message: "+msg+"\nNumber: "+phoneNo,Toast.LENGTH_LONG).show();

            }
        }
    }
}

