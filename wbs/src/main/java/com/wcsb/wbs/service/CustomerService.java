package com.wcsb.wbs.service;

import com.wcsb.wbs.domain.Customer;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jessica_Clorize
 * @since 2024-06-19
 */
public interface CustomerService extends IService<Customer> {
    // Get All Customers
    Map<String, Object> GetCustomerList();

    // Insert Customer
    Map<String, Object> AddCustomer(Customer customer);

    // Delete Customer
    Map<String, Object> DeleteCustomer(Customer customer);
}
