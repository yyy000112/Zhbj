package android.ye.zhbjj.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.ye.zhbjj.R;
import android.ye.zhbjj.activity.MainActivity;
import android.ye.zhbjj.base.BasePager;
import android.ye.zhbjj.base.subpager.GovPager;
import android.ye.zhbjj.base.subpager.HomePager;
import android.ye.zhbjj.base.subpager.NewsCenterPager;
import android.ye.zhbjj.base.subpager.SettingPager;
import android.ye.zhbjj.base.subpager.SmartPager;
import android.ye.zhbjj.view.NoScrollViewPager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

/**
 * 主页面Fragment
 * Created by ye on 2016/11/2.
 */
public class ContentFragment extends BaseFragment {

    private NoScrollViewPager mViewPager;
    private List<BasePager> mPagers;
    private RadioGroup rgGroup;

    @Override
    public void initData() {
        mPagers = new ArrayList<BasePager>();
        mPagers.add(new HomePager(mActivity));
        mPagers.add(new NewsCenterPager(mActivity));
        mPagers.add(new SmartPager(mActivity));
        mPagers.add(new GovPager(mActivity));
        mPagers.add(new SettingPager(mActivity));
        //给ViewPager设置数据适配器
        mViewPager.setAdapter(new ContentAdapter());

        /**
         * 设置底部标签的监听事件
         */
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_home:
                        mViewPager.setCurrentItem(0,false);
                        break;
                    case R.id.rb_news:
                        mViewPager.setCurrentItem(1,false);
                        break;
                    case R.id.rb_smart:
                        mViewPager.setCurrentItem(2,false);
                        break;
                    case R.id.rb_gov:
                        mViewPager.setCurrentItem(3,false);
                        break;
                    case R.id.rb_setting:
                        mViewPager.setCurrentItem(4,false);
                        break;
                }
            }
        });
        //设置viewPager监听事件
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BasePager pager = mPagers.get(position);
                pager.initData();
                if (position==0 || position==mPagers.size()-1){
                    setSlidingMenuEnable(false);
                }else {
                    setSlidingMenuEnable(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //手动加载第一页
        mPagers.get(0).initData();
        //首页禁用侧边栏
        setSlidingMenuEnable(false);
    }

    /**
     * 开启禁用侧边栏
     * @param enable
     */
    private void setSlidingMenuEnable(boolean enable) {
        //获取侧边栏对象,需要Activity
        MainActivity UI= (MainActivity) mActivity;
        SlidingMenu slidingMenu = UI.getSlidingMenu();
        if (enable){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            slidingMenu.setTouchModeAbove(slidingMenu.TOUCHMODE_NONE);
        }


    }

    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_content, null);
        mViewPager = (NoScrollViewPager) view.findViewById(R.id.vp_content);
        rgGroup = (RadioGroup) view.findViewById(R.id.rg_group);
        return view;
    }




     class ContentAdapter extends PagerAdapter {
         @Override
         public int getCount() {
             return mPagers.size();
         }

         @Override
         public boolean isViewFromObject(View view, Object object) {
             return view == object;
         }

         @Override
         public Object instantiateItem(ViewGroup container, int position) {
             BasePager pager = mPagers.get(position);
             //初始化页面
             View view = pager.mRootView;
             pager.initData();
             container.addView(view);
             return view;
         }

         @Override
         public void destroyItem(ViewGroup container, int position, Object object) {
             container.removeView((View) object);
         }
     }

    public NewsCenterPager getNewsCenterPager(){
        NewsCenterPager pager = (NewsCenterPager) mPagers.get(1);
        return pager;
    }
}
