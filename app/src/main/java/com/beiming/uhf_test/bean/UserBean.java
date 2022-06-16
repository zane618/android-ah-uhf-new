package com.beiming.uhf_test.bean;

import java.io.Serializable;

/**
 * 条形码bean
 * Created by htj on 2021/5/20.
 */
public class UserBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;//姓名
    private int age;//年龄

    public UserBean(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
