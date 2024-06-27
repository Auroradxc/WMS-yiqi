package com.wcsb.wbs.controller;


import com.wcsb.wbs.domain.*;
import com.wcsb.wbs.service.ApplianceService;
import com.wcsb.wbs.service.DailyListEntryService;
import com.wcsb.wbs.service.ExstoreListDetailService;
import com.wcsb.wbs.service.ExstoreListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
//@RequestMapping("/exstoreList")
public class ExstoreListController {
    @Autowired
    private ExstoreListService exstoreListService;

    @Autowired
    private ExstoreListDetailService exstoreListDetailService;

    @Autowired
    private DailyListEntryService dailyListEntryService;

    @Autowired
    private ApplianceService applianceService;

    @PostMapping("/request_all_exstore_list")
    public List<ExstoreList> requestAllExstoreList(@RequestBody ExstoreList exstoreList){
        String ListNum=exstoreList.getListNum();
        String Status=exstoreList.getListStatus();
        String Customer_ID=exstoreList.getCustomer();
        Map<String,Object> MapInfo=null;

        MapInfo=exstoreListService.GetExstoreList();
        int code=new Integer(String.valueOf(MapInfo.get("HTTPCode")));

        if(code==200){
            // Not Empty
            List<ExstoreList> OriginalExstoreList=(List<ExstoreList>)MapInfo.get("ExstoreList");
            List<ExstoreList> OriginalExstoreListCopy=(List<ExstoreList>)MapInfo.get("ExstoreList");
            List<Integer> ModifiedExstoreList=new ArrayList<Integer>();

            // Check if original is null, which is a case not supposed to happen
            if(OriginalExstoreList==null){
                return null;
            }

            // Set Modify Number as 0
            for(ExstoreList CurrentList:OriginalExstoreList) {
                ModifiedExstoreList.add(0);
            }

            // Filter ListNum
            if(ListNum!=null){
                for(int i=0; i<OriginalExstoreList.size(); i++){
                    if(!OriginalExstoreList.get(i).getListNum().equals(ListNum)) {
                        ModifiedExstoreList.set(i, 1);
                    }
                }
            }

            // Filter Status
            if(Status!=null){
                for(int i=0; i<OriginalExstoreList.size(); i++){
                    if(!OriginalExstoreList.get(i).getListStatus().equals(Status)) {
                        ModifiedExstoreList.set(i, 1);
                    }
                }
            }

            // Filter Customer
            if(Customer_ID!=null){
                // I personally think Customer is supposed to be random
                // (i dont want to have to build one other customer table)
                for(int i=0; i<OriginalExstoreList.size(); i++){
                    if(!OriginalExstoreList.get(i).getCustomer().equals(Customer_ID)) {
                        ModifiedExstoreList.set(i, 1);
                    }
                }
            }

            // Now Remove All Targeted
            OriginalExstoreList=new ArrayList<>();
            for(int i=0; i<ModifiedExstoreList.size(); i++){
                if(ModifiedExstoreList.get(i)==0) {
                    OriginalExstoreList.add(OriginalExstoreListCopy.get(i));
                }
            }

            if(OriginalExstoreList.size()==0){
                return null;
            }
            else{
                return OriginalExstoreList;
            }
        }
        else{
            return null;
        }
    }

    @PostMapping("/create_exstore_list")
    public String createExstoreList(@RequestBody ExstoreListBody exstoreList){
        // Save Button Clicked, Create ExstoreList...
        String ListNum=exstoreList.getListNum();
        String Status=exstoreList.getListStatus();
        String Customer=exstoreList.getCustomer();

        String Creator="Bemoel_Bomber";

        ExstoreList exstoreListMainBody=new ExstoreList();
        exstoreListMainBody.setListNum(ListNum);
        exstoreListMainBody.setListStatus(Status);
        exstoreListMainBody.setCustomer(Customer);
        exstoreListMainBody.setCreator(Creator);
        exstoreListMainBody.setUpdator(Creator);
        exstoreListMainBody.setCreateTime(LocalDateTime.now());
        exstoreListMainBody.setUpdateTime(LocalDateTime.now());

        // Save One Line
        Map<String, Object> MapInfo=exstoreListService.CreateExstoreList(exstoreListMainBody);
        int code=new Integer(String.valueOf(MapInfo.get("HTTPCode")));
        if(code==200){
            System.out.println("[ExstoreList] Exstore List Entry Created! Creating Exstore List Detail For Supplements...");
        }
        else{
            System.out.println("[ExstoreList] Exstore List Creation Failed!");
        }

        // Iterate Through List of Items
        List<SupplementWrapInfoExstore> SupplementInfoList=exstoreList.getWrapInfoDetails();

        int RandomSequence=(int)(Math.random()*100000);
        String DetailListID=LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).concat(String.valueOf(RandomSequence));

