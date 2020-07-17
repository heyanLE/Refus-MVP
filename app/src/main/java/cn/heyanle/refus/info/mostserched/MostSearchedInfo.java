package cn.heyanle.refus.info.mostserched;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by HeYanLe on 2020/5/5 0005.
 * https://github.com/heyanLE
 */
public class MostSearchedInfo {

    private String mName = "";
    private int mType = 3;
    private int mIndex = 0;

    public String getName() {
        return mName;
    }

    public MostSearchedInfo setName(String mName) {
        this.mName = mName;
        return this;
    }

    public int getType() {
        return mType;
    }

    public MostSearchedInfo setType(int mType) {
        this.mType = mType;
        return this;
    }

    public int getIndex() {
        return mIndex;
    }

    public MostSearchedInfo setIndex(int mIndex) {
        this.mIndex = mIndex;
        return this;
    }

    @Nullable
    public static MostSearchedInfo of(JSONObject jsonObject){
        MostSearchedInfo mostSearchedInfo = new MostSearchedInfo();
        try {
            mostSearchedInfo.mName = jsonObject.getString("name");
            mostSearchedInfo.mIndex = jsonObject.getInt("index");
            mostSearchedInfo.mType = jsonObject.getInt("type");
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
        return mostSearchedInfo;
    }

}
