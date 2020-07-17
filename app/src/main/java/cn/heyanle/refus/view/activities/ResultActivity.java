package cn.heyanle.refus.view.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.heyanle.basework.activity.BaseActivity;
import cn.heyanle.refus.R;
import cn.heyanle.refus.view.adapter.ResultAdapter;
import cn.heyanle.refus.info.refuse.GarbageInfo;
import cn.heyanle.refus.info.refuse.ResponseBody;
import cn.heyanle.refus.model.Refuse;
import cn.heyanle.refus.utils.BitmapUriUtil;
import cn.heyanle.refus.utils.BitmapUtil;
import cn.heyanle.rx.Observable;
import cn.heyanle.rx.Observer;
import cn.heyanle.rx.ThreadDispatcher;
import cn.heyanle.theme.ThemeUtils;
import cn.heyanle.viewbinder.BindView;
import cn.heyanle.viewbinder.Binder;

/**
 * Created by HeYanLe on 2020/5/10 0010.
 * https://github.com/heyanLE
 */
public class ResultActivity extends BaseActivity {

    static Uri URL;
    static String PATH = "";
    static int TYPE = 0;
    static Bitmap BITMAP;

    public static void startImage(Context context ,Uri url){
        TYPE = 0;
        URL = url;
        Intent intent = new Intent(context ,ResultActivity.class);
        context.startActivity(intent);
    }

    public static void startImage(Context context ,Bitmap bitmap){
        TYPE = -1;
        BITMAP = bitmap;
        Intent intent = new Intent(context ,ResultActivity.class);
        context.startActivity(intent);
    }

    public static void startVoice(Context context ,String path){
        TYPE = 1;
        PATH = path;
        Intent intent = new Intent(context ,ResultActivity.class);
        context.startActivity(intent);
    }



    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    ResultAdapter resultAdapter;
    List<GarbageInfo> results  = new ArrayList<>();

    @BindView(R.id.img)
    ImageView imageView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Binder.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if(TYPE == 0 || TYPE == -1){
            getSupportActionBar().setTitle(R.string.image_search);
        }else if(TYPE == 1){
            getSupportActionBar().setTitle(R.string.voice_search);
        }

        resultAdapter = new ResultAdapter(results ,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(resultAdapter);

        refreshLayout.setColorSchemeColors(ThemeUtils.getAttrColor(this ,R.attr.colorPrimary));
        refreshLayout.setEnabled(false);

        refreshLayout.setRefreshing(true);

        if (TYPE == 0) {
            try {
                final Bitmap imageBitmap = BitmapUriUtil.getBitmap(ResultActivity.this, URL);
                final String choosePath = getExternalCacheDir() + "/choose";
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(imageBitmap);
                ThreadDispatcher.getInstance().runOn(ThreadDispatcher.IO_THREAD, new Runnable() {
                    @Override
                    public void run() {
                        boolean b = BitmapUtil.saveBitmap(imageBitmap, choosePath);
                        if (b) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    PATH = choosePath;
                                    refresh();
                                }
                            });
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(TYPE == -1) {
            if (BITMAP == null){
                finish();
                return;
            }
            final String choosePath = getExternalCacheDir() + "/choose";

            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(BITMAP);

            ThreadDispatcher.getInstance().runOn(ThreadDispatcher.IO_THREAD, new Runnable() {
                @Override
                public void run() {
                    boolean b = BitmapUtil.saveBitmap(BITMAP, choosePath);
                    if (b) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                PATH = choosePath;
                                refresh();
                            }
                        });
                    }
                }
            });
        }else{
            imageView.setVisibility(View.GONE);
            refresh();
        }

    }

    public void refresh(){
        refreshLayout.setRefreshing(true);
        Observable<ResponseBody> observable;
        if (TYPE == 0 || TYPE == -1){
            observable = Refuse.imageSearch(PATH ,"310000");
        }else{

            observable = Refuse.voiceSearch(PATH ,"amr"  ,"310000");
        }
        observable.subscribeOn(ThreadDispatcher.IO_THREAD)
                .observerOn(ThreadDispatcher.ANDROID_UI_THREAD)
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody msg) {
                        results.clear();
                        results.addAll(msg.getResultBody().getGarbageInfo());
                        Collections.sort(results);
                        resultAdapter.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{

            BITMAP.recycle();
            BITMAP = null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
