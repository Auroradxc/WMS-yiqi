package com.wcsb.wbs.controller;


import com.wcsb.wbs.domain.*;
import com.wcsb.wbs.service.*;
import org.apache.ibatis.executor.loader.ResultLoader;
import org.omg.CORBA.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
//@RequestMapping("/stock")
public class StockController {
    @Autowired
    private StockService stockService;

    @Autowired
    private SupplementService supplementService;

    @Autowired
    private EnstoreListService enstoreListService;

    @Autowired
    private EnstoreListDetailService enstoreListDetailService;

    @Autowired
    private ExstoreListService exstoreListService;

    @Autowired
    private ExstoreListDetailService exstoreListDetailService;

    @PostMapping("/update_stock")
    public String UpdateStock(@RequestBody Stock stock) {
        // Check if stock exists in system, then update, supposed to be used in v-for conditions (once per cycle responsible for one entry)
        String SupID=stock.getSupId();
        System.out.println("[Stock] Looking For Stock Of Supplement ID ["+SupID+"]!");
        Integer StockNum=stock.getStockNum();

        // Search for Stock Via SupID
        Map<String, Object> MapInfo=stockService.CheckForStockExistence(SupID);
        int code=Integer.parseInt(MapInfo.get("HTTPCode").toString());

        Stock CurrentStock=new Stock();
        String Creator=new String("Jessica_Clorize");
        LocalDateTime CreateTime=LocalDateTime.now();

        if(code==500){
            // Stock Doesnt Exist
            System.out.println("[Stock] Stock Doesn't Exist, Creating One...");
            Map<String, Object> MapInfo2=stockService.CheckForStockNum();
            int StockSize=Integer.parseInt(MapInfo2.get("StockNum").toString())+1;

            CurrentStock.setId(StockSize);
            CurrentStock.setSupId(SupID);
            CurrentStock.setStockNum(StockNum);
            CurrentStock.setLowestNum(StockNum);
            CurrentStock.setHighestNum(StockNum);
            CurrentStock.setCreator(Creator);
            CurrentStock.setUpdator(Creator);
            CurrentStock.setCreateTime(CreateTime);
            CurrentStock.setUpdateTime(CreateTime);
            MapInfo=stockService.InsertStock(CurrentStock);
            int code_check=Integer.parseInt(MapInfo.get("HTTPCode").toString());
            if(code_check==200){
                System.out.println("[Stock] New Stock Inserted!");
            }
        }
        else if(code==200){
            // Stock Exists, Update with (SupID) to {StockNum+PreviousNum, UpdateTime}
            System.out.println("[Stock] Stock Exists, Updating...");
            String Updator=new String("Ellie McCoon");
            Stock PreviousEntry=(Stock)MapInfo.get("Stock");
            System.out.println("[Stock Temp] Stock Number: ["+StockNum+"]");

            CurrentStock.setId(PreviousEntry.getId());
            CurrentStock.setSupId(PreviousEntry.getSupId());

            // Problem: What if Partially Imported?
            CurrentStock.setStockNum(PreviousEntry.getStockNum()+StockNum);
            CurrentStock.setLowestNum(PreviousEntry.getLowestNum());

            Integer PrevHighestNum=PreviousEntry.getHighestNum();
            if(CurrentStock.getStockNum()>PrevHighestNum){
                CurrentStock.setHighestNum(CurrentStock.getStockNum());
            }
            else{
                CurrentStock.setHighestNum(PrevHighestNum);
            }

            CurrentStock.setCreator(CurrentStock.getCreator());
            CurrentStock.setUpdator(Updator);
            CurrentStock.setCreateTime(CurrentStock.getCreateTime());
            CurrentStock.setUpdateTime(LocalDateTime.now());

            MapInfo=stockService.UpdateStock(CurrentStock,SupID);
            int code_check=Integer.parseInt(MapInfo.get("HTTPCode").toString());
            if(code_check==200){
                System.out.println("[Stock] Stock Updated!");
            }
        }

        return "Update Succeeded!";
    }

