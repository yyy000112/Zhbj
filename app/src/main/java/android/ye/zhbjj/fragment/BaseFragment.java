package android.ye.zhbjj.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by ye on 2016/11/2.
 */
public abstract class BaseFragment extends Fragment {

    public Activity mActivity;

    //创建Fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取当前fragment所依赖的Activity
        mActivity = getActivity();
    }

    //初始化Fragment布局
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView();
        return view;
    }

    //fragment 所依赖的activity的onCreate方法执行结束
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化数据
        initData();
    }

    //由子类实现数据初始化
    public abstract void initData();

    //视图由子类具体去实现
    public abstract View initView();

}
