package com.bs.coursehelper.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bs.coursehelper.bean.User;

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
    private static final String DATABASE_USER_TABLE = "tb_user";

    /**
     * 表的行名称 用户的id、名称、密保、
     */
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "userName";
    public static final String KEY_PWD = "userPwd";
    public static final String KEY_NUMBER = "userNumber";
    public static final String KEY_SEX = "userSex";
    public static final String KEY_USER_TYPE = "userType";

    /**
     * 创建表数据
     */
    private static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + DATABASE_USER_TABLE + " ("+ KEY_ID +" integer primary key autoincrement, " +
            KEY_NAME + " text not null, " + KEY_PWD + " text not null, " + KEY_NUMBER + " text not null, " + KEY_SEX + " integer, " +
            KEY_USER_TYPE + " integer);";

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
            userValue.put(KEY_SEX, user.getUserSex());
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
            if (cursor.moveToNext()){
                user = new User();
                user.setUserId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                user.setUserName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                user.setUserPwd(cursor.getString(cursor.getColumnIndex(KEY_PWD)));
                user.setUserNumber(cursor.getString(cursor.getColumnIndex(KEY_NUMBER)));
                user.setUserSex(cursor.getInt(cursor.getColumnIndex(KEY_SEX)));
                user.setUserType(cursor.getInt(cursor.getColumnIndex(KEY_USER_TYPE)));
                Log.e(TAG, "queryUser: " + user.toString() );
            }
            cursor.close();
        }
        return user;
    }

    /**
     * 查找用户
     *
     * @param userName
     * @return
     */
    public User queryUser(String userName) {
        User user = null;
        Cursor cursor = null;
        if (cursor == null) {
            String sql = "select * from " + DATABASE_USER_TABLE + " where " + KEY_NAME + "= ? or " + KEY_NUMBER + "= ?";
            cursor = mDb.rawQuery(sql, new String[]{userName, userName});
        }
        if (cursor != null) {
            if (cursor.moveToNext()){
                user = new User();
                user.setUserId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                user.setUserName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                user.setUserPwd(cursor.getString(cursor.getColumnIndex(KEY_PWD)));
                user.setUserNumber(cursor.getString(cursor.getColumnIndex(KEY_NUMBER)));
                user.setUserSex(cursor.getInt(cursor.getColumnIndex(KEY_SEX)));
                Log.e(TAG, "queryUser: " + user.toString() );
            }
            cursor.close();
        }
        return user;
    }


    /**
     *  修改用户信息 修改密码
     *
     * @param user
     * @return
     */
    public long updateUserPwd(User user) {

        long result = -1;
        Log.e(TAG, "updateUser: use=="+ user.toString());
        ContentValues userValue = new ContentValues();
        userValue.put(KEY_NAME, user.getUserName());
        userValue.put(KEY_PWD, user.getUserPwd());
//        result = mDb.update(DATABASE_USER_TABLE, userValue, KEY_NAME+"=? and " + KEY_PWD_HELP + "=?" ,
//                new String[]{user.getUserName(), user.getPwdHelp()});
        Log.e(TAG, "updateUser: result==" + result);
        return result;
    }

    /**
     *  修改用户信息 修改用户信息
     *
     * @param user
     * @return
     */
    public long updateUserInfo(User user) {

        long result = -1;
        Log.e(TAG, "updateUser: use=="+ user.toString());
        ContentValues userValue = new ContentValues();
        userValue.put(KEY_NAME, user.getUserName());
        userValue.put(KEY_PWD, user.getUserPwd());
//        result = mDb.update(DATABASE_USER_TABLE, userValue, KEY_NAME+" =? and " + KEY_PWD_HELP + "=? and " + KEY_ID + "=?" ,
//                new String[]{user.getUserName(), user.getPwdHelp(), String.valueOf(user.getUserID())});
        Log.e(TAG, "updateUser: result==" + result);
        return result;
    }


}
