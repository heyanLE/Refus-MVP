package cn.heyanle.refus.presenter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cn.heyanle.refus.contract.SearchContract;
import cn.heyanle.refus.info.refuse.ResponseBody;
import cn.heyanle.refus.model.MostSearched;
import cn.heyanle.refus.model.Refuse;
import cn.heyanle.refus.model.Suggest;
import cn.heyanle.refus.utils.SPUtils;
import cn.heyanle.refus.view.activities.SearchActivity;
import cn.heyanle.rx.Observer;
import cn.heyanle.rx.ThreadDispatcher;

/**
 * Created by HeYanLe on 2020/7/17 0017 10:04.
 * https://github.com/heyanLE
 */
public class SearchPresenter implements SearchContract.IPresenter {

    private SearchContract.IActivity I;

    public SearchPresenter(SearchContract.IActivity iActivity) {
        this.I = iActivity;
    }

    @Override
    public void search(final String text, final boolean isHistory) {
        I.showLoading();
        I.dismissSuggest();
        Refuse.textSearch(text ,"310000")
                .subscribeOn(ThreadDispatcher.IO_THREAD)
                .observerOn(ThreadDispatcher.ANDROID_UI_THREAD)
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onNext(cn.heyanle.refus.info.refuse.ResponseBody msg) {
                        I.showResult();
                        I.setResult(msg.getResultBody().getGarbageInfo());
                        I.dismissLoading();

                        if (! isHistory){
                            I.getHistory().add(text);
                            StringBuilder jsb = new StringBuilder("[");
                            for (int i = 0 ; i < I.getHistory().size() ; i ++){
                                jsb.append(I.getHistory().get(i)).append(",");
                            }
                            if (jsb.charAt(jsb.length()-1) == ','){
                                jsb.deleteCharAt(jsb.length()-1);
                            }
                            jsb.append("]");
                            SPUtils.put(I.getContext() ,SPUtils.KEY_SEARCH_HISTORY ,jsb.toString());
                            I.setHistory();
                        }
                    }
                });
    }

    @Override
    public void backHome() {
        I.dismissSuggest();
        I.dismissResult();
        I.showLoading();
        refreshMostSearched();
        refreshHistory();
    }

    @Override
    public void onSuggest(String text) {
        if(text.isEmpty()){
            I.dismissSuggest();
        }else{
            Suggest.suggest(text)
                    .subscribeOn(ThreadDispatcher.IO_THREAD)
                    .observerOn(ThreadDispatcher.ANDROID_UI_THREAD)
                    .subscribe(new Observer<cn.heyanle.refus.info.suggest.ResponseBody>() {
                        @Override
                        public void onNext(cn.heyanle.refus.info.suggest.ResponseBody msg) {
                            List<String> suggests = new ArrayList<>();
                            if (msg == null){
                                return;
                            }
                            for(int i = 0; i < msg.getResult().size() ; i ++){
                                suggests.add(msg.getResult().get(i).get(0));
                            }
                            I.setSuggest(suggests);
                            I.dismissLoading();
                        }
                    });
        }
    }


    @Override
    public void unbind() {

    }

    void refreshMostSearched(){
        I.showSuggest();
        MostSearched.getMostSearched()
                .subscribeOn(ThreadDispatcher.IO_THREAD)
                .observerOn(ThreadDispatcher.ANDROID_UI_THREAD)
                .subscribe(new Observer<cn.heyanle.refus.info.mostserched.ResponseBody>() {
                    @Override
                    public void onNext(cn.heyanle.refus.info.mostserched.ResponseBody msg) {
                        I.setMostSearched(msg.getMostSearched());
                        I.dismissLoading();
                    }
                });

    }

    void refreshHistory(){
        String historyJson = ""+ SPUtils.get(I.getContext() ,SPUtils.KEY_SEARCH_HISTORY ,"[]");
        try{
            JSONArray jsonArray = new JSONArray(historyJson);
            I.getHistory().clear();
            for(int i = 0 ; i < jsonArray.length() ;  i ++){
                I.getHistory().add(jsonArray.getString(i));
            }
            I.setHistory();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
