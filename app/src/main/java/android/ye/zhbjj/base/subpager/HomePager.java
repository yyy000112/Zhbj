package android.ye.zhbjj.base.subpager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.ye.zhbjj.base.BasePager;

/**
 * 首页
 * Created by ye on 2016/11/2.
 */
public class HomePager extends BasePager {
    public HomePager(Activity activity) {
        super(activity);
    }

    public void initData(){
        //给帧布局填充对象
        TextView textView = new TextView(mActivity);
        textView.setText("首页");
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        flContent.addView(textView);
        //设置顶部标题
        tvTitle.setText("智慧北京");
    }


}
