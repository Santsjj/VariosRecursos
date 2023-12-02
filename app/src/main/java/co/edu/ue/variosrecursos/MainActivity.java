package co.edu.ue.variosrecursos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private Activity activity;
    //Versión
    private TextView versionAndroid;
    private int versionSDK;
    //Bateria
    private ProgressBar pbLevelBattery;
    private TextView tvLevelBattery;
    IntentFilter batteryFilter;
    //Conexión
    private TextView tvConexion;
    ConnectivityManager conexion;
    //Linterna
    CameraManager cameraManager;
    String camaraId;
    private Button btnOnFlash;
    private Button btnOffFlash;
    private Button btnOnBlue;
    private Button btnOffBlue;
    //Archivos
    private EditText nameFile;
    private BluetoothAdapter bluetoothAdapter;
    private clFile clFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        objInit();
        this.context = getApplicationContext();
        this.activity = this;
        btnOnFlash.setOnClickListener(this::onLight);
        btnOffFlash.setOnClickListener(this::offLight);
        btnOnBlue.setOnClickListener(this::onBlue);
        btnOffBlue.setOnClickListener(this::offBlue);
        batteryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(broReceiver, batteryFilter);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Verificar y solicitar permisos necesarios si no están concedidos
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT},
                    REQUEST_ENABLE_BLUETOOTH);
        }
    }

    private void offBlue(View view) {
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
            Toast.makeText(this, "Bluetooth apagado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Bluetooth ya está apagado", Toast.LENGTH_SHORT).show();
        }
    }
    private void onBlue(View view) {
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT},
                        REQUEST_ENABLE_BLUETOOTH);
            } else {
                startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH);
            }
        }
    }

    // Manejar el resultado de la solicitud de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, realizar las acciones necesarias
                // Puedes llamar a onBlue() u otra lógica aquí
            } else {
                // Permiso denegado, manejar según sea necesario
            }
        }
    }



    //Bateria
    BroadcastReceiver broReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int levelBattery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
            pbLevelBattery.setProgress(levelBattery);
            tvLevelBattery.setText("Level Battery:"+levelBattery+" %");
        }
    };

    //Conexion
    private void checkConnection(){
        conexion = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conexion.getActiveNetworkInfo();
        boolean stateNet = networkInfo != null && networkInfo.isConnectedOrConnecting();
        if (stateNet) tvConexion.setText("State On");
        else tvConexion.setText("State Off");
    }

    //Version Android
    @Override
    protected void onPostResume() {
        super.onPostResume();
        String versionSO = Build.VERSION.RELEASE;
        versionSDK = Build.VERSION.SDK_INT;
        versionAndroid.setText("Version SO: "+versionSO+" / SDK: "+versionSDK);
    }

    //Linterna
    private void onLight(View view) {
        try {
            cameraManager= (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            camaraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(camaraId, true);
        } catch (CameraAccessException e) {
            throw new RuntimeException("En la linterna"+e);
        }
    }
    private void offLight(View view) {
        try {
            cameraManager.setTorchMode(camaraId, false);
        } catch (CameraAccessException e) {
            throw new RuntimeException("En la linterna"+e);
        }
    }


    private void objInit(){
        this.versionAndroid = findViewById(R.id.tvVersionAndroid);
        this.pbLevelBattery = findViewById(R.id.pbLevelBattery);
        this.tvLevelBattery=findViewById(R.id.tvLevelBattery);
        this.tvConexion=findViewById(R.id.tvConexion);
        this.btnOffFlash = findViewById(R.id.btnOff);
        this.btnOnFlash = findViewById(R.id.btnOn);
        this.nameFile= findViewById(R.id.etNameFile);
        this.btnOnBlue=findViewById(R.id.btnOnBlue);
        this.btnOffBlue=findViewById(R.id.btnOffBlue);

    }
}