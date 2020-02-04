package com.example.locator;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.UUID;

public class DiscoverFriendsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    BluetoothAdapter mBluetoothAdapter;

    BluetoothConnectionService mBluetoothConnection;

    Button btnAddFriend;

    private static final UUID MY_UUID_INSECURE =
        UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    BluetoothDevice mBTDevice;

    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();

    public LinearLayout selectedView;
    public DiscoveredDeviceAdapter adapter;

    ListView lvNewDevices;

    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Tools.log("onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED && mBluetoothConnection == null) {
                    mBluetoothConnection = new BluetoothConnectionService(DiscoverFriendsActivity.this);

                }
                if (device.getAddress().isEmpty() || device.getName().isEmpty())
                    return;
                mBTDevices.add(device);
                Tools.log("onReceive: " + device.getName() + ": " + device.getAddress());
                adapter = new DiscoveredDeviceAdapter(context, R.layout.item_device, mBTDevices);
                lvNewDevices.setAdapter(adapter);
            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                    Tools.log("BroadcastReceiver: BOND_BONDED.");
                    if (mBluetoothConnection == null) {
                        mBluetoothConnection = new BluetoothConnectionService(DiscoverFriendsActivity.this);
                    }
                }
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Tools.log("BroadcastReceiver: BOND_BONDING.");
                }
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Tools.log("BroadcastReceiver: BOND_NONE.");
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_friends);

        lvNewDevices = findViewById(R.id.list_view);
        mBTDevices = new ArrayList<>();

        btnAddFriend = findViewById(R.id.btn_add_friend);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4, filter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        lvNewDevices.setOnItemClickListener(DiscoverFriendsActivity.this);


        btnAddFriend.setOnClickListener(view -> {
            if (mBTDevice == null) {
                Tools.showMsg(this, "please select device to add");
            } else {
                startConnection();
            }
        });

    }

    public void startConnection() {

        byte[] bytes = LocatorData.getInstance().getUser().getId().getBytes();
        mBluetoothConnection.startClient(mBTDevice, MY_UUID_INSECURE, bytes);
    }


    @Override
    protected void onDestroy() {
        Tools.log("onDestroy: called.");
        super.onDestroy();
        try {
            unregisterReceiver(mBroadcastReceiver3);
            unregisterReceiver(mBroadcastReceiver4);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mBluetoothAdapter.cancelDiscovery();
    }


    public void enableDiscoverable() {
        Tools.log("btnEnableDisable_Discoverable: Making device discoverable for 300 seconds.");
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);

    }


    public void btnDiscover(View view) {
        Tools.log("btnDiscover: Looking for unpaired devices.");
        enableDiscoverable();
        mBTDevices = new ArrayList<>();

        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
            Tools.log("btnDiscover: Canceling discovery.");

            //check BT permissions in manifest
            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
        if (!mBluetoothAdapter.isDiscovering()) {
            //check BT permissions in manifest
            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
    }

    private void checkBTPermissions() {
        int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
        permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
        if (permissionCheck != 0) {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mBluetoothAdapter.cancelDiscovery();

        Tools.log("onItemClick: You Clicked on a device.");
        String deviceName = mBTDevices.get(i).getName();
        String deviceAddress = mBTDevices.get(i).getAddress();

        Tools.log("onItemClick: deviceName = " + deviceName);
        Tools.log("onItemClick: deviceAddress = " + deviceAddress);

        Tools.log("Trying to pair with " + deviceName);
        mBTDevices.get(i).createBond();

        mBTDevice = mBTDevices.get(i);
        mBluetoothConnection = new BluetoothConnectionService(DiscoverFriendsActivity.this);


        if (selectedView != null) {
            selectedView.setBackgroundColor(getColor(R.color.background));
        }
        selectedView = view.findViewById(R.id.parentView);
        selectedView.setBackgroundColor(getColor(R.color.colorPrimary));

    }
}
