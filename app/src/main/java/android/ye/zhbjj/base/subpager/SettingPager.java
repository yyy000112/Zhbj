package android.ye.zhbjj.base.subpager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.ye.zhbjj.base.BasePager;

/**
 * 设置页面
 * Created by ye on 2016/11/2.
 */
public class SettingPager extends BasePager {
    public SettingPager(Activity activity) {
        super(activity);
    }

    public void initData(){
        //给帧布局填充对象
        TextView textView = new TextView(mActivity);
        textView.setText("设置");
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        flContent.addView(textView);
    }


}
