package cn.heyanle.refus.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.heyanle.refus.R;
import cn.heyanle.refus.info.mostserched.MostSearchedInfo;
import cn.heyanle.refus.utils.DensityUtil;
import cn.heyanle.theme.ThemeUtils;

/**
 * Created by HeYanLe on 2020/5/8 0008.
 * https://github.com/heyanLE
 */
public class MostAdapter extends RecyclerView.Adapter<MostAdapter.ViewHolder> {

    public interface OnItemClick{

        void onClick(ViewHolder holder, int position, MostAdapter mostAdapter);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private String text;

        public ViewHolder(@NonNull TextView textView) {
            super(textView);
            this.textView = textView;
        }

        public void setTextViewText(String text){
            textView.setText(text);
        }

        public void setText(String text){
            this.text = text;
        }

        public String getText(){
            return text;
        }
    }

    private List<MostSearchedInfo> mData = new ArrayList<>();
    private Context mContext;
    private OnItemClick onItemClick = new OnItemClick() {
        @Override
        public void onClick(ViewHolder holder, int position, MostAdapter mostAdapter) {

        }
    };

    public MostAdapter(List<MostSearchedInfo> mData, Context mContext) {
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
        int f = new DensityUtil().dip2px(8);
        textView.setPadding(f,f,f,f);
        textView.setTextColor(ThemeUtils.getAttrColor(mContext , R.attr.colorTextHigh));
        textView.setBackgroundResource(R.drawable.back_ripple);
        //parent.addView(textView);
        return new ViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.setText(mData.get(position).getName());
        holder.setTextViewText((position==9?"":"  ")+(position+1)+"  "+mData.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onClick(holder ,position ,MostAdapter.this);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Math.min(mData.size(),10);
    }
}


