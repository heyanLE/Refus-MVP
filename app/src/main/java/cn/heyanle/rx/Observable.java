package cn.heyanle.rx;

/**
 * 被观察者
 * 指定一个 Returnable
 * 绑定观察者后执行 Returnable 并传给 观察者
 * 支持线程调度
 * Created by HeYanLe on 2020/5/5 0005.
 * https://github.com/heyanLE
 * @see Observer
 */
public class Observable <T> {

    public interface Returnable<T> {
        T onReturn() throws Exception;
    }


    private int mObservableThread = ThreadDispatcher.ANDROID_UI_THREAD;
    private int mObserverThread = ThreadDispatcher.ANDROID_UI_THREAD;
    private Returnable<T> mReturnable = null;


    /**
     * 订阅 - 开始工作
     * @param observer 观察者
     */
    public void subscribe(final Observer<T> observer){
        if (mReturnable == null){
            return;
        }
        /*
         * 通过 ThreadDispatcher 进行线程调度
         */
        ThreadDispatcher.getInstance().runOn(mObservableThread, new Runnable() {
            @Override
            public void run() {
                final T msg;
                try{
                    msg = mReturnable.onReturn();
                    ThreadDispatcher.getInstance().runOn(mObserverThread, new Runnable() {
                        @Override
                        public void run() {
                            observer.onNext(msg);
                        }
                    });
                }catch (final Exception e){
                    ThreadDispatcher.getInstance().runOn(mObserverThread, new Runnable() {
                        @Override
                        public void run() {
                            observer.onError(e);
                        }
                    });
                }
            }
        });
    }


    public Observable<T> subscribeOn(int mObservableThread) {
        this.mObservableThread = mObservableThread;
        return this;
    }

    public Observable<T> observerOn(int mObserverThread) {
        this.mObserverThread = mObserverThread;
        return this;
    }

    private Observable(){};

    public static<T> Observable<T> of(Returnable<T> returnable){
        Observable<T> observable = new Observable<>();
        observable.mReturnable = returnable;
        return observable;
    }

}
