# FoldTextView
### 简单活好事少的展开收起TextView

### 效果图
<center class="half">
    <img src="https://github.com/zhangtiansimple/FoldTextView/blob/master/app/pic/pic_1.jpg" width="300"/><img src="https://github.com/zhangtiansimple/FoldTextView/blob/master/app/pic/pic_2.jpg" width="300"/>
</center>
              
### 使用

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

```
dependencies {
    implementation 'com.github.zhangtiansimple:FoldTextView:1.0.0'
}
```

```
<com.link.foldtextviewlibrary.FoldTextView
        android:id="@+id/fold_text_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginStart="15dp"
        android:layout_marginTop="30dp"
        android:layout_marginEnd="15dp"
        app:contentTextColor="@color/colorPrimary"
        app:foldText="More"
        app:contentLineSpaceMultiplier="1.2"
        app:foldDrawable="@drawable/ic_fold"
        app:unFoldDrawable="@drawable/ic_unfold"
        app:stateTvGravity="left"
        app:stateTextColor="@color/colorPrimary"
        app:unFoldText="Less" />
```

```
FoldTextView mFoldTextView = findViewById(R.id.fold_text_view);
mFoldTextView1.setText("your content");
mFoldTextView1.setStatusListener(new FoldStatusListener() {
	@Override
	public void onFoldStateChanged(TextView tv, boolean isFold) {
                if (isFold) {
                    Log.d(TAG, "isFold");
                } else {
                    Log.d(TAG, "isUnfold");
                }
            }
        });
```
|可选属性 |取值|
|---|---|
| maxUnfoldLines|折叠最大行数，默认为3|
| animDuration|展开收起动画执行时常，默认300毫秒|
| contentTextSize|内容字体大小，默认16|
| contentTextColor|内容字体颜色，默认为Color.BLACK|
| contentLineSpaceMultiplier|内容行间距，默认为1.0|
| unFoldDrawable|展开icon，默认为效果图最上黑色三角|
| foldDrawable|折叠icon，默认为效果图最上黑色三角|
| unFoldText|折叠文案，默认为“折叠”|
| foldText|展开文案，默认为“展开”|
| stateTextColor|展开折叠状态文字颜色|
| stateTvGravity|展开折叠状态文字Gravity|
