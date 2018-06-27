package com.bankscene.bes.welllinkbank.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Nylon on 2018/6/5.18:26
 */

public class NoscrollViewpage extends V4ViewPager {
    private boolean isCanScroll = true;
    public NoscrollViewpage(Context context) {
        super(context);
    }

    public NoscrollViewpage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCanScroll(boolean isCanScroll){
        this.isCanScroll = isCanScroll;
    }
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        if(isCanScroll){
            return super.onTouchEvent(arg0);
        }else{
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        if(isCanScroll){
            return super.onInterceptTouchEvent(arg0);
        }else{
            return false;
        }

    }
    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        // TODO Auto-generated method stub
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        // TODO Auto-generated method stub
        super.setCurrentItem(item, false);
    }

}
