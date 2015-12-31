package com.haibuzou.piclibrary.imageloager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.haibuzou.piclibrary.R;
import com.haibuzou.piclibrary.utils.ImageTools;
import com.haibuzou.piclibrary.view.ActionbarView;


public class ShowImageActivity extends Activity {

    private String path;
    private ActionbarView actionbar;
    private ImageView img;
    private Bitmap bitmap;
    private int degree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pic_show_img_activity);
        path = getIntent().getStringExtra("path");
        initView();
        initEvent();

    }

    private void initEvent() {
        actionbar.setOnRightClick(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("path", path);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    private void initView() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // WindowManager wm = this.getWindowManager();
        actionbar = (ActionbarView) findViewById(R.id.pic_show_action_bar);
        actionbar.setTitle("图片");
        actionbar.setRightText(R.string.pic_confirm);
        img = (ImageView) findViewById(R.id.pic_show_img);
        bitmap = ImageTools.getResizedBitmap(path, 480, 800);
        // degree = ImageTools.readPictureDegree(path);
        // if (degree != 0) {
        // bitmap = ImageTools.rotateBitmap(bitmap, degree);
        // }
        img.setImageBitmap(bitmap);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
        }
    }

}
