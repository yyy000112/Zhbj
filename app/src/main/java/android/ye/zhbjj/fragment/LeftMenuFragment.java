package android.ye.zhbjj.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.ye.zhbjj.R;
import android.ye.zhbjj.activity.MainActivity;
import android.ye.zhbjj.base.subpager.NewsCenterPager;
import android.ye.zhbjj.domain.NewsMenu;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;


/**
 * Created by ye on 2016/11/2.
 */
public class LeftMenuFragment extends BaseFragment {

    private ListView lvLeft;
    private ArrayList<NewsMenu.NewsMnenuData> mNewsMenuData;
    private int mCurrentPos;

    @Override
    public void initData() {

    }

    public void setMenuData(ArrayList<NewsMenu.NewsMnenuData> data ){
        //重设当前选中的位置
        mCurrentPos = 0;

        //更新数据
        mNewsMenuData = data;
        final MyAdapter myAdapter = new MyAdapter();
        lvLeft.setAdapter(myAdapter);
        //设置listView点击事件
        lvLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //更新当前被选中的位置
                mCurrentPos = position;
                //通知更新
                myAdapter.notifyDataSetChanged();
                //收起侧边栏
                toggle();
                //设置当前页详情
                setCurerentPager(position);
            }
        });
    }

    private void toggle() {
        MainActivity Ui = (MainActivity) mActivity;
        SlidingMenu slidingMenu = Ui.getSlidingMenu();
        slidingMenu.toggle();
    }

    //设置当前页详情,更新cotent内容
    private void setCurerentPager(int position) {
        MainActivity Ui = (MainActivity) mActivity;
        ContentFragment contentFragment = Ui.getContentFragment();
        //获取NewsCenterPaper
        NewsCenterPager newsCenterPager =contentFragment.getNewsCenterPager();
        //设置内容
        newsCenterPager.setCurrentDetailPager(position);
    }

    @Override
    public View initView() {
        View view = View.inflate(getActivity(),R.layout.fragment_left_menu,null);
        lvLeft = (ListView) view.findViewById(R.id.lv_left);
        return view;
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mNewsMenuData.size();
        }

        @Override
        public NewsMenu.NewsMnenuData getItem(int position) {
            return mNewsMenuData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity,R.layout.list_item_left_menu,null);
            TextView tvMenu = (TextView) view.findViewById(R.id.tv_menu);
            NewsMenu.NewsMnenuData newsMnenuData = getItem(position);
            tvMenu.setText(newsMnenuData.title);
            if (position == mCurrentPos){
                //文字变红
                tvMenu.setEnabled(true);
            }else {
               // 文字变白
                tvMenu.setEnabled(false);
            }
            return view;
        }
    }
}
