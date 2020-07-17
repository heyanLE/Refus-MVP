package cn.heyanle.refus.view.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import cn.heyanle.audio.AudioDialog;
import cn.heyanle.bannerview.BannerView;
import cn.heyanle.bannerview.DotLinear;
import cn.heyanle.bannerview.imginfo.DrawableInfo;
import cn.heyanle.bannerview.imginfo.ImageRing;
import cn.heyanle.basework.activity.BaseActivity;
import cn.heyanle.language.LanguageUtils;
import cn.heyanle.permission.PermissionHelper;
import cn.heyanle.refus.R;
import cn.heyanle.theme.ThemeUtils;
import cn.heyanle.viewbinder.BindView;
import cn.heyanle.viewbinder.Binder;

/**
 * Created by HeYanLe on 2020/5/4 0004.
 * https://github.com/heyanLE
 */
public class MainActivity extends BaseActivity {

    public final static int CHOOSE_PHOTO = 0;
    public final static int CAMERA_PHOTO = 1;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.banner)
    BannerView bannerView;

    @BindView(R.id.dot_linear)
    DotLinear dotLinear;

    @BindView(R.id.relative_text_search)
    RelativeLayout textSearch;

    @BindView(R.id.relative_image_search)
    RelativeLayout imageSearch;

    @BindView(R.id.relative_voice_search)
    RelativeLayout voiceSearch;

    @BindView(R.id.relative_theme)
    RelativeLayout theme;

    @BindView(R.id.relative_language)
    RelativeLayout language;

    ImageRing ring;

    Handler handler;

    PermissionHelper permissionHelper;

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            bannerView.scrollNext();
            handler.postDelayed(runnable ,4000);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Binder.bind(this);

        setSupportActionBar(toolbar);

        handler = new Handler();

        permissionHelper = PermissionHelper.of(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        ring = new ImageRing();
        ring.addImageInfo(DrawableInfo.of(R.drawable.f ,this).prepare());
        ring.addImageInfo(DrawableInfo.of(R.drawable.s ,this).prepare());
        ring.addImageInfo(DrawableInfo.of(R.drawable.t ,this).prepare());
        bannerView.setImageRing(ring);
        bannerView.bindDotLinear(dotLinear);
        bindClick();

        handler.post(runnable);

    }

    public void bindClick(){

        theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemeUtils.getInstance().showThemeDialog(MainActivity.this);
            }
        });

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LanguageUtils.getInstance().showLanguageDialog(MainActivity.this);
            }
        });

        textSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.start(MainActivity.this);
            }
        });

        imageSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setItems(getResources().getStringArray(R.array.image_search), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(intent, CHOOSE_PHOTO);
                                dialog.dismiss();
                                break;
                            case 1:
                                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //系统常量， 启动相机的关键
                                startActivityForResult(openCameraIntent, CAMERA_PHOTO); // 参数常量为自定义的request code, 在取返回结果时有用
                                dialog.dismiss();
                                break;
                        }
                    }
                });
                builder.show();
            }
        });

        voiceSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionHelper.requestPermission(new PermissionHelper.Callback() {
                    @Override
                    public void onPermission(boolean isPermission) {
                        if (isPermission){
                            AudioDialog.show(MainActivity.this, new AudioDialog.OnFinish() {
                                @Override
                                public void finish(String path) {
                                    ResultActivity.startVoice(MainActivity.this ,path);
                                }
                            });
                        }else {
                            Toast.makeText(MainActivity.this ,R.string.no_audio_permission ,Toast.LENGTH_SHORT).show();
                        }
                    }
                } ,new String[]{Manifest.permission.RECORD_AUDIO});
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK && data != null) {
                    Uri imageUri = data.getData();
                    ResultActivity.startImage(MainActivity.this ,imageUri);
                }
            case CAMERA_PHOTO:
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    if (bm != null) {
                        ResultActivity.startImage(MainActivity.this, bm);
                    }
                }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ring.recycler();
        handler.removeCallbacks(runnable);
    }
}
