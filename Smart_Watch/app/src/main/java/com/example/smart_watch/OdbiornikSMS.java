package com.example.smart_watch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

final public class OdbiornikSMS extends BroadcastReceiver {

    @Override

    public void onReceive(Context context, Intent intent) {
        Bundle bundle =intent.getExtras();
        SmsMessage[] msgs=null;
        String wiadomosc="";
        Object[] pdus=(Object[])bundle.get("pdus");
        msgs=new SmsMessage[pdus.length];
        for(int x=0;x<msgs.length;x++){
            msgs[x]=SmsMessage.createFromPdu((byte[])pdus[x]);
            wiadomosc=msgs[x].getMessageBody();
            Toast.makeText(context,wiadomosc,Toast.LENGTH_LONG).show();


        }
    }
}
