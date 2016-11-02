package android.ye.zhbjj.base.subpager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.ye.zhbjj.base.BasePager;

/**
 * 政务
 * Created by ye on 2016/11/2.
 */
public class GovPager extends BasePager {
    public GovPager(Activity activity) {
        super(activity);
    }

    public void initData(){
        //给帧布局填充对象
        TextView textView = new TextView(mActivity);
        textView.setText("政务");
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        flContent.addView(textView);
    }


}
