package android.ye.zhbjj.xutils;

import android.app.Application;

import org.xutils.x;

/**
 * Created by ye on 2016/11/2.
 */
public class LYJApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Xutils初始化
        x.Ext.init(this);
    }
}
