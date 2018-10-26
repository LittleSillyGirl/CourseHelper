package com.bs.coursehelper.bean;

import java.io.Serializable;

/**
 * 用户的信息
 *
 */

public class User implements Serializable {

    /**
     * 用户的姓名、密码、学号（教师工号）、性别
     *
     */
    private int userId;
    private String userName;
    private String userPwd;
    private String userNumber;
    private int userSex;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public int getUserSex() {
        return userSex;
    }

    public void setUserSex(int userSex) {
        this.userSex = userSex;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userPwd='" + userPwd + '\'' +
                ", userNumber='" + userNumber + '\'' +
                ", userSex=" + userSex +
                '}';
    }
}
