package android.ye.zhbjj.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.ye.zhbjj.R;

/**
 * 菜单详情页基类
 * Created by ye on 2016/11/2.
 */
public abstract class BaseMenuDetailPager {


    public final Activity mActivity;
    public final View mRootView;

    public BaseMenuDetailPager(Activity activity){
        mActivity = activity;
        //创建该对象时就开始初始化布局
        mRootView = initView();
    }

    //初始化布局
    //必须由子类实现
    public abstract View initView();

    public void initData(){

    }


}
