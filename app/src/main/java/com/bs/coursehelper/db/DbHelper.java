package com.bs.coursehelper.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bs.coursehelper.bean.CourseUserBean;
import com.bs.coursehelper.bean.MySubject;
import com.bs.coursehelper.bean.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtool.RxDataTool;

import java.util.ArrayList;
import java.util.List;

public class DbHelper {

    private static final String TAG = "SQLiteDatabaseDao";

    /**
     * db的名字
     */
    private static final String DATABASE_NAME = "yh_db";

    /**
     * 数据库的版本
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * 表的名字
     */
    private static final String DATABASE_USER_TABLE = "tb_user"; //用户表
    private static final String DATABASE_COURSE_TABLE = "tb_course";//课程表
    private static final String DATABASE_COURSE_USER_TABLE = "tb_course_user";//用户课程中间表

    /**
     * 表的行名称 用户的id、名称、密保、
     */
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "userName";
    public static final String KEY_PWD = "userPwd";
    public static final String KEY_NUMBER = "userNumber";
    public static final String KEY_HEAD_URL = "userHeadUrl";
    public static final String KEY_SEX = "userSex";
    public static final String KEY_USER_TYPE = "userType";
    public static final String KEY_USER_COURSE_SCORE = "userCourseScore";

    /**
     * 课程的名称(这里的课程  指的是教师上传的课程名称)
     */
    public static final String KEY_COURSE_ID = "id";
    public static final String KEY_COURSE_NAME = "courseName";
    public static final String KEY_COURSE_TEACHER_NAME = "courseTeacherName";
    public static final String KEY_COURSE_ADDR = "courseAddr";
    public static final String KEY_COURSE_WEEKLIST = "courseWeekList";
    public static final String KEY_COURSE_START = "courseStart";
    public static final String KEY_COURSE_STEP = "courseStep";
    public static final String KEY_COURSE_DAY = "courseDay";
    public static final String KEY_COURSE_TERM = "courseTerm";
    public static final String KEY_COURSE_COLORRANDOM = "courseColorRandom";
    public static final String KEY_COURSE_STU_NUM = "courseStuNum";
    public static final String KEY_COURSE_NUM = "courseNum";
    public static final String KEY_COURSE_SCORE = "courseColorScore";
    public static final String KEY_COURSE_COURSE_ID = "courseId";

    public static final String KEY_COURSE_USER_ID = "id";
    public static final String KEY_COURSE_USER_COURSE_ID = "courseId";
    public static final String KEY_COURSE_USER_USER_ID = "userId";




    /**
     * 创建表数据
     */
    private static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + DATABASE_USER_TABLE + " (" + KEY_ID + " integer primary key autoincrement, " +
            KEY_NAME + " text not null, " + KEY_PWD + " text not null, " + KEY_NUMBER + " text not null, " + KEY_SEX + " integer, " + KEY_HEAD_URL + " text, " +
            KEY_USER_TYPE + " integer, " + KEY_USER_COURSE_SCORE + " float);";

    private static final String CREATE_COURSE_TABLE = "CREATE TABLE IF NOT EXISTS " + DATABASE_COURSE_TABLE + " (" + KEY_COURSE_ID + " integer primary key autoincrement, " +
            KEY_COURSE_NAME + " text not null, " + KEY_COURSE_TEACHER_NAME + " text not null, " + KEY_COURSE_ADDR + " text not null, " + KEY_COURSE_WEEKLIST + " text not null, "+
            KEY_COURSE_START + " integer, " + KEY_COURSE_STEP + " integer, " + KEY_COURSE_DAY + " integer, " + KEY_COURSE_TERM + " text, " + KEY_COURSE_COLORRANDOM + " integer, " +
            KEY_COURSE_STU_NUM + " integer, " + KEY_COURSE_NUM + " integer, " + KEY_COURSE_SCORE + " float, " + KEY_COURSE_COURSE_ID + " integer);";

