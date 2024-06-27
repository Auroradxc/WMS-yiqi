package com.wcsb.wbs.mapper;

import com.wcsb.wbs.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Jessica_Clorize
 * @since 2024-06-03
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
