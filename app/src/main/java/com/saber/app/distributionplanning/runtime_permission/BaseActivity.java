package com.saber.app.distributionplanning.runtime_permission;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Class description
 *
 * @author ex-huxibing552
 * @date 2017-01-03 10:28
 */
public class BaseActivity extends AppCompatActivity {


    private static final int PERMISSION_REQUEST_CODE = 0x2;
    private OnRuntimePermissionResultListener mPermissionResultListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * when need to request runtime permissions call this method
     *
     * @param permissions the permissions you need to request ,such as {@link android.Manifest.permission#CAMERA}
     * @param listener    the result call back
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void requestRuntimePermissions(String[] permissions, OnRuntimePermissionResultListener listener) {
        mPermissionResultListener = listener;
        List<String> needRequestPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                needRequestPermissions.add(permission);
            }
        }
        if (needRequestPermissions.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    needRequestPermissions.toArray(new String[needRequestPermissions.size() - 1]),
                    PERMISSION_REQUEST_CODE);
        } else {
            mPermissionResultListener.onGranted();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            List<String> deniedPermissions = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    deniedPermissions.add(permissions[i]);
                }
            }
            if (deniedPermissions.size() > 0) {
                mPermissionResultListener.onDenied(deniedPermissions.toArray(new String[deniedPermissions.size() - 1]));
            } else {
                mPermissionResultListener.onGranted();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public interface OnRuntimePermissionResultListener {
        void onDenied(String[] permission);

        void onGranted();
    }
}
