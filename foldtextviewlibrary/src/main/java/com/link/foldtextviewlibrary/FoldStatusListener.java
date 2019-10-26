package com.link.foldtextviewlibrary;

import android.widget.TextView;

public interface FoldStatusListener {
    void onFoldStateChanged(TextView tv, boolean isFold);
}
