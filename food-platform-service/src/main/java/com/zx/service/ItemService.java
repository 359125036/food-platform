package com.zx.service;

import com.zx.pojo.Items;
import com.zx.pojo.ItemsImg;
import com.zx.pojo.ItemsParam;
import com.zx.pojo.ItemsSpec;
import com.zx.pojo.vo.CommentLevelCountsVO;
import com.zx.pojo.vo.ShopcartVO;
import com.zx.utils.PagedGridResult;

import java.util.List;

/**
 * @ClassName: ItemService
 * @Author: zhengxin
 * @Description: 商品信息
 * @Date: 2020/5/16 17:02
 * @Version: 1.0
 */
public interface ItemService {

    /**
     * 根据商品id查询商品详情
     * @param itemId
     * @return
     */
    public Items queryItemById(String itemId);

    /**
     * 根据商品id查询商品图片列表
     * @param itemId
     * @return
     */
    public List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品id查询商品规格列表
     * @param itemId
     * @return
     */
    public List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品id查询商品参数
     * @param itemId
     * @return
     */
    public ItemsParam queryItemsParam(String itemId);

    /**
     * 根据商品id查询商品评价等级数量
     * @param itemId
     * @return
     */
    public CommentLevelCountsVO queryCommentCounts(String itemId);

    /**
     * 根据商品id查询商品的评价（分页）
     * @param itemId
     * @param level
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult queryPagedComments(String itemId, Integer level,
                                              Integer page, Integer pageSize);


    /**
     * 搜索商品信息
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult searchItems(String keywords, String sort,
                                              Integer page, Integer pageSize);

    /**
     * 通过三级分类id搜索商品信息
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult searchItemsByThirdCat(Integer catId, String sort,
                                       Integer page, Integer pageSize);

    /**
     * @Method queryItemsBySpecIds
     * @Author zhengxin
     * @Description 根据商品拼接的规格itemSpecIds获取商品信息
     * @param itemSpecIds
     * @Return
     * @Exception
     * @Date 2020/5/18 20:43
     */
    public List<ShopcartVO> queryItemsBySpecIds(String itemSpecIds);
}
