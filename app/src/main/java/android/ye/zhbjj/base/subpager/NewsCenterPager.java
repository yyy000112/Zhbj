package android.ye.zhbjj.base.subpager;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.ye.zhbjj.activity.MainActivity;
import android.ye.zhbjj.base.BaseMenuDetailPager;
import android.ye.zhbjj.base.BasePager;
import android.ye.zhbjj.base.submenu.InteractMenuDetailPaper;
import android.ye.zhbjj.base.submenu.NewsMenuDetailPaper;
import android.ye.zhbjj.base.submenu.PhotoMenuDetailPaper;
import android.ye.zhbjj.base.submenu.TopicMenuDetailPaper;
import android.ye.zhbjj.domain.NewsMenu;
import android.ye.zhbjj.fragment.LeftMenuFragment;
import android.ye.zhbjj.utils.CacheUtils;
import android.ye.zhbjj.utils.ConstantValue;
import android.ye.zhbjj.utils.ToastUtils;

import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置页面
 * Created by ye on 2016/11/2.
 */
public class NewsCenterPager extends BasePager {

    private NewsMenu mNewsMenu;
    private List<BaseMenuDetailPager> mBaseMenuDetailPagers;

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    public void initData(){

        //设置顶部标题
        tvTitle.setText("新闻");
        //设置按钮可见
        ibtnMenu.setVisibility(View.VISIBLE);
        ibtnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });

        //检查缓存是否存在
        String cache = CacheUtils.getCache(mActivity,ConstantValue.URL_ADDRESS);
        if (!TextUtils.isEmpty(cache)){
            processData(cache);
        }

        //请求网络数据
        getDataFromServer();
    }

    //点击按钮时关闭或打开菜单栏
    private void toggle() {
        MainActivity UI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = UI.getSlidingMenu();
        slidingMenu.toggle();
    }

    //从服务器上获取json数据
    private void getDataFromServer() {
        RequestParams params = new RequestParams(ConstantValue.URL_ADDRESS);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onCancelled(Callback.CancelledException cex) {
                ToastUtils.show(mActivity, cex.getMessage());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.show(mActivity, ex.getMessage());
            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onSuccess(String result) {
                //解析Json数据
                processData(result);
                //存储缓存
                CacheUtils.setCache(mActivity,ConstantValue.URL_ADDRESS,result);

            }
        });


    }

    private void processData(String json) {
        Gson gson = new Gson();
        mNewsMenu = gson.fromJson(json,NewsMenu.class);
        //获取侧边栏
        MainActivity UI = (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = UI.getLeftMenuFragment();
        //给侧边栏设置数据
        leftMenuFragment.setMenuData(mNewsMenu.data);
        //初始化4个详情页
        mBaseMenuDetailPagers = new ArrayList<BaseMenuDetailPager>();
        mBaseMenuDetailPagers.add(new NewsMenuDetailPaper(mActivity,mNewsMenu.data.get(0).children));
        mBaseMenuDetailPagers.add(new TopicMenuDetailPaper(mActivity));
        mBaseMenuDetailPagers.add(new PhotoMenuDetailPaper(mActivity,btnPhoto));
        mBaseMenuDetailPagers.add(new InteractMenuDetailPaper(mActivity));

        //设置默认首页
        setCurrentDetailPager(0);
    }

    //设置菜单详情页
    public void setCurrentDetailPager(int position) {
        BaseMenuDetailPager pager = mBaseMenuDetailPagers.get(position);
        //当前页布局
        View view = pager.mRootView;

        //清楚旧的布局
        flContent.removeAllViews();
        //给帧布局添加
        flContent.addView(view);
        //初始化页面
        pager.initData();
        //更新标题
        tvTitle.setText(mNewsMenu.data.get(position).title);

        //如果是组图页面，需要显示切换按钮
        if (pager instanceof PhotoMenuDetailPaper){
            btnPhoto.setVisibility(View.VISIBLE);
        }else btnPhoto.setVisibility(View.GONE);

    }


}
