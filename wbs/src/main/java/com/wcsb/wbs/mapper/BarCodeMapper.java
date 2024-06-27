package com.wcsb.wbs.mapper;

import com.wcsb.wbs.domain.BarCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 不知道这个in_out_state是来自于那里的，所以目前不用这个表 Mapper 接口
 * </p>
 *
 * @author Jessica_Clorize
 * @since 2024-06-14
 */
@Mapper
public interface BarCodeMapper extends BaseMapper<BarCode> {

}
