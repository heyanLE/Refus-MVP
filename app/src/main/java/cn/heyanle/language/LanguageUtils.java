package cn.heyanle.language;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AlertDialog;

import java.util.List;
import java.util.Locale;

import cn.heyanle.basework.HeLog;
import cn.heyanle.basework.activity.ActivityMaster;
import cn.heyanle.refus.R;
import cn.heyanle.refus.utils.SPUtils;

/**
 * Created by HeYanLe on 2020/5/8 0008.
 * https://github.com/heyanLE
 */
public class LanguageUtils {

    private int lang = 0;
    private Locale nowLocal;

    public void init(Context context){
        lang = (Integer) SPUtils.get(context ,SPUtils.KEY_LANGUAGE ,0);
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        switch (lang){
            case 0:
                configuration.setLocale(Locale.getDefault());
                nowLocal = Locale.getDefault();
                break;
            case 1:
                configuration.setLocale( Locale.CHINESE);
                nowLocal = Locale.CHINESE;
                break;
            case 2:
                configuration.setLocale(Locale.ENGLISH);
                nowLocal = Locale.ENGLISH;
            default:
                break;
        }
        resources.updateConfiguration(configuration,displayMetrics);
        //refreshLanguage(context);
    }

    public void language(Activity activity){
        Resources resources = activity.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        switch (lang){
            case 0:
                configuration.setLocale(Locale.getDefault());
                nowLocal = Locale.getDefault();
                break;
            case 1:
                configuration.setLocale( Locale.CHINESE);
                nowLocal = Locale.CHINESE;
                break;
            case 2:
                configuration.setLocale(Locale.ENGLISH);
                nowLocal = Locale.ENGLISH;
            default:
                break;
        }
        resources.updateConfiguration(configuration,displayMetrics);
    }

    public void showLanguageDialog(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.language_switch);
        builder.setSingleChoiceItems(context.getResources().getStringArray(R.array.language), lang, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                lang = which;
                Resources resources = context.getResources();
                DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                Configuration configuration = resources.getConfiguration();
                switch (lang){
                    case 0:
                        configuration.setLocale(Locale.getDefault());
                        nowLocal = Locale.getDefault();
                        break;
                    case 1:
                        configuration.setLocale( Locale.CHINESE);
                        nowLocal = Locale.CHINESE;
                        break;
                    case 2:
                        configuration.setLocale(Locale.ENGLISH);
                        nowLocal = Locale.ENGLISH;
                    default:
                        break;
                }
                resources.updateConfiguration(configuration,displayMetrics);
                SPUtils.put(context ,SPUtils.KEY_LANGUAGE ,lang);
                dialog.dismiss();
                refreshLanguage(context);
            }
        });
        builder.show();
    }

    public void refreshLanguage(Context context){
        List<Activity> activities = ActivityMaster.getInstance().getAllActivities();
        if (activities.size() < 1){
            return;
        }

        for(int i = 0 ; i < activities.size() -1 ; i ++){
            activities.get(i).recreate();
            HeLog.i("Recreate" , activities.get(i).toString(),this);
        }

        Activity activity = activities.get(activities.size()-1);
        Intent intent = new Intent(context, activity.getClass());
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//activity跳转动画
        activity.finish();
    }

    private static LanguageUtils INSTANCE = new LanguageUtils();
    private LanguageUtils(){}
    public static LanguageUtils getInstance(){
        return INSTANCE;
    }

}
