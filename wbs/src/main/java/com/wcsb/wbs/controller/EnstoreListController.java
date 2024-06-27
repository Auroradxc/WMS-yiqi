package com.wcsb.wbs.controller;


import com.wcsb.wbs.domain.*;
import com.wcsb.wbs.service.*;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.*;

import javax.xml.soap.Detail;
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
//@RequestMapping("/enstoreList")
public class EnstoreListController {
    @Autowired
    private EnstoreListService enstoreListService;

    @Autowired
    private EnstoreListDetailService enstoreListDetailService;

    @Autowired
    private ApplianceService applianceService;

    @Autowired
    private DailyListEntryService dailyListEntryService;

    @PostMapping("/request_all_enstore_list")
    public List<EnstoreList> ReturnAllEnstoreList(@RequestBody EnstoreList enstoreList){
        // Searched by: {Status: {}, ListNum: {订单号}, Supplier(ID): {}} Ignore "Order Num"
        String ListNum=enstoreList.getListNum();
        String Status=enstoreList.getListStatus();
        String Supplier_ID=enstoreList.getSupplier();
        Map<String,Object> MapInfo=null;

        if(ListNum!=null && Status!=null){
            MapInfo=enstoreListService.GetEnstoreListByStatusAndListnum(Status,ListNum);
        }
        else if(ListNum==null && Status!=null){
            MapInfo=enstoreListService.GetEnstoreListByStatus(Status);
        }
        else if(ListNum!=null && Status==null){
            MapInfo=enstoreListService.GetEnstoreListByListNum(ListNum);
        }
        else if(ListNum==null && Status==null){
            MapInfo=enstoreListService.GetEnstoreList();
        }

        int code=new Integer(String.valueOf(MapInfo.get("HTTPCode")));
        if(code==200){
            List<EnstoreList> enstoreLists=(List<EnstoreList>)MapInfo.get("EnstoreLists");
            // Additional Logic of Supplier Because I didn't know there would be supplier column in table
            if(Supplier_ID!=null){
                for(EnstoreList enstorelist : enstoreLists){
                    if(!enstorelist.getSupplier().equals(Supplier_ID)){
                        enstoreLists.remove(enstorelist);}
                }
            }
            return enstoreLists;
        }
        else{
            // Something went wrong
            return null;
        }
    }

    @PostMapping("/create_enstore_list/chosen_wrap")
    public int GetWrapSize(@RequestBody SupplementWrapInfo supplementWrapInfo){
        // Called when user chosen wrap size for certain column
        String WrapID=supplementWrapInfo.getWrap_id();
        Map<String,Object> MapInfo=null;
        MapInfo=applianceService.GetApplianceByID(WrapID);
        int code=new Integer(String.valueOf(MapInfo.get("HTTPCode")));

        if(code==200){
            List<Appliance> Appliances=(List<Appliance>)MapInfo.get("ApplianceList");
            if(Appliances.size()>1){
                // Something must have went wrong if this happens, there shouldn't be 2 entries with the same wrap_id in one table
                return 0;
            }
            else{
                return new Integer(String.valueOf(Appliances.get(0).getWrapSize()));
            }
        }
        else{
            return 0;
        }
    }

    @PostMapping("/create_enstore_list/fill_appliance")
    public int CalculateBoxes(@RequestBody SupplementWrapInfo supplementWrapInfo){
        // Called when "chosen_wrap" is done
        int EnstoreNum=supplementWrapInfo.getEnstore_num();
        int WrapSize=supplementWrapInfo.getWrap_size();
        return (int) Math.ceil(EnstoreNum*1.0/(WrapSize*1.0));
    }

