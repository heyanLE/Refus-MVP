package cn.heyanle.refus.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import cn.heyanle.refus.info.mostserched.ResponseBody;
import cn.heyanle.rx.Observable;
import cn.heyanle.rx.ThreadDispatcher;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 天行数据 API
 * https://www.tianapi.com/
 * Created by HeYanLe on 2020/5/5 0005.
 * https://github.com/heyanLE
 */
public class MostSearched {

    private static  final String API_KEY = "48d1b1a85d4291d14f9f9c37169d20f8";
    private static final String url = "http://api.tianapi.com/txapi/hotlajifenlei/index";
    /**
     * 获取搜索热词
     * @return  被观察者
     */
    public static Observable<ResponseBody> getMostSearched(){
        return Observable.of(new Observable.Returnable<ResponseBody>() {
            @Override
            public ResponseBody onReturn() throws IOException, JSONException, NullPointerException {
                OkHttpClient client = new OkHttpClient.Builder().readTimeout(500, TimeUnit.SECONDS).build();
                Request request = new Request.Builder().url(url + "?key=" + API_KEY).get().build();
                Call call = client.newCall(request);
                Response response = call.execute();
                JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                return ResponseBody.of(jsonObject);
            }
        }).subscribeOn(ThreadDispatcher.IO_THREAD);
    }
}
