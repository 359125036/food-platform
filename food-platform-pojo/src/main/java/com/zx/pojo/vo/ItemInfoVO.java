package com.zx.pojo.vo;

import com.zx.pojo.Items;
import com.zx.pojo.ItemsImg;
import com.zx.pojo.ItemsParam;
import com.zx.pojo.ItemsSpec;

import java.util.List;

/**
 * @ClassName: ItemInfoVO
 * @Author: zhengxin
 * @Description: 商品详情VO
 * @Date: 2020/5/16 19:52
 * @Version: 1.0
 */
public class ItemInfoVO {

    private Items item;
    private List<ItemsImg> itemImgList;
    private List<ItemsSpec> itemSpecList;
    private ItemsParam itemParams;

    public Items getItem() {
        return item;
    }

    public void setItem(Items item) {
        this.item = item;
    }

    public List<ItemsImg> getItemImgList() {
        return itemImgList;
    }

    public void setItemImgList(List<ItemsImg> itemImgList) {
        this.itemImgList = itemImgList;
    }

    public List<ItemsSpec> getItemSpecList() {
        return itemSpecList;
    }

    public void setItemSpecList(List<ItemsSpec> itemSpecList) {
        this.itemSpecList = itemSpecList;
    }

    public ItemsParam getItemParams() {
        return itemParams;
    }

    public void setItemParams(ItemsParam itemParams) {
        this.itemParams = itemParams;
    }
}
