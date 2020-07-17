package cn.heyanle.refus.info.refuse;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by HeYanLe on 2020/5/5 0005.
 * https://github.com/heyanLE
 */
public class ResponseBody {

    private String code = "10000";
    private boolean mCharge = false;
    private int mRemain = 0;
    private String mMsg = "";
    ResultBody mResultBody;

    public String getCode() {
        return code;
    }

    public ResponseBody setCode(String code) {
        this.code = code;
        return this;
    }

    public boolean isCharge() {
        return mCharge;
    }

    public ResponseBody setCharge(boolean charge) {
        this.mCharge = charge;
        return this;
    }

    public int getRemain() {
        return mRemain;
    }

    public ResponseBody setRemain(int remain) {
        this.mRemain = remain;
        return this;
    }

    public String getMsg() {
        return mMsg;
    }

    public ResponseBody setMsg(String msg) {
        this.mMsg = msg;
        return this;
    }

    public ResultBody getResultBody() {
        return mResultBody;
    }

    public ResponseBody setResultBody(ResultBody resultBody) {
        this.mResultBody = resultBody;
        return this;
    }

    @Nullable
    public static ResponseBody of(JSONObject object){
        ResponseBody body = new ResponseBody();
        try {
            body.setCode(object.getString("code"))
                    .setCharge(object.getBoolean("charge"))
                    .setRemain(object.getInt("remain"))
                    .setMsg(object.getString("msg"))
                    .setResultBody(ResultBody.of(object.getJSONObject("result")));
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
        return body;

    }

}
