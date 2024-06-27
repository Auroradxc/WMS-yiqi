package com.wcsb.wbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wcsb.wbs.domain.EnstoreList;
import com.wcsb.wbs.domain.ExstoreList;
import com.wcsb.wbs.mapper.ExstoreListMapper;
import com.wcsb.wbs.service.ExstoreListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
public class ExstoreListServiceImpl extends ServiceImpl<ExstoreListMapper, ExstoreList> implements ExstoreListService {

    @Autowired
    private ExstoreListMapper exstoreListMapper;

    // Get All ExstoreList
    public Map<String, Object> GetExstoreList() {
        QueryWrapper<ExstoreList> queryWrapper = new QueryWrapper<>();
        List<ExstoreList> ExstoreQueryList=exstoreListMapper.selectList(queryWrapper);
        Map<String,Object> MapInfo=new HashMap<>();

        if(ExstoreQueryList.size()==0){
            MapInfo.put("HTTPCode",500);
            MapInfo.put("ExstoreList",null);
        }
        else{
            MapInfo.put("HTTPCode",200);
            MapInfo.put("ExstoreList",ExstoreQueryList);
        }
        return MapInfo;
    }

    // Get All ExstoreList Via ListNum (Supposedly Only One)
    public Map<String, Object> GetExstoreListByListNum(String ListNum){
        QueryWrapper<ExstoreList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("List_num",ListNum);
        List<ExstoreList> ExstoreQueryList=exstoreListMapper.selectList(queryWrapper);
        Map<String,Object> MapInfo=new HashMap<>();

        if(ExstoreQueryList.size()==0){
            MapInfo.put("HTTPCode",500);
            MapInfo.put("ExstoreLists",null);
        }
        else{
            MapInfo.put("HTTPCode",200);
            MapInfo.put("ExstoreLists",ExstoreQueryList);
        }
        return MapInfo;
    }

    // Write One Line Of ExstoreList
    public Map<String, Object> CreateExstoreList(ExstoreList ExstoreList) {
        exstoreListMapper.insert(ExstoreList);
        Map<String,Object> MapInfo=new HashMap<>();
        MapInfo.put("HTTPCode",200);
        return MapInfo;
    }

    // Modify ExstoreList Customer
    public Map<String, Object> UpdateExstoreListViaListNum(ExstoreList exstoreList, String Customer){
        UpdateWrapper<ExstoreList> UpdateWrapper=new UpdateWrapper<>();
        UpdateWrapper.set("customer", Customer);
        UpdateWrapper.set("update_time", LocalDateTime.now());
        UpdateWrapper.eq("List_num", exstoreList.getListNum());
        exstoreListMapper.update(null, UpdateWrapper);
        Map<String, Object> MapInfo=new HashMap<>();
        MapInfo=new HashMap<>();
        MapInfo.put("HTTPCode",200);
        return MapInfo;
    }

    public Map<String, Object> DeleteExstoreList(String ListNum){
        QueryWrapper<ExstoreList> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("List_num", ListNum);
        Map<String, Object> MapInfo=new HashMap<>();
        MapInfo.put("List_Num",ListNum);
        exstoreListMapper.deleteByMap(MapInfo);
        MapInfo=new HashMap<>();
        MapInfo.put("HTTPCode",200);
        return MapInfo;
    }

    public Map<String, Object> UpdateEnstoreListStatus(ExstoreList exstoreList, String Status){
        UpdateWrapper<ExstoreList> UpdateWrapper=new UpdateWrapper<>();
        UpdateWrapper.set("List_Status", Status);
        UpdateWrapper.set("update_time", LocalDateTime.now());
        UpdateWrapper.eq("List_num", exstoreList.getListNum());
        exstoreListMapper.update(null, UpdateWrapper);
        Map<String, Object> MapInfo=new HashMap<>();
        MapInfo.put("HTTPCode",200);
        return MapInfo;
    }

    public Map<String, Object> UpdateExstoreList(String ListNum, String Customer, String Status){
        UpdateWrapper<ExstoreList> UpdateWrapper=new UpdateWrapper<>();
        UpdateWrapper.set("List_Status", Status);
        UpdateWrapper.set("customer", Customer);
        UpdateWrapper.set("update_time", LocalDateTime.now());
        UpdateWrapper.eq("List_num", ListNum);
        exstoreListMapper.update(null, UpdateWrapper);
        Map<String, Object> MapInfo=new HashMap<>();
        MapInfo.put("HTTPCode",200);
        return MapInfo;
    }
}