    @PostMapping("/update_stock_accordingly")
    public String UpdateStockAccordingly(@RequestBody StockUpdateRequirement stockUpdateRequirement) {
        // Read from corresponding enstore_list_detail, get all (sup_id, act_num)
        String listNum=stockUpdateRequirement.getListNum();
        Map<String, Object> MapInfo=enstoreListDetailService.RetrieveEnstoreListTotal(listNum);
        List<EnstoreListDetail> DetailLists=(List<EnstoreListDetail>)MapInfo.get("EnstoreLists");
        System.out.println("[Stock] Updating Stock Via List With Number ["+listNum+"]... Please Hold!");

        List<Integer> OriginalActInNum=stockUpdateRequirement.getOriginalActInNum();

        String Status="PENDING";
        int Open_Flag=0;
        int Closed_Flag=0;
        // If All ActInNum Larger: CLOSED
        // If All ActInNum is 0: OPEN

        int Index=0;

        for (EnstoreListDetail detailList:DetailLists) {
            // Check Status
            Integer OriginalActInNumCurrent=OriginalActInNum.get(Index);

            Integer ActInNum=detailList.getActInNum();
            Integer ExpInNum=detailList.getExpInNum();

            System.out.println("[Stock] Actual Instock Number: ["+ActInNum+"]");
            System.out.println("[Stock] Expected Instock Number: ["+ExpInNum+"]");

            Open_Flag+=ActInNum;
            Closed_Flag+=(ExpInNum-ActInNum);

            String SupId=detailList.getSupId();
            Integer StockNum=detailList.getActInNum();
            System.out.println("[Stock] Derived Stock With Supplement ID ["+SupId+"] And Number ["+StockNum+"]... Please Hold!");
            Stock CurrentStock=new Stock();
            CurrentStock.setSupId(SupId);
            CurrentStock.setStockNum(StockNum-OriginalActInNumCurrent);
            UpdateStock(CurrentStock);

            Index+=1;
        }

        System.out.println("[Stock] Open Flag: "+Open_Flag);
        System.out.println("[Stock] Closed Flag: "+Closed_Flag);
        // Update Status
        if(Closed_Flag==0 && Open_Flag==0){
            Status="OPEN";
        }
        else if(Closed_Flag==0 && Open_Flag!=0){
            Status="CLOSED";
        }
        else if(Closed_Flag!=0 && Open_Flag==0){
            Status="OPEN";
        }
        else{
            Status="PENDING";
        }
        System.out.println("[Stock] Status: "+Status);

        EnstoreList enstoreList=new EnstoreList();
        enstoreList.setListNum(listNum);
        enstoreListService.UpdateEnstoreListStatus(enstoreList,Status);

        return "Update Succeeded!";
    }

    @PostMapping("/request_stock")
    public List<StockSupplementParameters> requestStock(@RequestBody StockRequestRequirement stockRequestRequirement) {
        // Filter Options
        String SupId=stockRequestRequirement.getSupId();
        String Supplier=stockRequestRequirement.getSupplier();

        // First Get All Supplement (Stock) That Is Available
        Map<String, Object> MapInfo=stockService.GetAllStock();
        int code=Integer.parseInt(MapInfo.get("HTTPCode").toString());
        if(code==200){
            // Filter Request
            List<Stock> StockListOriginal=(List<Stock>)MapInfo.get("StockList");
            List<StockSupplementParameters> SupplementUsabilityList=new ArrayList<>();

            // Get All Supplement
            MapInfo=supplementService.GetSupplementList();
            code=Integer.parseInt(MapInfo.get("HTTPCode").toString());
            if(code==500){
                return null;
            }
            System.out.println("[StockController] Filtering Stock...");

            List<Supplement> SupplementList=(List<Supplement>)MapInfo.get("Supplements");

            // Only When Stock Larger Than 0 Should It Be Available
            for(Supplement Supplement:SupplementList){
                if(SupId!=null){
                    if(!Supplement.getSupId().equals(SupId)){
                        continue;
                    }
                }
                if(Supplier!=null){
                    if(!Supplement.getSupplier().equals(Supplier)){
                        continue;
                    }
                }
                //System.out.println();
                //System.out.println("[StockController] For Supplement: "+Supplement.getSupId());

                StockSupplementParameters StockParam=new StockSupplementParameters();

                StockParam.setSupId(Supplement.getSupId());
                StockParam.setSupplier(Supplement.getSupplier());
                StockParam.setSupName(Supplement.getSupName());

                Stock CurrentStock=new Stock();
                int ChangedFlag=0;
                for(Stock Stock:StockListOriginal){
                    if(Stock.getSupId().equals(Supplement.getSupId())){
                        CurrentStock=Stock;
                        ChangedFlag=1;
                        break;
                    }
                }

                if(ChangedFlag==0){
                    StockParam.setAvailable("Not Available");
                    StockParam.setAmount(0);
                }
                else{
                    Integer CurrentNumber=CurrentStock.getStockNum();
                    System.out.println("[Stock] Found Stock With Amount: ["+CurrentStock.getStockNum()+"] And ID ["+CurrentStock.getSupId()+"]");

                    if(CurrentStock.getStockNum()>0){
                        StockParam.setAvailable("Available");
                        StockParam.setAmount(CurrentNumber);
                    }
                    else{
                        StockParam.setAvailable("Not Available");
                        StockParam.setAmount(0);
                    }
                }

                SupplementUsabilityList.add(new StockSupplementParameters(StockParam));
                //System.out.println(SupplementUsabilityList);
            }

            return SupplementUsabilityList;
        }
        else{
            // Stock is empty
            return null;
        }
    }

