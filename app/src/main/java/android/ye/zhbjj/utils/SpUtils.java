package android.ye.zhbjj.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ye on 2016/11/1.
 */
public class SpUtils {
    //boolean
    public static boolean getBoolean(Context context,String key, boolean defval){
        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        return sp.getBoolean(key,defval);
    }

    public static void setBoolean(Context context,String key, boolean defval){
        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,defval);
    }

    //String
    public static String getString(Context context,String key, String defval){
        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        return sp.getString(key, defval);
    }

    public static void setString(Context context,String key, String defval){
        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        sp.edit().putString(key, defval);
    }

    //int
    public static int getInt(Context context,String key, int defval){
        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        return sp.getInt(key, defval);
    }

    public static void setInt(Context context,String key, int defval){
        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        sp.edit().putInt(key, defval);
    }
}
