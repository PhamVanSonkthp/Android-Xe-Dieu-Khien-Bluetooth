package com.infinity.xe_tu_hanh_arduino;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    Set<BluetoothDevice> pairedDevices;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    String address = null, name = null;
    TextView textView, t1, t2, txtSpeed;
    Button btnDown, btnUp, btnLeft, btnRight, btnLess, btnMore;
    public static final String[] BLUETOOTH_PERMISSIONS_S = {android.Manifest.permission.BLUETOOTH_CONNECT};
    int speed = 255;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textView = findViewById(R.id.txt);
        t1 = findViewById(R.id.textView1);
        t2 = findViewById(R.id.textView2);
        txtSpeed = findViewById(R.id.txtSpeed);
        btnDown = findViewById(R.id.btnDown);
        btnUp = findViewById(R.id.btnUp);
        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);
        btnLess = findViewById(R.id.btnLess);
        btnMore = findViewById(R.id.btnMore);

        try {
            setw();
        } catch (IOException e) {
            e.printStackTrace();
        }
        addEvents();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addEvents() {
        t2.setOnClickListener(view -> {
            t1.setVisibility(View.GONE);
            t2.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        });

        btnLess.setOnClickListener(view -> {
            speed -= 10;
            if (speed < 1) speed = 1;
            txtSpeed.setText(speed + "");
            turnRobot("LESS");
        });

        btnMore.setOnClickListener(view -> {
            speed += 10;
            if (speed > 255) speed = 255;
            txtSpeed.setText(speed + "");
            turnRobot("MORE");
        });

        btnDown.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                turnRobot("DOWN");
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                turnRobot("IDLE");
            }

            return false;
        });

        btnLeft.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                turnRobot("LEFT");
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                turnRobot("IDLE");
            }

            return false;
        });

        btnRight.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                turnRobot("RIGHT");
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                turnRobot("IDLE");
            }

            return false;
        });

        btnUp.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                turnRobot("UP");
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                turnRobot("IDLE");
            }
            return false;
        });
    }

    private void setw() throws IOException {
        bluetooth_connect_device();
    }

    private void bluetooth_connect_device() throws IOException {
        try {
            myBluetooth = BluetoothAdapter.getDefaultAdapter();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    EasyPermissions.requestPermissions(this, "Bạn cần cấp quyền Bluetooth", 20, BLUETOOTH_PERMISSIONS_S);
                }
            }
            address = myBluetooth.getAddress();
            pairedDevices = myBluetooth.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice bt : pairedDevices) {
                    address = bt.getAddress();
                    name = bt.getName();
                    Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception we) {
        }
        myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
        btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
        btSocket.connect();
        try {
            t1.setText("BT Name: " + name + "\nBT Address: " + address);
        } catch (Exception e) {
        }
    }

    private void turnRobot(String i) {
        try {
            if (btSocket != null) {
                btSocket.getOutputStream().write(i.getBytes());
            }
        } catch (Exception e) {
        }
    }
}