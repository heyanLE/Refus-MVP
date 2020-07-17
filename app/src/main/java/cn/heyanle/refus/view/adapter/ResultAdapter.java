package cn.heyanle.refus.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.heyanle.refus.R;
import cn.heyanle.refus.info.refuse.GarbageInfo;

/**
 * Created by HeYanLe on 2020/5/9 0009.
 * https://github.com/heyanLE
 */
public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder>{


    public interface OnItemClick{

        void onClick(ViewHolder holder, int position, ResultAdapter resultAdapter);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        TextView tvConfidence;
        TextView tvType;
        TextView tvPS;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvConfidence = itemView.findViewById(R.id.tv_confidence);
            tvType = itemView.findViewById(R.id.tv_type);
            tvPS = itemView.findViewById(R.id.tv_ps);
        }

        public void setTitle(String title){
            tvTitle.setText(title);
        }

        public void setType(String type){
            tvType.setText(type);
        }

        public void setConfidence(String confidence){
            tvConfidence.setText(confidence);
        }

        public void setPs(String ps){
            if (ps.isEmpty()){
                tvPS.setVisibility(View.GONE);
            }else{
                tvPS.setText(ps);
                tvPS.setVisibility(View.VISIBLE);
            }
        }
    }

    private List<GarbageInfo> mData;
    private Context mContext;
    private OnItemClick onItemClick = new OnItemClick() {
        @Override
        public void onClick(ViewHolder holder, int position, ResultAdapter resultAdapter) {

        }
    };


    public ResultAdapter(List<GarbageInfo> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_result ,parent ,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.setType(mData.get(position).getCateName());
        holder.setTitle(mContext.getString(R.string.result_title ,mData.get(position).getGarbageName()));

        double d = mData.get(position).getConfidence();
        String con = ((int)(d*10000))/100d+"%";
        holder.setConfidence(mContext.getString(R.string.result_confidence ,con));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onClick(holder ,position ,ResultAdapter.this);
            }
        });
        if (d >= 0.5d || mData.size()<= 3){
            holder.setPs(mContext.getString(R.string.result_ps ,mData.get(position).getPS()));
        }else{
            holder.setPs("");
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
