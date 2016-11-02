package android.ye.zhbjj.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.ye.zhbjj.R;


/**
 * Created by ye on 2016/11/2.
 */
public class LeftMenuFragment extends BaseFragment {

    private ListView lvLeft;

    @Override
    public void initData() {
        lvLeft.setAdapter(new MyAdapter());
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
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
