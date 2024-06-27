package com.wcsb.wbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wcsb.wbs.domain.EnstoreList;
import com.wcsb.wbs.domain.Supplement;
import com.wcsb.wbs.mapper.EnstoreListMapper;
import com.wcsb.wbs.mapper.SupplementMapper;
import com.wcsb.wbs.service.EnstoreListService;
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
public class EnstoreListServiceImpl extends ServiceImpl<EnstoreListMapper, EnstoreList> implements EnstoreListService {

    @Autowired
    private EnstoreListMapper enstoreListMapper;

    private Map<String, Object> SearchEnstoreListTable(QueryWrapper<EnstoreList> QueryWrapper) {
        List<EnstoreList> EnstoreList=enstoreListMapper.selectList(QueryWrapper);
        Map<String, Object> MapInfo=new HashMap<>();
        if(EnstoreList.isEmpty()){
            MapInfo.put("HTTPCode", 500);
        }
        else{
            MapInfo.put("HTTPCode", 200);
            MapInfo.put("EnstoreLists", EnstoreList);
        }
        return MapInfo;
    }

    public Map<String, Object> GetEnstoreList(){
        QueryWrapper<EnstoreList> QueryWrapper = new QueryWrapper<>();
        Map<String, Object> MapInfo=SearchEnstoreListTable(QueryWrapper);
        return MapInfo;
    }

    public Map<String, Object> GetEnstoreListByStatus(String Status){
        QueryWrapper<EnstoreList> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("List_Status", Status);
        Map<String, Object> MapInfo=SearchEnstoreListTable(QueryWrapper);
        return MapInfo;
    }

    public Map<String, Object> GetEnstoreListByListNum(String ListNum){
        QueryWrapper<EnstoreList> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("List_num", ListNum);
        Map<String, Object> MapInfo=SearchEnstoreListTable(QueryWrapper);
        return MapInfo;
    }

    public Map<String, Object> GetEnstoreListByStatusAndListnum(String Status, String ListNum){
        QueryWrapper<EnstoreList> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("List_num", ListNum);
        QueryWrapper.eq("List_Status", Status);
        Map<String, Object> MapInfo=SearchEnstoreListTable(QueryWrapper);
        return MapInfo;
    }

    public int GetEnstoreListSize() {
        QueryWrapper<EnstoreList> QueryWrapper = new QueryWrapper<>();
        List<EnstoreList> EnstoreList=enstoreListMapper.selectList(QueryWrapper);
        int LargestID=0;
        for(EnstoreList enstoreList:EnstoreList){
            if(enstoreList.getId()>LargestID){
                LargestID=enstoreList.getId();
            }
        }
        return LargestID;
    }

    public Map<String, Object> InsertNewEnstoreList(EnstoreList EnstoreList){
        enstoreListMapper.insert(EnstoreList);
        Map<String, Object> MapInfo=new HashMap<>();
        MapInfo.put("HTTPCode",200);
        return MapInfo;
    }

    public Map<String, Object> DeleteEnstoreList(String ListNum){
        QueryWrapper<EnstoreList> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("List_num", ListNum);
        Map<String, Object> MapInfo=new HashMap<>();
        MapInfo.put("List_Num",ListNum);
        enstoreListMapper.deleteByMap(MapInfo);
        MapInfo=new HashMap<>();
        MapInfo.put("HTTPCode",200);
        return MapInfo;
    }

    public Map<String, Object> UpdateEnstoreListViaListNum(EnstoreList enstoreList, String Supplier){
        UpdateWrapper<EnstoreList> UpdateWrapper=new UpdateWrapper<>();
        UpdateWrapper.set("supplier", Supplier);
        UpdateWrapper.set("update_time", LocalDateTime.now());
        UpdateWrapper.eq("List_num", enstoreList.getListNum());
        enstoreListMapper.update(null, UpdateWrapper);
        Map<String, Object> MapInfo=new HashMap<>();
        MapInfo=new HashMap<>();
        MapInfo.put("HTTPCode",200);
        return MapInfo;
    }

    public Map<String, Object> UpdateEnstoreListStatus(EnstoreList enstoreList, String Status){
        UpdateWrapper<EnstoreList> UpdateWrapper=new UpdateWrapper<>();
        UpdateWrapper.set("List_Status", Status);
        UpdateWrapper.set("update_time", LocalDateTime.now());
        UpdateWrapper.eq("List_num", enstoreList.getListNum());
        enstoreListMapper.update(null, UpdateWrapper);
        Map<String, Object> MapInfo=new HashMap<>();
        MapInfo.put("HTTPCode",200);
        return MapInfo;
    }

    public Map<String, Object> UpdateEnstoreList(String ListNum, String Supplier, String Status){
        UpdateWrapper<EnstoreList> UpdateWrapper=new UpdateWrapper<>();
        UpdateWrapper.set("List_Status", Status);
        UpdateWrapper.set("supplier", Supplier);
        UpdateWrapper.set("update_time", LocalDateTime.now());
        UpdateWrapper.eq("List_num", ListNum);
        enstoreListMapper.update(null, UpdateWrapper);
        Map<String, Object> MapInfo=new HashMap<>();
        MapInfo.put("HTTPCode",200);
        return MapInfo;
    }

}
