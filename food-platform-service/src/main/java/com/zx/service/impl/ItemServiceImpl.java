package com.zx.service.impl;

import com.zx.enums.CommentLevel;
import com.zx.mapper.*;
import com.zx.pojo.*;
import com.zx.pojo.vo.CommentLevelCountsVO;
import com.zx.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @ClassName: ItemServiceImpl
 * @Author: zhengxin
 * @Description: 查询商品信息
 * @Date: 2020/5/16 17:15
 * @Version: 1.0
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemsMapper itemsMapper;
    @Autowired
    private ItemsImgMapper itemsImgMapper;
    @Autowired
    private ItemsSpecMapper itemsSpecMapper;
    @Autowired
    private ItemsParamMapper itemsParamMapper;
    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;

    /**
     * 根据商品id查询商品详情
     * @param itemId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Items queryItemById(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    /**
     * 根据商品id查询商品图片列表
     * @param itemId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> queryItemImgList(String itemId) {
        Example example=new Example(ItemsImg.class);
        Example.Criteria criteria =example.createCriteria();
        //根据商品id查询
        criteria.andEqualTo("itemId",itemId);
        //查询商品图片列表
        return itemsImgMapper.selectByExample(example);
    }

    /**
     * 根据商品id查询商品规格列表
     * @param itemId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        Example example=new Example(ItemsSpec.class);
        Example.Criteria criteria =example.createCriteria();
        //根据商品id查询
        criteria.andEqualTo("itemId",itemId);
        //查询商品规格列表
        return itemsSpecMapper.selectByExample(example);
    }

    /**
     * 根据商品id查询商品参数
     * @param itemId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam queryItemsParam(String itemId) {
        Example example=new Example(ItemsParam.class);
        Example.Criteria criteria =example.createCriteria();
        //根据商品id查询
        criteria.andEqualTo("itemId",itemId);
        //查询商品参数
        return itemsParamMapper.selectOneByExample(example);
    }

    /**
     * 根据商品id查询商品评价等级数量
     * @param itemId
     * @return CommentLevelCountsVO
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public CommentLevelCountsVO queryCommentCounts(String itemId) {
        //获取好评数量
        Integer goodCounts=getCommentCount(itemId, CommentLevel.GOOD.type);
        //获取中评数量
        Integer normalCounts=getCommentCount(itemId, CommentLevel.NORMAL.type);
        //获取差评数量
        Integer badCounts=getCommentCount(itemId, CommentLevel.BAD.type);
        //总量
        Integer totalCount=goodCounts+normalCounts+badCounts;
        CommentLevelCountsVO countsVO=new CommentLevelCountsVO();
        countsVO.setBadCounts(badCounts);
        countsVO.setGoodCounts(goodCounts);
        countsVO.setNormalCounts(normalCounts);
        countsVO.setTotalCounts(totalCount);
        return countsVO;
    }

    /**
     * 根据商品id和评价等级获取评价数量
     * @param itemId
     * @param level
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public Integer getCommentCount(String itemId,Integer level){
        ItemsComments itemsComments=new ItemsComments();
        itemsComments.setItemId(itemId);
        if(level!=null){
            itemsComments.setCommentLevel(level);
        }
        return itemsCommentsMapper.selectCount(itemsComments);
    }
}
