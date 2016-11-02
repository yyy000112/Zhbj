package android.ye.zhbjj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.ye.zhbjj.R;
import android.ye.zhbjj.utils.ConstantValue;
import android.ye.zhbjj.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 导航页面
 * Created by ye on 2016/11/1.
 */
public class GuideActivity extends Activity{

    private ViewPager mViewPager;
    private Button btnStart;
    private int[] mImages;
    private List<ImageView> mImageViewList;
    private LinearLayout llContainer;
    private ImageView ivRedPoint;
    private int mPointDis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initUI();
        initData();
        //改ViewPager设置数据
        mViewPager.setAdapter(new MyPagerAdatper());
        //设置ViewPager监听事件
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //计算小红点的左边距
                int mLeftMargin = (int) (mPointDis*positionOffset+position*mPointDis);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
                //修改左边距
                params.leftMargin =mLeftMargin;
                //重设布局参数
                ivRedPoint.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                if (position == mImageViewList.size() - 1) {
                    btnStart.setVisibility(View.VISIBLE);
                } else {
                    btnStart.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //监听小圆点
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //移除监听，避免重复回调
                ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //计算小圆点的移动距离
                mPointDis = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0).getLeft();
            }
        });

    }

    private void initData() {
        mImages = new int[]{R.mipmap.guide_1,R.mipmap.guide_2,R.mipmap.guide_3};
        mImageViewList = new ArrayList<ImageView>();
        for (int i = 0; i<mImages.length;i++){
            ImageView view = new ImageView(this);
            view.setBackgroundResource(mImages[i]);
            mImageViewList.add(view);
            //添加圆点
            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.guide_point_gray);
            //初始化布局参数, 宽高包裹内容,父控件是谁,就是谁声明的布局参数
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i>0){
                params.leftMargin=10;
            }
            point.setLayoutParams(params);
            llContainer.addView(point);
        }

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击后表示不是第一次进入
                SpUtils.setBoolean(getApplicationContext(), ConstantValue.ISFIRST_ENTER,false);
                //跳转到主页面
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    private void initUI() {
        mViewPager = (ViewPager) findViewById(R.id.vp_guide);
        btnStart = (Button) findViewById(R.id.btn_start);
        llContainer = (LinearLayout) findViewById(R.id.ll_container);
        ivRedPoint = (ImageView) findViewById(R.id.iv_red_point);
    }

    class MyPagerAdatper extends PagerAdapter {
        @Override
        public int getCount() {
            return mImageViewList.size();
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(getApplicationContext());
            view=mImageViewList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
