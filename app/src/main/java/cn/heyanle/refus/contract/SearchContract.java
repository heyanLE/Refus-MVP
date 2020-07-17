package cn.heyanle.refus.contract;

import android.content.Context;

import java.util.List;

import cn.heyanle.refus.info.mostserched.MostSearchedInfo;
import cn.heyanle.refus.info.refuse.GarbageInfo;
import cn.heyanle.refus.presenter.SearchPresenter;
import cn.heyanle.refus.view.adapter.HistoryAdapter;
import cn.heyanle.refus.view.adapter.MostAdapter;
import cn.heyanle.refus.view.adapter.ResultAdapter;
import cn.heyanle.refus.view.adapter.SuggestAdapter;

/**
 * Created by HeYanLe on 2020/7/17 0017 10:33.
 * https://github.com/heyanLE
 */
public class SearchContract {

    public static SearchContract.IPresenter bind(IActivity iActivity){
        return new SearchPresenter(iActivity);
    }

    public interface IActivity{
        void showLoading();
        void dismissLoading();


        void setMostSearched(List<MostSearchedInfo> mostSearches);
        void setHistory();
        List<String> getHistory();

        void setSuggest(List<String> suggest);
        void dismissSuggest();
        void showSuggest();

        void setResult(List<GarbageInfo> garbage);
        void dismissResult();
        void showResult();

        Context getContext();
    }

    public interface IPresenter{

        void search(String msg, boolean isHistory);
        void backHome();
        void onSuggest(String text);

        void unbind();

    }


}
