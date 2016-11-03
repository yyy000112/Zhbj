package android.ye.zhbjj.base.submenu;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.ye.zhbjj.R;
import android.ye.zhbjj.activity.MainActivity;
import android.ye.zhbjj.base.BaseMenuDetailPager;
import android.ye.zhbjj.domain.NewsMenu;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单详情页-新闻
 * Created by ye on 2016/11/3.
 */
public class NewsMenuDetailPaper extends BaseMenuDetailPager {

    private ViewPager vpNewsDetails;
    //页签网络数据
    private final ArrayList<NewsMenu.NewsTabData> mNewsTabData;
    //页签页面集合
    private List<TabDetailPager> tabDetailPagers;
    private TabPageIndicator indicator;
    private ImageButton ibtnNextPage;

    public NewsMenuDetailPaper(Activity activity, ArrayList<NewsMenu.NewsTabData> children) {
        super(activity);
        mNewsTabData = children;
    }

    @Override
    public View initView() {
       View view = View.inflate(mActivity,R.layout.news_detail_pager,null);
        vpNewsDetails = (ViewPager) view.findViewById(R.id.vp_news_menu_detail);
        indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        ibtnNextPage = (ImageButton) view.findViewById(R.id.itn_next_page);
        ibtnNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击时跳转页面
                int currentPos = vpNewsDetails.getCurrentItem();
                currentPos++;
                vpNewsDetails.setCurrentItem(currentPos);

            }
        });
        return view;
    }

    @Override
    public void initData() {
        //初始化页面
        tabDetailPagers = new ArrayList<TabDetailPager>();
        for (int i = 0; i<mNewsTabData.size();i++){
            TabDetailPager pager = new TabDetailPager(mActivity,mNewsTabData.get(i));

            tabDetailPagers.add(pager);
        }
        vpNewsDetails.setAdapter(new mAdapter());
        //将指示器与viewpager绑定，必须在设定适配器后进行
        indicator.setViewPager(vpNewsDetails);
        //设置指示器监听事件
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //当在第一页的时候允许开启侧边栏
                    if (position == 0){
                        setSlidingMenuEnable(true);
                    }else {
                        setSlidingMenuEnable(false);
                    }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    //禁用与开启侧边栏
    private void setSlidingMenuEnable(boolean enable) {
        MainActivity UI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = UI.getSlidingMenu();
        if (enable){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    class mAdapter extends PagerAdapter {

         //返回指定指示器的标题，还需在manifest设置主题样式
         @Override
         public CharSequence getPageTitle(int position) {
             NewsMenu.NewsTabData data = mNewsTabData.get(position);
             return data.title;
         }

         @Override
         public int getCount() {
             return tabDetailPagers.size();
         }

         @Override
         public boolean isViewFromObject(View view, Object object) {
             return view==object;
         }

         @Override
         public Object instantiateItem(ViewGroup container, int position) {
             TabDetailPager pager = tabDetailPagers.get(position);
             View view = pager.mRootView;
             container.addView(view);
             pager.initData();
             return view;
         }

         @Override
         public void destroyItem(ViewGroup container, int position, Object object) {
             container.removeView((View) object);
         }
     }
}
