package com.wcsb.wbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wcsb.wbs.domain.EnstoreListDetail;
import com.wcsb.wbs.domain.ExstoreListDetail;
import com.wcsb.wbs.mapper.ExstoreListDetailMapper;
import com.wcsb.wbs.service.ExstoreListDetailService;
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
public class ExstoreListDetailServiceImpl extends ServiceImpl<ExstoreListDetailMapper, ExstoreListDetail> implements ExstoreListDetailService {

    @Autowired
    private ExstoreListDetailMapper exstoreListDetailMapper;

    public Map<String, Object> InsertExstoreListDetail(ExstoreListDetail exstoreListDetail){
        exstoreListDetailMapper.insert(exstoreListDetail);
        Map<String, Object> MapInfo = new HashMap<String, Object>();
        MapInfo.put("HTTPCode", 200);
        return MapInfo;
    }

    public Map<String, Object> RetrieveExstoreListTotal(String ListNum){
        QueryWrapper<ExstoreListDetail> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("List_num", ListNum);
        List<ExstoreListDetail> ExstoreLists=exstoreListDetailMapper.selectList(QueryWrapper);
        String ListID=null;
        String Creator=null;
        LocalDateTime CreateTime=null;

        if(ExstoreLists.size()>0){
            ListID=ExstoreLists.get(0).getListId();
            Creator=ExstoreLists.get(0).getCreator();
            CreateTime=ExstoreLists.get(0).getCreateTime();
        }
        else{
            ListID=null;
            Creator=null;
            CreateTime=null;
        }

        Map<String, Object> MapInfo=new HashMap<>();
        MapInfo.put("HTTPCode",200);
        MapInfo.put("List_Num",ListNum);
        MapInfo.put("List_ID",ListID);
        MapInfo.put("Creator",Creator);
        MapInfo.put("CreateTime",CreateTime);

        MapInfo.put("ExstoreLists",ExstoreLists);
        return MapInfo;
    }

    public Map<String, Object> RetrieveOneExstoreList(String ListNum, String SupplementID){
        QueryWrapper<ExstoreListDetail> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("List_num", ListNum);
        QueryWrapper.eq("sup_id", SupplementID);
        List<ExstoreListDetail> ExstoreLists=exstoreListDetailMapper.selectList(QueryWrapper);
        Map<String, Object> MapInfo=new HashMap<>();
        if(ExstoreLists.size()>1){
            MapInfo.put("HTTPCode", 500);
            MapInfo.put("ActInNum", ExstoreLists.get(0).getActInNum());
            MapInfo.put("ExstoreList", ExstoreLists.get(0));
        }
        else if(ExstoreLists.size()==0){
            MapInfo.put("HTTPCode", 500);
        }
        else{
            MapInfo.put("HTTPCode", 200);
            MapInfo.put("ActInNum", ExstoreLists.get(0).getActInNum());
            MapInfo.put("ExstoreList", ExstoreLists.get(0));
        }
        return MapInfo;
    }

    public Map<String, Object> DeleteExstoreListDetail(String ListNum){
        QueryWrapper<EnstoreListDetail> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("List_num", ListNum);
        Map<String, Object> MapInfo=new HashMap<>();
        MapInfo.put("List_Num",ListNum);
        exstoreListDetailMapper.deleteByMap(MapInfo);
        MapInfo=new HashMap<>();
        MapInfo.put("HTTPCode",200);
        return MapInfo;
    }

    public Map<String, Object> RetrieveExstoreListFull(String ListNum){
        QueryWrapper<ExstoreListDetail> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("List_num", ListNum);

        List<ExstoreListDetail> ExstoreSupplementList=exstoreListDetailMapper.selectList(QueryWrapper);

        Map<String, Object> MapInfo=new HashMap<>();
        //System.out.println(EnstoreSupplementList.get(0).getListNum());
        if(ExstoreSupplementList.size()>0){
            MapInfo.put("HTTPCode",200);
        }
        else{
            MapInfo.put("HTTPCode",500);
        }

        MapInfo.put("DetailList",ExstoreSupplementList);
        return MapInfo;
    }

    public Map<String, Object> UpdateExstoreListViaSupID(ExstoreListDetail exstoreListDetail, Integer ActInNum){
        Map<String, Object> MapInfo=new HashMap<>();

        String ListNum=exstoreListDetail.getListNum();
        String SupID=exstoreListDetail.getSupId();
        QueryWrapper<ExstoreListDetail> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("List_num", ListNum);
        QueryWrapper.eq("sup_id",SupID);
        List<ExstoreListDetail> EnstoreListFound=exstoreListDetailMapper.selectList(QueryWrapper);
        // Check if system has this entry
        if(EnstoreListFound.size()>1 || EnstoreListFound.size()==0){
            MapInfo.put("HTTPCode",500);
            return MapInfo;
        }

        UpdateWrapper<ExstoreListDetail> UpdateWrapper=new UpdateWrapper<>();
        UpdateWrapper.set("act_in_num",ActInNum);
        UpdateWrapper.set("update_time", LocalDateTime.now());
        UpdateWrapper.eq("List_num",ListNum);
        UpdateWrapper.eq("sup_id",SupID);
        exstoreListDetailMapper.update(null, UpdateWrapper);
        MapInfo.put("HTTPCode",200);

        return MapInfo;
    }
}
