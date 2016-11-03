package android.ye.zhbjj.domain;

import java.util.ArrayList;

/**
 * Gson中的javabean
 * Created by ye on 2016/11/2.
 */
public class NewsMenu {
    public int retcode;
    public ArrayList<NewsMnenuData> data;
    public ArrayList<Integer> extend;

    public class NewsMnenuData {
        public int id;
        public String title;
        public int type;
        public ArrayList<NewsTabData> children;

        //侧边栏对象
        @Override
        public String toString() {
            return "NewsMnenuData{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", type=" + type +
                    ", children=" + children +
                    '}';
        }
    }

    //页签对象
    public class NewsTabData {
        public int id;
        public int type;
        public String title;
        public String url;

        @Override
        public String toString() {
            return "NewsTabData{" +
                    "id=" + id +
                    ", type=" + type +
                    ", title='" + title + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NewsMenu{" +
                "retcode=" + retcode +
                ", data=" + data +
                ", extend=" + extend +
                '}';
    }
}
