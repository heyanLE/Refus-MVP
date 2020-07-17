package cn.heyanle.refus.info.suggest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HeYanLe on 2020/5/6 0006.
 * https://github.com/heyanLE
 */
public class ResponseBody {

    private List<List<String>> result = new ArrayList<>();

    public List<List<String>> getResult() {
        return result;
    }

    public static ResponseBody of(JSONObject object){
        ResponseBody body = new ResponseBody();
        try{
            JSONArray array = object.getJSONArray("result");
            for(int i = 0 ; i < array.length() ; i ++){
                JSONArray a = array.getJSONArray(i);
                List<String> ss = new ArrayList<String>();
                ss.add(a.getString(0));
                ss.add(a.getString(1));
                body.result.add(ss);
            }
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
        return body;
    }
}
