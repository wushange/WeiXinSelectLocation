package com.wushange.weixinselectlocation.widget;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wushange.weixinselectlocation.R;


/**
 * 使用方法 init(标题类型APPtitleStyle) setTitle(各种类型)
 * <p>
 * 默认都有logo   如果设置返回按钮 就隐藏左侧logo
 *
 * @author wushange
 * @Description
 * @ClassName AppTitle
 * @date 2015-11-23 上午10:19:00
 */
public class AppTitle extends LinearLayout {

    private LayoutInflater mInflater;
    private View mHeader;

    private Button mLeftBtn;//左侧按钮
    private TextView mLeftText;//左侧文本
    private ImageView mLeftLogo;//左侧logo

    private TextView mCenterTitle;//标题

    private TextView mRightText;//右侧文本
    private ImageView mRightBtn;//右侧按钮
    private ImageView mRightSubBtn;//右侧sub按钮

    public AppTitle(Context context) {
        super(context);
        init(context);
    }

    public AppTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        mInflater = LayoutInflater.from(context);
        mHeader = mInflater.inflate(R.layout.apptitle_common_bar, null);
        addView(mHeader);
        initViews(context);
    }

    /**
     * 初始化view
     */
    public void initViews(Context context) {
        mLeftBtn = (Button) findViewById(R.id.back_btn);
        mLeftLogo = (ImageView) findViewByHeaderId(R.id.include_head_logo);
        mLeftText = (TextView) findViewByHeaderId(R.id.include_left_text);
        mCenterTitle = (TextView) findViewByHeaderId(R.id.include_head_title);
        mRightText = (TextView) findViewByHeaderId(R.id.include_right_text);
        mRightBtn = (ImageView) findViewById(R.id.include_right_btn);
        mRightSubBtn = (ImageView) findViewByHeaderId(R.id.include_right_btn_two);
        mLeftBtn.setVisibility(INVISIBLE);
    }

    public AppTitle setLeftText(String text) {
        mLeftText.setVisibility(VISIBLE);
        mLeftText.setText(text);
        return this;
    }

    public AppTitle setLeftTextClickListener(OnClickListener onClickListener) {
        mLeftText.setOnClickListener(onClickListener);
        return this;
    }

    public AppTitle setLeftButton(String text) {
        mLeftBtn.setVisibility(VISIBLE);
        mLeftBtn.setText(text);
        return this;
    }

    public AppTitle setLeftButtonRes(int res) {
        mLeftBtn.setVisibility(VISIBLE);
        mLeftBtn.setCompoundDrawablesWithIntrinsicBounds(res, 0, 0, 0);
        return this;
    }

    public AppTitle setLeftButtonClickListener(OnClickListener onClickListener) {
        mLeftBtn.setVisibility(VISIBLE);
        mLeftBtn.setOnClickListener(onClickListener);
        return this;

    }

    public AppTitle setLeftImage(@DrawableRes int res) {
        mLeftLogo.setVisibility(VISIBLE);
        mLeftLogo.setImageResource(res);
        return this;

    }

    public AppTitle setLeftImageClickListener(OnClickListener onClickListener) {
        mLeftLogo.setOnClickListener(onClickListener);
        return this;

    }

    public AppTitle setCenterTitle(String text) {
        mCenterTitle.setVisibility(VISIBLE);
        mCenterTitle.setText(text);
        return this;

    }

    public AppTitle setCenterTitilClickListener(OnClickListener onClickListener) {
        mCenterTitle.setOnClickListener(onClickListener);
        return this;
    }

    public AppTitle setRightText(String text) {
        mRightText.setVisibility(VISIBLE);
        mRightText.setText(text);
        return this;

    }

    public AppTitle setRightTextClickListener(OnClickListener onClickListener) {
        mRightText.setOnClickListener(onClickListener);
        return this;
    }

    public AppTitle setRightButtonRes(int res) {
        mRightBtn.setVisibility(VISIBLE);
        mRightBtn.setImageResource(res);
        return this;

    }

    public AppTitle setRightButtonClickListener(OnClickListener onClickListener) {
        mRightBtn.setOnClickListener(onClickListener);
        return this;

    }

    public AppTitle setRightSubButtonRes(int res) {
        mRightSubBtn.setVisibility(VISIBLE);
        mRightSubBtn.setImageResource(res);
        return this;

    }

    public AppTitle setRightSubButtonClickListener(OnClickListener onClickListener) {
        mRightSubBtn.setOnClickListener(onClickListener);
        return this;
    }


    public View findViewByHeaderId(int id) {
        return mHeader.findViewById(id);
    }


    public Button getmLeftBtn() {
        return mLeftBtn;
    }

    public TextView getmLeftText() {
        return mLeftText;
    }

    public ImageView getmLeftLogo() {
        return mLeftLogo;
    }

    public TextView getmCenterTitle() {
        return mCenterTitle;
    }

    public TextView getmRightText() {
        return mRightText;
    }

    public ImageView getmRightBtn() {
        return mRightBtn;
    }

    public ImageView getmRightSubBtn() {
        return mRightSubBtn;
    }
}