    @PostMapping("/request_stock_advanced")
    public List<StockSupplementParameters> requestStockAdvanced(@RequestBody StockRequestRequirementAdvanced stockRequestRequirementAdvanced) {
        // Filter Options
        String SupId=stockRequestRequirementAdvanced.getSupId();
        String Supplier=stockRequestRequirementAdvanced.getSupplier();
        // Used to search bound of stock
        Integer LowestStockNum=stockRequestRequirementAdvanced.getLowestNum();
        if(LowestStockNum==null){
            LowestStockNum=0;
        }
        Integer HighestStockNum=stockRequestRequirementAdvanced.getHighestNum();
        if(HighestStockNum==null){
            HighestStockNum=999999;
        }

        // First Get All Supplement (Stock) That Is Available
        Map<String, Object> MapInfo=stockService.GetAllStock();
        int code=Integer.parseInt(MapInfo.get("HTTPCode").toString());
        if(code==200){
            // Filter Request
            List<Stock> StockListOriginal=(List<Stock>)MapInfo.get("StockList");
            List<StockSupplementParameters> SupplementUsabilityList=new ArrayList<>();

            // Get All Supplement
            MapInfo=supplementService.GetSupplementList();
            code=Integer.parseInt(MapInfo.get("HTTPCode").toString());
            if(code==500){
                return null;
            }
            System.out.println("[StockController] Filtering Stock...");

            List<Supplement> SupplementList=(List<Supplement>)MapInfo.get("Supplements");

            // Only When Stock Larger Than 0 Should It Be Available
            for(Supplement Supplement:SupplementList){
                if(SupId!=null){
                    if(!Supplement.getSupId().equals(SupId)){
                        continue;
                    }
                }
                if(Supplier!=null){
                    if(!Supplement.getSupplier().equals(Supplier)){
                        continue;
                    }
                }

                //System.out.println();
                //System.out.println("[StockController] For Supplement: "+Supplement.getSupId());

                StockSupplementParameters StockParam=new StockSupplementParameters();

                StockParam.setSupId(Supplement.getSupId());
                StockParam.setSupplier(Supplement.getSupplier());
                StockParam.setSupName(Supplement.getSupName());

                Stock CurrentStock=new Stock();
                int ChangedFlag=0;
                for(Stock Stock:StockListOriginal){
                    if(Stock.getSupId().equals(Supplement.getSupId())){
                        CurrentStock=Stock;
                        ChangedFlag=1;
                        break;
                    }
                }

                if(ChangedFlag==0){
                    // Not Found
                    StockParam.setAvailable("Not Available");
                    StockParam.setAmount(0);
                }
                else if(ChangedFlag==1 && (CurrentStock.getStockNum()<LowestStockNum || CurrentStock.getStockNum()>HighestStockNum)){
                    // Not Within Boundaries
                    StockParam.setAvailable("Not Available");
                    StockParam.setAmount(0);
                }
                else{
                    Integer CurrentNumber=CurrentStock.getStockNum();
                    System.out.println("[Stock] Found Stock With Amount: ["+CurrentStock.getStockNum()+"] And ID ["+CurrentStock.getSupId()+"]");

                    if(CurrentStock.getStockNum()>0){
                        StockParam.setAvailable("Available");
                        StockParam.setAmount(CurrentNumber);
                    }
                    else{
                        StockParam.setAvailable("Not Available");
                        StockParam.setAmount(0);
                    }
                    SupplementUsabilityList.add(new StockSupplementParameters(StockParam));
                }
                //System.out.println(SupplementUsabilityList);
            }

            return SupplementUsabilityList;
        }
        else{
            // Stock is empty
            return null;
        }
    }

