package com.example.locator;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class BluetoothConnectionService {
    private static final String TAG = "BluetoothConnectionServ";

    private static final String appName = "MYAPP";

    private static final UUID MY_UUID_INSECURE =
        UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private final BluetoothAdapter mBluetoothAdapter;
    Context mContext;

    private AcceptThread mInsecureAcceptThread;

    private ConnectThread mConnectThread;
    private BluetoothDevice mmDevice;
    private UUID deviceUUID;
    private Handler handler;
    ProgressDialog mProgressDialog;

    private ConnectedThread mConnectedThread;

    public BluetoothConnectionService(Context context) {
        mContext = context;
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Tools.showMsg(mContext, (String) msg.obj);
            }
        };
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        start();
    }



    private class AcceptThread extends Thread {

        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;

            try {
                tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appName, MY_UUID_INSECURE);

                Tools.log("AcceptThread: Setting up Server using: " + MY_UUID_INSECURE);
            } catch (IOException e) {
                Tools.log("AcceptThread: IOException: " + e.getMessage());
            }

            mmServerSocket = tmp;
        }

        public void run() {
            Tools.log("run: AcceptThread Running.");

            BluetoothSocket socket = null;

            try {

                Tools.log("run: RFCOM server socket start.....");

                socket = mmServerSocket.accept();

                Tools.log("run: RFCOM server socket accepted connection.");

            } catch (IOException e) {
                Tools.log("AcceptThread: IOException: " + e.getMessage());
            }
            if (socket != null) {
                connected(socket, mmDevice, null, false);
            }

            Tools.log("END mAcceptThread ");
        }

        public void cancel() {
            Tools.log("cancel: Canceling AcceptThread.");
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Tools.log("cancel: Close of AcceptThread ServerSocket failed. " + e.getMessage());
            }
        }

    }


    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;
        private byte[] bytesToWrite;

        public ConnectThread(BluetoothDevice device, UUID uuid, byte[] bytesToWrite) {
            Tools.log("ConnectThread: started.");
            this.bytesToWrite = bytesToWrite;
            mmDevice = device;
            deviceUUID = uuid;
        }

        public void run() {
            BluetoothSocket tmp = null;
            Tools.log("RUN mConnectThread ");

            try {
                Tools.log("ConnectThread: Trying to create InsecureRfcommSocket using UUID: "
                    + MY_UUID_INSECURE);
                tmp = mmDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                Tools.log("ConnectThread: Could not create InsecureRfcommSocket " + e.getMessage());
            }

            mmSocket = tmp;


            mBluetoothAdapter.cancelDiscovery();



            try {
                mmSocket.connect();
                Tools.log("run: ConnectThread connected.");
            } catch (IOException e) {
                try {
                    mmSocket.close();
                    Tools.log("run: Closed Socket.");
                } catch (IOException e1) {
                    Tools.log("mConnectThread: run: Unable to close connection in socket " + e1.getMessage());
                }
                Tools.log("run: ConnectThread: Could not connect to UUID: " + MY_UUID_INSECURE);
            }

            connected(mmSocket, mmDevice, bytesToWrite, true);
        }

        public void cancel() {
            try {
                Tools.log("cancel: Closing Client Socket.");
                mmSocket.close();
            } catch (IOException e) {
                Tools.log("cancel: close() of mmSocket in Connectthread failed. " + e.getMessage());
            }
        }
    }



    public synchronized void start() {
        Tools.log("start");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mInsecureAcceptThread == null) {
            mInsecureAcceptThread = new AcceptThread();
            mInsecureAcceptThread.start();
        }
    }



    public void startClient(BluetoothDevice device, UUID uuid, byte[] bytes) {
        Tools.log("startClient: Started.");


        mProgressDialog = ProgressDialog.show(mContext, "Connecting Bluetooth"
            , "Please Wait...", true);

        mConnectThread = new ConnectThread(device, uuid, bytes);
        mConnectThread.start();
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Tools.log("ConnectedThread: Starting.");

            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            //dismiss the progressdialog when connection is established
            try {
                mProgressDialog.dismiss();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }


            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];

            int bytes;


            while (true) {

                try {
                    bytes = mmInStream.read(buffer);
                    String userId = new String(buffer, 0, bytes);
                    LocatorData.getInstance().addFriend(userId);
                    handler.obtainMessage(1, "New friend added").sendToTarget();
                } catch (IOException e) {
                    Tools.log("write: Error reading Input Stream. " + e.getMessage());
                    break;
                }
            }
        }

        public void write(byte[] bytes) {
            String text = new String(bytes, Charset.defaultCharset());
            Tools.log("write: Writing to outputstream: " + text);
            try {
                mmOutStream.write(bytes);
                handler.obtainMessage(1, "Friend added").sendToTarget();
            } catch (IOException e) {
                Tools.log("write: Error writing to output stream. " + e.getMessage());
                handler.obtainMessage(1, "Friend not added").sendToTarget();
            }
        }


        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice, byte[] out, boolean isClient) {
        Tools.log("connected: Starting.");


        mConnectedThread = new ConnectedThread(mmSocket);
        if (isClient)
            mConnectedThread.write(out);
        mConnectedThread.start();
    }


}