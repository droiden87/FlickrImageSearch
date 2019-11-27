package com.scalefocus.flickr.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.scalefocus.flickr.R;
import com.scalefocus.flickr.model.Photo;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private static final String TAG = ImageAdapter.class.getSimpleName();
    private Photo[] imageListData;
    private Context context;
    private String flickrJsonResults;

    public void setFlickrJsonResults(String jsonStr) {
        flickrJsonResults = jsonStr;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIdForListItem = R.layout.image_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {

        String ImageURL = imageListData[position].getThumbnailURL();

      //new ImageDownloaderTask(holder.mImageViewItem).execute(ImageURL);
        Glide.with(context)
                .load(ImageURL)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.mImageViewItem);

    }

    @Override
    public int getItemCount() {
        if (null == imageListData) return 0;
        return imageListData.length;
    }

    public void setImageListData(Photo[] imageListData) {
        imageListData = imageListData;
        notifyDataSetChanged();
    }

    public void clear() {
        imageListData = null;
        notifyDataSetChanged();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageViewItem;

        public ImageViewHolder(View itemView) {
            super(itemView);

            mImageViewItem = itemView.findViewById(R.id.iv_image_item);
        }

    }


}
