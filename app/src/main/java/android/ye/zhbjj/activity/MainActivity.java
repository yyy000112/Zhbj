package android.ye.zhbjj.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.ye.zhbjj.R;
import android.ye.zhbjj.fragment.ContentFragment;
import android.ye.zhbjj.fragment.LeftMenuFragment;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;


/**
 * Created by ye on 2016/11/1.
 */
public class MainActivity extends SlidingFragmentActivity  {
    private static final String TAG_LEFT_MENU = "left_menu_fragment";
    private static final String TAG_CONTENT = "content_fragment" ;
    private FragmentManager fm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WindowManager wm = getWindowManager();
        //获取屏幕宽度
        int width = wm.getDefaultDisplay().getWidth();

        //设置左侧边栏
        setBehindContentView(R.layout.left_menu);
        SlidingMenu sm = getSlidingMenu();
        //设置全屏触摸
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //设置屏幕预留宽度
        sm.setBehindOffset(200*width/320);

        initFragment();

    }

    //初始化Fragment
    private void initFragment() {
        //获得Fragment管理者
        fm = getSupportFragmentManager();
        //开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        //用Fragment替换帧布局
        transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(), TAG_LEFT_MENU);
        //替换主页面的帧布局
        transaction.replace(R.id.fl_main, new ContentFragment(), TAG_CONTENT);
        //提交事务
        transaction.commit();
    }
    //获取侧边栏对象
    public LeftMenuFragment getLeftMenuFragment(){
        LeftMenuFragment leftMenuFragment = (LeftMenuFragment) fm.findFragmentByTag(TAG_LEFT_MENU);
        return leftMenuFragment;
    }
    //获取主页面对象
    public ContentFragment getContentFragment(){
        ContentFragment contentFragment = (ContentFragment) fm.findFragmentByTag(TAG_CONTENT);
        return contentFragment;
    }
}
