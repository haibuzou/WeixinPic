package com.haibuzou.weixinsample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.haibuzou.piclibrary.imageloager.GalleryActivity;
import com.haibuzou.piclibrary.utils.Constant;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    GridAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.gridView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            gridAdapter = new GridAdapter(MainActivity.this, data.getStringArrayListExtra("path"), R.layout.grid_item);
            gridView.setAdapter(gridAdapter);
        }
    }

    public void choose(View view) {
        Intent intent = new Intent(this, GalleryActivity.class);
        intent.putExtra(Constant.TYPE, Constant.SEND_PIC);
        intent.putExtra(Constant.SEND_NUM, 9);
        startActivityForResult(intent, 0);
    }

    public void clipe(View view) {
        Intent intent = new Intent(this, GalleryActivity.class);
        intent.putExtra(Constant.TYPE, Constant.UPLOAD_PHOTO);
        startActivityForResult(intent, 0);
    }

}