        for(SupplementWrapInfoExstore Supplement:SupplementInfoList){
            ExstoreListDetail exstoreListDetail=new ExstoreListDetail();

            exstoreListDetail.setListId(DetailListID);
            exstoreListDetail.setListNum(ListNum);

            exstoreListDetail.setSupId(Supplement.getSup_id());
            exstoreListDetail.setWrapSize(Supplement.getWrap_size());
            exstoreListDetail.setExpInNum(Supplement.getExstore_num());
            exstoreListDetail.setActInNum(null);

            exstoreListDetail.setCreator(Creator);
            exstoreListDetail.setUpdator(Creator);
            exstoreListDetail.setCreateTime(LocalDateTime.now());
            exstoreListDetail.setUpdateTime(LocalDateTime.now());

            System.out.println("[ExstoreListDetail] Inserting New Exstore List Detail For Supplement ["
                    +Supplement.getSup_id()+"]!");

            Map<String, Object> MapInfoDetail=exstoreListDetailService.InsertExstoreListDetail(exstoreListDetail);
            int CodeDetail=new Integer(String.valueOf(MapInfo.get("HTTPCode")));
            if (CodeDetail==200) {
                System.out.println("[ExstoreListDetail] Exstore List Detail Insert Succeeded For Supplement ["
                        +Supplement.getSup_id()+"]!");
            }
        }

        int Year=LocalDateTime.now().getYear();
        int Month=LocalDateTime.now().getMonthValue();
        int Day=LocalDateTime.now().getDayOfMonth();
        Map<String, Object> MapInfoDailyEntry=dailyListEntryService.SetEntryOfDay(Year, Month, Day);
        code=new Integer(String.valueOf(MapInfo.get("HTTPCode")));
        if(code==200){
            System.out.println("[DailyListEntry] DailyListEntry Entry Updated!");
        }

