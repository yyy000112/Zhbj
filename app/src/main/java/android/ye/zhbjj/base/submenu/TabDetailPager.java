package android.ye.zhbjj.base.submenu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.ye.zhbjj.R;
import android.ye.zhbjj.activity.NewsDetailActivity;
import android.ye.zhbjj.base.BaseMenuDetailPager;
import android.ye.zhbjj.domain.NewsMenu;
import android.ye.zhbjj.domain.NewsTabBean;
import android.ye.zhbjj.utils.CacheUtils;
import android.ye.zhbjj.utils.ConstantValue;
import android.ye.zhbjj.utils.SpUtils;
import android.ye.zhbjj.utils.ToastUtils;
import android.ye.zhbjj.view.PullToRefresh;
import android.ye.zhbjj.view.TabNewsViewPager;

import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.security.Provider;
import java.util.ArrayList;

/**
 * 页签设置于NewsMenuDetailPaper
 * Created by ye on 2016/11/3.
 */
public class TabDetailPager extends BaseMenuDetailPager {

    private final NewsMenu.NewsTabData newsTabData1;
    private TextView textView;
    private PullToRefresh lvDetail;
    private final String mUrl;
    private NewsTabBean newsTabBean;
    private TabNewsAdapter mPagerAdapter;
    private ArrayList<NewsTabBean.TopNews> topnews;
    private ImageView view;
    private String imagUrl;
    private ImageOptions imageOptions;
    private TabNewsViewPager mViewPager;
    private TextView tvTabTitle;
    private CirclePageIndicator mIndicator;
    private NewsAdapter mNewsAdapter;
    private ArrayList<NewsTabBean.NewsData> newsList;
    private ViewHolder holder;
    private NewsTabBean.NewsData newsData;
    private String mMoreUrl;
    private NewsTabBean.NewsData news;
    private Handler mHandler;

    public TabDetailPager(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        newsTabData1 = newsTabData;
        mUrl = ConstantValue.SERVER + newsTabData1.url;

    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);
        //改listview添加头文件，可以跟着listView滑动
        View mHeaderView = View.inflate(mActivity,R.layout.news_list_head_item,null);

        mViewPager = (TabNewsViewPager) mHeaderView.findViewById(R.id.vp_tab_detail);
        lvDetail = (PullToRefresh) view.findViewById(R.id.lv_tab_detail);
        tvTabTitle = (TextView) mHeaderView.findViewById(R.id.tv_tab_title);
        //使用ViewIndicator中的小圆点
        mIndicator = (CirclePageIndicator) mHeaderView.findViewById(R.id.circleIndicator);
        lvDetail.addHeaderView(mHeaderView);
        //设置界面进行回调
        lvDetail.setOnRefreshListener(new PullToRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //更新数据
                getDataFromServer();
            }

