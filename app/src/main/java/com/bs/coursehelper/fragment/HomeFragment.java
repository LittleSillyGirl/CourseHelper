package com.bs.coursehelper.fragment;

import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bs.coursehelper.R;
import com.bs.coursehelper.adapter.HomeClassfiyAdapter;
import com.bs.coursehelper.adapter.HomeListAdapter;
import com.bs.coursehelper.base.BaseFragment;
import com.bs.coursehelper.bean.HomeClassfiyBean;
import com.bs.coursehelper.bean.HomeListBean;
import com.vondear.rxui.view.RxTextViewVerticalMore;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 首页
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.id_iv_top_home)
    ImageView idIvTopHome;
    @BindView(R.id.id_tl_home)
    Toolbar idTlHome;
    @BindView(R.id.id_ctl_home)
    CollapsingToolbarLayout idCtlHome;
    @BindView(R.id.id_rv_home)
    RecyclerView idRvHome;
    @BindView(R.id.id_rvvm_home)
    RxTextViewVerticalMore idRvvmHome;
//    @BindView(R.id.id_iv_top)
//    ImageView idIvTop;
//    @BindView(R.id.id_rrtv_notify)
//    RxRunTextView idRrtvNotify;
//    @BindView(R.id.id_iv_notify)
//    ImageView idIvNotify;
//    @BindView(R.id.id_iv_line)
//    ImageView idIvLine;
//    @BindView(R.id.id_tv_list_desc)
//    TextView idTvListDesc;
    @BindView(R.id.id_rv_home_list)
    RecyclerView idRvHomeList;

    private AnimationDrawable mAnimation;

    @Override
    protected void initView() {
        super.initView();
//        idIvNotify.setImageResource(R.drawable.anim_notify);
//        mAnimation = (AnimationDrawable) idIvNotify.getDrawable();
//        mAnimation.start();
        idCtlHome.setTitle("学校名字");
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData() {
        super.initData();
        String[] descArr = mContext.getResources().getStringArray(R.array.text_home_patient_classfiy);
        TypedArray ta = mContext.getResources().obtainTypedArray(R.array.int_home_patient_classfiy);
        List<HomeClassfiyBean> homeClassfiyBeanList = new ArrayList<>();
        for (int i = 0; i < descArr.length; i++) {
            HomeClassfiyBean homeClassfiyBean = new HomeClassfiyBean();
            homeClassfiyBean.setImgId(ta.getResourceId(i, 0));
            homeClassfiyBean.setClassfiyName(descArr[i]);
            homeClassfiyBean.setColorId(mContext.getResources().getColor(R.color.gray_8f));
            homeClassfiyBeanList.add(homeClassfiyBean);
        }
        HomeClassfiyAdapter homeClassfiyAdapter = new HomeClassfiyAdapter(homeClassfiyBeanList, mContext);
        homeClassfiyAdapter.setIRVOnItemListener((homeClassfiyBean, position) -> Log.e(TAG, "onItemClick: position==" + position));
        idRvHome.setLayoutManager(new GridLayoutManager(mContext, 3));
        idRvHome.setAdapter(homeClassfiyAdapter);

        List<View> viewList = new ArrayList<>();
        setUPMarqueeView(viewList, 3);
        idRvvmHome.setViews(viewList);


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
        idRvHomeList.setLayoutManager(new LinearLayoutManager(mContext));
        homeListAdapter.setIRVOnItemListener((homeListBean1, position) -> Log.e(TAG, "onItemClick: position==" + position));
        idRvHomeList.setAdapter(homeListAdapter);
    }

    private void setUPMarqueeView(List<View> views, int size) {
        for (int i = 0; i < size; i++) {
            //设置滚动的单个布局
            LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_view, null);
            RelativeLayout rl1 = moreView.findViewById(R.id.rl);
            RelativeLayout rl2 = moreView.findViewById(R.id.rl2);
            //初始化布局的控件
            TextView titleTv1 = moreView.findViewById(R.id.title_tv1);
            TextView tv1 = moreView.findViewById(R.id.tv1);
            titleTv1.setText("通知");
            tv1.setText("暂无新通知");

            /**
             * 设置监听
             */
            moreView.findViewById(R.id.rl).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            //初始化布局的控件
            TextView titleTv2 = moreView.findViewById(R.id.title_tv2);
            TextView tv2 = moreView.findViewById(R.id.tv2);
            titleTv2.setText("提醒");
            tv2.setText(i + "==下午两点你有一门大学英语课程，地点在教师");

            /**
             * 设置监听
             */
            moreView.findViewById(R.id.rl2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            //添加到循环滚动数组里面去
            views.add(moreView);
        }
    }

}