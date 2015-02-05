package com.choonchernlim.testHibernate.bean;

public class ProjectUserBean {
    private final String projectName;
    private final String userName;

    public ProjectUserBean(String projectName, String userName) {
        this.projectName = projectName;
        this.userName = userName;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getUserName() {
        return userName;
    }
}
