package com.zx.pojo.vo;

import java.util.List;

/**
 * @ClassName: CategoryVO
 * @Description: 显示在视图层的分类
 * @Author: zhengxin
 * @Date: 2019/12/25 15:47
 * @Version: 1.0
 */
public class CategoryVO {
    private Integer id;
    private String name;
    private String type;
    private Integer fatherId;
    private List<SubCategoryVO> subCatList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getFatherId() {
        return fatherId;
    }

    public void setFatherId(Integer fatherId) {
        this.fatherId = fatherId;
    }

    public List<SubCategoryVO> getSubCatList() {
        return subCatList;
    }

    public void setSubCatList(List<SubCategoryVO> subCatList) {
        this.subCatList = subCatList;
    }
}
