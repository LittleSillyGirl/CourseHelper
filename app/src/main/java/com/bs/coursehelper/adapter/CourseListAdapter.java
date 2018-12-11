package com.bs.coursehelper.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.coursehelper.R;
import com.bs.coursehelper.bean.CourseUserBean;
import com.bs.coursehelper.listener.IRVOnItemListener;
import com.bs.coursehelper.listener.IRVViewOnClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 */

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseListViewHolder> {
    private static final String TAG = "HomeClassfiyAdapter";

    private List<CourseUserBean> mDatas;
    private Context mContext;
    private IRVOnItemListener<CourseUserBean> mIRVOnItemListener;

    private IRVViewOnClickListener<CourseUserBean> mIRVViewOnClickListener;

    public void setIRVOnItemListener(IRVOnItemListener<CourseUserBean> iRVOnItemListener) {
        this.mIRVOnItemListener = iRVOnItemListener;
    }

    public void setIRVViewOnClickListener(IRVViewOnClickListener<CourseUserBean> iRVViewOnClickListener) {
        this.mIRVViewOnClickListener = iRVViewOnClickListener;
    }

    public CourseListAdapter(List<CourseUserBean> datas, Context context) {
        this.mDatas = datas;
        this.mContext = context;
    }

    @NonNull
    @Override
    public CourseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CourseListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recylcerview_course_list, parent, false));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(@NonNull CourseListViewHolder holder, int position) {
        CourseUserBean courseInfoBean = mDatas.get(position);
//        holder.idTvCourseName.setText(courseInfoBean.getCourseName());
//        holder.idClCourseList.setOnClickListener(view -> {
//            if (mIRVOnItemListener != null) {
//                mIRVOnItemListener.onItemClick(courseInfoBean, position);
//            }
//        });
//
//        holder.idIvCourseListDel.setOnClickListener(view -> {
//            mDatas.remove(courseInfoBean);
//            if (mIRVViewOnClickListener != null) {
//                mIRVViewOnClickListener.onClick(courseInfoBean, position);
//            }
//        });
    }


    public class CourseListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.id_tv_course_name)
        TextView idTvCourseName;
        @BindView(R.id.id_iv_course_list_del)
        ImageView idIvCourseListDel;
        @BindView(R.id.id_cl_course_list)
        ConstraintLayout idClCourseList;

        public CourseListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
