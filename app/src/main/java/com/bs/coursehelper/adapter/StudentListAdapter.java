package com.bs.coursehelper.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bs.coursehelper.R;
import com.bs.coursehelper.bean.HomeListBean;
import com.bs.coursehelper.listener.IRVOnItemListener;
import com.vondear.rxtool.RxTextTool;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 首页的头部分类
 */

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.HomeListViewHolder> {
    private static final String TAG = "StudentListAdapter";

    private List<HomeListBean> mDatas;
    private Context mContext;
    private IRVOnItemListener<HomeListBean> mIRVOnItemListener;

    public void setIRVOnItemListener(IRVOnItemListener<HomeListBean> iRVOnItemListener) {
        this.mIRVOnItemListener = iRVOnItemListener;
    }

    public StudentListAdapter(List<HomeListBean> datas, Context context) {
        this.mDatas = datas;
        this.mContext = context;
    }

    @NonNull
    @Override
    public HomeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recylcerview_stud_list, parent, false));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(@NonNull HomeListViewHolder holder, int position) {
        HomeListBean homeListBean = mDatas.get(position);
        String uriStr = homeListBean.getTeacherHeadUrl();
        if (!TextUtils.isEmpty(uriStr)) {
            holder.idCivStuHead.setImageURI(Uri.parse(uriStr));
        }
        holder.idTvStuName.setText(homeListBean.getTeacherName());
        RxTextTool.getBuilder("已修课程门数：")
                .append(String.valueOf(homeListBean.getStudentNum()))
                .setForegroundColor(mContext.getResources().getColor(R.color.tb_blue1))
                .setProportion(1.8f)
                .into(holder.idTvStuCourseNum);

        RxTextTool.getBuilder("已修学分：")
                .append(String.valueOf(homeListBean.getStudentNum()))
                .setForegroundColor(mContext.getResources().getColor(R.color.tb_blue1))
                .setProportion(1.8f)
                .into(holder.idTvStuScore);
        holder.idClStuList.setOnClickListener(view -> {
            if (mIRVOnItemListener != null) {
                mIRVOnItemListener.onItemClick(homeListBean, position);
            }
        });
    }

    public class HomeListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.id_civ_stu_head)
        CircleImageView idCivStuHead;
        @BindView(R.id.id_tv_stu_name)
        TextView idTvStuName;
        @BindView(R.id.id_tv_stu_course_num)
        TextView idTvStuCourseNum;
        @BindView(R.id.id_tv_stu_score)
        TextView idTvStuScore;
        @BindView(R.id.id_cl_stu_list)
        ConstraintLayout idClStuList;

        public HomeListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
