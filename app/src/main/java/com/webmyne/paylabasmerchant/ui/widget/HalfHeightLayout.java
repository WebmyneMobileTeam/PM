package com.webmyne.paylabasmerchant.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class HalfHeightLayout extends LinearLayout {

    public HalfHeightLayout(Context context) {
        super(context);
    }

    public HalfHeightLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        // this is the scale between width & height, for square should be == 1
        int scale = 1;

        if (width > (int)(scale * height + 0.5)) {
            width = width;
        } else {
            height = (int)(width / scale + 0.5);
        }

        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),MeasureSpec.makeMeasureSpec(height/2, MeasureSpec.EXACTLY)
        );
    }
}