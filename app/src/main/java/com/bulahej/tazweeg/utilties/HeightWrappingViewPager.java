package com.bulahej.tazweeg.utilties;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class HeightWrappingViewPager extends ViewPager {

    public HeightWrappingViewPager(Context context) {
        super(context);
        initPageChangeListener();
    }

    public HeightWrappingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPageChangeListener();
    }


    private void initPageChangeListener() {
        addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                requestLayout();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View child = getChildAt(getCurrentItem());
        if (child != null) {
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
