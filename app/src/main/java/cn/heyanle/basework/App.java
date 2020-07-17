package cn.heyanle.basework;

import android.app.Application;
import android.content.Context;
import android.os.Handler;


import java.io.PrintWriter;
import java.io.StringWriter;

import cn.heyanle.basework.activity.NulActivity;
import cn.heyanle.language.LanguageUtils;
import cn.heyanle.rx.ThreadDispatcher;
import cn.heyanle.theme.ThemeUtils;


/**
 * Created by HeYanLe on 2020/2/24 0024.
 * https://github.com/heyanLE
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /*
        CrashHandler初始化
         */
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));

        ThemeUtils.getInstance().init(this);

        ThreadDispatcher.getInstance().init(new Handler());

        LanguageUtils.getInstance().init(this);
    }
}

class CrashHandler implements Thread.UncaughtExceptionHandler{

    private Context mContext ;

    CrashHandler(Context context){
        mContext = context;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

        /*
        打印报错信息
         */
        StringWriter stringWriter = new StringWriter();
        try{
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            Throwable th = e.getCause();
            while (th != null){
                th.printStackTrace(printWriter);
                th = th.getCause();
            }
        }catch (Exception e1){
            e1.printStackTrace();
        }

        /*
        Log输出（只有DeBug模式才会输出）
         */
        HeLog.e("ThreadCrash",stringWriter.toString(),this);

        /*
        启动崩溃界面Activity
         */

        NulActivity.start(mContext ,stringWriter.toString());

    }
}
