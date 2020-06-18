package com.zx.controller;

import com.zx.pojo.Stu;
import com.zx.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @ClassName: HelloController
 * @Description: 测试
 * @Author: zhengxin
 * @Date: 2019/12/6 11:41
 * @Version: 1.0
 */
@ApiIgnore
@RestController//@RestController返回的都是json
public class StuController {
    @Autowired
    private StuService stuService;
    @GetMapping("/getStu")
    public Stu getStu(int id){
        return stuService.getStu(id);
    }

    @PostMapping("/saveStu")
    public Object saveStu(){
        stuService.saveStu();
        return "200";
    }

    @PostMapping("/updateStu")
    public Object updateStu(int id){
        stuService.updateStu(id);
        return "200";
    }

    @PostMapping("/deleteStu")
    public Object deleteStu(int id){
        stuService.deleteStu(id);
        return "200";
    }

}
