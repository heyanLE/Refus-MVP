package cn.heyanle.basework.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.heyanle.basework.HeLog;
import cn.heyanle.theme.ThemeUtils;

/**
 * Created by HeYanLe on 2020/2/24 0024.
 * https://github.com/heyanLE
 */
public class ActivityMaster {

    public static final String KEY_FIRST_BACK = "key_first_back";

    private boolean runInBack = false;

    private List<Activity> activities = new ArrayList<>();

    private int appCount = 0;

    public void addActivity(Activity activity){
        activities.add(activity);
        activity.setTheme(ThemeUtils.getThemeIdById(ThemeUtils.getInstance().getNowTheme()));
    }

    public void removeActivity(Activity activity){
        activities.remove(activity);
    }




    public void exit(){
        for( int i = 0 ; i < activities.size() ; i ++){
            Activity activity = activities.get(i);
            if (! activity.isFinishing()){
                activity.finish();
            }
        }
    }

    public List<Activity> getAllActivities() {
        return activities;
    }

    private static ActivityMaster INSTANCE = new ActivityMaster();

    private ActivityMaster(){}

    public static ActivityMaster getInstance(){
        return INSTANCE;
    }







}