        return "Insert Succeeeded!";
    }

    @PostMapping("/modify_exstore_list/modify_customer")
    public String modifyExstoreListDataCustomer(@RequestBody ExstoreListBody exstoreListBody){
        // Can only modify if status check is not closed
        // Modify Supplier, Which is totally ordinary
        String ListNum=exstoreListBody.getListNum();
        String Customer_ID=exstoreListBody.getCustomer();

        Map<String, Object> MapInfo=new HashMap<>();
        ExstoreList ModifiedList=new ExstoreList();
        ModifiedList.setListNum(ListNum);
        MapInfo=exstoreListService.UpdateExstoreListViaListNum(ModifiedList, Customer_ID);
        int code=new Integer(String.valueOf(MapInfo.get("HTTPCode")));
        if(code==200){
            System.out.println("[ExstoreList] Modify ExstoreList Entry Succeeded!");
        }

        return "Modification Succeeded";
    }

    @PostMapping("/modify_exstore_list/update_supplement")
    public String ModifyEnstoreDataUpdateSupplement(@RequestBody ExstoreListBody exstoreListBody) {
        // Can only modify if status check is not closed {The modification button is only there by v-if status=open, no service provided on backend}
        // Add Supplement to Enstore List
        String listNum = exstoreListBody.getListNum();
        String Status = exstoreListBody.getListStatus();
        String Customer_ID = exstoreListBody.getCustomer();
        List<SupplementWrapInfoExstore> WrapInfoDetails = exstoreListBody.getWrapInfoDetails();

        // Update EnstoreList (I gave you three fucking apis why would you make me combine them!
        // I literally thought the more the merrier!)
        exstoreListService.UpdateExstoreList(listNum, Customer_ID, Status);

        // Get ListID & Creator & CreateTime
        Map<String, Object> MapInfo = exstoreListDetailService.RetrieveExstoreListTotal(listNum);
        String DetailListID = (String) MapInfo.get("List_ID");

        if(DetailListID==null || DetailListID.equals("")){
            int RandomSequence=(int)(Math.random()*100000);
            DetailListID=LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).concat(String.valueOf(RandomSequence));
        }

        String Creator = (String) MapInfo.get("Creator");

        if(Creator==null || Creator.equals("")){
            Creator="Bemoel_Bomber";
        }

        LocalDateTime CreateTime = (LocalDateTime) MapInfo.get("CreateTime");

        if(CreateTime==null || CreateTime.toString().equals("")){
            CreateTime=LocalDateTime.now();
        }

        System.out.println("[ExstoreListDetail] Modifying List Detail Entry With List ID: ["+ DetailListID+"]!");

        String Updator = "Biff_Buzzard";

        // Update EnstoreListDetail for all entries under EnstoreList
        Map<String, Object> MapInfoDetailTotal=exstoreListDetailService.RetrieveExstoreListTotal(listNum);
        List<ExstoreListDetail> ExstoreListPrev=(List<ExstoreListDetail>)MapInfoDetailTotal.get("ExstoreLists");

        // First Delete all correspondent (of list_num) entries in table
        exstoreListDetailService.DeleteExstoreListDetail(listNum);

        for (SupplementWrapInfoExstore supplementWrapInfo : WrapInfoDetails) {
            ExstoreListDetail ExstoreListDetail = new ExstoreListDetail();
            ExstoreListDetail.setListId(DetailListID);
            ExstoreListDetail.setListNum(listNum);
            ExstoreListDetail.setSupId(supplementWrapInfo.getSup_id());
            ExstoreListDetail.setWrapSize(String.valueOf(supplementWrapInfo.getWrap_size()));
            ExstoreListDetail.setExpInNum(supplementWrapInfo.getExstore_num());

            if(ExstoreListPrev.size()>0){
                //Has Value, Try and Find ActInNum
                Integer ActInNum=null;
                for(ExstoreListDetail exstoreListPrev: ExstoreListPrev){
                    if(exstoreListPrev.getListNum().equals(listNum)
                            && exstoreListPrev.getSupId().equals(supplementWrapInfo.getSup_id())){
                        ActInNum=exstoreListPrev.getActInNum();
                        break;
                    }
                }
                ExstoreListDetail.setActInNum(ActInNum);
            }
            else{
                ExstoreListDetail.setActInNum(null);
            }

            ExstoreListDetail.setCreator(Creator);
            ExstoreListDetail.setUpdator(Updator);
            ExstoreListDetail.setCreateTime(CreateTime);
            ExstoreListDetail.setUpdateTime(LocalDateTime.now());
            exstoreListDetailService.InsertExstoreListDetail(ExstoreListDetail);
        }

        return "Update Succeeded";
    }

    @PostMapping("/delete_exstore_list_entry")
    public String DeleteEnstoreListEntry(@RequestBody ExstoreListBody exstoreListBody){
        // Delete from ExstoreList
        String ListNum=exstoreListBody.getListNum();
        Map<String, Object> MapInfo=new HashMap<>();
        MapInfo=exstoreListService.DeleteExstoreList(ListNum);
        int code=new Integer(String.valueOf(MapInfo.get("HTTPCode")));
        if(code==200){
            System.out.println("[ExstoreList] Delete EnstoreList Entry Succeeded!");
        }

        // Delete from EnstoreListDetail
        MapInfo=exstoreListDetailService.DeleteExstoreListDetail(ListNum);
        code=new Integer(String.valueOf(MapInfo.get("HTTPCode")));
        if(code==200){
            System.out.println("[ExstoreListDetail] Delete EnstoreListDetail Entry Succeeded!");
        }

        return "Delete Succeeded";
    }

    @PostMapping("/update_exstorelist_status")
    public String UpdateEnstoreListStatus(@RequestBody ExstoreList exstoreList) {
        // This method requires a calculated status via v-for of table in frontend system {list_num, status}
        String ListNum=exstoreList.getListNum();
        String Status = exstoreList.getListStatus();
        ExstoreList exstoreListPassed=new ExstoreList();
        exstoreListPassed.setListNum(ListNum);

        Map<String, Object> MapInfo=exstoreListService.UpdateEnstoreListStatus(exstoreListPassed,Status);
        int code=new Integer(String.valueOf(MapInfo.get("HTTPCode")));
        if(code==200){
            System.out.println("[ExstoreList] ExstoreList Status Updated!");
        }

        return "Update Succeeded";
    }

    @PostMapping("/request_exstorelist_detail")
    public ExstoreListRequest requestExstoreListDetail(@RequestBody ExstoreList exstoreList) {
        // Pass In List Number
        String ListNum=exstoreList.getListNum();
        String Status=null;
        String Customer=null;
        Integer HTTPCode=200;

        // Returns:
        // {listNum: ... supplier: ... }
        // {List<EnstoreListDetail>[]}

        // Get EnstoreList Corresponding to ListNum
        Map<String, Object> MapInfo=exstoreListService.GetExstoreListByListNum(ListNum);
        int code_supplement=(int)MapInfo.get("HTTPCode");
        ExstoreList SelectedExstoreList;
        if(code_supplement==200){
            List<ExstoreList> ExstoreLists=(List<ExstoreList>)MapInfo.get("ExstoreLists");

            if(ExstoreLists.size()>=1){
                SelectedExstoreList=ExstoreLists.get(0);
                Status=SelectedExstoreList.getListStatus();
                Customer=SelectedExstoreList.getCustomer();
            }
            else{
                SelectedExstoreList=null;
                HTTPCode=500; // Rectifying EnstoreList Retrieved
            }
        }
        else{
            SelectedExstoreList=null;
            HTTPCode=500; // Rectifying EnstoreList Retrieved
        }


        // Get EnstoreListDetail
        List<ExstoreListDetailDerived> ReturnedExstoreListDetail=new ArrayList<ExstoreListDetailDerived>();
        if(HTTPCode==200){
            MapInfo=exstoreListDetailService.RetrieveExstoreListFull(ListNum);
            int code=(int)MapInfo.get("HTTPCode");
            if(code==500){
                ReturnedExstoreListDetail=null;
                System.out.println("[ExstoreListDetail] No Detail of Such List Number ["+ListNum+"] Retrieved!");
            }
            else{
                List<ExstoreListDetail> RetrievedExstoreListDetail=(List<ExstoreListDetail>)MapInfo.get("DetailList");
                for(ExstoreListDetail exstoreListDetail: RetrievedExstoreListDetail){
                    Integer ExpInNum=exstoreListDetail.getExpInNum();
                    Integer ActInNum=exstoreListDetail.getActInNum();
                    System.out.println("[ExstoreListDetail] Judging Status of " +
                            "ExstoreList with Supplement ID ["+exstoreListDetail.getSupId()+"]:");
                    System.out.println("[ExstoreListDetail] ExpInNum: ["+ExpInNum+"]");
                    System.out.println("[ExstoreListDetail] ActInNum: ["+ActInNum+"]");
                    // Act In or Exp In here means Act OUT or EXP Out though the logic remains the same

                    if(ExpInNum==null){
                        ExpInNum=0;
                    }
                    if(ActInNum==null){
                        ActInNum=0;
                    }
                    String Status_Supplement="PENDING";

                    if(ActInNum==0){
                        Status_Supplement="OPEN";
                    }
                    else if(ActInNum>=ExpInNum){
                        Status_Supplement="CLOSED";
                    }
                    else if(ActInNum<ExpInNum){
                        Status_Supplement="PENDING";
                    }

                    // First Perform Hard Check
                    String WrapId=null;
                    Map<String, Object> ApplianceResultBetter=applianceService.GetApplianceBySupplementIDAndWrapSize(
                            exstoreListDetail.getSupId(), Integer.valueOf(exstoreListDetail.getWrapSize()));
                    int CodeApplianceBetter=(int)ApplianceResultBetter.get("HTTPCode");
                    if(CodeApplianceBetter==200){
                        WrapId=((Appliance)ApplianceResultBetter.get("Appliance")).getWrapId();
                    }
                    else{
                        // Loosen Constraints
                        // Supposedly Wrap(Appliance) & Supplement is one to one
                        System.out.println("[ExstoreList&Appliance Temp] Appliance Searching With " +
                                "Supplement ID ["+exstoreListDetail.getSupId()+"]");
                        Map<String, Object> ApplianceResult=applianceService.GetApplianceBySupID(exstoreListDetail.getSupId());
                        int CodeAppliance=(int)ApplianceResult.get("HTTPCode");

                        if(CodeAppliance==500){
                            WrapId=null;
                            System.out.println("[ExstoreList&Appliance Temp] No Appliance Found For " +
                                    "Supplement ID ["+exstoreListDetail.getSupId()+"]");
                        }
                        else{
                            WrapId=(String)ApplianceResult.get("ApplianceID");
                        }
                    }

                    Integer Boxes=0;
                    if(Integer.valueOf(exstoreListDetail.getWrapSize())==0){
                        Boxes=0;
                    }
                    else{
                        int CurExpInNum=exstoreListDetail.getExpInNum();
                        int CurWrapSize=Integer.valueOf(exstoreListDetail.getWrapSize());
                        if(CurExpInNum%CurWrapSize==0){
                            Boxes=CurExpInNum/CurWrapSize;
                        }
                        else{
                            Boxes=CurExpInNum/CurWrapSize+1;
                        }
                    }

                    ReturnedExstoreListDetail.add(
                            new ExstoreListDetailDerived()
                                    .setSupId(exstoreListDetail.getSupId())
                                    .setExpInNum(exstoreListDetail.getExpInNum())
                                    .setActInNum(exstoreListDetail.getActInNum())
                                    .setBoxes(Boxes)
                                    .setWrapSize(Integer.valueOf(exstoreListDetail.getWrapSize()))
                                    .setWrapId(WrapId)
                                    .setStatus(Status_Supplement)
                    );
                }
            }
        }

        ExstoreListRequest ReturnedExstoreListRequest=new ExstoreListRequest();
        ReturnedExstoreListRequest.setListNum(ListNum);
        ReturnedExstoreListRequest.setListStatus(Status);
        ReturnedExstoreListRequest.setCustomer(Customer);
        ReturnedExstoreListRequest.setHTTPCode(HTTPCode);
        ReturnedExstoreListRequest.setWrapInfoDetails(ReturnedExstoreListDetail);

        return ReturnedExstoreListRequest;
    }
}

