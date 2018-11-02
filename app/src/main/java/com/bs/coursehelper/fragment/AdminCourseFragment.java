package com.bs.coursehelper.fragment;

import com.bs.coursehelper.R;
import com.bs.coursehelper.base.BaseFragment;
import com.vondear.rxui.view.RxTitle;

import butterknife.BindView;

public class AdminCourseFragment extends BaseFragment {

    @BindView(R.id.id_rt_title)
    RxTitle idRtTitle;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_admin_course;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
        super.initData();
    }
}
