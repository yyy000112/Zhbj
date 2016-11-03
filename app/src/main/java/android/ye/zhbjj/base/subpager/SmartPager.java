package android.ye.zhbjj.base.subpager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.ye.zhbjj.base.BasePager;

/**
 * 智慧服务
 * Created by ye on 2016/11/2.
 */
public class SmartPager extends BasePager {
    public SmartPager(Activity activity) {
        super(activity);
    }

    public void initData(){
        //给帧布局填充对象
        TextView textView = new TextView(mActivity);
        textView.setText("智慧服务");
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        flContent.addView(textView);
        //设置顶部标题
        tvTitle.setText("生活");
    }


}