    @PostMapping("/create_enstore_list")
    public String CreateEnstoreList(@RequestBody EnstoreListBody enstoreListBody){
        // This is when the table should be filled
        // One table here is correspondent to a whole lot of records, featuring:
        // EnstoreList -> One Record
        // EnstoreListDetail -> A bunch of records, responsible for the supplements
        String listNum=enstoreListBody.getListNum();
        String Status=enstoreListBody.getListStatus();
        String Supplier_ID=enstoreListBody.getSupplier();
        List<SupplementWrapInfo> WrapInfoDetails=enstoreListBody.getWrapInfoDetails();
        System.out.println("[EnstoreList] Creating Enstore List For ["+listNum+"] With Status ["+Status+"]!");
        System.out.println("[EnstoreList] Supplement Number: "+WrapInfoDetails.size());

        // Write ((id), listnum, Status, Supplier_ID, creator, updator, create_time, update_time) in EnstoreList

        int ID=enstoreListService.GetEnstoreListSize()+1;
        String Creator="Jessica_Clorize";
        LocalDateTime CreateTime=LocalDateTime.now();

        EnstoreList enstoreList=new EnstoreList();
        enstoreList.setId(ID);
        enstoreList.setListNum(listNum);
        enstoreList.setListStatus(Status);
        enstoreList.setSupplier(Supplier_ID);
        enstoreList.setCreator(Creator);
        enstoreList.setUpdator(Creator);
        enstoreList.setCreateTime(CreateTime);
        enstoreList.setUpdateTime(CreateTime);

        Map<String, Object> MapInfo=enstoreListService.InsertNewEnstoreList(enstoreList);
        int code=new Integer(String.valueOf(MapInfo.get("HTTPCode")));

        if(code==200){
            System.out.println("[EnstoreList] Insert EnstoreList Entry Succeeded!");
        }

        // Write Each (*random_id fixed, *listnum fixed,
        //              sup_id from SupplementWrapInfo, wrap_size from SupplementWrapInfo,
        //              expected_number from SupplementWrapInfo-enstore_num, actual_number = 0,
        //              creator, updator, create_time, update_time) in EnstoreListDetail


        int RandomSequence=(int)(Math.random()*100000);
        String DetailListID=LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).concat(String.valueOf(RandomSequence));
        for(SupplementWrapInfo supplementWrapInfo : WrapInfoDetails){

            EnstoreListDetail EnstoreListDetail=new EnstoreListDetail();
            EnstoreListDetail.setListId(DetailListID);
            EnstoreListDetail.setListNum(listNum);
            EnstoreListDetail.setSupId(supplementWrapInfo.getSup_id());
            EnstoreListDetail.setWrapSize(String.valueOf(supplementWrapInfo.getWrap_size()));
            EnstoreListDetail.setExpInNum(supplementWrapInfo.getEnstore_num());
            EnstoreListDetail.setActInNum(null);
            EnstoreListDetail.setCreator(Creator);
            EnstoreListDetail.setUpdator(Creator);
            EnstoreListDetail.setCreateTime(CreateTime);
            EnstoreListDetail.setUpdateTime(CreateTime);

            MapInfo=enstoreListDetailService.InsertNewEnstoreListDetail(EnstoreListDetail);
            code=new Integer(String.valueOf(MapInfo.get("HTTPCode")));

            if(code==200){
                System.out.println("[EnstoreListDetail] Insert EnstoreListDetail Entry Succeeded!");
            }

        }

        // Enstore List Created, Add Enstore List Entry Number
        int Year=CreateTime.getYear();
        int Month=CreateTime.getMonthValue();
        int Day=CreateTime.getDayOfMonth();
        Map<String, Object> MapInfoDailyEntry=dailyListEntryService.SetEntryOfDay(Year, Month, Day);
        code=new Integer(String.valueOf(MapInfo.get("HTTPCode")));
        if(code==200){
            System.out.println("[DailyListEntry] DailyListEntry Entry Updated!");
        }


