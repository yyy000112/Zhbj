package android.ye.zhbjj.base.submenu;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.ye.zhbjj.base.BaseMenuDetailPager;

/**
 * 菜单详情页-互动
 * Created by ye on 2016/11/3.
 */
public class InteractMenuDetailPaper extends BaseMenuDetailPager {
    public InteractMenuDetailPaper(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView textView = new TextView(mActivity);
        textView.setText("菜单详情页-互动");
        textView.setTextColor(Color.RED);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public void initData() {

    }
}
