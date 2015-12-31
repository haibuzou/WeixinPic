package com.haibuzou.piclibrary.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haibuzou.piclibrary.R;


/**
 * actionbar view
 *
 * @author Edmin
 */
public class ActionbarView extends RelativeLayout {

    private View leftImgbtn;

    private Button rightBtn;

    private TextView titleView;

    private View leftView;

    private ImageButton homeImgbtn;

    private TextView rightTitle;

    private ImageButton rightImgbtn;

    private View rightView;

    private View rightCustomView;

    private RelativeLayout rightLayout;

    private TextView tipTxt;

    public ActionbarView(Context context) {
        super(context);
        init();
    }

    public ActionbarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ActionbarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View actionbarView = inflater.inflate(R.layout.common_actionbar_view, this);
        leftView = actionbarView.findViewById(R.id.common_actionbar_back_layout);
        leftImgbtn = actionbarView.findViewById(R.id.common_actionbar_back_imgbtn);
        homeImgbtn = (ImageButton) actionbarView.findViewById(R.id.common_actionbar_home_imgbtn);
        titleView = (TextView) actionbarView.findViewById(R.id.common_actionbar_title_txt);
        rightBtn = (Button) actionbarView.findViewById(R.id.common_actionbar_right_btn);
        rightTitle = (TextView) actionbarView.findViewById(R.id.common_actionbar_right_text);
        rightImgbtn = (ImageButton) actionbarView.findViewById(R.id.common_actionbar_right_img);
        rightCustomView = actionbarView.findViewById(R.id.common_actionbar_right_View);
        rightLayout = (RelativeLayout) actionbarView.findViewById(R.id.common_actionbar_right_layout);
        //tipTxt = (TextView) actionbarView.findViewById(R.id.common_actionbar_tip_txt);
        leftView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Activity activity = (Activity) getContext();
                activity.finish();
            }
        });
        rightView = rightBtn;
    }

    /**
     * 设置右边文字
     *
     * @param str
     */
    public void setRightTitle(String str) {
        rightTitle.setText(str);
        rightTitle.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏右边文字
     *
     * @param str
     */
    public void hiddenRightTitle() {
        rightTitle.setVisibility(View.GONE);
    }

    /**
     * 设置右边图标
     */
    public void setRightButtonBackground(int resId) {
        rightBtn.setBackgroundResource(resId);
        showRightButton(true);
    }

    /**
     * 隐藏左边图标
     */
    public void hiddenLeftView() {
        leftImgbtn.setVisibility(View.GONE);
    }

    /**
     * 隐藏右边图标
     */
    public void hiddenRightView() {
        rightView.setVisibility(View.GONE);
    }

    /**
     * 显示左边图标
     */
    public void showLeftView() {
        leftImgbtn.setVisibility(View.VISIBLE);
    }

    /**
     * 显示右边图标
     */
    public void showRightView() {
        rightView.setVisibility(View.VISIBLE);
    }

    /**
     * 设置标题文字
     */
    public void setTitle(int resid) {
        titleView.setText(resid);
    }

    /**
     * 设置标题文字
     */
    public void setTitle(CharSequence title) {
        titleView.setText(title);
    }

    /**
     * 设置右按钮文字
     */
    public void setRightText(CharSequence text) {
        rightBtn.setText(text);
        showRightButton(true);
    }

    /**
     * 设置右按钮文字
     */
    public void setRightText(int resid) {
        rightBtn.setText(resid);
        showRightButton(true);
    }


    /**
     * 设置右按钮事件
     */
    public void setOnRightClick(OnClickListener onClickListener) {
        rightImgbtn.setOnClickListener(onClickListener);
        rightBtn.setOnClickListener(onClickListener);
    }

    /**
     * 设置左按钮事件
     */
    public void setOnLeftClick(OnClickListener onClickListener) {
        leftView.setOnClickListener(onClickListener);
    }

    /**
     * 显示home图标，同时不可点击
     */
    public void showHome() {
        homeImgbtn.setVisibility(VISIBLE);
        leftView.setEnabled(false);
        leftImgbtn.setVisibility(GONE);
    }

    /**
     * 设置home图标
     *
     * @param imageResource
     */
    public void setHomeImageResource(int imageResource) {
        homeImgbtn.setImageResource(imageResource);
        showHome();
    }

    /**
     * 隐藏home图标
     */
    public void hideHome() {
        homeImgbtn.setVisibility(GONE);
    }

    /**
     * 设置右边按钮图标
     *
     * @param resId
     */
    public void setRightImageResouce(int resId) {
        rightImgbtn.setImageResource(resId);
        showRightButton(false);
    }

    public void setRightImageSize(int width, int height) {
        LayoutParams params = (LayoutParams) rightImgbtn.getLayoutParams();
        params.width = width;
        params.height = height;
        rightBtn.setLayoutParams(params);
    }

    /**
     * 设置右边按钮图标
     *
     * @param resId
     */
    public void setRightImageBitmap(Bitmap bitmap) {
        rightImgbtn.setImageBitmap(bitmap);
        showRightButton(false);
    }

    /**
     * 设置右边自定义view
     *
     * @param view
     */
    public void setRightCustomView(View view) {
        ViewGroup.LayoutParams lp = rightCustomView.getLayoutParams();
        view.setLayoutParams(lp);
        view.setPadding(rightCustomView.getPaddingLeft(), rightCustomView.getPaddingTop(), rightCustomView.getPaddingRight(), rightCustomView.getPaddingBottom());
        rightCustomView = view;
        rightLayout.addView(rightCustomView);
        rightCustomView.setVisibility(VISIBLE);
    }

    /**
     * 隐藏右边自定义view
     */
    public void hiddenRightCustomView() {
        rightCustomView.setVisibility(GONE);
    }

    /**
     * 右边按钮是否可用
     *
     * @param enabled
     */
    public void setRightEnable(boolean enabled) {
        rightView.setEnabled(enabled);
    }

    /**
     * 设置提示内容
     *
     * @param tip
     */
    public void setTip(String tip) {
        tipTxt.setText(tip);
        this.tipTxt.setVisibility(View.VISIBLE);
    }

    /**
     * 设置提示内容
     *
     * @param tipRes
     */
    public void setTip(int tipRes) {
        tipTxt.setText(tipRes);
        this.tipTxt.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏提示
     */
    public void hiddenTip() {
        this.tipTxt.setVisibility(View.GONE);
    }

    private void showRightButton(boolean show) {
        if (show) {
            rightBtn.setVisibility(VISIBLE);
            rightImgbtn.setVisibility(GONE);
            rightView = rightBtn;
        } else {
            rightBtn.setVisibility(GONE);
            rightImgbtn.setVisibility(VISIBLE);
            rightView = rightImgbtn;
        }
    }

    /**
     * 设置标题颜色
     *
     * @param colors
     */
    public void setTitleColor(ColorStateList colors) {
        titleView.setTextColor(colors);
    }

    /**
     * 设置标题颜色
     *
     * @param color
     */
    public void setTitleColor(int color) {
        titleView.setTextColor(color);
    }
}
