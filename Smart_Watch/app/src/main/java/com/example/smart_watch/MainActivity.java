package com.example.smart_watch;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
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
    TextView tekst, sms_text, czas;
    EditText edit;
    BluetoothAdapter myBlueotoothAdapter;
    BluetoothSocket mySocket;
    BluetoothDevice myDevice;
    OutputStream out;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wyslij = (Button)findViewById(R.id.wyslij);
        edit = (EditText)findViewById(R.id.do_wyslania);
        start=(Button)findViewById(R.id.start);
        tekst=(TextView)findViewById(R.id.TEXT);
        czas= (TextView) findViewById(R.id.czas);
        pokaz_sparowane=(Button)findViewById(R.id.pokaz_spar);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String address = extras.getString("MessageNumber");
            String message = extras.getString("Message");
           // TextView addressField = (TextView) findViewById(R.id.address);
            TextView messageField = (TextView) findViewById(R.id.sms_text);
            //addressField.setText("Message From : " +address);
            messageField.setText("Messsage : "+message);
        }
        wyslij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dane = edit.getText().toString();
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

                                                /*try{
                                                    sendData("2"+localTime+"*************");
                                                }
                                                catch (IOException x){}*/

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
        tekst.setText("Dane wysłane");
    }


}
