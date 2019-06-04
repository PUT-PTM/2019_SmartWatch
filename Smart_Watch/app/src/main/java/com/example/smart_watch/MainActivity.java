package com.example.smart_watch;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    Button wyslij, pokaz_sparowane, btn_czas, start;
    TextView tekst,  czas, sms;
    EditText edit;
    BluetoothAdapter myBlueotoothAdapter;
    BluetoothSocket mySocket;
    BluetoothDevice myDevice;
    OutputStream out;

    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 0;
    private IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");






    public void onResume() {
        super.onResume();
        registerReceiver(intentReceiver, filter);

    }
    BroadcastReceiver intentReceiver = new BroadcastReceiver() {
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
                    sms.setText(phoneNo+" : "+msg);
                    phoneNo = "3"+phoneNo;
                    msg= "1"+msg;
                    if(msg.length()<18) {
                        for (int i = phoneNo.length(); i < 19; i++) {
                            phoneNo = phoneNo + "\0";

                        }
                        for (int j = msg.length(); j < 19; j++) {
                            msg = msg + "\0";
                        }
                        try {
                            sendData(phoneNo);
                            for (int i = 0; i < 10000; i++) {
                                Log.i("INFO", "1");

                            }
                            sendData(msg);
                        } catch (IOException x) { }

                    }
                }
            }

    }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sms =(TextView)findViewById(R.id.sms_text);
        wyslij = (Button)findViewById(R.id.wyslij);
        edit = (EditText)findViewById(R.id.do_wyslania);
        start=(Button)findViewById(R.id.start);
        tekst=(TextView)findViewById(R.id.TEXT);
        czas= (TextView) findViewById(R.id.czas);
        pokaz_sparowane=(Button)findViewById(R.id.pokaz_spar);
        TextView sms_text = (TextView) findViewById(R.id.sms_text);
        String Sms;


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS))
            {

            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS},MY_PERMISSIONS_REQUEST_RECEIVE_SMS);
            }

        }



        wyslij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dane = edit.getText().toString();
                dane = "1"+dane;
                int lenght = dane.length();
                for (int i =lenght; i <19 ; i++) {
                    dane=dane+"\0";
                }
                try{
                    sendData(dane);
                }
                catch (IOException x){}
            }
        });
       /* msg = (Button)findViewById(R.id.CZAS);
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    sendData("1jakisfajnySMS\0");
                    for (int i = 0; i <1000; i++) {
                        System.out.println("+");
                    }

                    sendData("3694584808*********");
                }
                catch (IOException x){}
            }
        });*/


        //btn_czas.setOnClickListener(new View.OnClickListener() {
          //  @Override
          //  public void onClick(View v) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(true) {

                                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
                                Date currentLocalTime = cal.getTime();
                                DateFormat date = new SimpleDateFormat("HH:mm");
                                DateFormat seconds = new SimpleDateFormat("s");
                                final String sekundy = seconds.format(currentLocalTime);
                                date.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));

                                final String localTime = date.format(currentLocalTime);
                                Log.d("INFO", sekundy);
                                if(sekundy.equals("0")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                            czas.setText(localTime);

                                                try{
                                                    sendData("2"+localTime+"*************");
                                                }
                                                catch (IOException x){}

                                } });

                        }}
                    }}).start();
            //}
        //});

       start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    findBluetooth();
                    openBluetooth();
                 }
                catch (IOException ex){}
            }
        });
       pokaz_sparowane.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               pokazSparowane();
           }
       });

    }


    public void pokazSparowane(){
        Log.d("INFO", "Sparowane dla tego urzadzenia");
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size()>0){
            for(BluetoothDevice device : pairedDevices){
                Log.d("INFO",device.getName()+" - "+device.getAddress());
                tekst.setText(device.getName()+" - "+device.getAddress());
            }
        }
    }
    void findBluetooth(){
        myBlueotoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(myBlueotoothAdapter==null){
            tekst.setText("No bluetooth available");
        }
        if(!myBlueotoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth,0);
        }
        Set<BluetoothDevice> pairedDevices= myBlueotoothAdapter.getBondedDevices();
        if(pairedDevices.size()>0){
            for(BluetoothDevice device : pairedDevices){
                if(device.getName().equals("HC-06")){
                    myDevice = device;
                    break;
                }
            }
        }
        tekst.setText("Znaleziono urządzenia Bluetooth");
    }

    void openBluetooth() throws IOException{
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        mySocket= myDevice.createRfcommSocketToServiceRecord(uuid);
        mySocket.connect();
        out = mySocket.getOutputStream();
        tekst.setText("Bluetooth Otwarte");
        sendData("0000000000000000000");

    }

    void sendData(String msg)throws IOException{
        msg+="\n";
        out.write(msg.getBytes());
        tekst.setText("Dane wysłane: "+msg);
    }

    @Override

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_RECEIVE_SMS:
            {
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "Dziekuje za pozwolenie!",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(this, "Bez tego bedzie mi ciezko!",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    public void onPause() {
        unregisterReceiver(intentReceiver);
        // pamiętaj żeby wyrejestrować receivera !
        super.onPause();
    }

}
