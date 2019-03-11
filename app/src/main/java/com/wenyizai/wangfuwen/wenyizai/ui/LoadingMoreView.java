package com.wenyizai.wangfuwen.wenyizai.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.wenyizai.wangfuwen.wenyizai.R;


/**
 * Created by wangfuwen on 16/10/30.
 */

public class LoadingMoreView extends LinearLayout {

    public LoadingMoreView(Context context) {
        super(context);
        init();
    }

    public LoadingMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.loading_more_view, this);
    }


}