class ExstoreListBody{
    private String listNum; //入库单号
    private String listStatus; //入库状态（不知道有没有用）
    private String customer; //供应商
    private List<SupplementWrapInfoExstore> wrapInfoDetails;

    public String getListNum() {return listNum;}
    public String getListStatus() {return listStatus;}
    public String getCustomer() {return customer;}
    public List<SupplementWrapInfoExstore> getWrapInfoDetails() {return wrapInfoDetails;}
}

class SupplementWrapInfoExstore{
    private String sup_id;
    private int exstore_num;
    private String wrap_id;
    private String wrap_size;
    private int boxes;
    //private String available;

    public String getSup_id() {return sup_id;}
    public int getExstore_num() {return exstore_num;}
    public String getWrap_id() {return wrap_id;}
    public String getWrap_size() {return wrap_size;}
    public int getBoxes() {return boxes;}
    //public String getAvailable() {return available;}
}

class ExstoreListDetailDerived{
    private String supId;
    private Integer expInNum;
    private Integer actInNum;
    private Integer boxes;
    private String wrapId;
    private Integer wrapSize;

    private String status;

    public String getSupId() {return supId;}
    public Integer getExpInNum() {return expInNum;}
    public Integer getActInNum() {return actInNum;}
    public String getStatus() {return status;}

