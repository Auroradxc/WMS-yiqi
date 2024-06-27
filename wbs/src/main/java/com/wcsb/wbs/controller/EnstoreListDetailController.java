package com.wcsb.wbs.controller;


import com.wcsb.wbs.domain.EnstoreList;
import com.wcsb.wbs.domain.EnstoreListDetail;
import com.wcsb.wbs.service.EnstoreListDetailService;
import com.wcsb.wbs.service.EnstoreListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
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
//@RequestMapping("/enstoreListDetail")
public class EnstoreListDetailController {

    @Autowired
    private EnstoreListService enstoreListService;

    @Autowired
    private EnstoreListDetailService enstoreListDetailService;

    @PostMapping("/request_all_detail_list")
    public List<EnstoreListDetail> RequestAllDetailList(@RequestBody EnstoreList enstoreList){
        // The frontend has to display two things of a table
        // (ListNum -> List<EnstoreListDetail>) which the frontend can derive from keys "sup_id" and "exp_num"
        String ListNum=enstoreList.getListNum();
        Map<String, Object> MapInfo=enstoreListDetailService.RetrieveEnstoreListTotal(ListNum);
        System.out.println("[EnstoreListDetail] Requesting All Appliance Of List With ID ["+ListNum+"]!");
        List<EnstoreListDetail> EnstoreListDetailList=(List<EnstoreListDetail>)MapInfo.get("EnstoreLists");
        return EnstoreListDetailList;
    }

    @PostMapping("/update_detail_list")
    public String UpdateDetailList(@RequestBody EnstoreListDetailEntry enstoreListDetailEntry){
        // Passes in: (ListNum, Sup_ID) to designate which entry to modify, (Act_Num, Status) being the modification
        // v-for everything in this table (frontend)
        String ListNum=enstoreListDetailEntry.getListNum();
        String SupID=enstoreListDetailEntry.getSupID();
        Integer ActInNum=enstoreListDetailEntry.getActInNum();
        String Status=enstoreListDetailEntry.getStatus();
        Map<String, Object> MapInfo=new HashMap<>();

        System.out.println("[EnstoreListDetail] Updating Detail List With List Number ["+ListNum+"] and Supplement ID ["+SupID+"]!");

        // First Modify Entries In EnstoreListDetail
        EnstoreListDetail enstoreListDetail=new EnstoreListDetail();
        enstoreListDetail.setListNum(ListNum);
        enstoreListDetail.setSupId(SupID);
        MapInfo=enstoreListDetailService.UpdateEnstoreListViaSupID(enstoreListDetail,ActInNum);
        int code=new Integer(String.valueOf(MapInfo.get("HTTPCode")));
        if(code==500){
            System.out.println("[EnstoreListDetail] No Such Entry Found via Sup_ID and ListNum!");
            return "Update Failed";
        }
        else{
            System.out.println("[EnstoreListDetail] Entry Updated via ListNum!");
            return "Update Succeeded";
        }

        // Status update is supposed to be done in EnstoreListController, see that as a reference

    }

    //@PostMapping("/delete_enstorelist_detail_entry")
    //public String DeleteDetailList(@RequestBody EnstoreListDetailEntry enstoreListDetailEntry){

    //}

}

class EnstoreListDetailEntry{
    private String listNum;
    private String supID;
    private Integer actInNum;
    private String status;

    public String getListNum() {return listNum;}
    public String getSupID() {return supID;}
    public Integer getActInNum() {return actInNum;}
    public String getStatus() {return status;}
}