package com.wenyizai.wangfuwen.wenyizai.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by user on 15/9/17.
 */
public class CounterView extends View implements View.OnClickListener {

    private Paint mpaint;
    private Rect mBounds;
    private int mCount;

    public CounterView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        mpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBounds = new Rect();
        setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mpaint.setColor(Color.BLUE);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mpaint);
        mpaint.setColor(Color.CYAN);
        mpaint.setTextSize(30);
        String text = String.valueOf(mCount);
        mpaint.getTextBounds(text,0,text.length(),mBounds);
        float textwidth = mBounds.width();
        float textheight = mBounds.height();
        canvas.drawText(text,getWidth()/2-textwidth/2,getHeight()/2-textheight/2,mpaint);

    }

    @Override
    public void onClick(View view) {
        mCount++;
        invalidate();
    }
}
