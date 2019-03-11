package com.wenyizai.wangfuwen.wenyizai.ui;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wenyizai.wangfuwen.wenyizai.R;


/**
 * Created by user on 15/9/17.
 */
public class TitleView extends FrameLayout {

    private Button LeftButton;
    private TextView title;

    public TitleView(Context context, AttributeSet attributes){
        super(context, attributes);
        LayoutInflater.from(context).inflate(R.layout.head, this);
        title = (TextView) findViewById(R.id.title_text);
        LeftButton = (Button) findViewById(R.id.back);
        LeftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) getContext()).finish();
            }
        });

    }

    public void setTitle(String text) {
        title.setText(text);
    }

    public void setBackButtonText(String s){
        LeftButton.setText(s);
    }

    public void setLeftButtonListener(OnClickListener l) {
        LeftButton.setOnClickListener(l);
    }
}
