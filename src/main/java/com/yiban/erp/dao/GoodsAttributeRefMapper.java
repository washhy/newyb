package com.yiban.erp.dao;

import com.yiban.erp.entities.GoodsAttributeRef;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsAttributeRefMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsAttributeRef record);

    GoodsAttributeRef selectByPrimaryKey(Long id);

    int updateByPrimaryKey(GoodsAttributeRef record);

    List<GoodsAttributeRef> getByGoodsInfoId(Long goodsInfoId);

    int insertBatch(@Param("attributeRefs") List<GoodsAttributeRef> attributeRefs);

    int deleteByGoodsInfoId(Long goodsInfoId);
}