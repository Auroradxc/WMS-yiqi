package com.wcsb.wbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wcsb.wbs.domain.EnstoreList;
import com.wcsb.wbs.domain.EnstoreListDetail;
import com.wcsb.wbs.domain.Supplement;
import com.wcsb.wbs.mapper.EnstoreListDetailMapper;
import com.wcsb.wbs.service.EnstoreListDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
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
public class EnstoreListDetailServiceImpl extends ServiceImpl<EnstoreListDetailMapper, EnstoreListDetail> implements EnstoreListDetailService {

    @Autowired
    private EnstoreListDetailMapper enstoreListDetailMapper;

    public Map<String, Object> InsertNewEnstoreListDetail(EnstoreListDetail enstoreListDetail){
        enstoreListDetailMapper.insert(enstoreListDetail);
        Map<String, Object> MapInfo=new HashMap<>();
        MapInfo.put("HTTPCode",200);
        return MapInfo;
    }

    public Map<String, Object> DeleteEnstoreListDetail(String ListNum){
        QueryWrapper<EnstoreListDetail> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("List_num", ListNum);
        Map<String, Object> MapInfo=new HashMap<>();
        MapInfo.put("List_Num",ListNum);
        enstoreListDetailMapper.deleteByMap(MapInfo);
        MapInfo=new HashMap<>();
        MapInfo.put("HTTPCode",200);
        return MapInfo;
    }

    public Map<String, Object> RetrieveEnstoreListDetail(String ListNum){
        QueryWrapper<EnstoreListDetail> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("List_num", ListNum);

        List<EnstoreListDetail> EnstoreSupplementList=enstoreListDetailMapper.selectList(QueryWrapper);

        String ListID=null;
        String Creator=null;
        String Updator=null;
        LocalDateTime CreateTime=null;

        if(EnstoreSupplementList.size()>0){
            ListID=EnstoreSupplementList.get(0).getListId();
            Creator=EnstoreSupplementList.get(0).getCreator();
            Updator=EnstoreSupplementList.get(0).getUpdator();
            CreateTime=EnstoreSupplementList.get(0).getCreateTime();
        }
        else{
            ListID=null;
            Creator=null;
            Updator=null;
            CreateTime=null;
        }


        Map<String, Object> MapInfo=new HashMap<>();
        MapInfo.put("HTTPCode",200);
        MapInfo.put("List_ID",ListID);
        MapInfo.put("Creator",Creator);
        MapInfo.put("Updator",Updator);
        MapInfo.put("CreateTime",CreateTime);
        return MapInfo;
    }

    public Map<String, Object> RetrieveEnstoreListDetailOfSupplement(String ListNum, String SupID){
        QueryWrapper<EnstoreListDetail> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("sup_id", SupID);
        QueryWrapper.eq("List_num", ListNum);
        List<EnstoreListDetail> EnstoreSupplementList=enstoreListDetailMapper.selectList(QueryWrapper);

        Map<String, Object> MapInfo=new HashMap<>();
        if(EnstoreSupplementList.size()>1 || EnstoreSupplementList.size()==0){
            MapInfo.put("HTTPCode",500);
        }
        else{
            MapInfo.put("HTTPCode",200);
            MapInfo.put("EnstoreListDetailRow",EnstoreSupplementList.get(0));
        }
        return MapInfo;
    }

    public Map<String, Object> RetrieveEnstoreListFull(String ListNum){
        QueryWrapper<EnstoreListDetail> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("List_num", ListNum);

        List<EnstoreListDetail> EnstoreSupplementList=enstoreListDetailMapper.selectList(QueryWrapper);

        Map<String, Object> MapInfo=new HashMap<>();
        //System.out.println(EnstoreSupplementList.get(0).getListNum());
        if(EnstoreSupplementList.size()>0){
            MapInfo.put("HTTPCode",200);
        }
        else{
            MapInfo.put("HTTPCode",500);
        }

        MapInfo.put("DetailList",EnstoreSupplementList);
        return MapInfo;
    }

    public Map<String, Object> RetrieveEnstoreListTotal(String ListNum){
        QueryWrapper<EnstoreListDetail> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("List_num", ListNum);
        List<EnstoreListDetail> EnstoreLists=enstoreListDetailMapper.selectList(QueryWrapper);
        Map<String, Object> MapInfo=new HashMap<>();
        MapInfo.put("HTTPCode",200);
        MapInfo.put("List_Num",ListNum);
        MapInfo.put("EnstoreLists",EnstoreLists);
        return MapInfo;
    }

    public Map<String, Object> UpdateEnstoreListViaSupID(EnstoreListDetail enstoreListDetail, Integer ActInNum){
        Map<String, Object> MapInfo=new HashMap<>();

        String ListNum=enstoreListDetail.getListNum();
        String SupID=enstoreListDetail.getSupId();
        QueryWrapper<EnstoreListDetail> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("List_num", ListNum);
        QueryWrapper.eq("sup_id",SupID);
        List<EnstoreListDetail> EnstoreListFound=enstoreListDetailMapper.selectList(QueryWrapper);
        // Check if system has this entry
        if(EnstoreListFound.size()>1 || EnstoreListFound.size()==0){
            MapInfo.put("HTTPCode",500);
            return MapInfo;
        }

        UpdateWrapper<EnstoreListDetail> UpdateWrapper=new UpdateWrapper<>();
        UpdateWrapper.set("act_in_num",ActInNum);
        UpdateWrapper.set("update_time", LocalDateTime.now());
        UpdateWrapper.eq("List_num",ListNum);
        UpdateWrapper.eq("sup_id",SupID);
        enstoreListDetailMapper.update(null, UpdateWrapper);
        MapInfo.put("HTTPCode",200);

        return MapInfo;
    }
}
