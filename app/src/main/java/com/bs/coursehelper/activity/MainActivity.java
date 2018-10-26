package com.bs.coursehelper.activity;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.coursehelper.R;
import com.bs.coursehelper.adapter.MainAdapter;
import com.bs.coursehelper.base.BaseActivity;
import com.bs.coursehelper.base.BaseFragment;
import com.bs.coursehelper.fragment.HomeFragment;
import com.bs.coursehelper.fragment.MineFragment;
import com.bs.coursehelper.fragment.MyCourseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 首页
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.id_vp_content_home)
    ViewPager idVpContentHome;
    @BindView(R.id.id_tl_tabs_home)
    TabLayout idTlTabsHome;

    private MainAdapter mMainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        setTabs();
        List<BaseFragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new MyCourseFragment());
        fragmentList.add(new MineFragment());
        mMainAdapter = new MainAdapter(getSupportFragmentManager(), fragmentList);
        idVpContentHome.setAdapter(mMainAdapter);
        idVpContentHome.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(idTlTabsHome));
        idTlTabsHome.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(idVpContentHome));

    }

    /**
     * 设置添加Tab
     */
    private void setTabs() {
        TypedArray typedArray = mContext.getResources().obtainTypedArray(R.array.int_main_tab);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        String[] tabTitles = mContext.getResources().getStringArray(R.array.text_main_tab);
        for (int i = 0; i < tabTitles.length; i++) {
            TabLayout.Tab tab = idTlTabsHome.newTab();
            View view = inflater.inflate(R.layout.item_tab_main, null);
            view.measure(0, 0);
            TextView idTvMainTabItem = view.findViewById(R.id.id_tv_main_tab_item);
            idTvMainTabItem.setText(tabTitles[i]);
            ImageView idIvMainTabItem = view.findViewById(R.id.id_iv_main_tab_item);
            idIvMainTabItem.setImageResource(typedArray.getResourceId(i, 0));
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) idTlTabsHome.getLayoutParams();
            Log.d(TAG, "setTabs: view.getHeight()==" + view.getMeasuredHeight());
            layoutParams.height = view.getMeasuredHeight();
            idTlTabsHome.setLayoutParams(layoutParams);
            tab.setCustomView(view);
            idTlTabsHome.addTab(tab);
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }
}
