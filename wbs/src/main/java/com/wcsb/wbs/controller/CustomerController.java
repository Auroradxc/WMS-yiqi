package com.wcsb.wbs.controller;


import com.wcsb.wbs.domain.Customer;
import com.wcsb.wbs.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Jessica_Clorize
 * @since 2024-06-19
 */
@RestController
//@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/get_customer_list")
    public List<Customer> getCustomerList() {
        Map<String, Object> MapInfo=customerService.GetCustomerList();
        int code=(int)MapInfo.get("HTTPCode");
        if(code==200){
            return (List<Customer>)MapInfo.get("CustomerList");
        }
        else{
            return null;
        }
    }

    @PostMapping("/add_customer")
    public String addCustomer(@RequestBody Customer customer) {
        String Creator="Magic Stone White";
        customer.setCreator(Creator);
        customer.setUpdator(Creator);
        customer.setCreateTime(LocalDateTime.now());
        customer.setUpdateTime(LocalDateTime.now());
        Map<String, Object> MapInfo=customerService.AddCustomer(customer);
        int code=(int)MapInfo.get("HTTPCode");
        if(code==200){
            return "Insert Succeeded";
        }
        else{
            return "Insert Failed";
        }
    }

    @PostMapping("/delete_customer")
    public String deleteCustomer(@RequestBody Customer customer) {
        // Has "CustomerID" As A Necessity
        Map<String, Object> MapInfo=customerService.DeleteCustomer(customer);
        int code=(int)MapInfo.get("HTTPCode");
        if(code==200){
            return "Delete Succeeded";
        }
        else{
            return "Delete Failed";
        }
    }
}

