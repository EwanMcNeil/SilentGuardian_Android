package com.example.silentguardian_android.Bluetooth;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.silentguardian_android.Helpers.SharePreferenceHelper;
import com.example.silentguardian_android.Activities.MainActivity;
import com.example.silentguardian_android.R;
import com.example.silentguardian_android.Tutorial.MyImage;
import com.example.silentguardian_android.Tutorial.TutorialViewpagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BluetoothMainActivity extends ListActivity {

    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    private int deviceCount;
    //varibles for the service
    Intent mServiceIntent;
    private DeviceService mYourService;

    Dialog mInfoDialog;
    List<MyImage> mtutorialSlides;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deviceCount = 0;
        SharePreferenceHelper helper = new SharePreferenceHelper(getApplicationContext());

        mInfoDialog = new Dialog(BluetoothMainActivity.this, R.style.Theme_AppCompat);
        mtutorialSlides = loadTutorial();

        if(!helper.getTutorialSeen()){
            startActivityTutorial();
        }
        helper.setTutorialSeen(true);


        //code for the device service
        mYourService = new DeviceService();
        mServiceIntent = new Intent(this, mYourService.getClass());


        ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
       // setContentView(R.layout.listitem_device);
        getActionBar().setTitle(R.string.title_devices);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mHandler = new Handler();

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class );
            startActivity(intent);
            return;
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        //emulators should go to main now
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class );
            startActivity(intent);
            return;
        }

        //tutorial stuff, not elegant at all but it ll do for now


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if (!mScanning) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            //menu.findItem(R.id.tutorialButton).setActionView(null);
        } else {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            //menu.findItem(R.id.menu_refresh).setActionView(
                 //   R.layout.actionbar_indeterminate_progress);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                mLeDeviceListAdapter.clear();
                scanLeDevice(true);
                break;
            case R.id.menu_stop:
                scanLeDevice(false);
                break;
            case android.R.id.home:
                    onBackPressed();
                    break;
            case R.id.menu_gotoMain:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class );
                startActivity(intent);
                break;
            case R.id.tutorialButton:
                startActivityTutorial();
                break;
        }
        return true;
    }
    private void startActivityTutorial(){

       mInfoDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mInfoDialog.setContentView(R.layout.activity_tutorial);
        //UI elements
        ViewPager mScreenPager = mInfoDialog.findViewById(R.id.screen_viewpager);
        TabLayout mTabIndicator  = mInfoDialog.findViewById(R.id.tab_indicator);
        TextView  mSkip = mInfoDialog.findViewById(R.id.tv_skip);
        final Button mDialogButton = mInfoDialog.findViewById(R.id.btn_get_started);
        mSkip.setVisibility(View.INVISIBLE);
        //decodeSampledBitmapFromResource(getResources(),R.drawable.guardians_act_info, 220, 220);
        TutorialViewpagerAdapter mTutorialViewpagerAdapter = new TutorialViewpagerAdapter(getApplicationContext(),mtutorialSlides,false);
        mScreenPager.setAdapter(mTutorialViewpagerAdapter);
        // setup tablayout with viewpager
        mTabIndicator.setupWithViewPager(mScreenPager);
        mDialogButton.setText(R.string.end_tutorial_button_text);
        mTabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mtutorialSlides.size()-1)
                    mDialogButton.setVisibility(View.VISIBLE);
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) { }
            @Override public void onTabReselected(TabLayout.Tab tab) { }
        });
        mDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfoDialog.dismiss();
            }
        });
        mInfoDialog.show();
    }
    private List<MyImage> loadTutorial(){
        List<MyImage> mList = new ArrayList<>();
        mList.add(new MyImage("Turn on your Silent Guardian device",
                "Press on the rocker button on the device to power it on."
                ,R.mipmap.blue1));


        mList.add(new MyImage("Wait for detection",
                "Wait until the device's name shows up on the screen. If it does not, press on Scan to start scanning for the device."
                ,R.mipmap.blue2));

        mList.add(new MyImage("Pair your device",
                "Press on the device's name to initiate the pairing process."
                ,R.mipmap.blue3));

        return mList;
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        setListAdapter(mLeDeviceListAdapter);
        scanLeDevice(true);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        mLeDeviceListAdapter.clear();

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Log.i ("Service status", "beforeNull");
        final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
        if (device == null) return;

        if(isMyServiceRunning(DeviceService.class)){
            Toast.makeText(getApplicationContext(), "Error the device: " + device.getName() + " is already connected", Toast.LENGTH_LONG).show();
            return;
        }

        if(!device.getName().equals("Silent_Guardians")){
            Toast.makeText(getApplicationContext(), device.getName()+ " is not a compatible Device", Toast.LENGTH_LONG).show();
            return;
        }
        Log.i ("Service status", "passedNull");

        mServiceIntent.putExtra("DEVICE_NAME", device.getName());
        mServiceIntent.putExtra("DEVICE_ADDRESS", device.getAddress());
        if (!isMyServiceRunning(mYourService.getClass())) {
            startService(mServiceIntent);
            Log.i ("Service status", "passedIf");
        }




        Toast.makeText(getApplicationContext(), device.getName()+ " Your device has been sucessfully connected", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, MainActivity.class );
        startActivity(intent);

    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;


        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = BluetoothMainActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if(device.getName() != null) {
                if (!mLeDevices.contains(device)) {
                    deviceCount++;
                    mLeDevices.add(device);
                }
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);
            viewHolder.deviceAddress.setText(device.getAddress());

            return view;
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLeDeviceListAdapter.addDevice(device);
                            mLeDeviceListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            };

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }

    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        super.onDestroy();
    }


}
