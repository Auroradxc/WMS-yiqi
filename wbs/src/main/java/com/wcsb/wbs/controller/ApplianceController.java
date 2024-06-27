package com.wcsb.wbs.controller;


import com.wcsb.wbs.domain.Appliance;
import com.wcsb.wbs.service.ApplianceService;
import com.wcsb.wbs.service.EnstoreListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 器具表 前端控制器
 * </p>
 *
 * @author Jessica_Clorize
 * @since 2024-06-03
 */
@RestController
//@RequestMapping("/appliance")
public class ApplianceController {
    @Autowired
    private ApplianceService applianceService;

    @PostMapping("/request_all_appliances")
    public List<Appliance> ReturnAllAppliances() {
        // Returns all appliances
        Map<String, Object> MapInfo=applianceService.GetAllAppliances();
        int code=new Integer(String.valueOf(MapInfo.get("HTTPCode")));
        if(code==200){
            System.out.println("[Appliance] Appliances Found, Passing to FrontEnd...");
            List<Appliance> Appliances=(List<Appliance>)MapInfo.get("ApplianceList");
            return Appliances;
        }
        else{
            System.out.println("[Appliance] No Appliances Found!");
            return null;
        }
    }

    @PostMapping("/request_specific_appliance")
    public Appliance ReturnSpecificAppliance(@RequestBody Map<String, String> ApplianceMap){
        // Returns correspondent appliances to
        String ApplianceID=ApplianceMap.get("app_id");
        System.out.println("[Appliance] Requesting Appliance with ID: "+ApplianceID);

        Map<String, Object> MapInfo=applianceService.GetApplianceByID(ApplianceID);
        int code=new Integer(String.valueOf(MapInfo.get("HTTPCode")));
        if(code==200){
            System.out.println("[Appliance] Appliance Entry Found, Passing to FrontEnd...");
            List<Appliance> Appliances=(List<Appliance>)MapInfo.get("ApplianceList");
            if(Appliances.size()>1){
                // Something must have went wrong if this happens, there shouldn't be 2 entries with the same wrap_id in one table
                return null;
            }
            else{
                return Appliances.get(0);
            }
        }
        else{
            return null;
        }
    }

    // [To Be Added: Insert [This Is Insertion] / Delete]
    @PostMapping("/add_appliance")
    public String AddAppliance(@RequestBody ApplianceCompulsoryCheck applianceCompulsoryCheck){
        // Supposedly Defined Are: (SupID), (Supplier), (WrapID), (WrapSize)
        String Creator="Crimson Creator";
        Appliance appliance=applianceCompulsoryCheck.getNewAppliance();
        System.out.println(appliance.getWrapSize());
        appliance.setCreator(Creator);
        appliance.setUpdator(Creator);
        appliance.setCreateTime(LocalDateTime.now());
        appliance.setUpdateTime(LocalDateTime.now());

        // Should Forbid Appliance 1-1 Relation To Be Broken
        Map<String, Object> MapInfoApp=applianceService.GetApplianceBySupID(appliance.getSupId());
        int codeApp=new Integer(String.valueOf(MapInfoApp.get("HTTPCode")));
        if(codeApp==200 && applianceCompulsoryCheck.isCompulsoryKey()){
            // This Means System Has Compulsory Check, And Same SupID Wrap Exists
            return "Update Failed: Repetitive Entry!";
        }

        System.out.println("[Appliance] Adding New Appliance...");
        Map<String, Object> MapInfo=applianceService.InsertAppliance(appliance);
        int code=new Integer(String.valueOf(MapInfo.get("HTTPCode")));
        if(code==200){
            return "Update Succeeded!";
        }
        else{
            return "Update Failed!";
        }
    }

    @PostMapping("/delete_appliance")
    public String DeleteAppliance(@RequestBody Appliance appliance){
        String WrapID=appliance.getWrapId();
        System.out.println("[Appliance] Deleting Appliance with ID: "+WrapID);
        applianceService.DeleteAppliance(WrapID);
        return "Delete Succeeded!";
    }
}

class ApplianceCompulsoryCheck{
    private Appliance newAppliance;
    private boolean compulsoryKey;
    public Appliance getNewAppliance(){
        return newAppliance;
    }
    public boolean isCompulsoryKey(){
        return compulsoryKey;
    }
}