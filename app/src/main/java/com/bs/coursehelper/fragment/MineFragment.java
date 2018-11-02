package com.bs.coursehelper.fragment;

import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.coursehelper.R;
import com.bs.coursehelper.activity.LoginActivity;
import com.bs.coursehelper.adapter.MineDescAdapter;
import com.bs.coursehelper.base.BaseFragment;
import com.bs.coursehelper.bean.HomeClassfiyBean;
import com.bs.coursehelper.utils.SPUtil;
import com.jaeger.library.StatusBarUtil;
import com.vondear.rxtool.RxActivityTool;
import com.vondear.rxtool.view.RxToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 我的
 */

public class MineFragment extends BaseFragment {


    @BindView(R.id.id_cir_mine)
    CircleImageView idCirMine;
    @BindView(R.id.id_tv_user_name)
    TextView idTvUserName;
    @BindView(R.id.id_tv_user_phone)
    TextView idTvUserPhone;
    @BindView(R.id.id_iv_user_sex)
    ImageView idIvUserSex;
    @BindView(R.id.id_iv_mine_divider)
    ImageView idIvMineDivider;
    @BindView(R.id.id_rv_mine)
    RecyclerView idRvMine;
    @BindView(R.id.id_cl_mine)
    ConstraintLayout idClMine;
    @BindView(R.id.id_tv_login_out)
    TextView idTvLoginOut;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtil.setTransparentForImageViewInFragment(mActivity, idCirMine);
    }

    @Override
    protected void initData() {
        super.initData();
        idRvMine.setLayoutManager(new LinearLayoutManager(mContext));
        String[] descArr = mContext.getResources().getStringArray(R.array.text_mine_desc);
        TypedArray typedArray = mContext.getResources().obtainTypedArray(R.array.int_mine_desc);
        List<HomeClassfiyBean> descList = new ArrayList<>();
        for (int i = 0; i < descArr.length; i++) {
            HomeClassfiyBean homeClassfiyBean = new HomeClassfiyBean();
            homeClassfiyBean.setImgId(typedArray.getResourceId(i, 0));
            homeClassfiyBean.setClassfiyName(descArr[i]);
            descList.add(homeClassfiyBean);
        }
        MineDescAdapter mineDescAdapter = new MineDescAdapter(descList, mContext);
        mineDescAdapter.setIRVOnItemListener((s, position) -> {
            switch (s.getClassfiyName()) {
                case "个人信息":
                    break;
                case "健康档案":
                    break;
                case "我的处方":
                    break;
                case "我的预约":
                    break;
                case "设置":
                    break;
            }
            RxToast.normal(s.getClassfiyName());
        });
        idRvMine.setAdapter(mineDescAdapter);
    }

    @OnClick(R.id.id_tv_login_out)
    public void onClick() {
        SPUtil.getInstanse().clear();
        mActivity.finish();
        RxActivityTool.skipActivity(mContext, LoginActivity.class);
    }
}
