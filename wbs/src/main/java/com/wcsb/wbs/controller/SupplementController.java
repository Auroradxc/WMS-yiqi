package com.wcsb.wbs.controller;


import com.wcsb.wbs.domain.Supplement;
import com.wcsb.wbs.domain.Supplier;
import com.wcsb.wbs.service.SupplementService;
import com.wcsb.wbs.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
//@RequestMapping("/supplement")
public class SupplementController {
    @Autowired
    private SupplementService supplementService;

    @PostMapping("/request_supplement_list")
    public List<Supplement> ReturnSupplementList(@RequestBody Supplement data){
        // This method only returns the supplement (item) list, no parameters are originally required
        // Request Body: {sup_id(optional), supplier_id(optional)}
        String sup_id=data.getSupId();
        String supplier_id=data.getSupplier();
        Map<String,Object> MapInfo=null;
        // Notice how in database supplement is already stored with column of supplier_id :(

        if(sup_id!=null && supplier_id!=null){
            MapInfo=supplementService.GetSupplementByIDAndSupplierID(sup_id,supplier_id);
        }
        else if(sup_id!=null && supplier_id==null){
            MapInfo=supplementService.GetSupplementByID(sup_id);
        }
        else if(sup_id==null && supplier_id!=null){
            MapInfo=supplementService.GetSupplementBySupplierID(supplier_id);
        }
        else if(sup_id==null && supplier_id==null){
            MapInfo=supplementService.GetSupplementList();
        }

        int code=new Integer(String.valueOf(MapInfo.get("HTTPCode")));
        if(code==200){
            List<Supplement> supplements=(List<Supplement>)MapInfo.get("Supplements");
            return supplements;
        }
        else{
            // Something went wrong
            return null;
        }
    }

    // [To Be Added: Insert & Delete]
    @PostMapping("/add_supplement")
    public String AddSupplement(@RequestBody Supplement supplement){
        // Sup ID, Sup Name, Supplier Should be Given
        String Creator="Magic Stone Green";
        supplement.setCreator(Creator);
        supplement.setUpdator(Creator);
        supplement.setCreateTime(LocalDateTime.now());
        supplement.setUpdateTime(LocalDateTime.now());

        Map<String,Object> MapInfo=supplementService.InsertSupplement(supplement);
        int code=(int)MapInfo.get("HTTPCode");
        if(code==200){
            return "Insert Succeeded!";
        }
        else{
            return "Insert Failed!";
        }
    }

    @PostMapping("/delete_supplement")
    public String DeleteSupplement(@RequestBody Supplement supplement){
        Map<String, Object> MapInfo=supplementService.DeleteSupplement(supplement);
        int code=(int)MapInfo.get("HTTPCode");
        if(code==200){
            return "Delete Succeeded!";
        }
        else{
            return "Delete Failed!";
        }
    }
}

