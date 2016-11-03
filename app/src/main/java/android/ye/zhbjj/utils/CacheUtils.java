package android.ye.zhbjj.utils;

import android.content.Context;


/**
 * 以URL为KEY，JSON为Value 存储至本地
 */
 public class CacheUtils {

    public static void setCache(Context context,String url, String json){
        SpUtils.setString(context,url,json);
    }

    public static String getCache(Context context,String url){
        return SpUtils.getString(context,url,"");
    }
}
