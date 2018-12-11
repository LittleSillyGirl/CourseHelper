package com.bs.coursehelper.bean;

/**
 */

public class CourseUserBean {

    /**
     * 课程的id 或者 用户的id
     *
     */
    private int id;
    /**
     * 课程的名称 或者 用户的名字
     *
     */
    private String name;

    /**
     * 课程对应的教师 或者 用户的名字
     *
     */
    private String teacher;

    /**
     * 课程的数量 或者 用户的数量
     *
     */
    private int num;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "CourseUserBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", teacher='" + teacher + '\'' +
                ", num=" + num +
                '}';
    }
}
