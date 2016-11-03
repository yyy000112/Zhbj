package android.ye.zhbjj.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ye on 2016/11/2.
 */
public class ToastUtils {
    /**
     *
     * @param context 上下文
     * @param msg 文本内容
     */
    public static void show(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
