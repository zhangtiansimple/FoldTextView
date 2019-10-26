package com.link.foldtextviewlibrary;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

public class FoldAnimation extends Animation {

    private final View mTargetView;
    private final int mStartHeight;
    private final int mEndHeight;

    private TextView mContentTv;
    private int mMarginBetweenTxtAndBottom;

    public FoldAnimation(View view, int startHeight, int endHeight, TextView contentTv, int animationDuration, int marginBetweenTxtAndBottom) {
        mTargetView = view;
        mStartHeight = startHeight;
        mEndHeight = endHeight;
        mContentTv = contentTv;
        mMarginBetweenTxtAndBottom = marginBetweenTxtAndBottom;
        setDuration(animationDuration);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final int newHeight = (int) ((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight);
        mContentTv.setMaxHeight(newHeight - mMarginBetweenTxtAndBottom);
        mTargetView.getLayoutParams().height = newHeight;
        mTargetView.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