    @PostMapping("/relieve_stock_accordingly")
    public StockRelieveReturn RelieveStockAccordingly(@RequestBody StockRetrieveRequirement stockRetrieveRequirement) {
        // Read from corresponding enstore_list_detail, get all (sup_id, act_num)
        String listNum=stockRetrieveRequirement.getListNum();
        String supId=stockRetrieveRequirement.getSupId();
        Integer PreviousNum=stockRetrieveRequirement.getPreviousNum();
        if(PreviousNum==null){
            PreviousNum=new Integer(0);
        }

        Map<String, Object> MapInfo=exstoreListDetailService.RetrieveOneExstoreList(listNum, supId);
        ExstoreListDetail DetailList=(ExstoreListDetail)MapInfo.get("ExstoreList");

        System.out.println("[Stock] Relieving Stock Via List With Number ["+listNum+"]... Please Hold!");
        System.out.println("[Stock] Actual In Number: "+DetailList.getActInNum());
        System.out.println("[Stock] Previous Number: "+PreviousNum);

        Integer StockNum=DetailList.getActInNum()-PreviousNum;

        System.out.println("[Stock] Updated Part of Stock: ["+StockNum+"]!");

        // Search if Warehouse Has This Much Stock
        Map<String, Object> MapInfoStock=stockService.GetStockViaSupID(supId);
        StockRelieveReturn stockRelieveReturn=new StockRelieveReturn();
        int code=Integer.parseInt(MapInfoStock.get("HTTPCode").toString());
        if(code==500){
            // Stock is Invalid
            System.out.println("[Stock] Stock Of Supplement ID ["+supId+"] Failed To Retrieve!");

            stockRelieveReturn.setListNum(listNum);
            stockRelieveReturn.setSup_id(supId);
            stockRelieveReturn.setAvailable("Not Available");
        }
        else{
            //System.out.println("[Stock] Derived Stock With Supplement ID ["+supId+"] And Number ["+StockNum+"]... Please Hold!");
            Stock PrevStock=(Stock)MapInfoStock.get("Stock");

            int CurrentStockNum=PrevStock.getStockNum();
            int LowestStockNum=PrevStock.getLowestNum();
            int HighestStockNum=PrevStock.getHighestNum();
            //String Creator=PrevStock.getCreator();
            String Updator="Austin_Colossus";
            //LocalDateTime CreateTime=PrevStock.getCreateTime();
            LocalDateTime UpdateTime=LocalDateTime.now();

            if(StockNum>CurrentStockNum){
                // Requested Stock Number Is Greater, Update Failed
                System.out.println("[Stock] Stock Of Supplement ID ["+supId+"] Failed: Not Enough Stock!");

                stockRelieveReturn.setListNum(listNum);
                stockRelieveReturn.setSup_id(supId);
                stockRelieveReturn.setAvailable("Not Available");
            }
            else{
                Stock NewStock=new Stock();
                NewStock.setSupId(supId);
                NewStock.setStockNum(CurrentStockNum-StockNum);
                if(CurrentStockNum-StockNum<LowestStockNum){
                    NewStock.setLowestNum(CurrentStockNum-StockNum);
                }
                else{
                    NewStock.setLowestNum(LowestStockNum);
                }

                NewStock.setHighestNum(HighestStockNum);
                NewStock.setUpdateTime(UpdateTime);
                NewStock.setUpdator(Updator);
                Map<String, Object> StockReturn=stockService.UpdateStock(NewStock, supId);

                stockRelieveReturn.setListNum(listNum);
                stockRelieveReturn.setSup_id(supId);
                stockRelieveReturn.setAvailable("Available");
            }
        }

        Map<String, Object> CheckMap=exstoreListDetailService.RetrieveExstoreListTotal(listNum);
        List<ExstoreListDetail> exstoreListDetailEntries=(List<ExstoreListDetail>)CheckMap.get("ExstoreLists");
        String TotalStatus="PENDING";
        int Open_Flag=0;
        int Closed_Flag=0;

        for(ExstoreListDetail exstoreListDetailtemp:exstoreListDetailEntries){
            if(!(exstoreListDetailtemp.getActInNum().intValue()==exstoreListDetailtemp.getExpInNum().intValue())){
                Closed_Flag=1;
            }
            if(!(exstoreListDetailtemp.getActInNum().intValue()==0)){
                Open_Flag=1;
            }
        }
        if(Closed_Flag==0){
            TotalStatus="CLOSED";
        }
        if(Open_Flag==0){
            TotalStatus="OPEN";
        }

        ExstoreList NewExstoreListStatus=new ExstoreList();
        NewExstoreListStatus.setListNum(listNum);
        exstoreListService.UpdateEnstoreListStatus(NewExstoreListStatus,TotalStatus);

        return stockRelieveReturn;
    }
}

