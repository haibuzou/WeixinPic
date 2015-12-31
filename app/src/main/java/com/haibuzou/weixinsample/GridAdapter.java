package com.haibuzou.weixinsample;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.haibuzou.piclibrary.utils.CommonAdapter;
import com.haibuzou.piclibrary.utils.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.util.List;

/**
 * Created by haibuzou on 2015/12/31.
 */
public class GridAdapter extends CommonAdapter<String> {

    private DisplayImageOptions options;


    public GridAdapter(Context context, List<String> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(com.haibuzou.piclibrary.R.drawable.pic_no)
                .showImageOnFail(com.haibuzou.piclibrary.R.drawable.pic_no).cacheInMemory(false)
                .imageScaleType(ImageScaleType.EXACTLY).cacheOnDisc(false)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public void convert(ViewHolder helper, String item) {
        ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(item),
                (ImageView) helper.getView(R.id.image), options);
    }
}
