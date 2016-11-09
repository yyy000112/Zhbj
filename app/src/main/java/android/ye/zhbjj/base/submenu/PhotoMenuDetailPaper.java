package android.ye.zhbjj.base.submenu;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.ye.zhbjj.R;
import android.ye.zhbjj.base.BaseMenuDetailPager;
import android.ye.zhbjj.domain.PhotosBean;
import android.ye.zhbjj.utils.CacheUtils;
import android.ye.zhbjj.utils.ConstantValue;
import android.ye.zhbjj.utils.ToastUtils;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 菜单详情页-组图
 * Created by ye on 2016/11/3.
 */
public class PhotoMenuDetailPaper extends BaseMenuDetailPager implements View.OnClickListener{

    private View view;
    private ListView lvPhoto;
    private GridView gvPhoto;
    private ArrayList<PhotosBean.PhotoNews> mNewsList;
    private PhotosBean photosBean;
    private ViewHolder holder;
    private PhotosBean.PhotoNews item;
    private ImageOptions imageOptions;

    private ImageButton btnPhoto;
    public PhotoMenuDetailPaper(Activity activity, ImageButton  btnPhoto) {
        super(activity);
        btnPhoto.setOnClickListener(this);
        this.btnPhoto = btnPhoto;
    }

    @Override
    public View initView() {
        view = View.inflate(mActivity, R.layout.photo_menu_detail, null);
        lvPhoto = (ListView) view.findViewById(R.id.lv_photo);
        gvPhoto = (GridView) view.findViewById(R.id.gv_photo);
        return view;
    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(mActivity,ConstantValue.PHOTOURL);
        if (!TextUtils.isEmpty(cache)){
            processData(cache);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        RequestParams params = new RequestParams(ConstantValue.PHOTOURL);
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
                CacheUtils.setCache(mActivity, ConstantValue.PHOTOURL, result);

            }
        });
    }

    private void processData(String result) {
        Gson gson = new Gson();
        photosBean = gson.fromJson(result, PhotosBean.class);
        mNewsList = photosBean.data.news;

        lvPhoto.setAdapter(new PhotoAdapter());
        gvPhoto.setAdapter(new PhotoAdapter());

    }

    private boolean isListView = true;

    //按钮点击事件，用于切换listview或gridview
    @Override
    public void onClick(View v) {
        if (isListView){
            gvPhoto.setVisibility(View.VISIBLE);
            lvPhoto.setVisibility(View.GONE);
            btnPhoto.setBackgroundResource(R.mipmap.icon_pic_list_type);
            isListView = false;
        }else {
            gvPhoto.setVisibility(View.GONE);
            lvPhoto.setVisibility(View.VISIBLE);
            btnPhoto.setBackgroundResource(R.mipmap.icon_pic_grid_type);
            isListView = true;
        }
    }

    private class PhotoAdapter extends BaseAdapter {

        public PhotoAdapter() {
            //设置默认加载的图片
            ImageOptions.Builder imageOptions = new ImageOptions.Builder().setLoadingDrawableId(R.mipmap.news_pic_default);
        }
        @Override
        public int getCount() {
            return mNewsList.size() ;
        }

        @Override
        public PhotosBean.PhotoNews getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                convertView = View.inflate(mActivity,R.layout.list_item_photo,null);
                holder = new ViewHolder();
                holder.title= (TextView) convertView.findViewById(R.id.tv_title);
                holder.ivpic = (ImageView) convertView.findViewById(R.id.iv_pic);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            item = getItem(position);
            holder.title.setText(item.title);
            //使用Xutils3加载图片
            setImage();
            return convertView;
        }
    }

    private void setImage() {
        x.image().bind(holder.ivpic, item.listimage, imageOptions, new Callback.CommonCallback<Drawable>() {
            @Override
            public void onSuccess(Drawable result) {
                holder.ivpic.setBackground(result);
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
        public TextView title;
        public ImageView ivpic;
    }
}
