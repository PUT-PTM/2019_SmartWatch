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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    Button wykryj_bluetooth, pokaz_sparowane, btn_czas, start;
    TextView tekst;
    BluetoothAdapter myBlueotoothAdapter;
    BluetoothSocket mySocket;
    BluetoothDevice myDevice;
    OutputStream out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start=(Button)findViewById(R.id.start);
        tekst=(TextView)findViewById(R.id.TEXT);
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

       start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // try{
                    findBluetooth();
                    //openBluetooth();
               //   }
                //catch (IOException ex){}
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
                tekst.setText("Znaleziono urządzenie"+device.getName()+" - "+device.getAddress());
            }

        }
    };
    public void wykryjInne(){
        Log.d("Info","Szukam innych urzadzen(ok 12s)");
        tekst.setText("Szukam innych urzadzen(ok 12s)");
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
                if(device.getName().equals("HC-05")){
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

    }

    void sendData(String msg)throws IOException{
        msg+="\n";
        out.write(msg.getBytes());
        tekst.setText("Dane wysłane");
    }


}
