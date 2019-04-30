package com.example.smart_watch;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    Button wykryj_bluetooth, pokaz_sparowane, btn_czas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView tekst=(TextView)findViewById(R.id.TEXT);
        btn_czas=(Button)findViewById(R.id.CZAS);
        wykryj_bluetooth=(Button)findViewById(R.id.wykryj);
        pokaz_sparowane=(Button)findViewById(R.id.pokaz_spar);
        btn_czas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
                Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("HH:mm a");

                date.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));

                String localTime = date.format(currentLocalTime);
                tekst.setText(localTime);
            }
        });
        wykryj_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wykryjInne();
            }
        });
        pokaz_sparowane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pokazSparowane();
            }
        });


    }
    private final BroadcastReceiver odbiorca= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String akcja = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(akcja)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String status="";
                if(device.getBondState() !=BluetoothDevice.BOND_BONDED){
                    status="nie sparowane";
                }
                else {
                    status="sparowane";
                }
                Log.d("INFO", "znaleziono urzadzenie"+device.getName()+" - "+device.getAddress());
            }

        }
    };
    public void wykryjInne(){
        Log.d("Info","Szukam innych urzadzen(ok 12s)");
        IntentFilter filtr=new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(odbiorca,filtr);
        BluetoothAdapter ba =BluetoothAdapter.getDefaultAdapter();
        ba.startDiscovery();

    }
    public void pokazSparowane(){
        Log.d("INFO", "Sparowane dla tego urzadzenia");
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size()>0){
            for(BluetoothDevice device : pairedDevices){
                Log.d("INFO",device.getName()+" - "+device.getAddress());
            }
        }
    }
}
