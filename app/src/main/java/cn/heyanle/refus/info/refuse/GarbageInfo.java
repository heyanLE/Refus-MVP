package cn.heyanle.refus.info.refuse;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by HeYanLe on 2020/5/5 0005.
 * https://github.com/heyanLE
 */
public class GarbageInfo implements Comparable<GarbageInfo>{

    private String mCateName = "湿垃圾";
    private String mCityId = "310000";
    private String mCityName = "上海市";
    private double mConfidence = 1d;
    private String mGarbageName = "坚果炒货";
    private String mPS = "投放建议：容器与外包装为可回收物";

    public String getCateName() {
        return mCateName;
    }

    public GarbageInfo setCateName(String mCateName) {
        this.mCateName = mCateName;
        return this;
    }

    public String getCityId() {
        return mCityId;
    }

    public GarbageInfo setCityId(String mCityId) {
        this.mCityId = mCityId;
        return this;
    }

    public String getCityName() {
        return mCityName;
    }

    public GarbageInfo setCityName(String mCityName) {
        this.mCityName = mCityName;
        return this;
    }

    public double getConfidence() {
        return mConfidence;
    }

    public GarbageInfo setConfidence(double mConfidence) {
        this.mConfidence = mConfidence;
        return this;
    }

    public String getGarbageName() {
        return mGarbageName;
    }

    public GarbageInfo setGarbageName(String garbage_name) {
        this.mGarbageName = garbage_name;
        return this;
    }

    public String getPS() {
        return mPS;
    }

    public GarbageInfo setPS(String mPS) {
        this.mPS = mPS;
        return this;
    }

    @Override
    public int compareTo(GarbageInfo o) {
        if (mConfidence < o.mConfidence) {
            return 1;
        }else if(mConfidence == o.mConfidence){
            return 0;
        }
        return -1;
    }

    @Nullable
    public static GarbageInfo of(JSONObject object){

        GarbageInfo info = new GarbageInfo();
        try {
            info.setCateName(object.getString("cate_name"))
                    .setCityId(object.getString("city_id"))
                    .setCityName(object.getString("city_name"))
                    .setConfidence(object.getDouble("confidence"))
                    .setGarbageName(object.getString("garbage_name"))
                    .setPS(object.getString("ps"));
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
        return info;


    }
}
