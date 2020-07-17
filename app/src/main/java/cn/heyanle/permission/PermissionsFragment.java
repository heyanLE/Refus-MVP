package cn.heyanle.permission;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionsFragment extends Fragment {
    private final int PERMISSIONS_REQUEST_CODE = 1000;

    private PermissionHelper.Callback mPermissionResultCallback;


    @TargetApi(Build.VERSION_CODES.M)
    void requestPermissions(@NonNull String[] permissions) {
        List<String> permission = new ArrayList();

        for(int i = 0; i < permissions.length; ++i) {
            if (this.lacksPermission(permissions[i])) {
                permission.add(permissions[i]);
            }
            Log.d("permission = ", permissions[i] + "");
        }

        if (permission.size() > 0) {
            this.requestPermissions((String[])permission.toArray(new String[permission.size()]), 1000);
        } else if (mPermissionResultCallback != null) {
            mPermissionResultCallback.onPermission(true);
        }


    }

    public void setPermissionResultCallback(PermissionHelper.Callback permissionResultCallback) {
        this.mPermissionResultCallback = permissionResultCallback;
    }

    // 判断权限集合
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    // 判断是否缺少权限
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(getContext(), permission) ==
                PackageManager.PERMISSION_DENIED;
    }


    // 含有全部的权限
    public boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != PERMISSIONS_REQUEST_CODE) {
            return;
        }
        if (mPermissionResultCallback != null) {
            mPermissionResultCallback.onPermission(hasAllPermissionsGranted(grantResults));
        }
    }


}
