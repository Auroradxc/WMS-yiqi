package com.wcsb.wbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wcsb.wbs.domain.Supplement;
import com.wcsb.wbs.domain.User;
import com.wcsb.wbs.mapper.SupplementMapper;
import com.wcsb.wbs.service.SupplementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class SupplementServiceImpl extends ServiceImpl<SupplementMapper, Supplement> implements SupplementService {

    @Autowired
    private SupplementMapper supplementMapper;

    private Map<String, Object> SearchSupplementTable(QueryWrapper<Supplement> QueryWrapper) {
        List<Supplement> Supplements=supplementMapper.selectList(QueryWrapper);
        Map<String, Object> MapInfo=new HashMap<>();
        if(Supplements.isEmpty()){
            MapInfo.put("HTTPCode", 500);
        }
        else{
            MapInfo.put("HTTPCode", 200);
            MapInfo.put("Supplements", Supplements);
        }
        return MapInfo;
    }

    public Map<String, Object> GetSupplementList(){
        // Return {HTTPCode: xxx, Supplements: <List>}
        QueryWrapper<Supplement> QueryWrapper = new QueryWrapper<>();
        Map<String, Object> MapInfo=SearchSupplementTable(QueryWrapper);
        return MapInfo;
    }

    public Map<String, Object> GetSupplementByID(String sup_id){
        QueryWrapper<Supplement> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("sup_id", sup_id);
        Map<String, Object> MapInfo=SearchSupplementTable(QueryWrapper);
        return MapInfo;
    }

    public Map<String, Object> GetSupplementBySupplierID(String supplier_id){
        QueryWrapper<Supplement> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("supplier", supplier_id);
        Map<String, Object> MapInfo=SearchSupplementTable(QueryWrapper);
        return MapInfo;
    }

    public Map<String, Object> GetSupplementByIDAndSupplierID(String sup_id, String supplier_id){
        QueryWrapper<Supplement> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("sup_id", sup_id);
        QueryWrapper.eq("supplier", supplier_id);
        Map<String, Object> MapInfo=SearchSupplementTable(QueryWrapper);
        return MapInfo;
    }

    public Map<String, Object> InsertSupplement(Supplement supplement){
        QueryWrapper<Supplement> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("sup_id", supplement.getSupId());
        Map<String, Object> MapInfo=SearchSupplementTable(QueryWrapper);
        int code=(int)MapInfo.get("HTTPCode");
        MapInfo=new HashMap<>();
        if(code==500){
            // Empty
            supplementMapper.insert(supplement);
            MapInfo.put("HTTPCode", 200);
        }
        else if(code==200){
            MapInfo.put("HTTPCode", 500);
        }
        return MapInfo;
    }

    public Map<String, Object> DeleteSupplement(Supplement supplement){
        Map<String, Object> MapInfo=new HashMap<>();
        MapInfo.put("sup_id",supplement.getSupId());
        supplementMapper.deleteByMap(MapInfo);
        MapInfo=new HashMap<>();
        MapInfo.put("HTTPCode",200);
        return MapInfo;
    }
}