    public Integer getWrapSize() {return wrapSize;}
    public Integer getBoxes() {return boxes;}
    public String getWrapId() {return wrapId;}

    public ExstoreListDetailDerived setSupId(String supId) {
        this.supId=supId;
        return this;
    }
    public ExstoreListDetailDerived setExpInNum(Integer expInNum) {
        this.expInNum=expInNum;
        return this;
    }
    public ExstoreListDetailDerived setActInNum(Integer actInNum) {
        this.actInNum=actInNum;
        return this;
    }
    public ExstoreListDetailDerived setBoxes(Integer boxes) {
        this.boxes=boxes;
        return this;
    }
    public ExstoreListDetailDerived setWrapId(String wrapId) {
        this.wrapId=wrapId;
        return this;
    }
    public ExstoreListDetailDerived setWrapSize(Integer wrapSize) {
        this.wrapSize=wrapSize;
        return this;
    }
    public ExstoreListDetailDerived setStatus(String status) {
        this.status=status;
        return this;
    }
}

class ExstoreListRequest{
    private String ListNum;
    private String ListStatus;
    private String Customer;
    private Integer HTTPCode;

    private List<ExstoreListDetailDerived> WrapInfoDetails;

    public String getListNum() {return ListNum;}
    public String getListStatus() {return ListStatus;}
    public String getCustomer() {return Customer;}
    public Integer getHTTPCode(){return HTTPCode;}
    public List<ExstoreListDetailDerived> getWrapInfoDetails() {return WrapInfoDetails;}

    public void setListNum(String ListNum) {this.ListNum = ListNum;}
    public void setListStatus(String ListStatus) {this.ListStatus = ListStatus;}
    public void setCustomer(String Customer) {this.Customer = Customer;}
    public void setHTTPCode(Integer HTTPCode) {this.HTTPCode = HTTPCode;}
    public void setWrapInfoDetails(List<ExstoreListDetailDerived> WrapInfoDetails) {
        this.WrapInfoDetails = WrapInfoDetails;
    }
}