            @Override
            public void loadMore() {
                //判断url是否为空，不为空时加载更多
                if (mMoreUrl!=null){
                    getMoreDataFromServer();
                }else {
                    ToastUtils.show(mActivity,"没有更多数据，无法加载下一页");
                    lvDetail.onRefreshComplete(true);
                }
            }
        });

        //设置已读标记并且跳转到新闻详情页
        lvDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取头布局数量
                int headerViewsCount = lvDetail.getHeaderViewsCount();
                //需要减去头布局占位数量
                position = position - headerViewsCount;
                news = newsList.get(position);

                String readIds = SpUtils.getString(mActivity,"read_id","");
                //z只有在不包含当前id的才追加为已读，避免重复
                if (!readIds.contains(news.id+"")) {
                    readIds = readIds + news.id+",";
                    SpUtils.setString(mActivity,"read_id",readIds);
                    Log.d("TAG","走过");
                }
                    //将已被点击的条目文字设置为灰色，局部刷新
                    TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
                    tvTitle.setTextColor(Color.GRAY);


                    //跳转到新闻详情页面
                    Intent intent = new Intent(mActivity,NewsDetailActivity.class);
                    intent.putExtra("url", news.url);
                    mActivity.startActivity(intent);

            }
        });
        return view;
    }

    //加载更多数据
    private void getMoreDataFromServer() {
        RequestParams params = new RequestParams(mMoreUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onCancelled(Callback.CancelledException cex) {
                ToastUtils.show(mActivity, cex.getMessage());
                lvDetail.onRefreshComplete(false);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.show(mActivity, ex.getMessage());
                //收起下拉刷新
                lvDetail.onRefreshComplete(false);
            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onSuccess(String result) {
                //解析Json数据
                processData(result,true);
                //存储缓存
                CacheUtils.setCache(mActivity, mUrl, result);
                //下拉控件刷新
                lvDetail.onRefreshComplete(true);
            }
        });
    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(mActivity, mUrl);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache,false);
        }
        //发起网络请求
        getDataFromServer();
    }

    private void getDataFromServer() {
        RequestParams params = new RequestParams(mUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onCancelled(Callback.CancelledException cex) {
                ToastUtils.show(mActivity, cex.getMessage());
                lvDetail.onRefreshComplete(false);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.show(mActivity, ex.getMessage());
                //收起下拉刷新
                lvDetail.onRefreshComplete(false);
            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onSuccess(String result) {
                //解析Json数据
                processData(result, false);
                //存储缓存
                CacheUtils.setCache(mActivity, mUrl, result);
                //下拉控件刷新
                lvDetail.onRefreshComplete(true);
            }
        });
    }

    private void processData(String result,boolean isMore) {
        Gson gson = new Gson();
        newsTabBean = gson.fromJson(result, NewsTabBean.class);
        //获取加载更多的url
        String moreUrl = newsTabBean.data.more;
        if (!TextUtils.isEmpty(moreUrl)){
            mMoreUrl = ConstantValue.SERVER+moreUrl;
        }else {
            moreUrl =null;
        }
        if (!isMore){
            //填充新闻数据
            topnews = newsTabBean.data.topnews;
            if (topnews != null) {
                mPagerAdapter = new TabNewsAdapter();
                mViewPager.setAdapter(mPagerAdapter);
                mIndicator.setViewPager(mViewPager);
                //设置为True表示不跟着手指滑动
                mIndicator.setSnap(true);
                mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        //更新新闻标题
                        NewsTabBean.TopNews topNews = topnews.get(position);
                        tvTabTitle.setText(topNews.title);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                //手动更新第一页标题
                tvTabTitle.setText(topnews.get(0).title);
                // 默认让第一个选中
                mIndicator.onPageSelected(0);
            }
            //列表新闻
            newsList = newsTabBean.data.news;
            if (newsList !=null){
                mNewsAdapter = new NewsAdapter();
                lvDetail.setAdapter(mNewsAdapter);
            }
            //用于实现新闻轮播
            if (mHandler == null){
                mHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        int currentItem = mViewPager.getCurrentItem();
                        currentItem++;
                        if (currentItem>topnews.size()-1){
                           //跳回到第一页
                            currentItem = 0;
                        }
                        mViewPager.setCurrentItem(currentItem);
                        // 继续发送延时3秒的消息,形成内循环
                        mHandler.sendEmptyMessageDelayed(0, 3000);

                    }
                };
                //发送延时3秒的消息,保证启动自动轮播逻辑只执行一次
                mHandler.sendEmptyMessageDelayed(0,3000);

                mViewPager.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()){
                            //按下时停止轮播
                            case MotionEvent.ACTION_DOWN:
                                mHandler.removeCallbacksAndMessages(null);
                                break;
                            //事件被取消时继续发送消息轮播（当按下viewpager后,直接滑动listview,导致抬起事件无法响应）
                            case MotionEvent.ACTION_CANCEL:
                                mHandler.sendEmptyMessageDelayed(0, 3000);
                                break;
                            //抬起时发送消息继续轮播
                            case MotionEvent.ACTION_UP:
                                mHandler.sendEmptyMessageDelayed(0, 3000);
                                break;
                        }

                        return false;
                    }
                });
            }
        }else {
            ArrayList<NewsTabBean.NewsData> moreNews = newsTabBean.data.news;
            //追加更多数据
            newsList.addAll(moreNews);
            //刷新listview
            mNewsAdapter.notifyDataSetChanged();
        }


    }

    class TabNewsAdapter extends PagerAdapter {

        public TabNewsAdapter() {
            //设置默认加载的图片
            ImageOptions.Builder imageOptions = new ImageOptions.Builder().setLoadingDrawableId(R.mipmap.topnews_item_default);
        }

        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            view = new ImageView(mActivity);
            //设置图片缩放方式, 宽高填充父控件
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            //获取图片网络地址
            imagUrl = topnews.get(position).topimage;
            //下载图片
            loadImage();
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private void loadImage() {
        //使用Xutils3下载图片
        x.image().bind(view, imagUrl, imageOptions, new Callback.CommonCallback<Drawable>() {
            @Override
            public void onSuccess(Drawable result) {
                view.setBackground(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.show(mActivity,ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    private class NewsAdapter extends BaseAdapter {

        public NewsAdapter() {
            //设置默认加载的图片
            ImageOptions.Builder imageOptions = new ImageOptions.Builder().setLoadingDrawableId(R.mipmap.news_pic_default);
        }

        @Override
        public int getCount() {
            return newsList.size();
        }

        @Override
        public NewsTabBean.NewsData getItem(int position) {
            return newsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = View.inflate(mActivity,R.layout.news_list_item,null);
                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tvTime = (TextView) convertView.findViewById(R.id.tv_date);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            newsData = getItem(position);
            holder.tvTitle.setText(newsData.title);
            holder.tvTime.setText(newsData.pubdate);
            //根据本地记录来标记已读未读
            String readIds = SpUtils.getString(mActivity,"read_id","");
            if (readIds.contains(newsData.id+"")){
                holder.tvTitle.setTextColor(Color.GRAY);
            }else {
                holder.tvTitle.setTextColor(Color.BLACK);
            }
            //使用Xutils3加载图片
            setImage();

            return convertView;
        }
    }
    private void setImage() {
        x.image().bind(holder.icon, newsData.listimage, imageOptions, new Callback.CommonCallback<Drawable>() {
            @Override
            public void onSuccess(Drawable result) {
                holder.icon.setBackground(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.show(mActivity,ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }


    private class ViewHolder {
        public ImageView icon;
        public TextView tvTitle;
        public TextView tvTime;
    }
}