        return "Insert Succeeded";
    }

    @PostMapping("/delete_enstore_list_entry")
    public String DeleteEnstoreListEntry(@RequestBody EnstoreListBody enstoreListBody){
        // Delete from EnstoreList
        String ListNum=enstoreListBody.getListNum();
        Map<String, Object> MapInfo=new HashMap<>();
        MapInfo=enstoreListService.DeleteEnstoreList(ListNum);
        int code=new Integer(String.valueOf(MapInfo.get("HTTPCode")));
        if(code==200){
            System.out.println("[EnstoreList] Delete EnstoreList Entry Succeeded!");
        }

        // Delete from EnstoreListDetail
        MapInfo=enstoreListDetailService.DeleteEnstoreListDetail(ListNum);
        code=new Integer(String.valueOf(MapInfo.get("HTTPCode")));
        if(code==200){
            System.out.println("[EnstoreListDetail] Delete EnstoreListDetail Entry Succeeded!");
        }

        return "Delete Succeeded";
    }

    @PostMapping("/modify_enstore_list/modify_supplier")
    public String ModifyEnstoreDataSupplier(@RequestBody EnstoreListBody enstoreListBody){
        // Can only modify if status check is not closed
        // Modify Supplier, Which is totally ordinary
        String ListNum=enstoreListBody.getListNum();
        String Supplier_ID=enstoreListBody.getSupplier();
        Map<String, Object> MapInfo=new HashMap<>();
        EnstoreList ModifiedList=new EnstoreList();
        ModifiedList.setListNum(ListNum);
        MapInfo=enstoreListService.UpdateEnstoreListViaListNum(ModifiedList,Supplier_ID);
        int code=new Integer(String.valueOf(MapInfo.get("HTTPCode")));
        if(code==200){
            System.out.println("[EnstoreList] Modify EnstoreList Entry Succeeded!");
        }

        return "Modification Succeeded";
    }

    @PostMapping("/modify_enstore_list/update_supplement")
    public String ModifyEnstoreDataUpdateSupplement(@RequestBody EnstoreListBody enstoreListBody) {
        // Can only modify if status check is not closed {The modification button is only there by v-if status=open, no service provided on backend}
        // Add Supplement to Enstore List
        String listNum = enstoreListBody.getListNum();
        String Status = enstoreListBody.getListStatus();
        String Supplier_ID = enstoreListBody.getSupplier();
        List<SupplementWrapInfo> WrapInfoDetails = enstoreListBody.getWrapInfoDetails();

        // Update EnstoreList (I gave you three fucking apis why would you make me combine them!
        // I literally thought the more the merrier!)
        enstoreListService.UpdateEnstoreList(listNum, Supplier_ID, Status);

        // Get ListID & Creator & CreateTime
        Map<String, Object> MapInfo = enstoreListDetailService.RetrieveEnstoreListDetail(listNum);

        String DetailListID = (String) MapInfo.get("List_ID");

        if(DetailListID==null || DetailListID.equals("")){
            int RandomSequence=(int)(Math.random()*100000);
            DetailListID=LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).concat(String.valueOf(RandomSequence));
        }

        String Creator = (String) MapInfo.get("Creator");

        if(Creator==null || Creator.equals("")){
            Creator="Jessica_Clorize";
        }

        LocalDateTime CreateTime = (LocalDateTime) MapInfo.get("CreateTime");

        if(CreateTime==null || CreateTime.toString().equals("")){
            CreateTime=LocalDateTime.now();
        }

        System.out.println("[EnstoreListDetail] Modifying List Detail Entry With List ID: ["+ DetailListID+"]!");

        String Updator = "Biff_Buzzard";

        // Update EnstoreListDetail for all entries under EnstoreList
        Map<String, Object> MapInfoDetailTotal=enstoreListDetailService.RetrieveEnstoreListTotal(listNum);
        List<EnstoreListDetail> EnstoreListPrev=(List<EnstoreListDetail>)MapInfoDetailTotal.get("EnstoreLists");

        // First Delete all correspondent (of list_num) entries in table
        enstoreListDetailService.DeleteEnstoreListDetail(listNum);

        for (SupplementWrapInfo supplementWrapInfo : WrapInfoDetails) {
            EnstoreListDetail EnstoreListDetail = new EnstoreListDetail();
            EnstoreListDetail.setListId(DetailListID);
            EnstoreListDetail.setListNum(listNum);
            EnstoreListDetail.setSupId(supplementWrapInfo.getSup_id());
            EnstoreListDetail.setWrapSize((new Integer(supplementWrapInfo.getWrap_size())).toString());
            System.out.println(EnstoreListDetail.getWrapSize());
            EnstoreListDetail.setExpInNum(supplementWrapInfo.getEnstore_num());

            if(EnstoreListPrev.size()>0){
                //Has Value, Try and Find ActInNum
                Integer ActInNum=null;
                for(EnstoreListDetail enstoreListPrev: EnstoreListPrev){
                    if(enstoreListPrev.getListNum().equals(listNum)
                            && enstoreListPrev.getSupId().equals(supplementWrapInfo.getSup_id())){
                        ActInNum=enstoreListPrev.getActInNum();
                        break;
                    }
                }
                EnstoreListDetail.setActInNum(ActInNum);
            }
            else{
                EnstoreListDetail.setActInNum(null);
            }

            EnstoreListDetail.setCreator(Creator);
            EnstoreListDetail.setUpdator(Updator);
            EnstoreListDetail.setCreateTime(CreateTime);
            EnstoreListDetail.setUpdateTime(LocalDateTime.now());
            enstoreListDetailService.InsertNewEnstoreListDetail(EnstoreListDetail);
        }

        return "Update Succeeded";
    }

    @PostMapping("/update_enstorelist_status")
    public String UpdateEnstoreListStatus(@RequestBody EnstoreList enstoreList) {
        // This method requires a calculated status via v-for of table in frontend system {list_num, status}
        String ListNum=enstoreList.getListNum();
        String Status = enstoreList.getListStatus();
        EnstoreList enstoreListPassed=new EnstoreList();
        enstoreListPassed.setListNum(ListNum);

        Map<String, Object> MapInfo=enstoreListService.UpdateEnstoreListStatus(enstoreListPassed,Status);
        int code=new Integer(String.valueOf(MapInfo.get("HTTPCode")));
        if(code==200){
            System.out.println("[EnstoreList] EnstoreList Status Updated!");
        }

        return "Update Succeeded";
    }

    @PostMapping("/request_enstorelist_detail")
    public EnstoreListRequest requestEnstoreListDetail(@RequestBody EnstoreList enstoreList) {
        // Pass In List Number
        String ListNum=enstoreList.getListNum();
        String Status=null;
        String Supplier=null;
        Integer HTTPCode=200;

        // Returns:
        // {listNum: ... supplier: ... }
        // {List<EnstoreListDetail>[]}

        // Get EnstoreList Corresponding to ListNum
        Map<String, Object> MapInfo=enstoreListService.GetEnstoreListByListNum(ListNum);
        int code_supplement=(int)MapInfo.get("HTTPCode");
        EnstoreList SelectedEnstoreList;
        if(code_supplement==200){
            List<EnstoreList> EnstoreLists=(List<EnstoreList>)MapInfo.get("EnstoreLists");

            if(EnstoreLists.size()>=1){
                SelectedEnstoreList=EnstoreLists.get(0);
                Status=SelectedEnstoreList.getListStatus();
                Supplier=SelectedEnstoreList.getSupplier();
            }
            else{
                SelectedEnstoreList=null;
                HTTPCode=500; // Rectifying EnstoreList Retrieved
            }
        }
        else{
            SelectedEnstoreList=null;
            HTTPCode=500; // Rectifying EnstoreList Retrieved
        }


        // Get EnstoreListDetail
        List<EnstoreListDetailDerived> ReturnedEnstoreListDetail=new ArrayList<EnstoreListDetailDerived>();
        if(HTTPCode==200){
            MapInfo=enstoreListDetailService.RetrieveEnstoreListFull(ListNum);
            int code=(int)MapInfo.get("HTTPCode");
            if(code==500){
                ReturnedEnstoreListDetail=null;
                System.out.println("[EnstoreListDetail] No Detail of Such List Number ["+ListNum+"] Retrieved!");
            }
            else{
                List<EnstoreListDetail> RetrievedEnstoreListDetail=(List<EnstoreListDetail>)MapInfo.get("DetailList");
                for(EnstoreListDetail enstoreListDetail: RetrievedEnstoreListDetail){
                    Integer ExpInNum=enstoreListDetail.getExpInNum();
                    Integer ActInNum=enstoreListDetail.getActInNum();
                    System.out.println("[EnstoreListDetail] Judging Status of " +
                            "EnstoreList with Supplement ID ["+enstoreListDetail.getSupId()+"]:");
                    System.out.println("[EnstoreListDetail] ExpInNum: ["+ExpInNum+"]");
                    System.out.println("[EnstoreListDetail] ActInNum: ["+ActInNum+"]");
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

                    // Supposedly Wrap(Appliance) & Supplement is one to one
                    System.out.println("[EnstoreList&Appliance Temp] Appliance Searching With " +
                            "Supplement ID ["+enstoreListDetail.getSupId()+"]");

                    // Search With Better Constraints First
                    String WrapId=null;

                    Map<String, Object> ApplianceResultBetter=applianceService.GetApplianceBySupplementIDAndWrapSize(
                            enstoreListDetail.getSupId(), Integer.valueOf(enstoreListDetail.getWrapSize()));
                    int CodeApplianceBetter=(int)ApplianceResultBetter.get("HTTPCode");
                    if(CodeApplianceBetter==200){
                        WrapId=((Appliance)ApplianceResultBetter.get("Appliance")).getWrapId();
                    }
                    else{
                        // Loosen Constraints
                        Map<String, Object> ApplianceResult=applianceService.GetApplianceBySupID(enstoreListDetail.getSupId());
                        int CodeAppliance=(int)ApplianceResult.get("HTTPCode");
                        if(CodeAppliance==500){
                            WrapId=null;
                            System.out.println("[EnstoreList&Appliance Temp] No Appliance Found For " +
                                    "Supplement ID ["+enstoreListDetail.getSupId()+"]");
                        }
                        else{
                            WrapId=(String)ApplianceResult.get("ApplianceID");
                        }
                    }

                    Integer Boxes=0;
                    if(Integer.valueOf(enstoreListDetail.getWrapSize())==0){
                        Boxes=0;
                    }
                    else{
                        int CurExpInNum=enstoreListDetail.getExpInNum();
                        int CurWrapSize=Integer.valueOf(enstoreListDetail.getWrapSize());
                        if(CurExpInNum%CurWrapSize==0){
                            Boxes=CurExpInNum/CurWrapSize;
                        }
                        else{
                            Boxes=CurExpInNum/CurWrapSize+1;
                        }
                    }

                    ReturnedEnstoreListDetail.add(
                            new EnstoreListDetailDerived()
                                    .setSupId(enstoreListDetail.getSupId())
                                    .setExpInNum(enstoreListDetail.getExpInNum())
                                    .setActInNum(enstoreListDetail.getActInNum())
                                    .setBoxes(Boxes)
                                    .setWrapSize(Integer.valueOf(enstoreListDetail.getWrapSize()))
                                    .setWrapId(WrapId)
                                    .setStatus(Status_Supplement)
                    );
                }
            }
        }

        EnstoreListRequest ReturnedEnstoreListRequest=new EnstoreListRequest();
        ReturnedEnstoreListRequest.setListNum(ListNum);
        ReturnedEnstoreListRequest.setListStatus(Status);
        ReturnedEnstoreListRequest.setSupplier(Supplier);
        ReturnedEnstoreListRequest.setHTTPCode(HTTPCode);
        ReturnedEnstoreListRequest.setWrapInfoDetails(ReturnedEnstoreListDetail);

        return ReturnedEnstoreListRequest;
    }
}