class StockUpdateRequirement{
    private String listNum;
    private List<Integer> originalActInNum;
    public String getListNum(){return listNum;}
    public List<Integer> getOriginalActInNum(){
        return originalActInNum;
    }
}

class StockRetrieveRequirement{
    private String listNum;
    private String supId;
    private Integer previousNum;
    public String getListNum(){return listNum;}
    public String getSupId(){return supId;}
    public Integer getPreviousNum(){return previousNum;}
}

class StockRequestRequirement{
    private String supId;
    private String supplier;
    public String getSupId(){return supId;}
    public String getSupplier(){return supplier;}
}

class StockRequestRequirementAdvanced{
    private String supId;
    private String supplier;
    private Integer highestNum;
    private Integer lowestNum;
    public String getSupId(){return supId;}
    public String getSupplier(){return supplier;}
    public Integer getHighestNum(){return highestNum;}
    public Integer getLowestNum(){return lowestNum;}
}

class StockSupplementParameters{
    private String supId; // Supplement ID
    private String supName; // Supplement Name
    private String supplier; // Supplier
    private String available; // Has Stock : {"Available" | "Not Available"}
    private Integer amount;
    public void setSupplier(String supplier){
        this.supplier=supplier;
    }
    public void setSupId(String supId){
        this.supId=supId;
    }
    public void setSupName(String supName){
        this.supName=supName;
    }
    public void setAvailable(String available){
        this.available=available;
    }
    public void setAmount(Integer amount){
        this.amount=amount;
    }

    public String getSupplier(){
        return supplier;
    }
    public String getSupId(){
        return supId;
    }
    public String getSupName(){
        return supName;
    }
    public String getAvailable(){
        return available;
    }
    public Integer getAmount(){
        return amount;
    }

    public StockSupplementParameters(){

    }
    public StockSupplementParameters(StockSupplementParameters stockParams){
        this.supId=stockParams.supId;
        this.supName=stockParams.supName;
        this.supplier=stockParams.supplier;
        this.available=stockParams.available;
        this.amount=stockParams.amount;
    }
}

class StockRelieveReturn{
    private String listNum;
    private String sup_id;
    private String available; // "Available" if Succeeded, else Failed (Not Enough Stock)
    public void setListNum(String ListNum){
        this.listNum=ListNum;
    }
    public void setSup_id(String sup_id){
        this.sup_id=sup_id;
    }
    public void setAvailable(String available){
        this.available=available;
    }

    public String getAvailable() {
        return available;
    }
    public String getListNum() {
        return listNum;
    }
    public String getSup_id() {
        return sup_id;
    }
}