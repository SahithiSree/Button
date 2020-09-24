package com.vaagdevi.button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.LimitColumn;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

public class home extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    private static final int CONTACT_PICKER_REQUEST =1 ;
    private EditText editTextNumber;
    private EditText editTextMessage;
    List<ContactResult> results = new ArrayList<>();


   /* Button btLocation, turnonbtn, turnoffbtn, discoverable, paired;
    TextView StatusBluetooth, pair;

    FusedLocationProviderClient fusedLocationProviderClient;
    BluetoothAdapter bluetoothAdapter*/;
    TextView showLocation;
    LocationManager locationManager;
    String latitude, longitude;
    ImageView img;
    Button b1, b2, b3, b4,btLocation;
    private static final int MY_PERMISSION_SEND_SMS=0;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    ListView lv;
    private String[] permission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ActivityCompat.requestPermissions( this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

        btLocation = findViewById(R.id.bt_location);

        b1 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);
        BA = BluetoothAdapter.getDefaultAdapter();
        lv = (ListView) findViewById(R.id.listView);
        showLocation = findViewById(R.id.showLocation);
        btLocation = findViewById(R.id.bt_location);
        editTextMessage = findViewById(R.id.editText);
        editTextNumber = findViewById(R.id.editTextNumber);
        btLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    OnGPS();
                } else {
                    getLocation();
                }
            }
        });
    }
    public void sendSMS(View view) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            mymessage();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSION_SEND_SMS);

        }
    }
    public void mymessage(){

        String message = editTextMessage.getText().toString().trim();
        String number = editTextNumber.getText().toString().trim();
        if(number==null || number.equals("") || message==null || message.equals("")){
            Toast.makeText(this,"Fields are empty",Toast.LENGTH_LONG).show();
        }
        else{
            if(TextUtils.isDigitsOnly(number)){
                SmsManager mySmsManager = SmsManager.getDefault();
                mySmsManager.sendTextMessage(number,null, message, null, null);
                Toast.makeText(this,"message send",Toast.LENGTH_LONG).show();
            }
            else{
              Toast.makeText(this,"pls enter valid number",Toast.LENGTH_LONG).show();
            }
        }

    }
//    public void onRequestPermissionResult(int reqcode.String[] permission,int[] grantResult)
//    {
//        super.onRequestPermissionsResult(reqcode,permission,grantResult);
//        switch(reqcode){
//            case MY_PERMISSION_SEND_SMS:
//            {
//                if ((grantResult.length>0 && grantResult[0]==PackageManager.)){
//                    mymessage();
//
//                }
//                else{
//                    Toast.makeText(this,"no permission is granted",Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//
//    }





    public void on(View v) {
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Turned on", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Already on", Toast.LENGTH_LONG).show();
        }
    }

    public void off(View v) {
        BA.disable();
        Toast.makeText(getApplicationContext(), "Turned off", Toast.LENGTH_LONG).show();
    }


    public void visible(View v) {
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
    }


    public void list(View v) {
        pairedDevices = BA.getBondedDevices();

        ArrayList list = new ArrayList();

        for (BluetoothDevice bt : pairedDevices) list.add(bt.getName());
        Toast.makeText(getApplicationContext(), "Showing Paired Devices", Toast.LENGTH_SHORT).show();

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);

        lv.setAdapter(adapter);
    }



//        StatusBluetooth=findViewById(R.id.statusBluetooth);
//        discoverable=findViewById(R.id.discoverablebtn);
//        paired=findViewById(R.id.Paired);
//        pair=findViewById(R.id.pair);
//        img=findViewById(R.id.img);
//        turnonbtn=findViewById(R.id.onbtn);
//        turnoffbtn=findViewById(R.id.offbtn);
//        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        if(bluetoothAdapter ==null){
//            StatusBluetooth.setText("Bluetooth is not available");
//        }
//        else{
//            StatusBluetooth.setText("Bluetooth is available");
//        }
//        if(bluetoothAdapter.isEnabled()){
//         img.setImageResource(R.drawable.ic_action_on);
//        }
//        else{
//            img.setImageResource(R.drawable.ic_action_off);
//        }
//        turnonbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!bluetoothAdapter.isEnabled()){
//                    showToast("Turning on bluetooth...");
//                    Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    startActivityForResult(intent ,REQUEST_ENABLE_BT);
//                }
//                else{
//                    showToast("Bluetooth is already on ");
//                }
//
//            }
//        });
//        discoverable.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!bluetoothAdapter.isDiscovering()){
//                    showToast("Making your device Discoverable");
//                    Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//                    startActivityForResult(intent,REQUEST_DISCOVER_BT);
//
//                }
//
//            }
//        });
//        turnoffbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(bluetoothAdapter.isEnabled()){
//                    bluetoothAdapter.disable();
//                    showToast("Turning off");
//                    img.setImageResource(R.drawable.ic_action_off);
//                }
//                else{
//                    showToast("Bluetooth is already off");
//                }
//
//            }
//        });
//        paired.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(bluetoothAdapter.isEnabled()){
//                    paired.setText("paired Devices");
//                    Set<BluetoothDevice> devices=bluetoothAdapter.getBondedDevices();
//                    for(BluetoothDevice device:devices){
//                        pair.append("\nDevice" + device.getName() + "," + device);
//                    }
//                }
//                else{
//                    showToast("Turn on bluetooth to get paired devices");
//                }
//
//            }
//        });
//        btLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (ActivityCompat.checkSelfPermission(home.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    getLocation();
//                } else {
//                    ActivityCompat.requestPermissions(home.this,
//                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
//                }
//            }
//        });
//
//    }
//
    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                home.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                home.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
                String link= "http://maps.google.com/?q="+lat+","+longi;

            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode){
            case REQUEST_ENABLE_BT:
            if(requestCode==RESULT_OK){
                img.setImageResource(R.drawable.ic_action_on);
                showToast("bluetooth is on");
            }
            else{
                showToast("couldn't on bluetooth");
            }
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();

    }
}