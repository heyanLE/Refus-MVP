package cn.heyanle.rx;

import cn.heyanle.basework.HeLog;

/**
 * Created by HeYanLe on 2020/5/5 0005.
 * https://github.com/heyanLE
 */
public abstract class Observer <T> {

    public abstract void onNext(T msg);

    void onError(Exception e){
        e.printStackTrace();
    }

}
