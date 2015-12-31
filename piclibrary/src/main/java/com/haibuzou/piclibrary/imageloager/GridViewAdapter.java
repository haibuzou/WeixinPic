package com.haibuzou.piclibrary.imageloager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.haibuzou.piclibrary.R;
import com.haibuzou.piclibrary.utils.CommonAdapter;
import com.haibuzou.piclibrary.utils.ImageTools;
import com.haibuzou.piclibrary.utils.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class GridViewAdapter extends CommonAdapter<String> {

    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public List<String> mSelectedImage = new LinkedList<String>();

    /**
     * 文件夹路径
     */
    // private String mDirPath;

    // private Activity activity;
    private int type;
    private int size;
    private String path;
    private CountListener mCountListener;
    private Bitmap preset;
    private DisplayMetrics dm;

    private DisplayImageOptions options;

    public GridViewAdapter(Activity activity, List<String> mDatas,
                           int itemLayoutId, int type, int size, String path) {
        super(activity, mDatas, itemLayoutId);
        initOption();
        preset = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.pic_no);
        dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        // this.mDirPath = dirPath;
        // this.activity = activity;
        this.type = type;
        this.size = size;
        this.path = path;
    }

    public void setDate(List<String> data) {
        mDatas = data;
    }

    @Override
    public void convert(final ViewHolder helper, final String item) {
        final ImageView mImageView = helper.getView(R.id.pic_item_image);
        final ImageView mSelect = helper.getView(R.id.pic_item_select);
        // 魅族的文件路径/mnt/sdcard/
        // if (item.contains("/storage") || item.contains("/mnt/sdcard/")) {
        // helper.setImageByUrl(R.id.pic_item_image, item);
        // if (!aq.shouldDelay(helper.getPosition(), helper.getConvertView(),
        // helper.getParent(), item)) {
        // aq.id(mImageView).image(item, true, true, 150,
        // R.drawable.pic_no, preset, 0, 0);
        // } else {
        // aq.id(mImageView).image(preset);
        // }
        // ImageLoader.getInstance().displayImage(Scheme.FILE.wrap(item),
        // mImageView, options);
        if (item.equals("camera")) {
            helper.getView(R.id.pic_camera).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.pic_camera).setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(item),
                    mImageView, options);
        }
        // } else {
        // if (item.equals("camera")) {
        // helper.getView(R.id.pic_camera).setVisibility(View.VISIBLE);
        // } else {
        // helper.getView(R.id.pic_camera).setVisibility(View.GONE);
        // // helper.setImageByUrl(R.id.pic_item_image, mDirPath + "/" +
        // // item);
        // if (!aq.shouldDelay(helper.getPosition(),
        // helper.getConvertView(), helper.getParent(), item)) {
        // aq.id(mImageView).image(item, true, true,
        // 150, R.drawable.pic_no, preset, 0, 0);
        // } else {
        // aq.id(mImageView).image(preset);
        // }
        // // ImageLoader.getInstance().displayImage(
        // // Scheme.FILE.wrap(mDirPath + "/" + item), mImageView,
        // // options);
        // }
        // }

        if (type == 1 || item.equals("camera")) {
            helper.getView(R.id.pic_item_select).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.pic_item_select).setVisibility(View.VISIBLE);
            helper.setImageResource(R.id.pic_item_select,
                    R.drawable.pic_unselected);
        }
        mImageView.setColorFilter(null);
        // 设置ImageView的点击事件
        mImageView.setOnClickListener(new OnClickListener() {
            // 选择，则将图片变暗，反之则反之
            @Override
            public void onClick(View v) {
                if (item.equals("camera")) {// 相机的item调用照相动作
                    SharedPreferences sharedPreferences = mContext
                            .getSharedPreferences("temp", 0);
                    // 保存本次截图临时文件名字
                    String fileName = ImageTools
                            .getPhotoFileName(ImageTools.PHOTO_NAME);
                    Editor editor = sharedPreferences.edit();
                    editor.putString("tempName", fileName);
                    editor.commit();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri imageUri = Uri.fromFile(new File(path, fileName));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    ((Activity) mContext).startActivityForResult(intent, 1);
                } else {
                    // 所有图片文件夹和其他的文件夹处理方式不同
                    // if (item.contains("storage")) {
                    // if (type == 1) {
                    // ImageTools.startPhotoZoom(item,(Activity)mContext,path);
                    // return;
                    // }
                    // // 已经选择过该图片
                    // if (mSelectedImage.contains(item)) {
                    // mSelectedImage.remove(item);
                    // if (mCountListener != null) {
                    // mCountListener.count(mSelectedImage.size());
                    // }
                    // mSelect.setImageResource(R.drawable.pic_unselected);
                    // mImageView.setColorFilter(null);
                    // } else {
                    // // 未选择该图片
                    // if (mSelectedImage.size() >= size) {
                    // Toast.makeText(activity,
                    // "最多可以选择" + size + "张图片", 2000).show();
                    // } else {
                    // mSelectedImage.add(item);
                    // if (mCountListener != null) {
                    // mCountListener.count(mSelectedImage.size());
                    // }
                    // mSelect.setImageResource(R.drawable.pic_selected);
                    // mImageView.setColorFilter(Color
                    // .parseColor("#77000000"));
                    // }
                    // }
                    // } else {
                    if (type == 1) {
                        ImageTools.startPhotoZoom(item, (Activity) mContext,
                                path);
                        return;
                    }
                    // 已经选择过该图片
                    if (mSelectedImage.contains(item)) {
                        mSelectedImage.remove(item);
                        if (mCountListener != null) {
                            mCountListener.count(mSelectedImage.size());
                        }
                        mSelect.setImageResource(R.drawable.pic_unselected);
                        mImageView.setColorFilter(null);
                    } else
                    // 未选择该图片
                    {
                        if (mSelectedImage.size() >= size) {
                            Toast.makeText(mContext, "最多可以上传" + 9 + "张图片", 2000)
                                    .show();
                        } else {
                            // 其他相册的图片路径是文件夹和文件名分开存储
                            mSelectedImage.add(item);
                            if (mCountListener != null) {
                                mCountListener.count(mSelectedImage.size());
                            }
                            mSelect.setImageResource(R.drawable.pic_selected);
                            mImageView.setColorFilter(Color
                                    .parseColor("#77000000"));
                        }
                    }
                }
            }
        });

        /**
         * 已经选择过的图片，显示出选择过的效果
         */
        if (type != 1) {
            // if (item.contains("storage")) {
            // if (mSelectedImage.contains(item)) {
            // mSelect.setImageResource(R.drawable.pic_selected);
            // mImageView.setColorFilter(Color.parseColor("#77000000"));
            // }
            // } else {
            if (mSelectedImage.contains(item)) {
                mSelect.setImageResource(R.drawable.pic_selected);
                mImageView.setColorFilter(Color.parseColor("#77000000"));
            }
            // }
        }

    }

    public List<String> getmSelectedImage() {
        return mSelectedImage;
    }

    public void clearSelectedImage() {
        mSelectedImage.clear();
    }

    /**
     * 点击事件回调接口
     */
    public interface CountListener {
        public void count(int count);
    }

    public void setCountListener(CountListener mCountListener) {
        this.mCountListener = mCountListener;
    }

    public void initOption() {
        // 显示图片的配置
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.pic_no)
                .showImageOnFail(R.drawable.pic_no).cacheInMemory(false)
                .imageScaleType(ImageScaleType.EXACTLY).cacheOnDisc(false)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

}
