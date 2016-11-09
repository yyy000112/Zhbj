package android.ye.zhbjj.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.ye.zhbjj.R;

/**
 * Created by ye on 2016/11/2.
 */
public class BasePager {

    public Activity mActivity;
    public TextView tvTitle;
    public ImageButton ibtnMenu;
    public FrameLayout flContent;
    public final View mRootView;
    public ImageButton btnPhoto;

    public BasePager(Activity activity){
        mActivity = activity;
        //创建该对象时就开始初始化布局
        mRootView = initView();
    }

    //初始化布局
    public View initView(){
        View view =View.inflate(mActivity, R.layout.base_pager, null);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        ibtnMenu = (ImageButton) view.findViewById(R.id.btn_menu);
        flContent = (FrameLayout) view.findViewById(R.id.fl_content);
        btnPhoto = (ImageButton) view.findViewById(R.id.btn_photo);
        return view;
    }

    public void initData(){

    }
}
