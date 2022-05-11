package com.beiming.uhf_test.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by htj on 2017/9/21.
 */
@Entity
public class LoginBean implements Serializable {
    private static final long serialVersionUID = 0x111111111L;

    @Id(autoincrement = true)
    private Long loginNumber;//序号

    private String userName;//用户名
    private String password;//密码
    private String isEnable;//是否启用 1:启用,2:禁用
    private String creatTime;//创建时间
    private String type;//类型 1:超级用户,2:管理员,3:普通用户
    private String availableTimeType;//可用时间类型 1:超级用户,2:管理员,3:普通用户
    private String availableTime;//可用时间
    private String startEffectiveTime;//有效时长的开始时间(在每次更改可用的有效时长时记录并刷新一次该时间)
    private String projectTeam;//所属项目组
    private String answerOne;//答案一
    private String answerTwo;//答案二
    private String answerThree;//答案三

    @Generated(hash = 55387097)
    public LoginBean(Long loginNumber, String userName, String password,
            String isEnable, String creatTime, String type,
            String availableTimeType, String availableTime,
            String startEffectiveTime, String projectTeam, String answerOne,
            String answerTwo, String answerThree) {
        this.loginNumber = loginNumber;
        this.userName = userName;
        this.password = password;
        this.isEnable = isEnable;
        this.creatTime = creatTime;
        this.type = type;
        this.availableTimeType = availableTimeType;
        this.availableTime = availableTime;
        this.startEffectiveTime = startEffectiveTime;
        this.projectTeam = projectTeam;
        this.answerOne = answerOne;
        this.answerTwo = answerTwo;
        this.answerThree = answerThree;
    }

    @Generated(hash = 1112702939)
    public LoginBean() {
    }


    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStartEffectiveTime() {
        return startEffectiveTime;
    }

    public void setStartEffectiveTime(String startEffectiveTime) {
        this.startEffectiveTime = startEffectiveTime;
    }

    public String getAnswerOne() {
        return this.answerOne;
    }

    public void setAnswerOne(String answerOne) {
        this.answerOne = answerOne;
    }

    public String getAnswerTwo() {
        return this.answerTwo;
    }

    public void setAnswerTwo(String answerTwo) {
        this.answerTwo = answerTwo;
    }

    public String getAnswerThree() {
        return this.answerThree;
    }

    public void setAnswerThree(String answerThree) {
        this.answerThree = answerThree;
    }

    public Long getLoginNumber() {
        return this.loginNumber;
    }

    public void setLoginNumber(Long loginNumber) {
        this.loginNumber = loginNumber;
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "loginNumber=" + loginNumber +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", isEnable='" + isEnable + '\'' +
                ", creatTime='" + creatTime + '\'' +
                ", type='" + type + '\'' +
                ", availableTimeType='" + availableTimeType + '\'' +
                ", availableTime='" + availableTime + '\'' +
                ", startEffectiveTime='" + startEffectiveTime + '\'' +
                ", answerOne='" + answerOne + '\'' +
                ", answerTwo='" + answerTwo + '\'' +
                ", answerThree='" + answerThree + '\'' +
                '}';
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAvailableTimeType() {
        return availableTimeType;
    }

    public void setAvailableTimeType(String availableTimeType) {
        this.availableTimeType = availableTimeType;
    }

    public String getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(String availableTime) {
        this.availableTime = availableTime;
    }

    public String getProjectTeam() {
        return this.projectTeam;
    }

    public void setProjectTeam(String projectTeam) {
        this.projectTeam = projectTeam;
    }
}
