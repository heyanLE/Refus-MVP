package cn.heyanle.refus.model;

import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import cn.heyanle.refus.info.suggest.ResponseBody;
import cn.heyanle.rx.Observable;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by HeYanLe on 2020/5/6 0006.
 * https://github.com/heyanLE
 */
public class Suggest {

    public static final String URL = "https://api.zhetaoke.com:10001/api/api_suggest.ashx";
    public static final String APP_KEY = "e777e620c73542f1911f8e8ba9562f3e";

    public static Observable<ResponseBody> suggest(final String content){
        return Observable.of(new Observable.Returnable<ResponseBody>() {
            @Override
            public ResponseBody onReturn() throws Exception {
                OkHttpClient client = new OkHttpClient.Builder().readTimeout(500, TimeUnit.SECONDS).build();
                Request request = new Request.Builder().url(URL + "?appkey=" + APP_KEY
                        + "&content="+content).get().build();
                Call call = client.newCall(request);
                Response response = call.execute();
                String s = Objects.requireNonNull(response.body()).string();
                JSONObject jsonObject = new JSONObject(s);
                return ResponseBody.of(jsonObject);
            }
        });
    }

    public static String cn2Unicode(String cn) {
        char[] chars = cn.toCharArray();
        StringBuilder returnStr = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            returnStr.append("\\u").append(Integer.toString(chars[i], 16));
        }
        return returnStr.toString();
    }

}
