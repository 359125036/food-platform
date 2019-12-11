package com.zx.pojo;

import javax.persistence.Id;

public class Stu {
    @Id
    private Integer stu;

    private String name;

    private Integer age;

    /**
     * @return stu
     */
    public Integer getStu() {
        return stu;
    }

    /**
     * @param stu
     */
    public void setStu(Integer stu) {
        this.stu = stu;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * @param age
     */
    public void setAge(Integer age) {
        this.age = age;
    }
}