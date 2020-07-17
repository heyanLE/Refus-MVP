package cn.heyanle.refus.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.heyanle.refus.R;
import cn.heyanle.refus.utils.DensityUtil;
import cn.heyanle.theme.ThemeUtils;

/**
 * Created by HeYanLe on 2020/5/9 0009.
 * https://github.com/heyanLE
 */
public class HistoryAdapter  extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{

    public interface OnItemClick{

        void onClick(ViewHolder holder, int position, HistoryAdapter adapter);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public ViewHolder(@NonNull TextView textView) {
            super(textView);
            this.textView = textView;
        }

        public void setText(String text){
            textView.setText(text);
        }

        public String getText(){
            return textView.getText().toString();
        }
    }

    private List<String> mData = new ArrayList<>();
    private Context mContext;
    private OnItemClick onItemClick = new OnItemClick() {
        @Override
        public void onClick(ViewHolder holder, int position, HistoryAdapter adapter) {

        }
    };

    public HistoryAdapter(List<String> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView textView = new TextView(mContext);
        int f = new DensityUtil().dip2px(16);
        int d = new DensityUtil().dip2px(8);
        textView.setBackgroundColor(Color.WHITE);
        textView.setPadding(f,d,f,d);
        textView.setTextColor(ThemeUtils.getAttrColor(mContext , R.attr.colorTextHigh));
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                ,ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setBackgroundResource(R.drawable.back_history);
        //parent.addView(textView);
        return new ViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.setText(mData.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onClick(holder ,position ,HistoryAdapter.this);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
