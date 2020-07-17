package cn.heyanle.permission;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Build;

/**
 * Created by HeYanLe on 2020/5/11 0011.
 * https://github.com/heyanLE
 */
public class PermissionHelper {

    private PermissionsFragment mPermissionFragment;

    public interface Callback{
        void onPermission(boolean isPermission);
    }

    // 启动当前权限页面的公开接口
    public void requestPermission(Callback permissionResultCallback, String[] permissions) {
        if (!isMarshmallow()) {
            if (permissionResultCallback!=null){
                permissionResultCallback.onPermission(true);
            }
            return;
        }
        mPermissionFragment.setPermissionResultCallback(permissionResultCallback);
        mPermissionFragment.requestPermissions(permissions);
    }

    private boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private PermissionsFragment getPermissionsFragment(Activity activity) {
        boolean isNewInstance = mPermissionFragment == null;
        if (isNewInstance) {
            mPermissionFragment = new PermissionsFragment();
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(mPermissionFragment, "PermissionHelper")
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return mPermissionFragment;
    }

    public static PermissionHelper of(Activity activity){
        PermissionHelper helper = new PermissionHelper();
        helper.mPermissionFragment = helper.getPermissionsFragment(activity);
        return helper;
    }

}
