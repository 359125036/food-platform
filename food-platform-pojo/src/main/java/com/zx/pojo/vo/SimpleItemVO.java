package com.zx.pojo.vo;

/**
 * @ClassName: SimpleItemVO
 * @Description: 视图层简单商品信息
 * @Author: zhengxin
 * @Date: 2019/12/27 15:26
 * @Version: 1.0
 */
public class SimpleItemVO {
    private String itemId;
    private String itemName;
    private String itemUrl;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }
}
