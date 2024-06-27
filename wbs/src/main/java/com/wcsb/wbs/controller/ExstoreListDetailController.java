package com.wcsb.wbs.controller;


import com.wcsb.wbs.domain.EnstoreListDetail;
import com.wcsb.wbs.domain.ExstoreListDetail;
import com.wcsb.wbs.service.ExstoreListDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Jessica_Clorize
 * @since 2024-06-03
 */
@RestController
//@RequestMapping("/exstoreListDetail")
public class ExstoreListDetailController {
    @Autowired
    private ExstoreListDetailService exstoreListDetailService;

    @PostMapping("/account_detail_list")
    public String AccountDetailList(@RequestBody ExstoreListDetailEntry exstoreListDetailEntry){
        // Passes in: (ListNum, Sup_ID) to designate which entry to modify, (Act_Num, Status) being the modification
        // v-for everything in this table (frontend)
        String ListNum=exstoreListDetailEntry.getListNum();
        String SupID=exstoreListDetailEntry.getSupID();
        Integer ActInNum=exstoreListDetailEntry.getActInNum();
        String Status=exstoreListDetailEntry.getStatus();
        Map<String, Object> MapInfo=new HashMap<>();

        System.out.println("[ExstoreListDetail] Updating Detail List With List Number ["+ListNum+"] and Supplement ID ["+SupID+"]!");

        // First Modify Entries In EnstoreListDetail
        ExstoreListDetail exstoreListDetail=new ExstoreListDetail();
        exstoreListDetail.setListNum(ListNum);
        exstoreListDetail.setSupId(SupID);
        MapInfo=exstoreListDetailService.UpdateExstoreListViaSupID(exstoreListDetail,ActInNum);
        int code=new Integer(String.valueOf(MapInfo.get("HTTPCode")));
        if(code==500){
            System.out.println("[ExstoreListDetail] No Such Entry Found via Sup_ID and ListNum!");
            return "Update Failed";
        }
        else{
            System.out.println("[ExstoreListDetail] Entry Updated via ListNum!");
            return "Update Succeeded";
        }

        // Status update is supposed to be done in EnstoreListController, see that as a reference

    }
}

class ExstoreListDetailEntry{
    private String listNum;
    private String supID;
    private Integer actInNum;
    private String status;

    public String getListNum() {return listNum;}
    public String getSupID() {return supID;}
    public Integer getActInNum() {return actInNum;}
    public String getStatus() {return status;}
}