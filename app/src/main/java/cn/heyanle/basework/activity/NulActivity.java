package cn.heyanle.basework.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.heyanle.refus.R;


/**
 * Created by HeYanLe on 2020/2/24 0024.
 * https://github.com/heyanLE
 */
public class NulActivity extends AppCompatActivity {

    public static String INTENT_KEY = "cn.heyanle.nul.intent.key";

    public static void start(Context context ,String errorMsg){
        Intent intent = new Intent(context ,NulActivity.class);
        intent.putExtra(INTENT_KEY ,errorMsg);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nul);

        initView();
    }

    /**
     * 初始化View
     */
    private void initView(){

        TextView tvModel = findViewById(R.id.activity_nul_tv_model);
        TextView tvAndroidVersion = findViewById(R.id.activity_nul_tv_androidVersion);
        TextView tvELog = findViewById(R.id.activity_nul_tv_eLog);

        /*
        手机型号
         */
        String model = getResources().getString(R.string.phone_model,android.os.Build.MODEL);
        tvModel.setText(model);

        /*
        安卓版本
         */
        String aVersion = getResources().getString(R.string.android_version,android.os.Build.VERSION.RELEASE);
        tvAndroidVersion.setText(aVersion);

        /*
        报错信息
         */
        Intent intent = getIntent();
        if (intent == null) return;

        String eLog = intent.getStringExtra(INTENT_KEY);

        tvELog.setText(eLog);

    }
}
