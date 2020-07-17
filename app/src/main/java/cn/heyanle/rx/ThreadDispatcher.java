package cn.heyanle.rx;

import android.os.Handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 线程调度者
 * Created by HeYanLe on 2020/5/5 0005.
 * https://github.com/heyanLE
 */
public class ThreadDispatcher {

    public static final int ANDROID_UI_THREAD = 0;
    public static final int IO_THREAD = 1;
    public static final int NEW_THREAD = 2;

    /*
    Handler 用于回到主线程
     */
    private Handler mHandler;

    /*
    IO 线程池
     */
    private ExecutorService mIOThreadPool = Executors.newFixedThreadPool(5);


    public void init(Handler handler){
        mHandler = handler;
    }

    public void runOn(int thread ,Runnable runnable){
        switch (thread){
            case ANDROID_UI_THREAD:
                mHandler.post(runnable);
                break;
            case IO_THREAD:
                mIOThreadPool.execute(runnable);
                break;
            case NEW_THREAD:
                new Thread(runnable).start();
                break;
        }
    }


    private static ThreadDispatcher INSTANCE = new ThreadDispatcher();
    private ThreadDispatcher(){};
    public static ThreadDispatcher getInstance(){
        return INSTANCE;
    }

}
