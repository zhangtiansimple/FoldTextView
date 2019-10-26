package com.link.foldtextviewlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.link.flodtextviewlibrary.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class FoldTextView extends LinearLayout implements View.OnClickListener {

    //显示内容的TextView
    private TextView mContentTv;

    //显示状态的TextView
    private TextView mStateTv;

    //是否折叠 默认状态为折叠
    private boolean isFold = true;

    //展开最大行数
    private int mMaxUnfoldLines;

    //动画执行时间
    private int mAnimDuration;

    //内容字符大小
    private int mContentTextSize;

    //内容行间距
    private float mContentTextSpaceMultiplier;

    //内容颜色
    private int mContentTextColor;

    //展开icon
    private Drawable mUnFoldDrawable;

    //折叠icon
    private Drawable mFoldDrawable;

    //展开文字
    private String mUnFoldString;

    //折叠文字
    private String mFoldString;

    //状态TextView的Gravity
    private int mStateTvGravity;

    //状态文字颜色
    private int mStateTextColor;

    //最大行时的高度
    private int mTextHeightWithMaxLines;

    //展开时的高度
    private int mUnfoldHeight;

    //是否重新布局
    private boolean isReLayout;

    //动画是否在执行
    private boolean isAnimating;

    //动画执行距离
    private int mAnimDistance;

    //展开折叠的状态监听接口
    private FoldStatusListener mListener;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mAnimDistance = getHeight() - mContentTv.getHeight();
        }
    };

    public FoldTextView(Context context) {
        this(context, null);
    }

    public FoldTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FoldTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFoldTextView(context, attrs);
    }

    private void initFoldTextView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_fold_textview, this, true);
        setOrientation(LinearLayout.VERTICAL);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FoldTextView);

        //内容文本设置
        mMaxUnfoldLines = typedArray.getInt(R.styleable.FoldTextView_maxUnfoldLines, FoldConfig.MAX_UNFOLD_LINES);
        mAnimDuration = typedArray.getInt(R.styleable.FoldTextView_animDuration, FoldConfig.ANIM_DURATION);
        mContentTextSize = typedArray.getDimensionPixelSize(R.styleable.FoldTextView_contentTextSize, FoldConfig.CONTENT_TEXT_SIZE);
        mContentTextSpaceMultiplier = typedArray.getFloat(R.styleable.FoldTextView_contentLineSpaceMultiplier, FoldConfig.CONTENT_LINE_SPACE_MULTIPLIER);
        mContentTextColor = typedArray.getColor(R.styleable.FoldTextView_contentTextColor, Color.BLACK);

        //状态文本设置
        mUnFoldString = typedArray.getString(R.styleable.FoldTextView_unFoldText) == null ? context.getString(R.string.str_unfold) : typedArray.getString(R.styleable.FoldTextView_unFoldText);
        mFoldString = typedArray.getString(R.styleable.FoldTextView_foldText) == null ? context.getString(R.string.str_fold) : typedArray.getString(R.styleable.FoldTextView_foldText);
        mStateTvGravity = typedArray.getInt(R.styleable.FoldTextView_stateTvGravity, FoldConfig.STATE_TV_GRAVITY);
        mStateTextColor = typedArray.getColor(R.styleable.FoldTextView_stateTextColor, Color.BLACK);
        mUnFoldDrawable = FoldUtils.resize(getContext(), typedArray.getDrawable(R.styleable.FoldTextView_unFoldDrawable) == null ? ContextCompat.getDrawable(context, R.drawable.ic_fold) : typedArray.getDrawable(R.styleable.FoldTextView_unFoldDrawable), FoldConfig.STATE_TV_ICON_SIZE);
        mFoldDrawable = FoldUtils.resize(getContext(), typedArray.getDrawable(R.styleable.FoldTextView_foldDrawable) == null ? ContextCompat.getDrawable(context, R.drawable.ic_unfold) : typedArray.getDrawable(R.styleable.FoldTextView_foldDrawable), FoldConfig.STATE_TV_ICON_SIZE);

        typedArray.recycle();
    }

    private void initView() {
        mContentTv = findViewById(R.id.tv_content);
        mContentTv.setTextColor(mContentTextColor);
        mContentTv.setTextSize(mContentTextSize);
        mContentTv.setLineSpacing(0, mContentTextSpaceMultiplier);
        mContentTv.setOnClickListener(this);

        mStateTv = findViewById(R.id.tv_state);
        LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        switch (mStateTvGravity) {
            case FoldConfig.STATE_TV_GRAVITY_START: {
                params.gravity = Gravity.START;
                break;
            }
            case FoldConfig.STATE_TV_GRAVITY_CENTER: {
                params.gravity = Gravity.CENTER;
                break;
            }
            case FoldConfig.STATE_TV_GRAVITY_END: {
                params.gravity = Gravity.END;
                break;
            }
        }
        mStateTv.setLayoutParams(params);
        mStateTv.setTextColor(mStateTextColor);
        mStateTv.setCompoundDrawablePadding(FoldConfig.STATE_TV_DRAWABLE_PADDING);
        mStateTv.setOnClickListener(this);
        refreshStateTv();
    }

    private int getRealTextViewHeight(@NonNull TextView textView) {
        int textHeight = textView.getLayout().getLineTop(textView.getLineCount());
        int padding = textView.getCompoundPaddingTop() + textView.getCompoundPaddingBottom();
        return textHeight + padding;
    }

    private void refreshStateTv() {
        mStateTv.setText(isFold ? mFoldString : mUnFoldString);
        mStateTv.setCompoundDrawablesWithIntrinsicBounds(isFold ? mFoldDrawable : mUnFoldDrawable, null, null, null);
    }

    public void setStatusListener(FoldStatusListener listener) {
        mListener = listener;
    }

    public void setText(@Nullable CharSequence text) {
        isReLayout = true;
        mContentTv.setText(text);
        setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    @Override
    public void setOrientation(int orientation) {
        if (LinearLayout.HORIZONTAL == orientation) {
            throw new IllegalArgumentException("目前只支持垂直布局！");
        }
        super.setOrientation(orientation);
    }

    @Nullable
    public CharSequence getText() {
        if (mContentTv == null) {
            return "";
        }
        return mContentTv.getText();
    }

    //如果动画正在执行 就消费事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isAnimating;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //如果没有重新布局或者此时没有加入到视图中 直接默认测量并返回
        if (!isReLayout || getVisibility() == View.GONE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        isReLayout = false;

        //如果可以显示的下 则不需要状态Tv
        mStateTv.setVisibility(View.GONE);
        mContentTv.setMaxLines(Integer.MAX_VALUE);

        //测量
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //如果实际行数小于最大折叠行数
        if (mContentTv.getLineCount() <= mMaxUnfoldLines) {
            return;
        }

        mTextHeightWithMaxLines = getRealTextViewHeight(mContentTv);

        if (isFold) {
            mContentTv.setMaxLines(mMaxUnfoldLines);
        }
        mStateTv.setVisibility(View.VISIBLE);

        //重新测量
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (isFold) {
            mContentTv.post(mRunnable);
            mUnfoldHeight = getMeasuredHeight();
        }
    }

    @Override
    public void onClick(View v) {
        if (mStateTv.getVisibility() != View.VISIBLE) {
            return;
        }

        isFold = !isFold;
        refreshStateTv();

        isAnimating = true;
        Animation animation;
        if (isFold) {
            animation = new FoldAnimation(this, getHeight(), mUnfoldHeight, mContentTv, mAnimDuration, mAnimDistance);
        } else {
            animation = new FoldAnimation(this, getHeight(), getHeight() +
                    mTextHeightWithMaxLines - mContentTv.getHeight(), mContentTv, mAnimDuration, mAnimDistance);
        }

        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                clearAnimation();
                isAnimating = false;
                if (mListener != null) {
                    mListener.onFoldStateChanged(mContentTv, isFold);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        clearAnimation();
        startAnimation(animation);
    }
}
