package com.example.testmodules;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;

import com.example.testmodules.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private String TAG=this.getClass().getName();
    private ActivityMainBinding activityMainBinding;
    private String future2package="com.example.dynamicfeature2";
    private String futureLucherClass=future2package+".MainActivity2";
    SimpleDateFormat sdf4;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sdf4 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
        activityMainBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        activityMainBinding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new Thread(){
                   @Override
                   public void run() {

                       Date newTime = new Date();
                       String formatDate4 = sdf4.format(newTime);
                       Log.e(TAG, "存储归档 开始时间run: "+formatDate4);
                       try {
                           ZipEntryAnne.deflater("/sdcard/yasuoceshi/tt.iso","/sdcard/yasuoceshi2/tt2.zip");
                       } catch (Exception e) {
                           e.printStackTrace();
                           Log.e(TAG, "错误了：",e);
                       }
                       Date newTime2 = new Date();
                       String formatDate42 = sdf4.format(newTime2);
                       Log.e(TAG, "结束时间run: "+formatDate42);

                   }
               }.start();
            }
        });
        activityMainBinding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        Date newTime = new Date();
                        String formatDate4 = sdf4.format(newTime);
                        Log.e(TAG, "0压缩 开始时间run: "+formatDate4);
                        try {
                            ZipEntryAnne.createZipFile("/sdcard/yasuoceshi","/sdcard/yasuoceshi2/tt.zip");
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "错误了：",e);
                        }
                        Date newTime2 = new Date();
                        String formatDate42 = sdf4.format(newTime2);
                        Log.e(TAG, "结束时间run: "+formatDate42);
                    }
                }.start();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }
}
