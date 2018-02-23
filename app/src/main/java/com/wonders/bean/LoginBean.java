package com.wonders.bean;

/**
 * Created by bjy on 2016/11/7.
 */
public class LoginBean {
    private String deptName;
    private String todoCount;
    private String userId;
    private String userName;
    private boolean isManager;

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getTodoCount() {
        return todoCount;
    }

    public void setTodoCount(String todoCount) {
        this.todoCount = todoCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
