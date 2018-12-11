package com.bs.coursehelper.fragment;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bs.coursehelper.R;
import com.bs.coursehelper.activity.CourseListActivity;
import com.bs.coursehelper.adapter.AdminHomeListAdapter;
import com.bs.coursehelper.base.BaseFragment;
import com.bs.coursehelper.bean.MySubject;
import com.bs.coursehelper.db.DbHelper;
import com.bs.coursehelper.utils.ListUtils;
import com.vondear.rxtool.RxActivityTool;
import com.vondear.rxtool.RxBarTool;
import com.vondear.rxtool.RxTextTool;
import com.vondear.rxui.view.RxTitle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AdminCourseFragment extends BaseFragment {

    @BindView(R.id.id_rt_title)
    RxTitle idRtTitle;
    @BindView(R.id.id_rv_course_list)
    RecyclerView idRvCourseList;
    @BindView(R.id.id_tv_no_data)
    TextView idTvNoData;

    private DbHelper mDbHelper;
    private SweetAlertDialog mSweetAlertDialog;

    private AdminHomeListAdapter adminHomeListAdapter;
    private List<MySubject> mySubjectList;

    public static final int ADD_COURSE = 100;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_admin_course;
    }

    @Override
    protected void initView() {
        super.initView();
        idRtTitle.setPadding(0, RxBarTool.getStatusBarHeight(mContext), 0, 0);
        idRtTitle.setRightOnClickListener(view -> {
            //添加课程：选择时间、选择课程、选择教师、选择教室（也就是上课地方）、设置报名人数、几个课时、多少个学分
            RxActivityTool.skipActivity(mActivity, CourseListActivity.class);
        });

        mSweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        mSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mSweetAlertDialog.setTitleText("Loading");
        mSweetAlertDialog.setCancelable(false);
    }

    @Override
    protected void initData() {
        super.initData();
        mDbHelper = DbHelper.getInstance();
        mySubjectList = new ArrayList<>();
        adminHomeListAdapter = new AdminHomeListAdapter(mySubjectList, mContext);
        idRvCourseList.setLayoutManager(new LinearLayoutManager(mContext));
        adminHomeListAdapter.setIRVOnItemListener((mySubject, position) -> {
            showCourseDetail(mySubject);
        });
        idRvCourseList.setAdapter(adminHomeListAdapter);

        getCourseList();
    }

    /**
     * 获取课程的列表
     */
    private void getCourseList() {
        mSweetAlertDialog.show();
        Observable.just(mDbHelper.queryCourses())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subjectList -> {
                    //只要是课程的名称和授课教师名字通过拼接(加上courseId)，相同我们就认为是同一门课程
                    Map<String, List<MySubject>> mySubjectMap = ListUtils.groupBy(subjectList, subject -> subject.getName() + "@" + subject.getTeacher() + "@" + subject.getCourseId());
                    if (subjectList.size() == 0) {
                        idRvCourseList.setVisibility(View.GONE);
                        idTvNoData.setText("暂无课程信息，请点击右上角添加新课程！！！");
                        idTvNoData.setVisibility(View.VISIBLE);
                    } else {
                        if (mySubjectList.size() > 0) {
                            mySubjectList.clear();
                        }
                        //课程的信息 我们只要需要去一个就行了
                        for (Map.Entry<String, List<MySubject>> subjects : mySubjectMap.entrySet()) {
                            MySubject mySubject = subjects.getValue().get(0);
                            mySubjectList.add(mySubject);
                        }
                        adminHomeListAdapter.notifyDataSetChanged();
                        idTvNoData.setVisibility(View.GONE);
                        idRvCourseList.setVisibility(View.VISIBLE);
                    }
                    mSweetAlertDialog.dismissWithAnimation();
                });
    }


    /**
     * 显示课程的详情
     *
     * @param mySubject
     */
    private void showCourseDetail(MySubject mySubject) {
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_course_detail_home, null, false);
        TextView idTvCourseName = dialogView.findViewById(R.id.id_tv_course_desc);
        TextView idTvCourseTeacher = dialogView.findViewById(R.id.id_tv_course_teacher_desc);
        TextView idTvCourseRoom = dialogView.findViewById(R.id.id_tv_course_addr_desc);
        TextView idTvCourseStuNum = dialogView.findViewById(R.id.id_tv_course_stu_num_desc);
        TextView idTvCourseScore = dialogView.findViewById(R.id.id_tv_course_score_desc);
        int bgColor = mContext.getResources().getColor(R.color.tb_blue3);
        RxTextTool.getBuilder("课程名称：").append(mySubject.getName()).setForegroundColor(bgColor).into(idTvCourseName);
        RxTextTool.getBuilder("授课教师：").append(mySubject.getTeacher()).setForegroundColor(bgColor).into(idTvCourseTeacher);
        RxTextTool.getBuilder("授课地点：").append(mySubject.getRoom()).setForegroundColor(bgColor).into(idTvCourseRoom);
        RxTextTool.getBuilder("报名人数：").append(mySubject.getCourseStuApplications() + " (" + mySubject.getCourseStuNum() + ")").setForegroundColor(bgColor).into(idTvCourseStuNum);
        RxTextTool.getBuilder("课程学分：").append(String.valueOf(mySubject.getCourseScore())).setForegroundColor(bgColor).into(idTvCourseScore);

        new android.app.AlertDialog.Builder(mContext)
                .setView(dialogView)
                .create().show();
    }
}
