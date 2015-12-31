package com.haibuzou.piclibrary.imageloager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haibuzou.piclibrary.R;
import com.haibuzou.piclibrary.bean.ImageFloder;
import com.haibuzou.piclibrary.utils.Constant;
import com.haibuzou.piclibrary.utils.ImageTools;
import com.haibuzou.piclibrary.utils.ToastUtil;
import com.haibuzou.piclibrary.view.ActionbarView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


public class GalleryActivity extends Activity implements ListImageDirPopupWindow.OnImageDirSelected,
        GridViewAdapter.CountListener {

    private ProgressDialog pd;

    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;
    /**
     * 图片数量最多的文件夹
     */
    private File mImgDir;
    /**
     * 所有的图片
     */
    private List<String> mImgs = new ArrayList<String>();

    /**
     * 一个文件夹中的图片
     */
    private List<String> imgs = new ArrayList<String>();

    private GridView mGirdView;
    private GridViewAdapter mAdapter;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();

    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();

    /**
     * 存放所有图片的文件夹
     */
    private ImageFloder allImageFloders = new ImageFloder();

    private RelativeLayout mBottomLy;
    private TextView mChooseDir;
    int totalCount = 0;

    private int mScreenHeight;

    private ListImageDirPopupWindow mListImageDirPopupWindow;
    Uri imageUri = Uri.fromFile(new File(ImageTools.getSDCardRootPath(),
            ImageTools.getPhotoFileName(ImageTools.PHOTO_NAME)));
    private int type = 0;
    private int size = 9;
    private int resWidth;
    private int resHeight;
    private String path;
    public final int CODE_PHOTO_IMG = 1; // 选择拍照的请求码
    public final int CODE_RESULT_IMG = 3;// 裁剪图片请求码
    public final int CODE_SHOW_IMG = 0;
    private ArrayList<String> paths = new ArrayList<String>();
    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public List<String> mSelectedImage = new LinkedList<String>();

    private ActionbarView actionBar;
    private DisplayMetrics outMetrics;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.arg1 == 1) {
                // 完成后清空选择的图片
                pd.dismiss();
                mAdapter.clearSelectedImage();
                Intent intent = new Intent();
                intent.putStringArrayListExtra("path", paths);
                setResult(RESULT_OK, intent);
                finish();
                return;
            }
            pd.dismiss();
            // 为View绑定数据
            data2View();
            // 初始化展示文件夹的popupWindw
            initListDirPopupWindw();
        }
    };

    /**
     * 为View绑定数据
     */
    private void data2View() {
        if (mImgDir == null) {
            // Toast.makeText(getApplicationContext(),
            // getResources().getString(R.string.no_pic),
            // Toast.LENGTH_SHORT).show();
            // 没有检测到图片 加载第一个相机图片
            mImgs.add(0, "camera");
            mAdapter = new GridViewAdapter(GalleryActivity.this, mImgs,
                    R.layout.pic_grid_item, type, size, path);
            mGirdView.setAdapter(mAdapter);
            return;
        }

        // mImgs = Arrays.asList(mImgDir.list());
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        // 添加一项自定义的相机选项
        mImgs.add(0, "camera");
        mAdapter = new GridViewAdapter(GalleryActivity.this, mImgs,
                R.layout.pic_grid_item, type, size, path);
        mAdapter.setCountListener(this);
        mGirdView.setAdapter(mAdapter);
        // mPreview.setText(totalCount + "张");
    }

    ;

    /**
     * 初始化展示文件夹的popupWindw
     */
    private void initListDirPopupWindw() {
        mListImageDirPopupWindow = new ListImageDirPopupWindow(
                LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7),
                mImageFloders, LayoutInflater.from(this).inflate(
                R.layout.pic_list_dir_view, null));

        mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        // 设置选择文件夹的回调
        mListImageDirPopupWindow.setOnImageDirSelected(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.gc();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pic_main_activity);
        outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;
        initView();
        getImages();
        initEvent();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            // case R.id.action_confirm:

            // return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        // 显示进度条
        pd = ProgressDialog.show(this, null, "正在加载");

        new Thread(new Runnable() {
            @Override
            public void run() {

                String firstImage = null;
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = GalleryActivity.this
                        .getContentResolver();

                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED + " DESC");
                if (mCursor == null) {
                    return;
                }
                // Log.e("TAG", mCursor.getCount() + "");
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));
                    File file = new File(path);
                    if (file == null || !file.exists()) {
                        continue;
                    }
                    if (!isGoodImg(path)) {
                        continue;
                    }
                    // Log.e("TAG", path);
                    mImgs.add(path);
                    // 拿到第一张图片的路径
                    if (firstImage == null)
                        firstImage = path;
                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    ImageFloder imageFloder = null;
                    // 利用一个HashSet防止多次扫描同一个文件夹
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        // 初始化imageFloder
                        imageFloder = new ImageFloder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }

                    String[] listFile = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg")
                                    || filename.endsWith(".png")
                                    || filename.endsWith(".jpeg"))
                                return true;
                            return false;
                        }
                    });
                    if (listFile == null || listFile.length <= 0) {
                        continue;
                    }
                    int picSize = listFile.length;
                    // totalCount += picSize;

                    imageFloder.setCount(picSize);
                    imageFloder.setNameList(listFile);
                    mImageFloders.add(imageFloder);
                    if (picSize > mPicsSize) {
                        mPicsSize = picSize;
                        mImgDir = parentFile;
                    }
                }
                if (mCursor != null) {
                    mCursor.close();
                    mCursor = null;
                }
                // 添加一个文件夹为展示所有图片
                allImageFloders.setDir("/所有图片");
                allImageFloders.setCount(mImgs.size());
                allImageFloders.setFirstImagePath(firstImage);
                mImageFloders.add(0, allImageFloders);


                // 扫描完成，辅助的HashSet也就可以释放内存了
                mDirPaths = null;

                // 通知Handler扫描图片完成
                mHandler.sendEmptyMessage(0x110);

            }
        }).start();

    }

    /**
     * 初始化View
     */
    @SuppressLint("NewApi")
    private void initView() {
        mGirdView = (GridView) findViewById(R.id.pic_gridView);
        mChooseDir = (TextView) findViewById(R.id.pic_choose_dir);
        mBottomLy = (RelativeLayout) findViewById(R.id.pic_bottom_ly);
        actionBar = (ActionbarView) findViewById(R.id.pic_action_bar);
        actionBar.setTitle(R.string.pic_title);
        actionBar.setRightText(R.string.pic_confirm);
        Intent intent = getIntent();
        type = intent.getIntExtra(Constant.TYPE, type);
        size = intent.getIntExtra(Constant.SEND_NUM, size);
        if (outMetrics.widthPixels < 480) {
            resWidth = intent.getIntExtra(Constant.PIC_WIDTH,
                    outMetrics.widthPixels);
        } else {
            resWidth = intent.getIntExtra(Constant.PIC_WIDTH,
                    Constant.RES_WIDTH);
        }
        if (outMetrics.heightPixels < 800) {
            resHeight = intent.getIntExtra(Constant.PIC_HEIGHT,
                    outMetrics.heightPixels);
        } else {
            resHeight = intent.getIntExtra(Constant.PIC_HEIGHT,
                    Constant.RES_HEIGHT);
        }
        path = intent.getStringExtra(Constant.SAVE_PATH);
        ImageTools.path = path;
        if (path == null || path.equals("")) {
            path = ImageTools.getSDCardRootPath();
        }
        if (ImageTools.RemainingSpace() / 1024 < 300) {
            ToastUtil.show(GalleryActivity.this, "SD卡存储空间不足!");
        }
    }

    /**
     *
     */
    /**
     *
     */
    private void initEvent() {
        /**
         * 为底部的布局设置点击事件，弹出popupWindow
         */
        mChooseDir.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(GalleryActivity.this, "暂无外部存储",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                mListImageDirPopupWindow
                        .setAnimationStyle(R.style.anim_popup_dir);
                mListImageDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);

                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = .3f;
                getWindow().setAttributes(lp);
            }
        });

        actionBar.setOnRightClick(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (ImageTools.RemainingSpace() / 1024 < 300) {
                    ToastUtil.show(GalleryActivity.this, "SD卡存储空间不足!");
                    return;
                }
                // 显示进度条
                pd = ProgressDialog.show(GalleryActivity.this, null, "正在发送");
                // TODO Auto-generated method stub
                mSelectedImage = mAdapter.getmSelectedImage();
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        int i = 1;
                        for (String path : mSelectedImage) {
                            File file = null;
                            if (ImageTools.isNeedCompress(path)) {
                                file = ImageTools.savePhotoToSDCard(ImageTools
                                        .getResizedBitmap(path, resWidth,
                                                resHeight), ImageTools
                                        .getSDCardRootPath(), ImageTools
                                        .getPhotoFileName(i));
                            } else {
                                file = new File(path);
                            }
                            i++;
                            if (file == null) {
                                paths.add("");
                            } else {
                                paths.add(file.getAbsolutePath());
                            }
                        }
                        // System.out.println(paths);
                        Message msg = new Message();
                        msg.arg1 = 1;
                        mHandler.sendMessage(msg);
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case CODE_PHOTO_IMG:
                    Uri uri = null;
                    if (data != null) {
                        uri = data.getData();
                    } else {
                        String fileName = getSharedPreferences("temp", 0)
                                .getString("tempName", "");
                        uri = Uri.fromFile(new File(new File(ImageTools
                                .getSDCardRootPath()), fileName));
                    }
                    if (type == Constant.UPLOAD_PHOTO) {
                        ImageTools.startPhotoZoom(uri.getPath(), GalleryActivity.this, path);
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("path", uri.toString().split("file://")[1]);
                        intent.setClass(this, ShowImageActivity.class);
                        startActivityForResult(intent, CODE_SHOW_IMG);
                    }
                    break;

                case CODE_RESULT_IMG:
                    // String path = getSharedPreferences("photoZoom", 0).getString(
                    // "path", "");
                    // path = path.split(":")[1];
                    // paths.add(path.substring(2));
                    paths.add(data.getStringExtra("path"));
                    Message msg = new Message();
                    msg.arg1 = 1;
                    mHandler.sendMessage(msg);
                    break;
                case CODE_SHOW_IMG:
                    File file = null;
                    if (ImageTools.isNeedCompress(path)) {
                        file = ImageTools.savePhotoToSDCard(ImageTools
                                .getResizedBitmap(data.getStringExtra("path"),
                                        resWidth, resHeight), ImageTools
                                .getSDCardRootPath(), ImageTools
                                .getPhotoFileName(0));
                    } else {
                        file = new File(path);
                    }
                    if (file == null) {
                        paths.add("");
                    } else {
                        paths.add(file.getAbsolutePath());
                    }
                    Message mMsg = new Message();
                    mMsg.arg1 = 1;
                    mHandler.sendMessage(mMsg);
                    break;
            }
        }
    }

    @Override
    public void selected(ImageFloder floder, boolean isAllPic) {
        if (isAllPic) {
            //if (mImgDir != null) {
//				mAdapter = new GridViewAdapter(GalleryActivity.this, mImgs,
//						R.layout.pic_grid_item,	type, size, path);
//				mAdapter.setCountListener(this);
//				mGirdView.setAdapter(mAdapter);
            mAdapter.setDate(mImgs);
            mAdapter.notifyDataSetChanged();
            mChooseDir.setText(floder.getName().replace("/", ""));
            mListImageDirPopupWindow.dismiss();
            //}
            return;
        }
        mImgDir = new File(floder.getDir());
        imgs = Arrays.asList(floder.getNameList());
        List<String> imageList = new ArrayList<String>();
        for (String img : imgs) {
            imageList.add(mImgDir + "/" + img);
        }
//		imgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
//			@Override
//			public boolean accept(File dir, String filename) {
//				if (filename.endsWith(".jpg") || filename.endsWith(".png")
//						|| filename.endsWith(".jpeg"))
//					return true;
//				return false;
//			}
//		}));
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
//		mAdapter = new GridViewAdapter(GalleryActivity.this, imageList,
//				R.layout.pic_grid_item, type, size,	path);
//		mAdapter.setCountListener(this);
//		mGirdView.setAdapter(mAdapter);
        // mAdapter.notifyDataSetChanged();
        // mPreview.setText(floder.getCount() + "张");
        mAdapter.setDate(imageList);
        mAdapter.notifyDataSetChanged();
        mChooseDir.setText(floder.getName().replace("/", ""));
        mListImageDirPopupWindow.dismiss();

    }

    // 在Activity里面捕获按BACK的事件
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        mAdapter.clearSelectedImage();
        super.onBackPressed();
    }

    public boolean isGoodImg(String filePath) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, op);
        if (op.mCancel || op.outWidth == -1 || op.outHeight == -1) {
            return false;
        }
        return true;
    }

    @Override
    public void count(int count) {
        actionBar.setRightText(getString(R.string.pic_confirm) + "(" + count
                + ")");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//		android.os.Process.killProcess(android.os.Process.myPid());
    }

}
