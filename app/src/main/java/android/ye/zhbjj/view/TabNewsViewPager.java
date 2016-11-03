package android.ye.zhbjj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 头条新闻自定义view
 * Created by ye on 2016/11/3.
 */
public class TabNewsViewPager extends ViewPager {

    private int startY;
    private int startX;
    private int endX;
    private int endY;

    public TabNewsViewPager(Context context) {
        super(context);
    }

    public TabNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 需要拦截上下滑动
     * 拦截第一页
     * 拦截最后一页
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = (int) getX();
                startY = (int) getY();
                break;
            case MotionEvent.ACTION_MOVE:
                endX = (int) getX();
                endY = (int) getY();
                int dx = endX - startX;
                int dy = endY-startY;
                if (Math.abs(dx) > Math.abs(dy)){
                    int currentItem = getCurrentItem();
                    if (dx>0){
                        if (currentItem == 0){
                            //拦截左右滑事件
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }else {
                            //item总数
                            int count = getAdapter().getCount();
                            if (currentItem == count-1){
                                //拦截左右滑事件
                                getParent().requestDisallowInterceptTouchEvent(false);
                            }
                        }
                    }
                }else {
                    //拦截左右滑事件,可以上下滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
