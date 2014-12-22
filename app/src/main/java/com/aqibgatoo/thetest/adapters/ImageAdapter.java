package com.aqibgatoo.thetest.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aqibgatoo.thetest.R;
import com.aqibgatoo.thetest.Util.ImageFetcher;
import com.aqibgatoo.thetest.model.ImageEntity;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private static final String TAG = ImageAdapter.class.getSimpleName();
    private static ImageFetcher mImageFetcher;
    private ArrayList<ImageEntity> mImageEntities;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public ImageAdapter(Context context, ArrayList<ImageEntity> imageEntities) {
        mContext = context;
        mImageEntities = imageEntities;
        mLayoutInflater = LayoutInflater.from(context);
        mImageFetcher = new ImageFetcher(context);
    }

    @Override
    public int getCount() {
        return mImageEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return mImageEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mLayoutInflater.inflate(R.layout.grid_item_layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        ImageEntity imageEntity = mImageEntities.get(position);
        Log.d(TAG, imageEntity.getId());
        viewHolder.textView.setText(imageEntity.getId());
        viewHolder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher));
//        mImageFetcher.loadImage(viewHolder.imageView, imageEntity.getId());
        return convertView;
    }


    static class ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(View v) {
            imageView = (ImageView) v.findViewById(R.id.id_simple_image);
            textView = (TextView) v.findViewById(R.id.textview);

        }

    }

}