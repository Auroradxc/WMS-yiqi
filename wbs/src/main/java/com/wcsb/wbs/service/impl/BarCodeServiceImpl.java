package com.wcsb.wbs.service.impl;

import com.wcsb.wbs.domain.BarCode;
import com.wcsb.wbs.mapper.BarCodeMapper;
import com.wcsb.wbs.service.BarCodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 不知道这个in_out_state是来自于那里的，所以目前不用这个表 服务实现类
 * </p>
 *
 * @author Jessica_Clorize
 * @since 2024-06-14
 */
@Service
public class BarCodeServiceImpl extends ServiceImpl<BarCodeMapper, BarCode> implements BarCodeService {

}
