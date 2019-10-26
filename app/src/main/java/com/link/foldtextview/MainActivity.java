package com.link.foldtextview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.link.foldtextviewlibrary.FoldStatusListener;
import com.link.foldtextviewlibrary.FoldTextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FoldTextView mFoldTextView1 = findViewById(R.id.fold_text_view1);
        mFoldTextView1.setText("当你老了，回顾一生，就会发觉：什么时候出国读书、什么时候决定做第一份职业、何时选定了对象而恋爱、什么时候结婚，其实都是命运的巨变。只是当时站在三岔路口，还以为是生命中普通的一天。");
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

        FoldTextView mFoldTextView2 = findViewById(R.id.fold_text_view2);
        mFoldTextView2.setText("我天性不宜交际。在多数场合，我不是觉得对方乏味，就是害怕对方觉得我乏味。可是我不愿忍受对方的乏味，也不愿费劲使自己显得有趣，那都太累了。我独处时最轻松，因为我不觉得自己乏味，即使乏味，也自己承受，不累及他人，无需感到不安。");

        FoldTextView mFoldTextView3 = findViewById(R.id.fold_text_view3);
        mFoldTextView3.setText("我曾经以为日子是过不完的，未来是完全不一样的。现在，我就呆在我自己的未来，我没有发现自己有什么真正的变化，我的梦想还像小时候一样遥远，唯一不同的是我已经不打算实现它了。");

        FoldTextView mFoldTextView4 = findViewById(R.id.fold_text_view4);
        mFoldTextView4.setText("我行过许多地方的桥，看过许多次数的云，喝过许多种类的酒，却只爱过一个正当最好年龄的人。");
    }
}