class EnstoreListBody{
    private String listNum; //入库单号
    private String listStatus; //入库状态（不知道有没有用）
    private String supplier; //供应商
    private List<SupplementWrapInfo> wrapInfoDetails;

    public String getListNum() {return listNum;}
    public String getListStatus() {return listStatus;}
    public String getSupplier() {return supplier;}
    public List<SupplementWrapInfo> getWrapInfoDetails() {return wrapInfoDetails;}
}

class SupplementWrapInfo{
    private String sup_id;
    private int enstore_num;
    private String wrap_id;
    private int wrap_size;
    private int boxes;

    public String getSup_id() {return sup_id;}
    public int getEnstore_num() {return enstore_num;}
    public String getWrap_id() {return wrap_id;}
    public int getWrap_size() {return wrap_size;}
    public int getBoxes() {return boxes;}
}

class EnstoreListDetailDerived{
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

    public EnstoreListDetailDerived setSupId(String supId) {
        this.supId=supId;
        return this;
    }
    public EnstoreListDetailDerived setExpInNum(Integer expInNum) {
        this.expInNum=expInNum;
        return this;
    }
    public EnstoreListDetailDerived setActInNum(Integer actInNum) {
        this.actInNum=actInNum;
        return this;
    }
    public EnstoreListDetailDerived setBoxes(Integer boxes) {
        this.boxes=boxes;
        return this;
    }
    public EnstoreListDetailDerived setWrapId(String wrapId) {
        this.wrapId=wrapId;
        return this;
    }
    public EnstoreListDetailDerived setWrapSize(Integer wrapSize) {
        this.wrapSize=wrapSize;
        return this;
    }
    public EnstoreListDetailDerived setStatus(String status) {
        this.status=status;
        return this;
    }
}

class EnstoreListRequest{
    private String ListNum;
    private String ListStatus;
    private String Supplier;
    private Integer HTTPCode;

    private List<EnstoreListDetailDerived> WrapInfoDetails;

    public String getListNum() {return ListNum;}
    public String getListStatus() {return ListStatus;}
    public String getSupplier() {return Supplier;}
    public Integer getHTTPCode(){return HTTPCode;}
    public List<EnstoreListDetailDerived> getWrapInfoDetails() {return WrapInfoDetails;}

    public void setListNum(String ListNum) {this.ListNum = ListNum;}
    public void setListStatus(String ListStatus) {this.ListStatus = ListStatus;}
    public void setSupplier(String Supplier) {this.Supplier = Supplier;}
    public void setHTTPCode(Integer HTTPCode) {this.HTTPCode = HTTPCode;}
    public void setWrapInfoDetails(List<EnstoreListDetailDerived> WrapInfoDetails) {
        this.WrapInfoDetails = WrapInfoDetails;
    }
}