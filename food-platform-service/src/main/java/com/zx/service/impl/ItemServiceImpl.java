package com.zx.service.impl;

import com.github.pagehelper.PageHelper;
import com.zx.enums.CommentLevel;
import com.zx.enums.YesOrNoEnum;
import com.zx.mapper.*;
import com.zx.pojo.*;
import com.zx.pojo.vo.CommentLevelCountsVO;
import com.zx.pojo.vo.ItemCommentVO;
import com.zx.pojo.vo.SearchItemsVO;
import com.zx.pojo.vo.ShopcartVO;
import com.zx.service.ItemService;
import com.zx.utils.DesensitizationUtil;
import com.zx.utils.PageUtil;
import com.zx.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

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
    @Autowired
    private ItemsMapperCustom itemsMapperCustom;

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

    /**
     * 根据商品id查询商品的评价（分页）
     * @param itemId
     * @param level
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize) {
        Map<String,Object> map=new HashMap<>();
        map.put("itemId",itemId);
        map.put("level",level);
        /**
         * page:第几页
         * pageSize：每页显示条数
         */
        PageHelper.startPage(page,pageSize);
        //获取分页后的评价数据
        List<ItemCommentVO> list=itemsMapperCustom.queryItemComments(map);
        //脱敏
        for (ItemCommentVO commentVO : list) {
            commentVO.setNickname(DesensitizationUtil.commonDisplay(commentVO.getNickname()));
        }
        return PageUtil.page(list,page);
    }

    /**
     * 根据关键字或排序搜索商品信息
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) {
        Map<String ,Object> map=new HashMap<>();
        map.put("keywords",keywords);
        map.put("sort",sort);

        /**
         * page:第几页
         * pageSize：每页显示条数
         */
        PageHelper.startPage(page,pageSize);
        List<SearchItemsVO> itemsVOList=itemsMapperCustom.searchItems(map);
        return PageUtil.page(itemsVOList,page);
    }

    /**
     * @Method: searchItemsByThirdCat
     * @Author: zhengxin
     * @Description: 通过三级分类id搜索商品信息
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @Date: 2020/5/17 14:53
     * @Exception:
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItemsByThirdCat(Integer catId, String sort, Integer page, Integer pageSize) {
        Map<String ,Object> map=new HashMap<>();
        map.put("catId",catId);
        map.put("sort",sort);
        /**
         * page:第几页
         * pageSize：每页显示条数
         */
        PageHelper.startPage(page,pageSize);
        List<SearchItemsVO> itemsVOList=itemsMapperCustom.searchItemsByThirdCat(map);
        return PageUtil.page(itemsVOList,page);
    }


    /**
     * @Method queryItemsBySpecIds
     * @Author zhengxin
     * @Description 根据商品拼接的规格itemSpecIds获取商品信息
     * @param itemSpecIds
     * @Return
     * @Exception
     * @Date 2020/5/18 20:43
     */
    @Override
    public List<ShopcartVO> queryItemsBySpecIds(String itemSpecIds) {
        String ids[] =itemSpecIds.split(",");
        List<String> list=new ArrayList<>();
        Collections.addAll(list,ids);
        return itemsMapperCustom.queryItemsBySpecIds(list);
    }

    /**
     * @Method queryItemSpecById
     * @Author zhengxin
     * @Description 获取商品规格信息
     * @param specId 商品规格id
     * @Return
     * @Exception
     * @Date 2020/5/23 21:03
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsSpec queryItemSpecById(String specId) {
        return itemsSpecMapper.selectByPrimaryKey(specId);
    }

    /**
     * @Method queryItemMainImgById
     * @Author zhengxin
     * @Description 查询主图片id
     * @param itemId 商品id
     * @Return
     * @Exception
     * @Date 2020/5/23 21:04
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public String queryItemMainImgById(String itemId) {
        ItemsImg itemsImg = new ItemsImg();
        itemsImg.setItemId(itemId);
        itemsImg.setIsMain(YesOrNoEnum.YES.type);
        ItemsImg result = itemsImgMapper.selectOne(itemsImg);
        return result != null ? result.getUrl() : "";
    }

    /**
     * @Method decreaseItemSpecStock
     * @Author zhengxin
     * @Description 根据购买数量减少库存
     * @param specId 商品规格id
     * @param buyCounts 购买件数
     * @Return void
     * @Exception
     * @Date 2020/5/23 21:08
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void decreaseItemSpecStock(String specId, int buyCounts) {

        // synchronized 不推荐使用，集群下无用，性能低下
        // 锁数据库: 不推荐，导致数据库性能低下
        // 分布式锁 zookeeper redis

        // lockUtil.getLock(); -- 加锁

        // 1. 查询库存
//        int stock = 10;

        // 2. 判断库存，是否能够减少到0以下
//        if (stock - buyCounts < 0) {
        // 提示用户库存不够
//            10 - 3 -3 - 5 = -1
//        }

        // lockUtil.unLock(); -- 解锁

        //修改库存
        int result = itemsMapperCustom.decreaseItemSpecStock(specId, buyCounts);
        if (result != 1) {
            throw new RuntimeException("订单创建失败，原因：库存不足!");
        }
    }
}
