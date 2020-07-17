package cn.heyanle.refus.model;

import android.text.TextUtils;
import android.util.Base64;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import cn.heyanle.refus.info.refuse.ResponseBody;
import cn.heyanle.rx.Observable;
import cn.heyanle.rx.ThreadDispatcher;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by HeYanLe on 2020/5/5 0005.
 * https://github.com/heyanLE
 */
public class Refuse {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static  final String APP_KEY = "8ffc19595c20802d6b95b02d987f62e9";
    private static final String SECRET_KEY = "1dc443ddc20970b1ad2f3c9faa008f44";
    private static final String IMAGE_URL = "https://aiapi.jd.com/jdai/garbageImageSearch";
    private static final String VOICE_URL = "https://aiapi.jd.com/jdai/garbageVoiceSearch";
    private static final String TEXT_URL = "https://aiapi.jd.com/jdai/garbageTextSearch";

    public static Observable<ResponseBody> textSearch(final String text , final String cityId){
        return Observable.of(new Observable.Returnable<ResponseBody>() {
            @Override
            public ResponseBody onReturn() throws IOException, JSONException, NullPointerException {
                OkHttpClient client = new OkHttpClient.Builder().readTimeout(500, TimeUnit.SECONDS).build();
                RequestBody body = RequestBody.create("{text:\""+text+"\",cityId:\""+cityId+"\"}",JSON);
                Request request = new Request.Builder().url(getUrl(TEXT_URL)).post(body).build();
                Call call = client.newCall(request);
                Response response = call.execute();
                String s = Objects.requireNonNull(response.body()).string();
                JSONObject jsonObject = new JSONObject(s);
                return ResponseBody.of(jsonObject);
            }
        }).subscribeOn(ThreadDispatcher.IO_THREAD);
    }

    public static Observable<ResponseBody> imageSearch(final String path , final String cityId){
        return Observable.of(new Observable.Returnable<ResponseBody>() {
            @Override
            public ResponseBody onReturn() throws IOException, JSONException, NullPointerException {
                if(! new File(path).exists() || ! new File(path).isFile()){
                    return null;
                }
                OkHttpClient client = new OkHttpClient.Builder().readTimeout(500, TimeUnit.SECONDS).build();
                RequestBody body = RequestBody.create("{imgBase64:\""+img2base64(path)
                        +"\",cityId:\""+cityId+"\"}",JSON);
                Request request = new Request.Builder().url(getUrl(IMAGE_URL)).post(body).build();
                Call call = client.newCall(request);
                Response response = call.execute();
                JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                return ResponseBody.of(jsonObject);
            }
        }).subscribeOn(ThreadDispatcher.IO_THREAD);
    }

    public static Observable<ResponseBody> voiceSearch(final String path , final String format  , final String cityId){
        return Observable.of(new Observable.Returnable<ResponseBody>() {
            @Override
            public ResponseBody onReturn() throws Exception {
                File file = new File(path);
                if(! file.exists() || ! file.isFile()){
                    return null;
                }
                InputStream inputStream = new FileInputStream(path);
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);

                OkHttpClient client = new OkHttpClient.Builder().readTimeout(500, TimeUnit.SECONDS).build();
                RequestBody requestBody = RequestBody.create(bytes);
                Request request = new Request.Builder().url(getUrl(VOICE_URL))
                        .addHeader("cityId",cityId).addHeader("property",
                                "{\"autoend\":false," +
                                "\"encode\":{" +
                                "\"channel\":1," +
                                "\"format\":\""+format+"\"," +
                                "\"sample_rate\":16000," +
                                "\"post_process\":0" +
                                "}," +
                                "\"platform\":\"Android&"+android.os.Build.MODEL+"&"+android.os.Build.VERSION.RELEASE
                                        +"\"," +
                                "\"version\":\"0.0.0.1\"}").post(requestBody).build();


                Call call = client.newCall(request);
                Response response = call.execute();
                String s = Objects.requireNonNull(response.body()).string();
                JSONObject jsonObject = new JSONObject(s);
                return ResponseBody.of(jsonObject);
            }
        });
    }

    private static String img2base64(String path){
        if(TextUtils.isEmpty(path)){
            return null;
        }
        byte[] data = null;
        String result = null;
        try(InputStream is = new FileInputStream(path)){
            data = new byte[(int)new File(path).length()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data,Base64.DEFAULT);
        } catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }

    private static String getUrl(String url){

        long time = System.currentTimeMillis();
        return url + "?appkey=" + APP_KEY +
                "&timestamp=" + time +
                "&sign=" + MD5(SECRET_KEY + time);

    }

    @NonNull
    public static String MD5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}

