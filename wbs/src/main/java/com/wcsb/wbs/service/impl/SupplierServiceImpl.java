package com.wcsb.wbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wcsb.wbs.domain.Customer;
import com.wcsb.wbs.domain.DailyListEntry;
import com.wcsb.wbs.domain.Supplier;
import com.wcsb.wbs.mapper.SupplierMapper;
import com.wcsb.wbs.service.SupplierService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jessica_Clorize
 * @since 2024-06-03
 */
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements SupplierService {

    @Autowired
    private SupplierMapper supplierMapper;

    public Map<String, Object> GetAllSuppliers(){
        QueryWrapper<Supplier> queryWrapper = new QueryWrapper<>();
        List<Supplier> SupplierList=supplierMapper.selectList(queryWrapper);
        Map<String, Object> MapInfo = new HashMap<>();
        if(SupplierList.size()>0){
            MapInfo.put("SupplierList",SupplierList);
            MapInfo.put("HTTPCode",200);
        }
        else{
            MapInfo.put("HTTPCode",500);
        }
        return MapInfo;
    }

    public Map<String, Object> AddSupplier(Supplier supplier){
        QueryWrapper<Supplier> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("supplier_id", supplier.getSupplierId());
        List<Supplier> supplierList=supplierMapper.selectList(queryWrapper);
        Map<String, Object> MapInfo=new HashMap<>();
        if(supplierList.size()==0){
            supplierMapper.insert(supplier);
            MapInfo.put("HTTPCode", 200);
        }
        else{
            MapInfo.put("HTTPCode", 500);
        }
        return MapInfo;
    }

    public Map<String, Object> DeleteSupplier(Supplier supplier){
        Map<String, Object> MapInfo=new HashMap<>();
        MapInfo.put("supplier_id",supplier.getSupplierId());
        supplierMapper.deleteByMap(MapInfo);
        MapInfo=new HashMap<>();
        MapInfo.put("HTTPCode",200);
        return MapInfo;
    }

}
