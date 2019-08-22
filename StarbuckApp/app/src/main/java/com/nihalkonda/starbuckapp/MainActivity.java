package com.nihalkonda.starbuckapp;

import android.Manifest;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nihalkonda.starbuckapp.cardactivity.MyCardsActivity;
import com.nihalkonda.starbuckapp.data.Transaction;
import com.nihalkonda.starbuckapp.data.User;
import com.nihalkonda.starbuckapp.data.Card;
import com.scottyab.aescrypt.AESCrypt;

import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

import javax.crypto.NoSuchPaddingException;


public class MainActivity extends AppCompatActivity {

    private final int QR_REQUEST_CODE = 500 ;

    private CryptLib cryptLib;

    public static Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        c=this;

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //test();
        permCheck();

//        startActivityForResult(new Intent(this,QRScannerActivity.class),QR_REQUEST_CODE);


    }



    private void permCheck() {
        System.out.println("Perm Check");
        Dexter.withActivity(this)
                .withPermissions(new String[]{
                        Manifest.permission.INTERNET,
                        Manifest.permission.CAMERA
                }).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    System.out.println("All Perm OK");
                    init();
                } else {
                    permCheck();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();

    }

    private void init() {

        startActivity(new Intent(this, GmailActivity.class));
        finish();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == QR_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String returnedResult = data.getData().toString();
                System.out.println("QR_RESULT:"+returnedResult);

                try {
                    JSONObject jsonObject = new JSONObject(returnedResult);
                    String enc = jsonObject.getString("data");

                    enc = cryptLib.decryptCipherTextWithRandomIV(enc,"SDTrocks");
                    jsonObject = new JSONObject(enc);
                    System.out.println(jsonObject.toString(3));
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }

            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(this,"Sorry, This Feature Isn't Available Right Now.",Toast.LENGTH_SHORT).show();
    }

}
