package com.zx.service;

import com.zx.pojo.Stu;

public interface StuService {

    public Stu getStu(int id);

    public void saveStu();

    public void updateStu(int id);

    public void deleteStu(int id);


    public Stu getStuInfo(int id);

    public void saveParent();
    public void saveChildren();

}
