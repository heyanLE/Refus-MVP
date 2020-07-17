package cn.heyanle.refus.info.refuse;

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
public class ResultBody {

    private List<GarbageInfo> mGarbageInfo = new ArrayList<>();
    private String mMessage = "success";
    private int mStatus = 0;

    public List<GarbageInfo> getGarbageInfo() {
        return mGarbageInfo;
    }

    public ResultBody setGarbageInfo(List<GarbageInfo> mGarbageInfo) {
        this.mGarbageInfo = mGarbageInfo;
        return this;
    }

    public String getMessage() {
        return mMessage;
    }

    public ResultBody setMessage(String mMessage) {
        this.mMessage = mMessage;
        return this;
    }

    public int getStatus() {
        return mStatus;
    }

    public ResultBody setStatus(int mStatus) {
        this.mStatus = mStatus;
        return this;
    }

    @Nullable
    public static ResultBody of(JSONObject object){
        ResultBody body = new ResultBody();
        try {
            body.setMessage(object.getString("message"))
                    .setStatus(object.getInt("status"));

            try {
                JSONArray array = object.getJSONArray("garbage_info");
                for (int i = 0; i < array.length(); i++) {
                    GarbageInfo info = GarbageInfo.of(array.getJSONObject(i));
                    body.getGarbageInfo().add(info);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
        return body;
    }

}
