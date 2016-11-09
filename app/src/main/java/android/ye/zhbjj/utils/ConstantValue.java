package android.ye.zhbjj.utils;

/**
 * Created by ye on 2016/11/1.
 */
public class ConstantValue {

    //判断是否是第一次进入应用
    public static final String ISFIRST_ENTER ="isFirst_Enter";

    //通过Tomcat获取本地json的地址
    //172.20.10.2,10.0.2.2
    public static final String URL_ADDRESS ="http://172.20.10.2:8080/zhbj/categories.json";

    //设置服务器地址，后缀动态添加
    public static final String SERVER = "http://172.20.10.2:8080/zhbj";

    //图片
    public static final String PHOTO = "/photos/photos_1.json";
    //图片地址
    public static final String PHOTOURL = SERVER+PHOTO;
}
