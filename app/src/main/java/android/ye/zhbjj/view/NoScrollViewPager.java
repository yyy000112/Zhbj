package android.ye.zhbjj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义主页面viewPager
 * Created by ye on 2016/11/2.
 */
public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //禁止拦截子类页面
        return false;
    }

    //禁用滑动功能
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }
}
