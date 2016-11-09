package android.ye.zhbjj.domain;

import java.util.ArrayList;

/**
 * 组图javabean
 * Created by ye on 2016/11/9.
 */
public class PhotosBean {

    public PhotoData data;

    public class PhotoData{
        public ArrayList<PhotoNews> news;
    }

    public class PhotoNews {
        public int id;
        public String listimage;
        public String title;
    }
}
