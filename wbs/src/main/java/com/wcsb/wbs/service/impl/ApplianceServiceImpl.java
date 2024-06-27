package com.wcsb.wbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wcsb.wbs.domain.Appliance;
import com.wcsb.wbs.domain.EnstoreList;
import com.wcsb.wbs.mapper.ApplianceMapper;
import com.wcsb.wbs.mapper.EnstoreListMapper;
import com.wcsb.wbs.service.ApplianceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 器具表 服务实现类
 * </p>
 *
 * @author Jessica_Clorize
 * @since 2024-06-03
 */
@Service
public class ApplianceServiceImpl extends ServiceImpl<ApplianceMapper, Appliance> implements ApplianceService {

    @Autowired
    private ApplianceMapper applianceMapper;

    private Map<String, Object> SearchApplianceTable(QueryWrapper<Appliance> QueryWrapper){
        List<Appliance> ApplianceList=applianceMapper.selectList(QueryWrapper);
        Map<String,Object> MapInfo=new HashMap<>();
        if(ApplianceList.isEmpty()){
            MapInfo.put("HTTPCode","500");
        }
        else{
            MapInfo.put("HTTPCode","200");
            MapInfo.put("ApplianceList",ApplianceList);
        }
        return MapInfo;
    }

    public Map<String, Object> GetAllAppliances(){
        QueryWrapper<Appliance> QueryWrapper = new QueryWrapper<>();
        Map<String,Object> MapInfo=SearchApplianceTable(QueryWrapper);
        return MapInfo;
    }

    public Map<String, Object> GetApplianceByID(String ID){
        QueryWrapper<Appliance> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("wrap_id",ID);
        Map<String,Object> MapInfo=SearchApplianceTable(QueryWrapper);
        return MapInfo;
    }

    public Map<String, Object> GetApplianceBySupplementIDAndWrapSize(String SupplementID,Integer WrapSize){
        QueryWrapper<Appliance> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("sup_id",SupplementID);
        QueryWrapper.eq("wrap_size",WrapSize);
        List<Appliance> ApplianceList=applianceMapper.selectList(QueryWrapper);
        Map<String,Object> MapInfo=new HashMap<>();
        if(ApplianceList.isEmpty()){
            MapInfo.put("HTTPCode",500);
        }
        else if(ApplianceList.size()>1){
            MapInfo.put("HTTPCode",200);
            MapInfo.put("Appliance",ApplianceList.get(0));
        }
        else{
            MapInfo.put("HTTPCode",200);
            MapInfo.put("Appliance",ApplianceList.get(0));
        }
        return MapInfo;
    }

    public Map<String, Object> GetApplianceBySupID(String SupplementID){
        QueryWrapper<Appliance> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("sup_id",SupplementID);
        List<Appliance> ApplianceList=applianceMapper.selectList(QueryWrapper);
        Map<String,Object> MapInfo=new HashMap<>();
        if(ApplianceList.isEmpty()){
            MapInfo.put("HTTPCode",500);
        }
        else if(ApplianceList.size()>1){
            MapInfo.put("HTTPCode",200);
            MapInfo.put("ApplianceID",ApplianceList.get(0).getWrapId());
        }
        else{
            MapInfo.put("HTTPCode",200);
            MapInfo.put("ApplianceID",ApplianceList.get(0).getWrapId());
        }
        return MapInfo;
    }

    public Map<String, Object> InsertAppliance(Appliance appliance){
        QueryWrapper<Appliance> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("wrap_id",appliance.getWrapId());
        List<Appliance> ApplianceList=applianceMapper.selectList(QueryWrapper);
        Integer HTTPCode=200;
        //if(!ApplianceList.isEmpty()){
        //    HTTPCode=500;
        //}

        if(HTTPCode==200){
            applianceMapper.insert(appliance);
        }
        Map<String,Object> MapInfo=new HashMap<>();
        MapInfo.put("HTTPCode", HTTPCode);
        return MapInfo;
    }

    public Map<String, Object> DeleteAppliance(String WrapID){
        QueryWrapper<Appliance> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("wrap_id", WrapID);
        Map<String, Object> MapInfo=new HashMap<>();
        MapInfo.put("wrap_id",WrapID);
        applianceMapper.deleteByMap(MapInfo);
        MapInfo=new HashMap<>();
        MapInfo.put("HTTPCode",200);
        return MapInfo;
    }

}
