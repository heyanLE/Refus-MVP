package cn.heyanle.refus.info.mostserched;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HeYanLe on 2020/5/5 0005.
 * https://github.com/heyanLE
 */
public class ResponseBody {

    private int mCode = 0;
    private String mMsg = "success";
    private List<MostSearchedInfo> mMostSearchedInfo = new ArrayList<>();

    public int getCode() {
        return mCode;
    }

    public ResponseBody setCode(int mCode) {
        this.mCode = mCode;
        return this;
    }

    public String getMsg() {
        return mMsg;
    }

    public ResponseBody setMsg(String mMsg) {
        this.mMsg = mMsg;
        return this;
    }

    public List<MostSearchedInfo> getMostSearched() {
        return mMostSearchedInfo;
    }

    public ResponseBody setMostSearched(List<MostSearchedInfo> mMostSearchedInfo) {
        this.mMostSearchedInfo = mMostSearchedInfo;
        return this;
    }

    @Nullable
    public static ResponseBody of(JSONObject jsonObject){
        ResponseBody responseBody = new ResponseBody();
        try {
            responseBody.mCode = jsonObject.getInt("code");
            responseBody.mMsg = jsonObject.getString("msg");
            JSONArray jsonArray = jsonObject.getJSONArray("newslist");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                MostSearchedInfo searched = MostSearchedInfo.of(object);
                responseBody.mMostSearchedInfo.add(searched);
            }
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
        return responseBody;
    }
}
