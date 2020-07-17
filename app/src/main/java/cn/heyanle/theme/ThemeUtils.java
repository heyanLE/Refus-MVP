package cn.heyanle.theme;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AlertDialog;

import java.util.List;

import cn.heyanle.basework.HeLog;
import cn.heyanle.basework.activity.ActivityMaster;
import cn.heyanle.refus.R;
import cn.heyanle.refus.utils.SPUtils;


/**
 * Created by HeYanLe on 2020/2/25 0025.
 * https://github.com/heyanLE
 */
public class ThemeUtils {

    public final static int ThemeIdBlue = 0;
    public final static int ThemeIdPink = 1;
    public final static int ThemeIdRed = 2;
    public final static int ThemeIdYellow = 3;
    public final static int ThemeIdGreen = 4;
    public final static int ThemeIdPurple = 5;
    public final static int ThemeIdBlack = 6;
    public final static int ThemeIdGray = 7;
    public final static int ThemeIdDark = 8;

    private final static String FILE_NAME = "MrPasswordTheme";
    private final static String FILE_KEY = "MrPasswordTheme";

    private int nowTheme ;

    public static int getAttrColor(Context context, int id){
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(id, typedValue, true);
        return typedValue.data;
    }

    public void showThemeDialog(final Context context){
        @SuppressLint("InflateParams") GridView gridView = (GridView) LayoutInflater.from(context)
                .inflate(R.layout.dialog_theme, null);

        int[] themeDrawableList = new int[]{
                R.drawable.theme_blue,
                R.drawable.theme_pink,
                R.drawable.theme_red,
                R.drawable.theme_yellow,
                R.drawable.theme_green,
                R.drawable.theme_purple,
                R.drawable.theme_black,
                R.drawable.theme_gray
        };


        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.theme_switch);
        builder.setView(gridView);

        ThemeAdapter adapter = new ThemeAdapter(themeDrawableList ,context);
        gridView.setAdapter(adapter);

        final AlertDialog alertDialog = builder.show();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alertDialog.dismiss();
                refreshTheme(context,position);
            }
        });

    }

    public void init(Application application){
        nowTheme = (Integer) SPUtils.get(application ,SPUtils.KEY_THEME , 0);
        application.setTheme(getThemeIdById(nowTheme));



    }

    public void refreshTheme(Context context  ,int themeId){
        nowTheme = themeId;

        SPUtils.put(context ,SPUtils.KEY_THEME ,nowTheme);

        refreshTheme(context );
    }

    public void refreshTheme(Context context){
        List<Activity> activities = ActivityMaster.getInstance().getAllActivities();
        if (activities.size() < 1){
            return;
        }
        context.getApplicationContext().setTheme(getThemeIdById(nowTheme));
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

    public int getNowTheme(){
        return nowTheme;
    }

    public int getNowThemeId(){
        return getThemeIdById(nowTheme);
    }

    public static int getThemeIdById(int id){
        switch (id){
            case ThemeIdBlue:
                return R.style.Theme_Blue;
            case ThemeIdPink:
                return R.style.Theme_Pink;
            case ThemeIdRed:
                return R.style.Theme_Red;
            case ThemeIdYellow:
                return R.style.Theme_Yellow;
            case ThemeIdPurple:
                return R.style.Theme_Purple;
            case ThemeIdGreen:
                return R.style.Theme_Green;
            case ThemeIdBlack:
                return R.style.Theme_Black;
            case ThemeIdGray:
                return R.style.Theme_Gray;
            case ThemeIdDark:
                return R.style.Theme_Dark;
        }
        return 0;
    }

    private static ThemeUtils INSTANCE = new ThemeUtils();

    private ThemeUtils(){}

    public static ThemeUtils getInstance(){
        return INSTANCE;
    }

}
