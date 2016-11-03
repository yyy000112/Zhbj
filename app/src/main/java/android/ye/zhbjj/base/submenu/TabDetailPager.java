package android.ye.zhbjj.base.submenu;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.ye.zhbjj.R;
import android.ye.zhbjj.base.BaseMenuDetailPager;
import android.ye.zhbjj.domain.NewsMenu;
import android.ye.zhbjj.domain.NewsTabBean;
import android.ye.zhbjj.utils.CacheUtils;
import android.ye.zhbjj.utils.ConstantValue;
import android.ye.zhbjj.utils.ToastUtils;
import android.ye.zhbjj.view.TabNewsViewPager;

import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 页签设置于NewsMenuDetailPaper
 * Created by ye on 2016/11/3.
 */
public class TabDetailPager extends BaseMenuDetailPager {

    private final NewsMenu.NewsTabData newsTabData1;
    private TextView textView;
    private ListView lvDetail;
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
        lvDetail = (ListView) view.findViewById(R.id.lv_tab_detail);
        tvTabTitle = (TextView) mHeaderView.findViewById(R.id.tv_tab_title);
        //使用ViewIndicator中的小圆点
        mIndicator = (CirclePageIndicator) mHeaderView.findViewById(R.id.circleIndicator);
        lvDetail.addHeaderView(mHeaderView);
        return view;
    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(mActivity, mUrl);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache);
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
                CacheUtils.setCache(mActivity, mUrl, result);

            }
        });
    }

    private void processData(String result) {
        Gson gson = new Gson();
        newsTabBean = gson.fromJson(result, NewsTabBean.class);

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