    private static final String CREATE_COURSE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + DATABASE_COURSE_USER_TABLE + " (" + KEY_COURSE_USER_ID + " integer primary key autoincrement, " +
            KEY_COURSE_USER_COURSE_ID + " integer, " + KEY_COURSE_USER_USER_ID + " integer);";

    private static List<String> tables;

    /**
     * 上下文
     */
    private Context mCtx;

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * 数据库的辅助类
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * 创建表
         *
         * @param db
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            for (String tableSql : tables) {
                Log.i(TAG, "Creating DataBase: " + tableSql);
                db.execSQL(tableSql);
            }
        }

        /**
         * 更新版本
         *
         * @param db
         * @param oldVersion
         * @param newVersion
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        }
    }

    private static class Singleton {
        private static final DbHelper INSTANCE = new DbHelper();
    }


    /**
     * 获取对象的单列
     *
     * @return
     */
    public static DbHelper getInstance() {
        return DbHelper.Singleton.INSTANCE;
    }

    /**
     * 初始化context
     *
     * @param ctx
     */
    public DbHelper init(Context ctx) {
        this.mCtx = ctx;
        tables = new ArrayList<>();
        tables.add(CREATE_USER_TABLE);
        tables.add(CREATE_COURSE_TABLE);
        tables.add(CREATE_COURSE_USER_TABLE);
        openDb();
        return this;
    }

    /**
     * 打开数据库
     *
     * @return
     * @throws SQLException
     */
    public DbHelper openDb() throws SQLException {
        if (mCtx != null) {
            mDbHelper = new DatabaseHelper(mCtx);
            mDb = mDbHelper.getReadableDatabase();
        }
        return this;
    }

    /**
     * 关闭数据库
     */
    public void closeDb() {
        mDbHelper.close();
    }


    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    public long insertUser(User user) {
        long result = -1;
        mDb.beginTransaction();
        try {
            ContentValues userValue = new ContentValues();
            userValue.put(KEY_NAME, user.getUserName());
            userValue.put(KEY_PWD, user.getUserPwd());
            userValue.put(KEY_NUMBER, user.getUserNumber());
            userValue.put(KEY_SEX, user.getUserSex());
            userValue.put(KEY_USER_TYPE, user.getUserType());
            result = mDb.insert(DATABASE_USER_TABLE, null, userValue);
            Log.e(TAG, "insertUser: result==" + result);
            mDb.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            mDb.endTransaction();
        }
        return result;
    }

    /**
     * 查找用户
     *
     * @param userName
     * @param userPwd
     * @return
     */
    public User queryUser(String userName, String userPwd) {
        User user = null;
        Cursor cursor = null;
        if (cursor == null) {
            String sql = "select * from " + DATABASE_USER_TABLE + " where (" + KEY_NAME + "= ? or " + KEY_NUMBER + "= ?) and " + KEY_PWD + " = ?";
            cursor = mDb.rawQuery(sql, new String[]{userName, userName, userPwd});
//            cursor = mDb.query(DATABASE_USER_TABLE, new String[]{KEY_ID, KEY_NAME, KEY_PWD, KEY_PWD_HELP},
//                    KEY_NAME + "= ? and " + KEY_PWD + " = ?", new String[]{userName, userPwd},
//                    null, null, null);
        }
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                user = new User();
                setUser(user, cursor);
            }
            cursor.close();
        }
        return user;
    }

    /**
     * 设置用户的值
     *
     * @param user
     * @param cursor
     */
    private void setUser(User user, Cursor cursor) {
        int userId = cursor.getInt(cursor.getColumnIndex(KEY_ID));
        user.setUserId(userId);
        user.setUserName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
        user.setUserPwd(cursor.getString(cursor.getColumnIndex(KEY_PWD)));
        user.setUserNumber(cursor.getString(cursor.getColumnIndex(KEY_NUMBER)));
        Log.i(TAG, "setUser: cursor.getColumnIndex(KEY_HEAD_URL)===" + cursor.getColumnIndex(KEY_HEAD_URL));
        user.setUserHeadUrl(cursor.getString(cursor.getColumnIndex(KEY_HEAD_URL)));
        user.setUserSex(cursor.getInt(cursor.getColumnIndex(KEY_SEX)));
        user.setUserType(cursor.getInt(cursor.getColumnIndex(KEY_USER_TYPE)));
        user.setUserCourses(queryCourseUsers(userId));
        user.setUserCourseScore(querySelectedScore(userId));
    }

    /**
     * 查找用户
     *
     * @param userName
     * @return
     */
    public User queryUserIsExist(String userName, String userNumber) {
        User user = null;
        Cursor cursor = null;
        if (cursor == null) {
            String sql = "select * from " + DATABASE_USER_TABLE + " where " + KEY_NAME + "= ? or " + KEY_NUMBER + "= ?";
            cursor = mDb.rawQuery(sql, new String[]{userName, userNumber});
        }
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                user = new User();
                setUser(user, cursor);
            }
            cursor.close();
        }
        return user;
    }


    /**
     * 修改用户信息 修改密码
     *
     * @param user
     * @return
     */
    public long updateUserPwd(User user) {

        long result = -1;
        mDb.beginTransaction();
        ContentValues userValue = new ContentValues();
        userValue.put(KEY_PWD, user.getUserPwd());
        result = mDb.update(DATABASE_USER_TABLE, userValue, KEY_NUMBER + "=?",
                new String[]{user.getUserNumber()});
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
        return result;
    }

    /**
     * 修改用户信息 修改密码
     *
     * @param userId
     * @return
     */
    public long deleteUser(int userId) {
        long result = -1;
        mDb.beginTransaction();
        result = mDb.delete(DATABASE_USER_TABLE, KEY_ID + "=?", new String[]{String.valueOf(userId)});
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
        return result;
    }

    /**
     * 修改用户信息 修改用户信息
     *
     * @param user
     * @return
     */
    public long updateUserInfo(User user) {

        long result = -1;
        Log.e(TAG, "updateUser: use==" + user.toString());
        ContentValues userValue = new ContentValues();
        userValue.put(KEY_NAME, user.getUserName());
        userValue.put(KEY_PWD, user.getUserPwd());
//        result = mDb.update(DATABASE_USER_TABLE, userValue, KEY_NAME+" =? and " + KEY_PWD_HELP + "=? and " + KEY_ID + "=?" ,
//                new String[]{user.getUserName(), user.getPwdHelp(), String.valueOf(user.getUserID())});
        Log.e(TAG, "updateUser: result==" + result);
        return result;
    }

    /**
     * 获取所有注册学生信息
     *
     * @return
     */
    public List<User> queryUsers(int userType) {
        List<User> userList = new ArrayList<>();
        Cursor cursor = mDb.query(DATABASE_USER_TABLE, null, KEY_USER_TYPE + "=?", new String[]{String.valueOf(userType)}, null, null, null, null);
        while (cursor.moveToNext()) {
            User user = new User();
            setUser(user, cursor);
            userList.add(user);
        }
        cursor.close();
        return userList;
    }


    /**
     * 新增课程
     *
     * @param mySubject
     * @return
     */
    public long insertCourse(MySubject mySubject) {
        long result = -1;
        mDb.beginTransaction();
        try {
            ContentValues courseValue = new ContentValues();
            courseValue.put(KEY_COURSE_NAME, mySubject.getName());
            courseValue.put(KEY_COURSE_TEACHER_NAME, mySubject.getTeacher());
            courseValue.put(KEY_COURSE_ADDR, mySubject.getRoom());
            courseValue.put(KEY_COURSE_STU_NUM, mySubject.getCourseStuNum());
//            courseValue.put(KEY_COURSE_STU_APPLICATIONS, mySubject.getCourseStuNum());//默认的都是0
            courseValue.put(KEY_COURSE_WEEKLIST, new Gson().toJson(mySubject.getWeekList()));
            courseValue.put(KEY_COURSE_START, mySubject.getStart());
            courseValue.put(KEY_COURSE_STEP, mySubject.getStep());
            courseValue.put(KEY_COURSE_DAY, mySubject.getDay());
//            courseValue.put(KEY_COURSE_COLORRANDOM, mySubject.getColorRandom());
            courseValue.put(KEY_COURSE_SCORE, mySubject.getCourseScore());
            courseValue.put(KEY_COURSE_COURSE_ID, mySubject.getCourseId());
            result = mDb.insert(DATABASE_COURSE_TABLE, null, courseValue);
            mDb.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            mDb.endTransaction();
        }
        return result;
    }

    /**
     * 新增课程用户 也就是二者的关系表
     *
     * @return
     */
    public long insertCourseUser(int courseId, int userId) {
        long result = -1;
        mDb.beginTransaction();
        try {
            ContentValues courseValue = new ContentValues();
            courseValue.put(KEY_COURSE_USER_COURSE_ID, courseId);
            courseValue.put(KEY_COURSE_USER_USER_ID, userId);
            result = mDb.insert(DATABASE_COURSE_USER_TABLE, null, courseValue);
            mDb.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            mDb.endTransaction();
        }
        return result;
    }

    /**
     * 查询对应课程的申请人数
     *
     * @return
     */
    public int queryCourseApplications(int courseId) {
        Cursor cursor = mDb.query(DATABASE_COURSE_USER_TABLE, null, KEY_COURSE_USER_COURSE_ID + "=?", new String[]{String.valueOf(courseId)}, null, null, null, null);
        int applications = cursor.getCount();
        cursor.close();
        return applications;
    }


    /**
     * 查询用户对应的课程
     *
     * @return
     */
    public long queryCourseApplys(int userId, int courseId) {
        long result = -1;
        mDb.beginTransaction();
        try {
            Cursor cursor = mDb.query(DATABASE_COURSE_USER_TABLE, null, KEY_COURSE_USER_USER_ID + "=? and " + KEY_COURSE_USER_COURSE_ID,
                    new String[]{String.valueOf(userId), String.valueOf(courseId)},null, null, null, null);
            mDb.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            mDb.endTransaction();
        }
        return result;
    }

    /**
     * 查询用户对应的课程是否报名过
     *
     * @return
     */
    public int queryCourseIsApply(int userId, int courseId) {
        Cursor cursor = mDb.query(DATABASE_COURSE_USER_TABLE, null, KEY_COURSE_USER_USER_ID + "=? and " + KEY_COURSE_USER_COURSE_ID + "=?",
                new String[]{String.valueOf(userId), String.valueOf(courseId)},null, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    /**
     * 直接获取所有的课程
     *
     * @return
     */
    public List<CourseUserBean> queryCourseUsers(int userId) {
        List<CourseUserBean> courseList = new ArrayList<>();
        Cursor cursor = mDb.query(DATABASE_COURSE_USER_TABLE, null, KEY_COURSE_USER_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null, null);
        while (cursor.moveToNext()) {
            int courseId = cursor.getInt(cursor.getColumnIndex(KEY_COURSE_USER_COURSE_ID));
            MySubject mySubject = queryMySubject(courseId);
            CourseUserBean  courseUserBean = new CourseUserBean();
            courseUserBean.setId(courseId);
            courseUserBean.setName(mySubject.getName());
            courseUserBean.setTeacher(mySubject.getTeacher());
            courseList.add(courseUserBean);
        }
        cursor.close();
        return courseList;
    }


    /**
     * 新增课程用户 也就是二者的关系表
     *
     * @return
     */
    public long deleteCourseUser(int userId) {
        long result = -1;
        mDb.beginTransaction();
        try {
            result = mDb.delete(DATABASE_COURSE_USER_TABLE, KEY_COURSE_USER_USER_ID + "=?", new String[]{String.valueOf(userId)});
            mDb.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            mDb.endTransaction();
        }
        return result;
    }

    /**
     * 新增课程用户 也就是二者的关系表
     *
     * @return
     */
    public long deleteCourseUser(int userId, int courseId) {
        long result = -1;
        mDb.beginTransaction();
        try {
            result = mDb.delete(DATABASE_COURSE_USER_TABLE, KEY_COURSE_USER_USER_ID + "=? and " + KEY_COURSE_USER_COURSE_ID + "=?",
                    new String[]{String.valueOf(userId), String.valueOf(courseId)});
            mDb.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            mDb.endTransaction();
        }
        return result;
    }



    /**
     * 通过课程id删除课程,这里的删除指的的是，我们把周 更改了，如果weeklist 为空我们就删除课程
     *
     * @param courseId
     * @param deleteWeek 删除课程对应的周 值
     * @return
     */
    public long deleteCourseById(int courseId, int deleteWeek) {
        long result = -1;
        mDb.beginTransaction();
        try {
            MySubject mySubject = queryMySubject(courseId);
            List<Integer> weekList = mySubject.getWeekList();
            if (null == weekList || weekList.size()==0){
                result = mDb.delete(DATABASE_COURSE_TABLE, KEY_COURSE_ID + "=?", new String[]{String.valueOf(courseId)});
            }else if (weekList.size()>0){
                ContentValues courseValue = new ContentValues();
                courseValue.put(KEY_COURSE_ID, mySubject.getId());
                courseValue.put(KEY_COURSE_NAME, mySubject.getName());
                courseValue.put(KEY_COURSE_TEACHER_NAME, mySubject.getTeacher());
                courseValue.put(KEY_COURSE_ADDR, mySubject.getRoom());
                courseValue.put(KEY_COURSE_WEEKLIST, new Gson().toJson(mySubject.getWeekList()));
                courseValue.put(KEY_COURSE_START, mySubject.getStart());
                courseValue.put(KEY_COURSE_STEP, mySubject.getStep());
                courseValue.put(KEY_COURSE_DAY, mySubject.getDay());
                courseValue.put(KEY_COURSE_TERM, mySubject.getTerm());
//                courseValue.put(KEY_COURSE_COLORRANDOM, mySubject.getColorRandom());
                courseValue.put(KEY_COURSE_STU_NUM, mySubject.getCourseStuNum());
//                courseValue.put(KEY_COURSE_STU_APPLICATIONS, mySubject.getCourseStuNum());
                courseValue.put(KEY_COURSE_NUM, mySubject.getCourseNum());
                courseValue.put(KEY_COURSE_SCORE, mySubject.getCourseScore());
                result = mDb.update(DATABASE_COURSE_TABLE, courseValue, KEY_COURSE_ID + "=?", new String[]{String.valueOf(courseId)});
            }
            mDb.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            mDb.endTransaction();
        }
        return result;
    }

    /**
     * 查找课程
     *
     * @param courseId
     * @return
     */
    public MySubject queryMySubject(int courseId) {
        MySubject mySubject = null;
        Cursor cursor = null;
        if (cursor == null) {
            String sql = "select * from " + DATABASE_COURSE_TABLE + " where " + KEY_COURSE_COURSE_ID + "= ?";
            cursor = mDb.rawQuery(sql, new String[]{String.valueOf(courseId)});
        }
        if (cursor != null) {
            if (cursor.moveToNext()) {
                mySubject = new MySubject();
                setMySubject(mySubject, cursor);
            }
            cursor.close();
        }
        return mySubject;
    }

    /**
     * 查找课程, 这里简单起见我们只要认为课程名字、授课教师，我们就认为是同一门课程
     *
     * @param name
     * @param teacher
     * @return
     */
    public MySubject queryMySubject(String name, String teacher) {
        MySubject mySubject = null;
        Cursor cursor = null;
        if (cursor == null) {
            String sql = "select * from " + DATABASE_COURSE_TABLE + " where " + KEY_COURSE_NAME + "=? and " + KEY_COURSE_TEACHER_NAME + "=?";
            cursor = mDb.rawQuery(sql, new String[]{name, teacher});
        }
        if (cursor != null) {
            if (cursor.moveToNext()) {
                mySubject = new MySubject();
                setMySubject(mySubject, cursor);
            }
            cursor.close();
        }
        return mySubject;
    }

    /**
     * 给mySubject 赋值
     *
     * @param mySubject
     * @param cursor
     */
    private void setMySubject(MySubject mySubject, Cursor cursor) {
        mySubject.setId(cursor.getInt(cursor.getColumnIndex(KEY_COURSE_ID)));
        mySubject.setName(cursor.getString(cursor.getColumnIndex(KEY_COURSE_NAME)));
        mySubject.setTeacher(cursor.getString(cursor.getColumnIndex(KEY_COURSE_TEACHER_NAME)));
        mySubject.setRoom(cursor.getString(cursor.getColumnIndex(KEY_COURSE_ADDR)));
        String weekListStr = cursor.getString(cursor.getColumnIndex(KEY_COURSE_WEEKLIST));
        List<Integer> weekList = new ArrayList<>();
        if (!RxDataTool.isEmpty(weekListStr)){
            weekList.addAll(new Gson().fromJson(weekListStr, new TypeToken<List<Integer>>(){}.getType()));
        }
        mySubject.setWeekList(weekList);
        mySubject.setTerm(cursor.getString(cursor.getColumnIndex(KEY_COURSE_TERM)));
        mySubject.setStart(cursor.getInt(cursor.getColumnIndex(KEY_COURSE_START)));
        mySubject.setStep(cursor.getInt(cursor.getColumnIndex(KEY_COURSE_STEP)));
        mySubject.setDay(cursor.getInt(cursor.getColumnIndex(KEY_COURSE_DAY)));
//                mySubject.setColorRandom(cursor.getInt(cursor.getColumnIndex(KEY_COURSE_COLORRANDOM)));
        mySubject.setCourseStuNum(cursor.getInt(cursor.getColumnIndex(KEY_COURSE_STU_NUM)));
        int courseId = cursor.getInt(cursor.getColumnIndex(KEY_COURSE_COURSE_ID));
        mySubject.setCourseId(courseId);
        // 查询已报名人数
        mySubject.setCourseStuApplications(queryCourseApplications(courseId));
        mySubject.setCourseNum(cursor.getInt(cursor.getColumnIndex(KEY_COURSE_NUM)));
        mySubject.setCourseScore(cursor.getFloat(cursor.getColumnIndex(KEY_COURSE_SCORE)));
    }

    /**
     * 更新课程的信息
     *
     * @param mySubject
     * @return
     */
    public long updateCourse(MySubject mySubject) {
        long result = -1;
        mDb.beginTransaction();
        try {
            ContentValues courseValue = new ContentValues();
            courseValue.put(KEY_COURSE_ID, mySubject.getId());
            courseValue.put(KEY_COURSE_NAME, mySubject.getName());
            courseValue.put(KEY_COURSE_TEACHER_NAME, mySubject.getTeacher());
            courseValue.put(KEY_COURSE_ADDR, mySubject.getRoom());
            courseValue.put(KEY_COURSE_WEEKLIST, new Gson().toJson(mySubject.getWeekList()));
            courseValue.put(KEY_COURSE_START, mySubject.getStart());
            courseValue.put(KEY_COURSE_STEP, mySubject.getStep());
            courseValue.put(KEY_COURSE_DAY, mySubject.getDay());
            courseValue.put(KEY_COURSE_TERM, mySubject.getTerm());
//                courseValue.put(KEY_COURSE_COLORRANDOM, mySubject.getColorRandom());
            courseValue.put(KEY_COURSE_STU_NUM, mySubject.getCourseStuNum());
            courseValue.put(KEY_COURSE_NUM, mySubject.getCourseNum());
            courseValue.put(KEY_COURSE_SCORE, mySubject.getCourseScore());
            result = mDb.update(DATABASE_COURSE_TABLE, courseValue, KEY_COURSE_ID + "=?", new String[]{String.valueOf(mySubject.getId())});
            mDb.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            mDb.endTransaction();
        }
        return result;
    }

    /**
     * 直接获取所有的课程
     *
     * @return
     */
    public List<MySubject> queryCourses() {
        List<MySubject> mySubjectList = new ArrayList<>();
        Cursor cursor = mDb.query(DATABASE_COURSE_TABLE, null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            MySubject mySubject = new MySubject();
            setMySubject(mySubject, cursor);
            mySubjectList.add(mySubject);
        }
        cursor.close();
        return mySubjectList;
    }

    /**
     * 查询对应课程id下面的所有课程
     *
     * @return
     */
    public List<MySubject> queryCourses(int courseId) {
        List<MySubject> mySubjectList = new ArrayList<>();
        Cursor cursor = mDb.query(DATABASE_COURSE_TABLE, null, KEY_COURSE_COURSE_ID + "=?",
                new String[]{String.valueOf(courseId)}, null, null, null, null);
        while (cursor.moveToNext()) {
            MySubject mySubject = new MySubject();
            setMySubject(mySubject, cursor);
            mySubjectList.add(mySubject);
        }
        cursor.close();
        return mySubjectList;
    }

    /**
     * 获取用户下课程表对应的课程
     *
     * @return
     */
    public List<MySubject> queryUserCourses(int userId) {
        List<MySubject> mySubjectList = new ArrayList<>();
        Cursor cursorUser = mDb.query(DATABASE_COURSE_USER_TABLE, null, KEY_COURSE_USER_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null, null);
        while (cursorUser.moveToNext()) {
            int courseId = cursorUser.getInt(cursorUser.getColumnIndex(KEY_COURSE_USER_COURSE_ID));
            //因为我们是为了显示课程表对应的所有需要查询多个
            List<MySubject> mySubjectListTmp = queryCourses(courseId);
            mySubjectList.addAll(mySubjectListTmp);
        }
        cursorUser.close();
        return mySubjectList;
    }

    /**
     * 获取用户对应的课程
     *
     * @return
     */
    public List<MySubject> querySelectedCourses(int userId) {
        List<MySubject> mySubjectList = new ArrayList<>();
        Cursor cursorUser = mDb.query(DATABASE_COURSE_USER_TABLE, null, KEY_COURSE_USER_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null, null);
        while (cursorUser.moveToNext()) {
            int courseId = cursorUser.getInt(cursorUser.getColumnIndex(KEY_COURSE_USER_COURSE_ID));
            //我们是查询已选课程，所以只需要一个就行了
            MySubject mySubject = queryMySubject(courseId);
            mySubjectList.add(mySubject);
        }
        cursorUser.close();
        return mySubjectList;
    }

    /**
     * 获取用户已选的学分
     *
     * @return
     */
    public float querySelectedScore(int userId) {
        float score = 0f;
        Cursor cursorUser = mDb.query(DATABASE_COURSE_USER_TABLE, null, KEY_COURSE_USER_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null, null);
        while (cursorUser.moveToNext()) {
            int courseId = cursorUser.getInt(cursorUser.getColumnIndex(KEY_COURSE_USER_COURSE_ID));
            //我们是查询已选课程，所以只需要一个就行了
            MySubject mySubject = queryMySubject(courseId);
            score+=mySubject.getCourseScore();
        }
        cursorUser.close();
        return score;
    }

}
