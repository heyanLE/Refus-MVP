package cn.heyanle.refus.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cn.heyanle.basework.activity.BaseActivity;
import cn.heyanle.refus.R;
import cn.heyanle.refus.contract.SearchContract;
import cn.heyanle.refus.view.adapter.HistoryAdapter;
import cn.heyanle.refus.view.adapter.MostAdapter;
import cn.heyanle.refus.view.adapter.ResultAdapter;
import cn.heyanle.refus.view.adapter.SuggestAdapter;
import cn.heyanle.refus.info.mostserched.MostSearchedInfo;
import cn.heyanle.refus.info.refuse.GarbageInfo;
import cn.heyanle.refus.utils.SPUtils;
import cn.heyanle.theme.ThemeUtils;
import cn.heyanle.viewbinder.BindView;
import cn.heyanle.viewbinder.Binder;

/**
 * Created by HeYanLe on 2020/5/8 0008.
 * https://github.com/heyanLE
 */
public class SearchActivity extends BaseActivity{

    public static void start(Context context){
        Intent intent = new Intent(context ,SearchActivity.class);
        context.startActivity(intent);
    }

    Handler mHandler = new Handler();

    @BindView(R.id.swipe)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_most)
    RecyclerView recyclerMost;
    MostAdapter mostAdapter;
    List<MostSearchedInfo> mostSearched = new ArrayList<>();

    @BindView(R.id.et_search)
    EditText etSearch;

    @BindView(R.id.tv_search)
    TextView tvSearch;

    @BindView(R.id.tv_clear)
    TextView tvClear;

    @BindView(R.id.relative_suggest)
    RelativeLayout suggestRelative;

    @BindView(R.id.suggest_recycler)
    RecyclerView suggestRecycler;
    SuggestAdapter suggestAdapter;
    List<String> suggests = new ArrayList<>();

    @BindView(R.id.recycler_result)
    RecyclerView resultRecycler;
    ResultAdapter resultAdapter;
    List<GarbageInfo> results  = new ArrayList<>();

    @BindView(R.id.recycler_history)
    RecyclerView historyRecycler;
    HistoryAdapter historyAdapter;
    List<String> histories = new ArrayList<>();

    Runnable suggestRunnable = new Runnable() {
        @Override
        public void run() {
            P.onSuggest(etSearch.getText().toString());
        }
    };

    SearchContract.IPresenter P;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_search);
        Binder.bind(this);

        P = SearchContract.bind(new ISearch());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        refreshLayout.setEnabled(false);
        refreshLayout.setColorSchemeColors(ThemeUtils.getAttrColor(this ,R.attr.colorPrimary));
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etSearch.getText().toString().isEmpty()){
                    return;
                }
                P.search(etSearch.getText().toString(), isHistory);
            }
        });

        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                histories.clear();
                SPUtils.put(SearchActivity.this, SPUtils.KEY_SEARCH_HISTORY, "[]");
                historyAdapter.notifyDataSetChanged();
            }
        });

        initMostSearched();
        initEtSearch();
        initSuggest();
        initResult();
        initHistory();

        P.backHome();
    }

    public void initMostSearched(){
        mostAdapter = new MostAdapter(mostSearched ,this);
        recyclerMost.setAdapter(mostAdapter);
        recyclerMost.setLayoutManager(new GridLayoutManager(this ,2));
        refreshLayout.setRefreshing(true);
        mostAdapter.setOnItemClick(new MostAdapter.OnItemClick() {
            @Override
            public void onClick(MostAdapter.ViewHolder holder, int position, MostAdapter mostAdapter) {
                etSearch.getText().replace(0 ,etSearch.getText().length() ,holder.getText());
                P.search(etSearch.getText().toString(), isHistory);
            }
        });
    }

    public void initSuggest(){
        suggestAdapter = new SuggestAdapter(suggests ,this);
        suggestRecycler.setLayoutManager(new LinearLayoutManager(this));
        suggestRecycler.setAdapter(suggestAdapter);

        suggestRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suggestRelative.setVisibility(View.GONE);
            }
        });
        suggestAdapter.setOnItemClick(new SuggestAdapter.OnItemClick() {
            @Override
            public void onClick(SuggestAdapter.ViewHolder holder, int position, SuggestAdapter adapter) {
                etSearch.getText().replace(0 ,etSearch.getText().length() ,holder.getText());
                P.search(etSearch.getText().toString(), isHistory);
            }
        });
    }

    public void initResult(){
        resultAdapter = new ResultAdapter(results ,this);
        resultRecycler.setAdapter(resultAdapter);
        resultRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    public void initEtSearch(){
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (etSearch.getText().toString().isEmpty()){
                    suggestRelative.setVisibility(View.GONE);
                }else{
                    suggestRelative.setVisibility(View.VISIBLE);
                }
                mHandler.removeCallbacks(suggestRunnable);
                mHandler.postDelayed(suggestRunnable ,500);

                resultRecycler.setVisibility(View.GONE);
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_SEARCH) && !etSearch.getText().toString().isEmpty()) {
                    P.search(etSearch.getText().toString(), isHistory);
                    return true;
                }
                return false;
            }
        });

    }

    boolean isHistory = false;
    public void initHistory() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL);
        historyAdapter = new HistoryAdapter(histories, SearchActivity.this);
        historyRecycler.setLayoutManager(layoutManager);
        historyRecycler.setAdapter(historyAdapter);
        historyAdapter.setOnItemClick(new HistoryAdapter.OnItemClick() {
            @Override
            public void onClick(HistoryAdapter.ViewHolder holder, int position, HistoryAdapter adapter) {
                isHistory = true;
                etSearch.getText().replace(0, etSearch.getText().length(), holder.getText());
                P.search(etSearch.getText().toString(), isHistory);
                isHistory = false;
            }
        });
    }







    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (resultRecycler.getVisibility() == View.VISIBLE){
            etSearch.getText().clear();
            return;
        }
        super.onBackPressed();

    }

    class ISearch implements SearchContract.IActivity{


        @Override
        public void showLoading() {
            refreshLayout.setRefreshing(true);
        }

        @Override
        public void dismissLoading() {
            refreshLayout.setRefreshing(false);
        }

        @Override
        public void setMostSearched(List<MostSearchedInfo> mostSearches) {
            mostSearched.clear();
            mostSearched.addAll(mostSearches);
            mostAdapter.notifyDataSetChanged();
        }

        @Override
        public void setHistory() {
            historyAdapter.notifyDataSetChanged();
        }

        @Override
        public List<String> getHistory() {
            return histories;
        }

        @Override
        public void setSuggest(List<String> suggest) {
            suggests.clear();
            suggests.addAll(suggest);
            suggestAdapter.notifyDataSetChanged();
        }

        @Override
        public void dismissSuggest() {
            suggestRecycler.setVisibility(View.GONE);
        }

        @Override
        public void showSuggest() {
            suggestRecycler.setVisibility(View.VISIBLE);
        }

        @Override
        public void setResult(List<GarbageInfo> garbage) {
            results.clear();
            results.addAll(garbage);
            resultAdapter.notifyDataSetChanged();
        }

        @Override
        public void dismissResult() {
            resultRecycler.setVisibility(View.GONE);
        }

        @Override
        public void showResult() {
            resultRecycler.setVisibility(View.VISIBLE);
        }

        @Override
        public Context getContext() {
            return SearchActivity.this;
        }
    }

}



