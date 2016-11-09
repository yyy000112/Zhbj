package android.ye.zhbjj.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.ye.zhbjj.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ye on 2016/11/8.
 */
public class PullToRefresh extends ListView implements AbsListView.OnScrollListener {

    private static final int STATE_PULL_REFRESH = 1;
    private static final int STATE_RELEASE_REFRESH = 2;
    private static final int STATE_REFRESHING = 3;
    //默认当前状态
    private int mCurrentState = STATE_PULL_REFRESH;

    private View mInitHeaderView;
    private int headerViewHeight;
    private int startY;
    private ProgressBar pbLoading;
    private ImageView ivArrow;
    private TextView tvPullRefresh;
    private TextView tvDateTime;
    private RotateAnimation animUp;
    private RotateAnimation animDown;
    private OnRefreshListener mOnRefreshListener;
    private View mFootView;
    private ProgressBar pbLoadMore;
    private TextView tvMoreRefresh;
    private int mFootViewHeight;

    public PullToRefresh(Context context) {
        super(context);
        initHeaderView();
        initFootView();
    }

    public PullToRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFootView();
    }

    public PullToRefresh(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFootView();
    }

    public PullToRefresh(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initHeaderView();
        initFootView();
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView(){
        mInitHeaderView = View.inflate(getContext(), R.layout.pull_to_refresh, null);
        pbLoading = (ProgressBar) mInitHeaderView.findViewById(R.id.pb_pull);
        ivArrow = (ImageView) mInitHeaderView.findViewById(R.id.iv_arrow);
        tvPullRefresh = (TextView) mInitHeaderView.findViewById(R.id.tv_pull_refresh);
        tvDateTime = (TextView) mInitHeaderView.findViewById(R.id.tv_date_time);
        this.addHeaderView(mInitHeaderView);

        mInitHeaderView.measure(0, 0);
        headerViewHeight = mInitHeaderView.getMeasuredHeight();
        mInitHeaderView.setPadding(0, -headerViewHeight, 0, 0);

        initAnimation();
        setCurrentTime();

    }

    //初始化脚布局
    private void initFootView() {
        mFootView = View.inflate(getContext(), R.layout.foot_pull_to_refresh, null);
        pbLoadMore = (ProgressBar) mFootView.findViewById(R.id.pb_load_more);
        tvMoreRefresh = (TextView) mFootView.findViewById(R.id.tv_pull_more_refresh);
        this.addFooterView(mFootView);

        mFootView.measure(0, 0);
        mFootViewHeight = mFootView.getHeight();
        mFootView.setPadding(0,-mFootViewHeight,0,0);
        //设置滚动监听
        this.setOnScrollListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //用于判断用户触摸在viewpager上时无法进行下拉刷新操作
                if (startY == -1){
                    startY = (int) ev.getY();
                }
                //当状态为正在刷新时，跳出循环
                if (mCurrentState == STATE_REFRESHING){
                    break;
                }
                int moveY = (int) ev.getY();
                int dY= moveY-startY;
                int firstVisiblePosition = getFirstVisiblePosition();
                //当显示第一个item并且dY>0时才触发事件
                if (dY>0 && firstVisiblePosition == 0){
                    int padding = dY - headerViewHeight;
                    mInitHeaderView.setPadding(0,padding,0,0);
                    if (padding>0 && mCurrentState != STATE_RELEASE_REFRESH){

                        //显示松开刷新
                        mCurrentState = STATE_RELEASE_REFRESH;
                        refreshState();
                    }else if (padding<0 && mCurrentState != STATE_PULL_REFRESH){
                        //显示下拉刷新
                        mCurrentState = STATE_PULL_REFRESH;
                        refreshState();
                    }

                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mCurrentState == STATE_RELEASE_REFRESH){
                    mCurrentState = STATE_REFRESHING;
                    refreshState();
                    //完整展示头布局
                    mInitHeaderView.setPadding(0,0,0,0);
                    if (mOnRefreshListener != null){
                        //回调
                        mOnRefreshListener.onRefresh();
                    }

                }else if (mCurrentState == STATE_PULL_REFRESH){
                    //隐藏头布局
                    mInitHeaderView.setPadding(0,-headerViewHeight,0,0);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }


    /**
     * 初始化箭头动画
     */
    private void initAnimation(){
        animUp = new RotateAnimation(0,-180, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);

        animDown = new RotateAnimation(-180,0, Animation.RELATIVE_TO_PARENT,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animDown.setDuration(200);
        animDown.setFillAfter(true);
    }

    /**
     * 状态更新
     */
    private void refreshState() {
        switch (mCurrentState){
            case STATE_PULL_REFRESH:
                tvPullRefresh.setText("下拉刷新");
                pbLoading.setVisibility(View.INVISIBLE);
                ivArrow.setVisibility(View.VISIBLE);
                ivArrow.startAnimation(animDown);
                break;
            case STATE_REFRESHING:
                pbLoading.setVisibility(View.VISIBLE);
                tvPullRefresh.setText("正在刷新...");
                //清除箭头动画，否则无法隐藏
                ivArrow.clearAnimation();
                ivArrow.setVisibility(View.INVISIBLE);
                break;
            case STATE_RELEASE_REFRESH:
                tvPullRefresh.setText("松开刷新");
                pbLoading.setVisibility(View.INVISIBLE);
                ivArrow.setVisibility(View.VISIBLE);
                ivArrow.startAnimation(animUp);
                break;
        }
    }

    /**
     * 刷新完成后收起控件并更新时间
     * @param success
     */
    public void onRefreshComplete(boolean success){
        if (!isLoadMore){
            //刷新结束隐藏头布局
            mInitHeaderView.setPadding(0,-headerViewHeight,0,0);
            //更改当前状态
            mCurrentState = STATE_PULL_REFRESH;
            ivArrow.setVisibility(View.VISIBLE);
            pbLoading.setVisibility(View.INVISIBLE);
            if (success){
                setCurrentTime();
            }
        }else {
            //加载更多结束时隐藏脚布局
            mFootView.setPadding(0,-mFootViewHeight,0,0);
            isLoadMore = false;
        }

    }

    private void setCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(new Date());
        tvDateTime.setText(time);
    }


    //设置监听
    public void setOnRefreshListener(OnRefreshListener listener){
        mOnRefreshListener = listener;
    }

    //设置有无加载更多的标记
   private boolean isLoadMore;
    //滑动状态变化
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE){
            int lastVisiblePosition = getLastVisiblePosition();
            //滑到最后一条并且没有在加载更多
           ;
            if (lastVisiblePosition == getCount()-1 && !isLoadMore){
                isLoadMore = true;
                //显示脚布局
                mFootView.setPadding(0,0,0,0);
                //强脚布局设置成最后一个item
                setSelection(getCount()-1);
                //通知主界面加载更多数据
                if (mOnRefreshListener!=null){
                    mOnRefreshListener.loadMore();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    //建立回调接口
    public interface OnRefreshListener{
        public void onRefresh();

        public void loadMore();
    }

}
