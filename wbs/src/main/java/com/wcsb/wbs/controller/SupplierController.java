package com.wcsb.wbs.controller;


import com.wcsb.wbs.domain.Customer;
import com.wcsb.wbs.domain.Supplier;
import com.wcsb.wbs.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
//@RequestMapping("/supplier")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    @PostMapping("/get_supplier_list")
    public List<Supplier> getSupplierList() {
        // Supposed to return all suppliers in database
        Map<String, Object> MapInfo=supplierService.GetAllSuppliers();
        int code=(int)MapInfo.get("HTTPCode");
        if(code==200){
            // Found Supplier
            return (List<Supplier>)MapInfo.get("SupplierList");
        }
        else{
            return null;
        }
    }

    // [To Be Added: Insert & Delete]
    @PostMapping("/add_supplier")
    public String addCustomer(@RequestBody Supplier supplier) {
        String Creator="Magic Stone Black";
        supplier.setCreator(Creator);
        supplier.setUpdator(Creator);
        supplier.setCreateTime(LocalDateTime.now());
        supplier.setUpdateTime(LocalDateTime.now());
        Map<String, Object> MapInfo=supplierService.AddSupplier(supplier);
        int code=(int)MapInfo.get("HTTPCode");
        if(code==200){
            return "Insert Succeeded";
        }
        else{
            return "Insert Failed";
        }
    }

    @PostMapping("/delete_supplier")
    public String deleteCustomer(@RequestBody Supplier supplier) {
        // Has "SupplierID" As A Necessity
        Map<String, Object> MapInfo=supplierService.DeleteSupplier(supplier);
        int code=(int)MapInfo.get("HTTPCode");
        if(code==200){
            return "Delete Succeeded";
        }
        else{
            return "Delete Failed";
        }
    }
}

