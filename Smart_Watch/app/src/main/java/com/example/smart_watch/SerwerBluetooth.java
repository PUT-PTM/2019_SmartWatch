package com.example.smart_watch;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.UUID;

public class SerwerBluetooth extends Thread {
    private final BluetoothServerSocket mmServerSocket;
    private OutputStream out;
    public SerwerBluetooth(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothServerSocket tmp = null;

        try {
            UUID uuid = UUID.fromString("0000110E-0000-1000-8000-00805F9B34FB");
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("Usługa Witająca!", uuid);
        }
        catch(IOException e){ }
        mmServerSocket=tmp;
    }

    public void run(){
        Log.d("INFO", "Uruchamiam serwer");
        BluetoothSocket socket = null;
        while(true){
            try{
                Log.d("INFO","Czekam na połączenie od clienta");
                socket = mmServerSocket.accept();
                Log.d("INFO", "Mam clienta");
                out = socket.getOutputStream();
            }
            catch(IOException e){break;};
            if(socket!=null){
                //instrukcje
                try{
                    mmServerSocket.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public void write(String a){
        try {
            out.write(a.getBytes());
        }
        catch(IOException e){}
    }
}
