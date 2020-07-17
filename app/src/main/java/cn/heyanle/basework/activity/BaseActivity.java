package cn.heyanle.basework.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.heyanle.language.LanguageUtils;
import cn.heyanle.theme.ThemeUtils;

/**
 * Created by HeYanLe on 2020/2/24 0024.
 * https://github.com/heyanLE
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(ThemeUtils.getInstance().getNowThemeId());
        LanguageUtils.getInstance().language(this);
        super.onCreate(savedInstanceState);
        //ActivityMaster.getInstance()
        ActivityMaster.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityMaster.getInstance().removeActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //ActivityMaster.getInstance().onActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //ActivityMaster.getInstance().onActivityStop();
    }
}
