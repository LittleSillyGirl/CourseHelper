package com.bs.coursehelper.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.bs.coursehelper.R;
import com.bs.coursehelper.adapter.HomeListAdapter;
import com.bs.coursehelper.base.BaseFragment;
import com.bs.coursehelper.bean.HomeListBean;
import com.vondear.rxtool.RxBarTool;
import com.vondear.rxui.view.RxTitle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AdminCourseFragment extends BaseFragment {

    @BindView(R.id.id_rt_title)
    RxTitle idRtTitle;
    @BindView(R.id.id_rv_course_list)
    RecyclerView idRvCourseList;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_admin_course;
    }

    @Override
    protected void initView() {
        super.initView();
        idRtTitle.setPadding(0, RxBarTool.getStatusBarHeight(mContext), 0, 0);
    }

    @Override
    protected void initData() {
        super.initData();
        List<HomeListBean> homeListBeanList = new ArrayList<>();
        HomeListBean homeListBean = new HomeListBean();
        homeListBean.setTeacherHeadUrl("");
        homeListBean.setTeacherName("老师01");
        homeListBean.setStudentNum(8);
        homeListBean.setCourseName("大学英语");
        homeListBeanList.add(homeListBean);
        homeListBeanList.add(homeListBean);
        homeListBeanList.add(homeListBean);
        homeListBeanList.add(homeListBean);
        homeListBeanList.add(homeListBean);
        HomeListAdapter homeListAdapter = new HomeListAdapter(homeListBeanList, mContext);
        idRvCourseList.setLayoutManager(new LinearLayoutManager(mContext));
        homeListAdapter.setIRVOnItemListener((homeListBean1, position) -> Log.e(TAG, "onItemClick: position==" + position));
        idRvCourseList.setAdapter(homeListAdapter);
    }

}
