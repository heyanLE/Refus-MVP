package cn.heyanle.audio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import cn.heyanle.refus.R;
import cn.heyanle.viewbinder.BindView;
import cn.heyanle.viewbinder.Binder;

/**
 * Created by HeYanLe on 2020/5/11 0011.
 * https://github.com/heyanLE
 */
public class AudioDialog extends AlertDialog {

    @BindView(R.id.card)
    CardView cardView;

    @BindView(R.id.text)
    TextView textView;

    RecorderInfo info = RecorderInfo.of();

    public interface OnFinish{
        void finish(String path);
    }

    public static OnFinish OnFinish;

    public static void show(Context context ,OnFinish onFinish){
        AudioDialog dialog = new AudioDialog(context);
        OnFinish = onFinish;
        dialog.show();
    }

    public AudioDialog(@NonNull Context context) {
        super(context);
    }

    public AudioDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public AudioDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_audio);
        Binder.bind(this);
    }

    private long startTime;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onStart() {
        super.onStart();

        final String path = getContext().getExternalCacheDir().getAbsolutePath()+"/audio.amr";

        textView.setText(R.string.long_press_recording);
        try {
            info.prepare(path);
        }catch (Exception e){
            e.printStackTrace();
            dismiss();
        }

        cardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        info.start();
                        startTime = System.currentTimeMillis();
                        textView.setText(R.string.let_go_search);
                        return true;
                    case MotionEvent.ACTION_UP:
                        info.stop();
                        textView.setText(R.string.long_press_recording);
                        if (System.currentTimeMillis() - startTime < 100){
                            Toast.makeText(getContext() ,"时间太短了" ,Toast.LENGTH_SHORT).show();

                        }else{
                            OnFinish.finish(path);
                            dismiss();
                        }
                        break;
                }
                return false;
            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                try{
                    info.release();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
}
