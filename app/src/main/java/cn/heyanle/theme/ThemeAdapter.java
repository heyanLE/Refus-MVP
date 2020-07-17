package cn.heyanle.theme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import cn.heyanle.refus.R;


/**
 * Created by HeYanLe on 2020/2/25 0025.
 * https://github.com/heyanLE
 */
public class ThemeAdapter extends BaseAdapter {


    private int[] mData;
    private Context mContext;

    public ThemeAdapter(int[] mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.length;
    }

    @Override
    public Object getItem(int position) {
        return mData[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_theme, null);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.img);
        imageView.setImageResource(mData[position]);
        return convertView;
    }

}
